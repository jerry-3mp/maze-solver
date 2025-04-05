package io.jistud.mazesolver.server.builder;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

/**
 * Builder for creating and configuring Maze objects.
 * Uses a staged builder pattern to provide a fluent API for maze generation
 * with compile-time enforcement of the correct building sequence.
 */
public class MazeBuilder {

    private MazeBuilder() {
        // Private constructor to force usage of builder() method
    }

    /**
     * Creates a new MazeBuilder instance.
     *
     * @return a new DimensionStage builder
     */
    public static DimensionStage builder() {
        return new BuilderImpl();
    }

    /**
     * Stage for setting the dimensions of the maze.
     * This is the initial stage in the builder sequence.
     */
    public interface DimensionStage {
        /**
         * Sets the height of the maze.
         *
         * @param height the height of the maze in cells
         * @return the DimensionStage for continued configuration
         * @throws IllegalArgumentException if height is less than 2
         */
        DimensionStage height(int height);

        /**
         * Sets the width of the maze and transitions to the position stage.
         *
         * @param width the width of the maze in cells
         * @return the PositionStage for setting start and end positions
         * @throws IllegalArgumentException if width is less than 2
         */
        PositionStage width(int width);
    }

    /**
     * Stage for setting positions in the maze.
     */
    public interface PositionStage {
        /**
         * Sets the start position of the maze.
         *
         * @param row the row index of the start position
         * @param col the column index of the start position
         * @return the PositionStage for continued configuration
         * @throws IllegalArgumentException if the position is outside the maze boundaries
         */
        PositionStage start(int row, int col);

        /**
         * Sets the end position of the maze and transitions to the path stage.
         *
         * @param row the row index of the end position
         * @param col the column index of the end position
         * @return the PathStage for configuring the maze path
         * @throws IllegalArgumentException if the position is outside the maze boundaries or conflicts with the start position
         */
        PathStage end(int row, int col);

        /**
         * Automatically sets random start and end positions and transitions to the path stage.
         * The positions will be placed on opposite sides of the maze boundary when possible.
         *
         * @return the PathStage for configuring the maze path
         */
        PathStage randomStartAndEnd();
    }

    /**
     * Stage for configuring the path in the maze.
     */
    public interface PathStage {
        /**
         * Generates a random path between start and end positions and transitions to the wall stage.
         * Uses a randomized algorithm to create a path that is guaranteed to connect start and end positions.
         *
         * @return the WallStage for configuring maze walls
         */
        WallStage withRandomPath();
    }

    /**
     * Stage for adding walls to the maze.
     */
    public interface WallStage {
        /**
         * Adds random walls throughout the maze with the specified density.
         * Will not block the path between start and end positions.
         *
         * @param density the density of walls (0.0 to 1.0) where 0 means no walls and 1 means maximum walls without blocking the path
         * @return the WallStage for continued configuration
         * @throws IllegalArgumentException if density is not between 0.0 and 1.0
         */
        WallStage withRandomWalls(double density);

        /**
         * Creates walls around the perimeter of the maze.
         *
         * @return the FinalStage for continued configuration
         */
        FinalStage withPerimeterWalls();
    }

    /**
     * Final stage for completing the maze build.
     */
    public interface FinalStage extends BuildStage {
        /**
         * Clears all path cells to make the maze unsolved.
         *
         * @return the FinalStage for finishing the build
         */
        FinalStage withEmptyPath();
    }

    /**
     * Common interface for all stages that can build the maze.
     * Provides the build method that can be called at any allowed stage to finalize the maze creation.
     */
    public interface BuildStage {
        /**
         * Builds and returns the final Maze object.
         *
         * @return the constructed Maze
         * @throws IllegalStateException if required properties are not set
         */
        Maze build();
    }

    /**
     * Internal implementation of the builder with stage transitions.
     */
    private static class BuilderImpl implements DimensionStage, PositionStage, PathStage, WallStage, FinalStage {
        private int height;
        private int width;
        private Position startPosition;
        private Position endPosition;
        private char[][] grid;

        @Override
        public DimensionStage height(int height) {
            if (height < 5) {
                throw new IllegalArgumentException("Height must be at least 2, but was " + height);
            }
            this.height = height;
            return this;
        }

        @Override
        public PositionStage width(int width) {
            if (width < 5) {
                throw new IllegalArgumentException("Width must be at least 2, but was " + width);
            }
            this.width = width;
            return this;
        }

        @Override
        public PositionStage start(int row, int col) {
            validatePosition(row, col, "Start position");
            this.startPosition = new Position(row, col);
            return this;
        }

        @Override
        public PathStage end(int row, int col) {
            validatePosition(row, col, "End position");
            Position endPos = new Position(row, col);

            if (endPos.equals(startPosition)) {
                throw new IllegalArgumentException("End position cannot be the same as start position");
            }

            this.endPosition = endPos;
            return this;
        }

