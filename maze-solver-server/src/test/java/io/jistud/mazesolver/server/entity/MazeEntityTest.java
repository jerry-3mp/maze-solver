package io.jistud.mazesolver.server.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

import static org.junit.jupiter.api.Assertions.*;

class MazeEntityTest {

    @Test
    void testCreateMazeEntity() {
        // Given
        UUID id = UUID.randomUUID();
        String mazeData = "w w w\ns . w\nw e w";
        int width = 3;
        int height = 3;
        int startRow = 1;
        int startCol = 0;
        int endRow = 1;
        int endCol = 2;
        boolean solved = true;
        String solutionPath = "1,0;1,1;1,2";
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // When
        MazeEntity entity = new MazeEntity();
        entity.setId(id);
        entity.setMazeData(mazeData);
        entity.setWidth(width);
        entity.setHeight(height);
        entity.setStartRow(startRow);
        entity.setStartCol(startCol);
        entity.setEndRow(endRow);
        entity.setEndCol(endCol);
        entity.setSolved(solved);
        entity.setSolutionPath(solutionPath);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        // Then
        assertEquals(id, entity.getId());
        assertEquals(mazeData, entity.getMazeData());
        assertEquals(width, entity.getWidth());
        assertEquals(height, entity.getHeight());
        assertEquals(startRow, entity.getStartRow());
        assertEquals(startCol, entity.getStartCol());
        assertEquals(endRow, entity.getEndRow());
        assertEquals(endCol, entity.getEndCol());
        assertEquals(solved, entity.isSolved());
        assertEquals(solutionPath, entity.getSolutionPath());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void testConvertFromDomainModel() {
        // Given
        Maze maze = new Maze(3, 3);
        // Simple maze with walls on the outside
        maze.setCell(0, 0, Maze.WALL);
        maze.setCell(1, 0, Maze.WALL);
        maze.setCell(2, 0, Maze.WALL);
        maze.setCell(0, 1, Maze.START);
        maze.setCell(1, 1, Maze.EMPTY);
        maze.setCell(2, 1, Maze.END);
        maze.setCell(0, 2, Maze.WALL);
        maze.setCell(1, 2, Maze.WALL);
        maze.setCell(2, 2, Maze.WALL);

        // Add a solution path
        List<Position> solutionPath = new ArrayList<>();
        solutionPath.add(new Position(1, 0));
        solutionPath.add(new Position(1, 1));
        solutionPath.add(new Position(1, 2));
        maze.setSolvedPath(solutionPath);

        // When
        MazeEntity entity = MazeEntity.fromDomain(maze);

        // Then
        assertEquals(maze.getWidth(), entity.getWidth());
        assertEquals(maze.getHeight(), entity.getHeight());
        assertEquals(0, entity.getStartRow()); // Start position at (0, 1)
        assertEquals(1, entity.getStartCol());
        assertEquals(2, entity.getEndRow()); // End position at (2, 1)
        assertEquals(1, entity.getEndCol());
        assertTrue(entity.isSolved());
        assertNotNull(entity.getSolutionPath());

        // Verify the solution path is correctly serialized
        String expectedPath = "1,0;1,1;1,2";
        assertEquals(expectedPath, entity.getSolutionPath());
    }

    @Test
    void testConvertToDomainModel() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setId(UUID.randomUUID());
        entity.setMazeData("www\nsew\nwww");
        entity.setWidth(3);
        entity.setHeight(3);
        entity.setStartRow(1);
        entity.setStartCol(0);
        entity.setEndRow(1);
        entity.setEndCol(2);
        entity.setSolved(true);
        entity.setSolutionPath("1,0;1,1;1,2");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When
        Maze maze = entity.toDomain();

        // Then
        assertEquals(entity.getWidth(), maze.getWidth());
        assertEquals(entity.getHeight(), maze.getHeight());
        assertEquals(Maze.WALL, maze.getCell(0, 0));
        assertEquals(Maze.WALL, maze.getCell(0, 1));
        assertEquals(Maze.WALL, maze.getCell(0, 2));
        assertEquals(Maze.START, maze.getCell(1, 0));
        assertEquals(Maze.END, maze.getCell(1, 1));
        assertEquals(Maze.WALL, maze.getCell(1, 2));
        assertEquals(Maze.WALL, maze.getCell(2, 0));
        assertEquals(Maze.WALL, maze.getCell(2, 1));
        assertEquals(Maze.WALL, maze.getCell(2, 2));

        // Verify solution path is loaded
        assertTrue(maze.isSolved());
        assertNotNull(maze.getSolvedPath());
        assertEquals(3, maze.getSolvedPath().size());
        assertEquals(new Position(1, 0), maze.getSolvedPath().get(0));
        assertEquals(new Position(1, 1), maze.getSolvedPath().get(1));
        assertEquals(new Position(1, 2), maze.getSolvedPath().get(2));
    }

    @Test
    void testGetStartAndEndPositions() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setStartRow(1);
        entity.setStartCol(2);
        entity.setEndRow(3);
        entity.setEndCol(4);

        // When
        Position start = entity.getStartPosition();
        Position end = entity.getEndPosition();

        // Then
        assertEquals(1, start.row());
        assertEquals(2, start.col());
        assertEquals(3, end.row());
        assertEquals(4, end.col());
    }
}
