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
            throw new UnsupportedOperationException("Method not implemented yet");
        }

        @Override
        public WallStage withRandomWalls(double density) {
            throw new UnsupportedOperationException("Method not implemented yet");
        }

        @Override
        public FinalStage withPerimeterWalls() {
            throw new UnsupportedOperationException("Method not implemented yet");
        }

        @Override
        public FinalStage withEmptyPath() {
            throw new UnsupportedOperationException("Method not implemented yet");
        }

        @Override
        public Maze build() {
            throw new UnsupportedOperationException("Method not implemented yet");
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