        @Override
        public PathStage randomStartAndEnd() {
            // Generate random start and end positions on opposite sides when possible
            java.util.Random random = new java.util.Random();

            // Decide if positioning horizontally or vertically
            boolean horizontalPlacement = random.nextBoolean();

            if (horizontalPlacement) {
                // Start on left, end on right (or vice versa)
                int startRow = 1 + random.nextInt(height - 2);
                int endRow = 1 + random.nextInt(height - 2);

                if (random.nextBoolean()) {
                    // Start on left, end on right
                    this.startPosition = new Position(startRow, 1);
                    this.endPosition = new Position(endRow, width - 2);
                } else {
                    // Start on right, end on left
                    this.startPosition = new Position(startRow, width - 2);
                    this.endPosition = new Position(endRow, 1);
                }
            } else {
                // Start on top, end on bottom (or vice versa)
                int startCol = 1 + random.nextInt(width - 2);
                int endCol = 1 + random.nextInt(width - 2);

                if (random.nextBoolean()) {
                    // Start on top, end on bottom
                    this.startPosition = new Position(1, startCol);
                    this.endPosition = new Position(height - 2, endCol);
                } else {
                    // Start on bottom, end on top
                    this.startPosition = new Position(height - 2, startCol);
                    this.endPosition = new Position(1, endCol);
                }
            }

            return this;
        }

        @Override
        public WallStage withRandomPath() {
            // Initialize grid if not already initialized
            if (grid == null) {
                initializeGrid();
            }

            // Mark start and end positions
            grid[startPosition.row()][startPosition.col()] = 's';
            grid[endPosition.row()][endPosition.col()] = 'e';

            // Generate random path from start to end
            generateRandomPath();

            return this;
        }

