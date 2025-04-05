package io.jistud.mazesolver.server.builder;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

import static org.junit.jupiter.api.Assertions.*;

class MazeBuilderTest {

    @Test
    @DisplayName("Basic builder should create maze with specified dimensions")
    void basicBuilder() {
        Maze maze = MazeBuilder.builder().height(10).width(15).build();

        assertEquals(10, maze.getHeight());
        assertEquals(15, maze.getWidth());
    }

    @Test
    @DisplayName("Builder should throw exception with invalid dimensions")
    void builderWithInvalidDimensions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MazeBuilder.builder().height(0).width(10).build());

        assertThrows(
                IllegalArgumentException.class,
                () -> MazeBuilder.builder().height(10).width(0).build());
    }

    @Test
    @DisplayName("Builder should set start and end positions if specified")
    void builderWithExplicitStartAndEnd() {
        Maze maze =
                MazeBuilder.builder().height(10).width(10).start(0, 0).end(9, 9).build();

        assertEquals('s', maze.getCell(0, 0));
        assertEquals('e', maze.getCell(9, 9));
    }

    @RepeatedTest(5)
    @DisplayName("Builder should randomly select corner positions for start and end if not specified")
    void builderWithRandomStartAndEnd() {
        Maze maze =
                MazeBuilder.builder().height(10).width(10).randomStartAndEnd().build();

        List<Position> cornerPositions =
                Arrays.asList(new Position(0, 0), new Position(0, 9), new Position(9, 0), new Position(9, 9));

        var startPositions = maze.findCellsWithValue('s');
        var endPositions = maze.findCellsWithValue('e');

        assertEquals(1, startPositions.size());
        assertEquals(1, endPositions.size());

        Position start = startPositions.get(0);
        Position end = endPositions.get(0);

        assertTrue(cornerPositions.contains(start));
        assertTrue(cornerPositions.contains(end));
        assertNotEquals(start, end);
    }

    @Test
    @DisplayName("Builder should throw exception if start and end are same position")
    void builderWithSameStartAndEnd() {
        assertThrows(IllegalArgumentException.class, () -> MazeBuilder.builder()
                .height(10)
                .width(10)
                .start(0, 0)
                .end(0, 0)
                .build());
    }

    @Test
    @DisplayName("Builder should create walls around the perimeter")
    void builderWithPerimeterWalls() {
        Maze maze = MazeBuilder.builder()
                .height(5)
                .width(5)
                .start(0, 0) // Override corner wall at start
                .end(4, 4) // Override corner wall at end
                .withPerimeterWalls()
                .build();

        // Check top and bottom walls (excluding corners)
        for (int col = 1; col < 4; col++) {
            assertEquals('w', maze.getCell(0, col));
            assertEquals('w', maze.getCell(4, col));
        }

        // Check left and right walls (excluding corners)
        for (int row = 1; row < 4; row++) {
            assertEquals('w', maze.getCell(row, 0));
            assertEquals('w', maze.getCell(row, 4));
        }
    }

    @RepeatedTest(3)
    @DisplayName("Builder should generate random walls")
    void builderWithRandomWalls() {
        Maze maze = MazeBuilder.builder()
                .height(10)
                .width(10)
                .start(0, 0)
                .end(9, 9)
                .withRandomWalls(0.3) // 30% wall density
                .build();

        // Count walls
        int wallCount = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (maze.getCell(row, col) == 'w') {
                    wallCount++;
                }
            }
        }

        // Check start and end are preserved
        assertEquals('s', maze.getCell(0, 0));
        assertEquals('e', maze.getCell(9, 9));

        // With 30% density, we should have around 30 walls in a 10x10 maze (minus start and end positions)
        // Allow some variance due to randomness
        assertTrue(wallCount > 15 && wallCount < 45);
    }

    @Test
    @DisplayName("Builder should generate a random path between start and end")
    void builderWithRandomPath() {
        Maze maze = MazeBuilder.builder()
                .height(10)
                .width(10)
                .start(0, 0)
                .end(9, 9)
                .withRandomPath()
                .build();

        var pathCells = maze.findCellsWithValue('p');
        assertFalse(pathCells.isEmpty());

        // Start and end should be preserved
        assertEquals('s', maze.getCell(0, 0));
        assertEquals('e', maze.getCell(9, 9));
    }

    @Test
    @DisplayName("Builder should generate a complete random maze")
    void builderWithCompleteRandomMaze() {
        Maze maze = MazeBuilder.builder()
                .height(15)
                .width(15)
                .randomStartAndEnd()
                .withRandomPath()
                .withRandomWalls(0.2)
                .withEmptyPath() // Clear the path to make maze unsolved
                .build();

        // Verify there's exactly one start and one end
        assertEquals(1, maze.findCellsWithValue('s').size());
        assertEquals(1, maze.findCellsWithValue('e').size());

        // Path cells should be cleared
        assertTrue(maze.findCellsWithValue('p').isEmpty());

        // There should be some walls
        assertFalse(maze.findCellsWithValue('w').isEmpty());
    }

    @Test
    @DisplayName("Builder should clear path cells to make maze unsolved")
    void builderWithEmptyPath() {
        Maze maze = MazeBuilder.builder()
                .height(10)
                .width(10)
                .start(0, 0)
                .end(9, 9)
                .withRandomPath()
                .withEmptyPath() // Clear the path
                .build();

        // Path cells should be empty
        assertTrue(maze.findCellsWithValue('p').isEmpty());

        // Start and end should be preserved
        assertEquals('s', maze.getCell(0, 0));
        assertEquals('e', maze.getCell(9, 9));
    }
}
