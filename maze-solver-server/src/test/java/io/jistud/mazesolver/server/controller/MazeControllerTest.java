package io.jistud.mazesolver.server.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.jistud.mazesolver.server.service.MazeService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MazeController.class)
class MazeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MazeService mazeService;

    private MazeEntity testMazeEntity;
    private Maze testMaze;

    @BeforeEach
    void setUp() {
        // Setup test maze entity
        testMazeEntity = new MazeEntity();
        testMazeEntity.setId(1);
        testMazeEntity.setMazeData("wwsww\nw   w\nw w w\nw   w\nwweww");
        testMazeEntity.setCreatedAt(Instant.now());
        testMazeEntity.setUpdatedAt(Instant.now());
        testMazeEntity.setSolved(false);
        testMazeEntity.setSolutionPath(null);

        // Setup test maze model
        testMaze = new Maze(5, 5);
        // Add walls and paths to the maze
        // This would typically be more detailed in a real test
    }

    @Test
    void getMazes_ShouldReturnPaginatedListOfMazeSummaries() throws Exception {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MazeEntity> mazePage = new PageImpl<>(List.of(testMazeEntity), pageRequest, 1);

        when(mazeService.findAll(any(Pageable.class))).thenReturn(mazePage);

        // When/Then
        mockMvc.perform(get("/api/v1/mazes")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[0].updatedAt").exists())
                .andExpect(jsonPath("$.content[0].solved").value(false))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getMaze_WithValidId_ShouldReturnMazeDetails() throws Exception {
        // Given
        when(mazeService.findById(eq(1))).thenReturn(Optional.of(testMazeEntity));
        when(mazeService.convertToModel(eq(testMazeEntity))).thenReturn(testMaze);

        // When/Then
        mockMvc.perform(get("/api/v1/mazes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.grid").isArray())
                .andExpect(jsonPath("$.solved").value(false))
                .andExpect(jsonPath("$.solvedPath").doesNotExist());
    }

    @Test
    void getMaze_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        when(mazeService.findById(anyInt())).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/mazes/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void solveMaze_WithValidId_ShouldReturnSolvedMaze() throws Exception {
        // Given
        MazeEntity solvedEntity = new MazeEntity();
        solvedEntity.setId(1);
        solvedEntity.setMazeData(testMazeEntity.getMazeData());
        solvedEntity.setCreatedAt(testMazeEntity.getCreatedAt());
        solvedEntity.setUpdatedAt(Instant.now());
        solvedEntity.setSolved(true);
        solvedEntity.setSolutionPath("[(2,0), (2,1), (1,1), (1,2), (1,3), (2,3), (2,4)]");

        // Create a new Maze instance for the solved state instead of cloning
        Maze solvedMaze = new Maze(5, 5);
        List<Position> solutionPath = List.of(
                new Position(2, 0),
                new Position(2, 1),
                new Position(1, 1),
                new Position(1, 2),
                new Position(1, 3),
                new Position(2, 3),
                new Position(2, 4));
        solvedMaze.setSolvedPath(solutionPath);

        when(mazeService.findById(eq(1))).thenReturn(Optional.of(testMazeEntity));
        when(mazeService.solveMaze(eq(1))).thenReturn(Optional.of(solvedEntity));
        when(mazeService.convertToModel(eq(solvedEntity))).thenReturn(solvedMaze);

        // When/Then
        mockMvc.perform(put("/api/v1/mazes/1/solve").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.grid").isArray())
                .andExpect(jsonPath("$.solved").value(true))
                .andExpect(jsonPath("$.solvedPath").isArray())
                .andExpect(jsonPath("$.solvedPath.length()").value(7));
    }

    @Test
    void solveMaze_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        when(mazeService.findById(anyInt())).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/mazes/999/solve").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static <T> T any(Class<T> type) {
        return org.mockito.ArgumentMatchers.any(type);
    }
}
