package io.jistud.mazesolver.server.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.jistud.mazesolver.server.builder.MazeBuilder;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    @Test
    @DisplayName("Maze initialization should create grid with proper dimensions")
    void initializeMaze() {
        Maze maze = new Maze(5, 10);

        assertEquals(5, maze.getHeight());
        assertEquals(10, maze.getWidth());
        assertEquals(Maze.EMPTY, maze.getCell(2, 5));
    }

    @Test
    @DisplayName("Maze initialization should throw exception for invalid dimensions")
    void initializeMazeWithInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Maze(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Maze(10, 0));
        assertThrows(IllegalArgumentException.class, () -> new Maze(-5, 10));
        assertThrows(IllegalArgumentException.class, () -> new Maze(10, -5));
    }

    @Test
    @DisplayName("Setting and getting cell values should work correctly")
    void setCellAndGetCell() {
        Maze maze = new Maze(5, 5);

        maze.setCell(2, 3, Maze.WALL);
        assertEquals(Maze.WALL, maze.getCell(2, 3));

        maze.setCell(1, 4, Maze.START);
        assertEquals(Maze.START, maze.getCell(1, 4));
    }

    @Test
    @DisplayName("Setting invalid cell values should throw exception")
    void setInvalidCellValue() {
        Maze maze = new Maze(5, 5);

        assertThrows(IllegalArgumentException.class, () -> maze.setCell(2, 3, 'x'));
    }

    @ParameterizedTest
    @CsvSource({"0, 0, true", "4, 4, true", "5, 5, false", "-1, 0, false", "0, -1, false", "5, 0, false", "0, 5, false"
    })
    @DisplayName("isValidPosition should return correct values")
    void isValidPosition(int row, int col, boolean expected) {
        Maze maze = new Maze(5, 5);
        assertEquals(expected, maze.isValidPosition(row, col));
    }

    @Test
    @DisplayName("Setting cell outside maze boundaries should throw exception")
    void setCellOutOfBounds() {
        Maze maze = new Maze(5, 5);

        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(5, 0, Maze.WALL));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(0, 5, Maze.WALL));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(-1, 0, Maze.WALL));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(0, -1, Maze.WALL));
    }

    @Test
    @DisplayName("Getting cell outside maze boundaries should throw exception")
    void getCellOutOfBounds() {
        Maze maze = new Maze(5, 5);

        assertThrows(IndexOutOfBoundsException.class, () -> maze.getCell(5, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getCell(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getCell(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.getCell(0, -1));
    }

    @Test
    @DisplayName("Finding cells with specific values should work correctly")
    void findCellsWithValue() {
        Maze maze = new Maze(5, 5);

        maze.setCell(1, 1, Maze.START);
        maze.setCell(3, 3, Maze.END);
        maze.setCell(2, 2, Maze.PATH);
        maze.setCell(0, 4, Maze.PATH);

        var startPositions = maze.findCellsWithValue(Maze.START);
        assertEquals(1, startPositions.size());
        assertTrue(startPositions.stream().anyMatch(p -> p.row() == 1 && p.col() == 1));

        var pathPositions = maze.findCellsWithValue(Maze.PATH);
        assertEquals(2, pathPositions.size());
    }

    @Test
    @DisplayName("toString should display a human-readable representation of the maze")
    void testToString() {
        Maze maze = new Maze(3, 3);
        maze.setCell(0, 0, Maze.START);
        maze.setCell(2, 2, Maze.END);
        maze.setCell(1, 1, Maze.WALL);

        String expected = "s  \n" + " w \n" + "  e\n";

        assertEquals(expected, maze.toString());
    }

    @Test
    @DisplayName("solve should solve the maze correctly")
    void solve() {
        char[][] grid = {
            {' ', ' ', 'w', ' ', 'w', 'w', 'w', 'w'},
            {'w', 's', 'w', ' ', ' ', ' ', ' ', 'w'},
            {' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w'},
            {' ', ' ', 'w', 'w', ' ', 'w', 'e', 'w'},
            {'w', 'w', 'w', 'w', ' ', ' ', ' ', 'w'}
        };
        Maze maze = new Maze(5, 8, grid);
        Boolean isSolvalbe = maze.solve();
        String solvedMaze = "  w wwww\n" + "wswpp  w\n" + " ppppw w\n" + "  wwpwew\n" + "wwwwpppw\n";

        assertEquals(true, isSolvalbe);
        assertEquals(solvedMaze, maze.toString());
    }

    @Test
    @DisplayName("solve should solve the random maze correctly")
    void solveRandom() {
        Maze maze = MazeBuilder.builder()
                .height(5)
                .width(8)
                .start(1, 1)
                .end(3, 6)
                .withRandomPath()
                .withRandomWalls(0.5)
                .withPerimeterWalls()
                .withEmptyPath()
                .build();
        Boolean isSolvalbe = maze.solve();
        assertEquals(true, isSolvalbe);
    }
}
