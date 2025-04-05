package io.jistud.mazesolver.server.controller.dto;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeResponseDTOTest {

    @Test
    void fromMaze_WithUnsolvedMaze_ShouldMapCorrectly() {
        // Given
        char[][] grid = {
            {'s', ' ', 'w'},
            {'w', ' ', 'w'},
            {'w', ' ', 'e'}
        };
        Maze maze = new Maze(3, 3, grid);

        // When
        MazeResponseDTO dto = MazeResponseDTO.fromMaze(1, maze);

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(3, dto.getGrid().length);
        assertEquals(3, dto.getGrid()[0].length);
        assertEquals('s', dto.getGrid()[0][0]);
        assertEquals('e', dto.getGrid()[2][2]);
        assertFalse(dto.isSolved());
        assertNull(dto.getSolvedPath());
    }

    @Test
    void fromMaze_WithSolvedMaze_ShouldIncludeSolutionPath() {
        // Given
        char[][] grid = {
            {'s', ' ', 'w'},
            {'w', ' ', 'w'},
            {'w', ' ', 'e'}
        };
        Maze maze = new Maze(3, 3, grid);

        List<Position> solutionPath = Arrays.asList(
                new Position(0, 0), new Position(0, 1), new Position(1, 1), new Position(2, 1), new Position(2, 2));
        maze.setSolvedPath(solutionPath);

        // When
        MazeResponseDTO dto = MazeResponseDTO.fromMaze(1, maze);

        // Then
        assertNotNull(dto);
        assertTrue(dto.isSolved());
        assertNotNull(dto.getSolvedPath());
        assertEquals(5, dto.getSolvedPath().size());
        assertEquals(0, dto.getSolvedPath().get(0).getRow());
        assertEquals(0, dto.getSolvedPath().get(0).getCol());
        assertEquals(2, dto.getSolvedPath().get(4).getRow());
        assertEquals(2, dto.getSolvedPath().get(4).getCol());
    }
}
