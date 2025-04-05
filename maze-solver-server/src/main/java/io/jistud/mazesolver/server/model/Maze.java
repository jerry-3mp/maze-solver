package io.jistud.mazesolver.server.model;

import java.util.List;
import java.util.Set;

/**
 * Represents a maze with a 2D grid of characters.
 * The maze can contain:
 * 's' - start position
 * 'e' - end position
 * 'p' - path (solution)
 * 'w' - wall
 * ' ' - empty cell
 */
public class Maze {

    private final int height;
    private final int width;
    private final char[][] grid;

    // Valid cell values
    public static final char START = 's';
    public static final char END = 'e';
    public static final char PATH = 'p';
    public static final char WALL = 'w';
    public static final char EMPTY = ' ';

    private static final char[] VALID_CELL_VALUES = {START, END, PATH, WALL, EMPTY};

    /**
     * Creates a new maze with the specified dimensions.
     * All cells are initialized as empty.
     *
     * @param height the height of the maze
     * @param width the width of the maze
     * @throws IllegalArgumentException if height or width is less than or equal to 0
     */
    public Maze(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and width must be greater than 0");
        }

        this.height = height;
        this.width = width;
        this.grid = new char[height][width];

        // Initialize all cells as empty
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.grid[row][col] = EMPTY;
            }
        }
    }

    /**
     * Creates a new maze with the specified dimensions and grid.
     * Makes a deep copy of the provided grid.
     *
     * @param height the height of the maze
     * @param width the width of the maze
     * @param grid the pre-populated grid to use
     * @throws IllegalArgumentException if height or width is less than or equal to 0,
     *         or if grid dimensions don't match the specified height and width,
     *         or if grid contains invalid cell values
     */
    public Maze(int height, int width, char[][] grid) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and width must be greater than 0");
        }

        // Validate grid dimensions
        if (grid.length != height) {
            throw new IllegalArgumentException("Grid height does not match specified height");
        }

        for (int row = 0; row < height; row++) {
            if (grid[row].length != width) {
                throw new IllegalArgumentException("Grid width at row " + row + " does not match specified width");
            }
        }

        this.height = height;
        this.width = width;
        this.grid = new char[height][width];

        // Create a deep copy of the grid and validate cell values
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char value = grid[row][col];
                if (!isValidCellValue(value)) {
                    throw new IllegalArgumentException(
                            "Invalid cell value at position [" + row + ", " + col + "]: " + value);
                }
                this.grid[row][col] = value;
            }
        }
    }

    /**
     * Returns the height of the maze.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the maze.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the value of the cell at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return the character representing the cell value
     * @throws IndexOutOfBoundsException if the position is outside the maze boundaries
     */
    public char getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IndexOutOfBoundsException("Position [" + row + ", " + col + "] is outside the maze boundaries");
        }
        return grid[row][col];
    }

    /**
     * Sets the value of the cell at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @param value the character value to set ('s', 'e', 'p', 'w', ' ')
     * @throws IndexOutOfBoundsException if the position is outside the maze boundaries
     * @throws IllegalArgumentException if the value is not one of the allowed characters
     */
    public void setCell(int row, int col, char value) {
        if (!isValidPosition(row, col)) {
            throw new IndexOutOfBoundsException("Position [" + row + ", " + col + "] is outside the maze boundaries");
        }

        if (!isValidCellValue(value)) {
            throw new IllegalArgumentException("Invalid cell value: " + value);
        }

        grid[row][col] = value;
    }

    /**
     * Checks if the specified position is within the maze boundaries.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /**
     * Finds all cells with the specified value.
     *
     * @param value the value to search for
     * @return a list of positions that contain the specified value
     */
    public List<Position> findCellsWithValue(char value) {
        List<Position> positions = new java.util.ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col] == value) {
                    positions.add(new Position(row, col));
                }
            }
        }

        return positions;
    }

    public boolean solve() {
        Position startPosition = null;
        Position endPosition = null;

        List<Position> startPositions = findCellsWithValue(START);
        List<Position> endPositions = findCellsWithValue(END);

        if (startPositions.size() != 1 || endPositions.size() != 1) {
            throw new IllegalStateException("Maze must have exactly one start position and one end position");
        }

        startPosition = startPositions.getFirst();
        endPosition = endPositions.getFirst();
        java.util.Set<Position> visited = new java.util.HashSet<>();
        List<Position> ans = dfsTraverse(startPosition, endPosition, visited, new java.util.ArrayList<>());
        if (ans == null || ans.isEmpty()) {
            return false;
        } else {
            for (Position position : ans) {
                char cell = grid[position.row()][position.col()];
                if (cell == EMPTY) grid[position.row()][position.col()] = PATH;
            }
            return true;
        }
    }

    private List<Position> dfsTraverse(
            Position currentPosition, Position endPosition, Set<Position> visited, List<Position> pathSteps) {
        if (currentPosition == null || visited.contains(currentPosition)) return null;
        visited.add(currentPosition);
        List<Position> newPathSteps = new java.util.ArrayList<>(pathSteps);
        newPathSteps.add(currentPosition);
        if (currentPosition.equals(endPosition)) return newPathSteps;
        int rowIndex = currentPosition.row();
        int colIndex = currentPosition.col();
        Position upPosition = null;
        if (isValidPosition(rowIndex - 1, colIndex) && grid[rowIndex - 1][colIndex] != Maze.WALL) {
            upPosition = new Position(rowIndex - 1, colIndex);
        }
        Position downPosition = null;
        if (isValidPosition(rowIndex + 1, colIndex) && grid[rowIndex + 1][colIndex] != Maze.WALL) {
            downPosition = new Position(rowIndex + 1, colIndex);
        }
        Position leftPosition = null;
        if (isValidPosition(rowIndex, colIndex - 1) && grid[rowIndex][colIndex - 1] != Maze.WALL) {
            leftPosition = new Position(rowIndex, colIndex - 1);
        }
        Position rightPosition = null;
        if (isValidPosition(rowIndex, colIndex + 1) && grid[rowIndex][colIndex + 1] != Maze.WALL) {
            rightPosition = new Position(rowIndex, colIndex + 1);
        }
        List<Position> upPathSteps = dfsTraverse(upPosition, endPosition, visited, newPathSteps);
        if (upPathSteps != null) return upPathSteps;
        List<Position> downPathSteps = dfsTraverse(downPosition, endPosition, visited, newPathSteps);
        if (downPathSteps != null) return downPathSteps;
        List<Position> leftPathSteps = dfsTraverse(leftPosition, endPosition, visited, newPathSteps);
        if (leftPathSteps != null) return leftPathSteps;
        List<Position> rightPathSteps = dfsTraverse(rightPosition, endPosition, visited, newPathSteps);
        return rightPathSteps;
    }

    /**
     * Returns a string representation of the maze.
     *
     * @return a string showing the maze layout
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                sb.append(grid[row][col]);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Checks if the provided value is a valid cell value.
     *
     * @param value the value to check
     * @return true if the value is valid, false otherwise
     */
    private boolean isValidCellValue(char value) {
        for (char validValue : VALID_CELL_VALUES) {
            if (value == validValue) {
                return true;
            }
        }
        return false;
    }
}