        /**
         * Initializes the grid with empty cells.
         */
        private void initializeGrid() {
            grid = new char[height][width];

            // Fill grid with empty cells
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    grid[row][col] = ' ';
                }
            }
        }

        /**
         * Generates a random path from start to end position using a random walk with backtracking.
         */
        private void generateRandomPath() {
            // Create a copy of the grid for path finding
            char[][] tempGrid = new char[height][width];
            for (int i = 0; i < height; i++) {
                System.arraycopy(grid[i], 0, tempGrid[i], 0, width);
            }

            // Keep track of the path
            java.util.List<Position> pathSteps = new java.util.ArrayList<>();
            java.util.Set<Position> visited = new java.util.HashSet<>();

            // Start from the start position
            Position current = startPosition;
            pathSteps.add(current);
            visited.add(current);

            // Random generator
            java.util.Random random = new java.util.Random();

            // Continue until we reach the end
            while (!current.equals(endPosition)) {
                // Get valid neighbors (up, down, left, right)
                java.util.List<Position> neighbors = getUnvisitedNeighbors(current, visited);

                if (neighbors.isEmpty()) {
                    // Dead end - remove current step and backtrack
                    if (pathSteps.size() > 1) { // Ensure we don't remove the start
                        pathSteps.removeLast();
                        current = pathSteps.getLast();

                        // Backtrack to a random previous position
                        int backtrackIndex = random.nextInt(pathSteps.size());

                        // Optionally prune the path after the backtrack point for more randomness
                        if (backtrackIndex < pathSteps.size() - 1) {
                            // Remove corresponding positions from the visited set using forEach
                            pathSteps
                                    .subList(backtrackIndex + 1, pathSteps.size())
                                    .forEach(posToRemove -> {
                                        // Don't unvisit the start or end position
                                        if (!posToRemove.equals(startPosition) && !posToRemove.equals(endPosition)) {
                                            visited.remove(posToRemove);
                                        }
                                    });

                            // Then clear the path steps
                            pathSteps
                                    .subList(backtrackIndex + 1, pathSteps.size())
                                    .clear();
                            current = pathSteps.get(backtrackIndex);
                        }
                    }
                } else {
                    // Move to a random unvisited neighbor
                    current = neighbors.get(random.nextInt(neighbors.size()));
                    pathSteps.add(current);
                    visited.add(current);

                    // If we've reached the end, break
                    if (current.equals(endPosition)) {
                        break;
                    }
                }
            }

            // Mark the path in the actual grid
            for (Position pos : pathSteps) {
                // Don't overwrite start and end markers
                if (!pos.equals(startPosition) && !pos.equals(endPosition)) {
                    grid[pos.row()][pos.col()] = 'p';
                }
            }
        }

        /**
         * Gets unvisited neighboring positions (up, down, left, right).
         *
         * @param pos the current position
         * @param visited set of already visited positions
         * @return list of valid unvisited neighbors
         */
        private java.util.List<Position> getUnvisitedNeighbors(Position pos, java.util.Set<Position> visited) {
            int row = pos.row();
            int col = pos.col();
            java.util.List<Position> neighbors = new java.util.ArrayList<>();

            // Check all four directions
            Position[] possibleNeighbors = {
                new Position(row - 1, col), // Up
                new Position(row + 1, col), // Down
                new Position(row, col - 1), // Left
                new Position(row, col + 1) // Right
            };

            for (Position neighbor : possibleNeighbors) {
                // Check if position is valid and not visited
                if (isValidPosition(neighbor) && !visited.contains(neighbor)) {
                    // Special case: allow destination even if visited
                    if (neighbor.equals(endPosition)) {
                        return java.util.List.of(endPosition); // Prioritize reaching the end
                    }
                    neighbors.add(neighbor);
                }
            }

            return neighbors;
        }

        /**
         * Checks if a position is valid within the maze.
         *
         * @param pos the position to check
         * @return true if position is valid, false otherwise
         */
        private boolean isValidPosition(Position pos) {
            return pos.row() >= 0 && pos.row() < height && pos.col() >= 0 && pos.col() < width;
        }

        @Override
        public WallStage withRandomWalls(double density) {
            if (density < 0.0 || density > 1.0) {
                throw new IllegalArgumentException("Wall density must be between 0.0 and 1.0, but was " + density);
            }

            // Initialize grid if not already initialized
            if (grid == null) {
                initializeGrid();
            }

            // Create a copy of the grid to work with
            char[][] tempGrid = new char[height][width];
            for (int i = 0; i < height; i++) {
                System.arraycopy(grid[i], 0, tempGrid[i], 0, width);
            }

            // Collect all positions that are empty and not part of the path
            java.util.List<Position> emptyPositions = new java.util.ArrayList<>();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (tempGrid[row][col] == ' ') {
                        emptyPositions.add(new Position(row, col));
                    }
                }
            }

            // Shuffle the empty positions
            java.util.Collections.shuffle(emptyPositions);

            // Calculate number of walls to add based on density
            int totalEmptyCells = emptyPositions.size();
            int wallsToAdd = (int) Math.round(totalEmptyCells * density);

            // Add walls at random positions
            int wallsAdded = 0;

            for (Position pos : emptyPositions) {
                if (wallsAdded >= wallsToAdd) {
                    break;
                }

                // Add a wall at this position
                grid[pos.row()][pos.col()] = 'w';
                wallsAdded++;
            }

            return this;
        }

        @Override
        public FinalStage withPerimeterWalls() {
            // Initialize grid if not already initialized
            if (grid == null) {
                initializeGrid();
            }

            // Add walls to the top and bottom rows, preserving special cells
            for (int col = 0; col < width; col++) {
                // Top row - add wall only if not a special cell
                if (grid[0][col] == ' ') {
                    grid[0][col] = 'w';
                }

                // Bottom row - add wall only if not a special cell
                if (grid[height - 1][col] == ' ') {
                    grid[height - 1][col] = 'w';
                }
            }

            // Add walls to the left and right columns, preserving special cells
            for (int row = 0; row < height; row++) {
                // Left column - add wall only if not a special cell
                if (grid[row][0] == ' ') {
                    grid[row][0] = 'w';
                }

                // Right column - add wall only if not a special cell
                if (grid[row][width - 1] == ' ') {
                    grid[row][width - 1] = 'w';
                }
            }

            return this;
        }

        @Override
        public FinalStage withEmptyPath() {
            // Unmark path cells
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (grid[row][col] == 'p') {
                        grid[row][col] = ' ';
                    }
                }
            }

            return this;
        }

        @Override
        public Maze build() {
            // Initialize grid if not already initialized
            if (grid == null) {
                initializeGrid();
            }

            // Validate that we have start and end positions
            if (startPosition == null) {
                throw new IllegalStateException("Start position must be set before building the maze");
            }
            if (endPosition == null) {
                throw new IllegalStateException("End position must be set before building the maze");
            }

            // Create a new maze with the specified dimensions with given grid
            return new Maze(height, width, grid);
        }

        /**
         * Validates that a position is within the maze boundaries.
         *
         * @param row the row index to validate
         * @param col the column index to validate
         * @param positionName name of the position for error message
         * @throws IllegalArgumentException if the position is outside the maze boundaries
         */
        private void validatePosition(int row, int col, String positionName) {
            if (row < 0 || row >= height) {
                throw new IllegalArgumentException(
                        positionName + " row must be between 0 and " + (height - 1) + ", but was " + row);
            }
            if (col < 0 || col >= width) {
                throw new IllegalArgumentException(
                        positionName + " column must be between 0 and " + (width - 1) + ", but was " + col);
            }
        }
    }
}
