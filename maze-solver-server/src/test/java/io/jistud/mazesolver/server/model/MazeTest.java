package io.jistud.mazesolver.server.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    @Test
    @DisplayName("Maze initialization should create grid with proper dimensions")
    void initializeMaze() {
        Maze maze = new Maze(5, 10);

        assertEquals(5, maze.getHeight());
        assertEquals(10, maze.getWidth());
        assertEquals(' ', maze.getCell(2, 5));
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

        maze.setCell(2, 3, 'w');
        assertEquals('w', maze.getCell(2, 3));

        maze.setCell(1, 4, 's');
        assertEquals('s', maze.getCell(1, 4));
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

        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(5, 0, 'w'));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(0, 5, 'w'));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(-1, 0, 'w'));
        assertThrows(IndexOutOfBoundsException.class, () -> maze.setCell(0, -1, 'w'));
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

        maze.setCell(1, 1, 's');
        maze.setCell(3, 3, 'e');
        maze.setCell(2, 2, 'p');
        maze.setCell(0, 4, 'p');

        var startPositions = maze.findCellsWithValue('s');
        assertEquals(1, startPositions.size());
        assertTrue(startPositions.stream().anyMatch(p -> p.row() == 1 && p.col() == 1));

        var pathPositions = maze.findCellsWithValue('p');
        assertEquals(2, pathPositions.size());
    }

    @Test
    @DisplayName("toString should display a human-readable representation of the maze")
    void testToString() {
        Maze maze = new Maze(3, 3);
        maze.setCell(0, 0, 's');
        maze.setCell(2, 2, 'e');
        maze.setCell(1, 1, 'w');

        String expected =
                """
                          s  \s
                           w \s
                            e\s
                          """
                        .stripIndent();

        assertEquals(expected, maze.toString());
    }
}
