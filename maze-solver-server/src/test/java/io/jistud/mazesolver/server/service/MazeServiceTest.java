package io.jistud.mazesolver.server.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.jistud.mazesolver.server.repository.MazeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MazeServiceTest {

    @Mock
    private MazeRepository mazeRepository;

    private MazeService mazeService;

    @Mock
    private ArgumentCaptor<MazeEntity> entityCaptor;

    @BeforeEach
    void setUp() {
        mazeService = new MazeServiceImpl(mazeRepository);
        entityCaptor = ArgumentCaptor.forClass(MazeEntity.class);
    }

    @Test
    void testGenerateRandomMaze() {
        // Given
        int width = 10;
        int height = 10;

        // When
        Maze maze = mazeService.generateRandomMaze(width, height);

        // Then
        assertNotNull(maze);
        assertEquals(width, maze.getWidth());
        assertEquals(height, maze.getHeight());

        // Verify the maze has valid start and end positions
        List<Position> startPositions = maze.findCellsWithValue(Maze.START);
        List<Position> endPositions = maze.findCellsWithValue(Maze.END);

        assertEquals(1, startPositions.size(), "Maze should have exactly one start position");
        assertEquals(1, endPositions.size(), "Maze should have exactly one end position");

        // Verify the maze path cells has been removed
        List<Position> pathPositions = maze.findCellsWithValue(Maze.PATH);
        assertEquals(0, pathPositions.size(), "Maze should not have path cells");
    }

    @Test
    void testSaveEntityFromMaze() {
        // Given
        Maze maze = new Maze(5, 5);
        maze.setCell(0, 2, Maze.START);
        maze.setCell(4, 2, Maze.END);
        maze.setCell(1, 1, Maze.WALL);
        maze.setCell(2, 2, Maze.WALL);

        MazeEntity savedEntity = new MazeEntity();
        savedEntity.setId(123);
        savedEntity.setMazeData("Sample maze data");
        savedEntity.setCreatedAt(Instant.now());
        savedEntity.setUpdatedAt(Instant.now());

        when(mazeRepository.save(any(MazeEntity.class))).thenReturn(savedEntity);

        // When
        MazeEntity result = mazeService.saveEntityFromMaze(maze);

        // Then
        assertNotNull(result);
        assertEquals(123, result.getId().intValue());

        // Verify the correct data was saved
        verify(mazeRepository).save(any(MazeEntity.class));
    }

    @Test
    void testFindById() {
        // Given
        Integer id = new java.util.Random().nextInt();
        MazeEntity mazeEntity = new MazeEntity();
        mazeEntity.setId(id);
        mazeEntity.setMazeData("www\nsew\nwww");
        mazeEntity.setCreatedAt(Instant.now());
        mazeEntity.setUpdatedAt(Instant.now());
        mazeEntity.setSolved(false);

        when(mazeRepository.findById(id)).thenReturn(Optional.of(mazeEntity));

        // When
        Optional<MazeEntity> result = mazeService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        // Given
        Integer id = new java.util.Random().nextInt();
        when(mazeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<MazeEntity> result = mazeService.findById(id);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll() {
        // Given
        MazeEntity maze1 = new MazeEntity();
        maze1.setId(new java.util.Random().nextInt());
        maze1.setMazeData("www\nsew\nwww");
        maze1.setCreatedAt(Instant.now());
        maze1.setUpdatedAt(Instant.now());
        maze1.setSolved(false);

        MazeEntity maze2 = new MazeEntity();
        maze2.setId(new java.util.Random().nextInt());
        maze2.setMazeData("wwww\ns  e\nwwww");
        maze2.setCreatedAt(Instant.now());
        maze2.setUpdatedAt(Instant.now());
        maze2.setSolved(false);

        org.springframework.data.domain.PageImpl<MazeEntity> page =
                new org.springframework.data.domain.PageImpl<>(Arrays.asList(maze1, maze2));
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);

        when(mazeRepository.findAll(pageable)).thenReturn(page);

        // When
        org.springframework.data.domain.Page<MazeEntity> result = mazeService.findAll(pageable);

        // Then
        assertEquals(2, result.getContent().size());
        verify(mazeRepository).findAll(pageable);
    }

    @Test
    void testDeleteMaze() {
        // Given
        Integer id = new java.util.Random().nextInt();

        // When
        mazeService.deleteMaze(id);

        // Then
        verify(mazeRepository).deleteById(id);
    }

    @Test
    void testSolveMaze_AlreadySolved() {
        // Given
        Integer id = new java.util.Random().nextInt();
        MazeEntity entity = new MazeEntity();
        entity.setId(id);
        entity.setMazeData("www\nsew\nwww");
        entity.setSolved(true);
        entity.setSolutionPath("[(1,0), (1,1), (1,2)]");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        when(mazeRepository.findById(id)).thenReturn(Optional.of(entity));

        // When
        Optional<MazeEntity> resultOpt = mazeService.solveMaze(id);

        // Then
        assertTrue(resultOpt.isPresent());
        MazeEntity result = resultOpt.get();
        assertTrue(result.isSolved());
        assertEquals("[(1,0), (1,1), (1,2)]", result.getSolutionPath());

        // Verify the maze entity was not updated (already solved)
        verify(mazeRepository, never()).save(any(MazeEntity.class));
    }

    @Test
    void testConvertToModel() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setId(1);
        entity.setMazeData("www\nsew\nwww");
        entity.setSolved(false);
        entity.setSolutionPath(null);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When
        Maze result = mazeService.convertToModel(entity);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getHeight());
        assertEquals(3, result.getWidth());
        assertFalse(result.isSolved());
        assertEquals(1, result.findCellsWithValue(Maze.START).size());
        assertEquals(1, result.findCellsWithValue(Maze.END).size());
    }

    @Test
    void testSolveMaze_NotYetSolved() {
        // Given
        Integer id = new java.util.Random().nextInt();

        // Create a simple maze data string
        String mazeData = "wwsww\nw   w\nw w w\nw   w\nwweww";

        MazeEntity entity = new MazeEntity();
        entity.setId(id);
        entity.setMazeData(mazeData);
        entity.setSolved(false);
        entity.setSolutionPath(null);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // Create a maze model for the convertToModel method to return
        char[][] grid = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 4 || j == 0 || j == 4) {
                    grid[i][j] = Maze.WALL;
                } else {
                    grid[i][j] = Maze.EMPTY;
                }
            }
        }
        grid[0][2] = Maze.START;
        grid[4][2] = Maze.END;

        Maze maze = new Maze(5, 5, grid);

        // Setup saved entity with solution
        MazeEntity solvedEntity = new MazeEntity();
        solvedEntity.setId(id);
        solvedEntity.setMazeData(mazeData);
        solvedEntity.setSolved(true);
        solvedEntity.setSolutionPath("[(0,2), (1,2), (2,2), (3,2), (4,2)]");
        solvedEntity.setCreatedAt(entity.getCreatedAt());
        solvedEntity.setUpdatedAt(Instant.now());

        when(mazeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mazeRepository.save(any(MazeEntity.class))).thenReturn(solvedEntity);

        // When
        Optional<MazeEntity> resultOpt = mazeService.solveMaze(id);

        // Then
        assertTrue(resultOpt.isPresent());
        MazeEntity result = resultOpt.get();
        assertTrue(result.isSolved());
        assertNotNull(result.getSolutionPath());

        // Verify the maze entity was updated with the solution
        ArgumentCaptor<MazeEntity> entityCaptor = ArgumentCaptor.forClass(MazeEntity.class);
        verify(mazeRepository).save(entityCaptor.capture());

        MazeEntity savedEntity = entityCaptor.getValue();
        assertTrue(savedEntity.isSolved());
        assertNotNull(savedEntity.getSolutionPath());
        assertNotNull(savedEntity.getUpdatedAt());
    }
}
