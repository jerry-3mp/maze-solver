package io.jistud.mazesolver.server.builder;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MazeBuilder Tests")
class MazeBuilderTest {

    @Nested
    @DisplayName("DimensionStage Tests")
    class DimensionStageTests {
        @Test
        @DisplayName("builder() should return a new DimensionStage instance")
        void builderShouldReturnNewDimensionStage() {
            // This test will initially fail due to UnsupportedOperationException
            // In the implemented version, this should pass
            assertNotNull(MazeBuilder.builder());
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 10, 20, 30})
        @DisplayName("height() should accept valid height values")
        void heightAcceptsValidValues(int height) {
            // In the implemented version, this should return a non-null object
            MazeBuilder.DimensionStage stage = MazeBuilder.builder().height(height);
            assertNotNull(stage);
        }

        @ParameterizedTest
        @ValueSource(ints = {-5, -1, 0, 1})
        @DisplayName("height() should reject invalid height values")
        void heightRejectsInvalidValues(int height) {
            // In the implemented version, this should throw IllegalArgumentException
            Exception exception = assertThrows(
                    IllegalArgumentException.class, () -> MazeBuilder.builder().height(height));
            assertFalse(exception.getMessage().contains("height"));
        }

        @ParameterizedTest
        @ValueSource(ints = {5, 10, 20, 30})
        @DisplayName("width() should accept valid width values")
        void widthAcceptsValidValues(int width) {
            // In the implemented version, this should return PositionStage
            MazeBuilder.PositionStage stage = MazeBuilder.builder().height(5).width(width);
            assertNotNull(stage);
        }

        @ParameterizedTest
        @ValueSource(ints = {-5, -1, 0, 1})
        @DisplayName("width() should reject invalid width values")
        void widthRejectsInvalidValues(int width) {
            // In the implemented version, this should throw IllegalArgumentException
            Exception exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(5).width(width));
            assertFalse(exception.getMessage().contains("width"));
        }
    }

    @Nested
    @DisplayName("PositionStage Tests")
    class PositionStageTests {

        @Test
        @DisplayName("start() should set start position")
        void startShouldSetStartPosition() {
            // In the implemented version, this should not throw and return PositionStage
            MazeBuilder.PositionStage stage =
                    MazeBuilder.builder().height(10).width(10).start(1, 1);
            assertNotNull(stage);
        }

        @Test
        @DisplayName("start() should reject positions outside maze boundaries")
        void startShouldRejectInvalidPositions() {
            // In the implemented version, these should throw IllegalArgumentException
            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(-1, 5));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(10, 5));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(5, -1));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(5, 10));
        }

        @Test
        @DisplayName("end() should set end position")
        void endShouldSetEndPosition() {
            // In the implemented version, this should not throw and return PathStage
            MazeBuilder.PathStage stage =
                    MazeBuilder.builder().height(10).width(10).start(1, 1).end(8, 8);
            assertNotNull(stage);
        }

        @Test
        @DisplayName("end() should reject positions outside maze boundaries")
        void endShouldRejectInvalidPositions() {
            // In the implemented version, these should throw IllegalArgumentException
            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(1, 1).end(-1, 5));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(1, 1).end(10, 5));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(1, 1).end(5, -1));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(1, 1).end(5, 10));
        }

        @Test
        @DisplayName("end() should reject same position as start")
        void endShouldRejectStartPosition() {
            // In the implemented version, this should throw IllegalArgumentException
            Exception exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> MazeBuilder.builder().height(10).width(10).start(1, 1).end(1, 1));
            assertTrue(exception.getMessage().contains("start position"));
        }

        @Test
        @DisplayName("randomStartAndEnd() should set random start and end positions")
        void randomStartAndEndShouldSetRandomPositions() {
            // In the implemented version, this should not throw and return PathStage
            MazeBuilder.PathStage stage =
                    MazeBuilder.builder().height(10).width(10).randomStartAndEnd();
            assertNotNull(stage);
        }
    }

    @Nested
    @DisplayName("PathStage Tests")
    class PathStageTests {
        @Test
        @DisplayName("withRandomPath() should generate a random path")
        void withRandomPathShouldGenerateRandomPath() {
            // In the implemented version, this should not throw and return WallStage
            MazeBuilder.WallStage stage = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath();
            assertNotNull(stage);
        }
    }

    @Nested
    @DisplayName("WallStage Tests")
    class WallStageTests {
        @Test
        @DisplayName("withRandomWalls() should add random walls")
        void withRandomWallsShouldAddRandomWalls() {
            // In the implemented version, this should not throw and return WallStage
            MazeBuilder.WallStage stage = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath()
                    .withRandomWalls(0.3);
            assertNotNull(stage);
        }

        @Test
        @DisplayName("withRandomWalls() should add walls based on density")
        void withRandomWallsShouldAddWallsBasedOnDensity() {
            // Create two mazes with different densities
            Maze lowDensityMaze = MazeBuilder.builder()
                    .height(15)
                    .width(15)
                    .start(1, 1)
                    .end(13, 13)
                    .withRandomPath()
                    .withRandomWalls(0.1)
                    .withPerimeterWalls()
                    .build();

            Maze highDensityMaze = MazeBuilder.builder()
                    .height(15)
                    .width(15)
                    .start(1, 1)
                    .end(13, 13)
                    .withRandomPath()
                    .withRandomWalls(0.5)
                    .withPerimeterWalls()
                    .build();

            // Verify both mazes have walls
            List<Position> lowDensityWalls = lowDensityMaze.findCellsWithValue('w');
            List<Position> highDensityWalls = highDensityMaze.findCellsWithValue('w');

            assertFalse(lowDensityWalls.isEmpty(), "Low density maze should have walls");
            assertFalse(highDensityWalls.isEmpty(), "High density maze should have walls");

            // Higher density should result in more walls
            assertTrue(highDensityWalls.size() > lowDensityWalls.size(), "Higher density should create more walls");
        }

        @Test
        @DisplayName("withRandomWalls() should preserve path between start and end")
        void withRandomWallsShouldPreservePath() {
            // Create a maze with random walls
            Maze maze = MazeBuilder.builder()
                    .height(12)
                    .width(12)
                    .start(1, 1)
                    .end(10, 10)
                    .withRandomPath()
                    .withRandomWalls(0.4)
                    .withPerimeterWalls()
                    .build();

            // Verify we have a start, end, and path
            List<Position> startPositions = maze.findCellsWithValue('s');
            List<Position> endPositions = maze.findCellsWithValue('e');
            List<Position> pathPositions = maze.findCellsWithValue('p');

            assertEquals(1, startPositions.size(), "Should have exactly one start position");
            assertEquals(1, endPositions.size(), "Should have exactly one end position");
            assertFalse(pathPositions.isEmpty(), "Should have a path from start to end");

            // Analyze if path is continuous by checking if each path cell is adjacent to another path cell
            // Start from start position and recursively check connectivity
            Position start = startPositions.getFirst();
            Position end = endPositions.getFirst();

            // Create a set of all path positions plus start and end for connectivity check
            java.util.Set<Position> pathSet = new java.util.HashSet<>(pathPositions);
            pathSet.add(start);
            pathSet.add(end);

            java.util.Set<Position> visited = new java.util.HashSet<>();
            java.util.Deque<Position> queue = new java.util.ArrayDeque<>();
            queue.add(start);
            visited.add(start);

            while (!queue.isEmpty()) {
                Position current = queue.poll();

                // If we've reached the end, the path is continuous
                if (current.equals(end)) {
                    break;
                }

                // Check all four directions
                Position[] neighbors = {
                    new Position(current.row() - 1, current.col()), // Up
                    new Position(current.row() + 1, current.col()), // Down
                    new Position(current.row(), current.col() - 1), // Left
                    new Position(current.row(), current.col() + 1) // Right
                };

                for (Position neighbor : neighbors) {
                    if (maze.isValidPosition(neighbor.row(), neighbor.col())
                            && pathSet.contains(neighbor)
                            && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            // Verify we've visited all path cells including the end position
            assertTrue(visited.contains(end), "Path should be continuous from start to end");
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -1.0, 1.1, 2.0})
        @DisplayName("withRandomWalls() should reject invalid density values")
        void withRandomWallsShouldRejectInvalidDensity(double density) {
            // In the implemented version, this should throw IllegalArgumentException
            Exception exception = assertThrows(IllegalArgumentException.class, () -> MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath()
                    .withRandomWalls(density));
            assertTrue(exception.getMessage().contains("density"));
        }

        @Test
        @DisplayName("withPerimeterWalls() should add walls around the perimeter")
        void withPerimeterWallsShouldAddPerimeterWalls() {
            // In the implemented version, this should not throw and return FinalStage
            MazeBuilder.FinalStage stage = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath()
                    .withPerimeterWalls();
            assertNotNull(stage);
        }

        @Test
        @DisplayName("withPerimeterWalls() should create walls on all four sides while preserving special cells")
        void withPerimeterWallsShouldCreateWallsWhilePreservingSpecialCells() {
            // Create a maze with start and end potentially on the perimeter
            Maze maze = MazeBuilder.builder()
                    .height(8)
                    .width(6)
                    .randomStartAndEnd() // Random start/end positions could be on perimeter
                    .withRandomPath()
                    .withPerimeterWalls()
                    .build();

            // Find special cells (start, end, path)
            List<Position> specialCells = new java.util.ArrayList<>();
            specialCells.addAll(maze.findCellsWithValue('s')); // Start
            specialCells.addAll(maze.findCellsWithValue('e')); // End
            specialCells.addAll(maze.findCellsWithValue('p')); // Path

            // Check top row (row 0)
            for (int col = 0; col < maze.getWidth(); col++) {
                Position pos = new Position(0, col);
                char cell = maze.getCell(0, col);
                if (!specialCells.contains(pos)) {
                    assertEquals('w', cell, "Top perimeter non-special cell should be a wall at column " + col);
                }
            }

            // Check bottom row (row height-1)
            for (int col = 0; col < maze.getWidth(); col++) {
                Position pos = new Position(maze.getHeight() - 1, col);
                char cell = maze.getCell(maze.getHeight() - 1, col);
                if (!specialCells.contains(pos)) {
                    assertEquals('w', cell, "Bottom perimeter non-special cell should be a wall at column " + col);
                }
            }

            // Check left column (col 0)
            for (int row = 0; row < maze.getHeight(); row++) {
                Position pos = new Position(row, 0);
                char cell = maze.getCell(row, 0);
                if (!specialCells.contains(pos)) {
                    assertEquals('w', cell, "Left perimeter non-special cell should be a wall at row " + row);
                }
            }

            // Check right column (col width-1)
            for (int row = 0; row < maze.getHeight(); row++) {
                Position pos = new Position(row, maze.getWidth() - 1);
                char cell = maze.getCell(row, maze.getWidth() - 1);
                if (!specialCells.contains(pos)) {
                    assertEquals('w', cell, "Right perimeter non-special cell should be a wall at row " + row);
                }
            }
        }

        @Test
        @DisplayName("withPerimeterWalls() should preserve start, end, and path")
        void withPerimeterWallsShouldPreservePathAndPositions() {
            Maze maze = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .randomStartAndEnd()
                    .withRandomPath()
                    .withPerimeterWalls()
                    .build();

            // Verify we have exactly one start and one end
            List<Position> startPositions = maze.findCellsWithValue('s');
            List<Position> endPositions = maze.findCellsWithValue('e');

            assertEquals(1, startPositions.size(), "Should have exactly one start position");
            assertEquals(1, endPositions.size(), "Should have exactly one end position");

            // Verify the path exists and connects start to end
            List<Position> pathPositions = maze.findCellsWithValue('p');
            assertFalse(pathPositions.isEmpty(), "Should have a path from start to end");

            // Verify start and end are not on the perimeter (since randomStartAndEnd places them away from edges)
            Position start = startPositions.getFirst();
            Position end = endPositions.getFirst();

            assertNotEquals(0, start.row(), "Start should not be on top perimeter");
            assertNotEquals(maze.getHeight() - 1, start.row(), "Start should not be on bottom perimeter");
            assertNotEquals(0, start.col(), "Start should not be on left perimeter");
            assertNotEquals(maze.getWidth() - 1, start.col(), "Start should not be on right perimeter");

            assertNotEquals(0, end.row(), "End should not be on top perimeter");
            assertNotEquals(maze.getHeight() - 1, end.row(), "End should not be on bottom perimeter");
            assertNotEquals(0, end.col(), "End should not be on left perimeter");
            assertNotEquals(maze.getWidth() - 1, end.col(), "End should not be on right perimeter");
        }
    }

    @Nested
    @DisplayName("FinalStage Tests")
    class FinalStageTests {
        @Test
        @DisplayName("withEmptyPath() should clear the path")
        void withEmptyPathShouldClearPath() {
            // In the implemented version, this should not throw and return FinalStage
            MazeBuilder.FinalStage stage = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath()
                    .withPerimeterWalls()
                    .withEmptyPath();
            assertNotNull(stage);
        }

        @Test
        @DisplayName("build() should create a maze object")
        void buildShouldCreateMaze() {
            // In the implemented version, this should not throw and return a Maze
            Maze maze = MazeBuilder.builder()
                    .height(10)
                    .width(10)
                    .start(1, 1)
                    .end(8, 8)
                    .withRandomPath()
                    .withPerimeterWalls()
                    .build();

            assertNotNull(maze);
            assertEquals(10, maze.getHeight());
            assertEquals(10, maze.getWidth());

            // Verify start position
            List<Position> startPositions = maze.findCellsWithValue('s');
            assertEquals(1, startPositions.size());
            assertEquals(new Position(1, 1), startPositions.get(0));

            // Verify end position
            List<Position> endPositions = maze.findCellsWithValue('e');
            assertEquals(1, endPositions.size());
            assertEquals(new Position(8, 8), endPositions.get(0));

            // Verify that we have walls
            List<Position> walls = maze.findCellsWithValue('w');
            assertFalse(walls.isEmpty());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {
        @Test
        @DisplayName("Should build a complete maze using all stages")
        void shouldBuildCompleteMazeUsingAllStages() {
            // In the implemented version, this should create a valid Maze
            Maze maze = MazeBuilder.builder()
                    .height(20)
                    .width(20)
                    .start(1, 1)
                    .end(18, 18)
                    .withRandomPath()
                    .withRandomWalls(0.2)
                    .withPerimeterWalls()
                    .withEmptyPath()
                    .build();

            assertNotNull(maze);
            assertEquals(20, maze.getHeight());
            assertEquals(20, maze.getWidth());

            // Verify start position
            List<Position> startPositions = maze.findCellsWithValue('s');
            assertEquals(1, startPositions.size());
            assertEquals(new Position(1, 1), startPositions.get(0));

            // Verify end position
            List<Position> endPositions = maze.findCellsWithValue('e');
            assertEquals(1, endPositions.size());
            assertEquals(new Position(18, 18), endPositions.get(0));

            // Verify that we have walls
            List<Position> walls = maze.findCellsWithValue('w');
            assertFalse(walls.isEmpty());

            // Verify that the path is empty (withEmptyPath was called)
            List<Position> paths = maze.findCellsWithValue('p');
            assertTrue(paths.isEmpty());
        }

        @Test
        @DisplayName("Should build a maze with random start/end positions")
        void shouldBuildMazeWithRandomStartEnd() {
            // In the implemented version, this should create a valid Maze
            Maze maze = MazeBuilder.builder()
                    .height(15)
                    .width(15)
                    .randomStartAndEnd()
                    .withRandomPath()
                    .withPerimeterWalls()
                    .build();

            assertNotNull(maze);
            assertEquals(15, maze.getHeight());
            assertEquals(15, maze.getWidth());

            // Verify that we have exactly one start position
            List<Position> startPositions = maze.findCellsWithValue('s');
            assertEquals(1, startPositions.size());

            // Verify that we have exactly one end position
            List<Position> endPositions = maze.findCellsWithValue('e');
            assertEquals(1, endPositions.size());

            // Verify that start and end are different positions
            assertNotEquals(startPositions.get(0), endPositions.get(0));

            // Verify that we have walls and a path
            List<Position> walls = maze.findCellsWithValue('w');
            assertFalse(walls.isEmpty());

            List<Position> paths = maze.findCellsWithValue('p');
            assertFalse(paths.isEmpty());
        }

        @Test
        @DisplayName("Should respect maze boundaries when building")
        void shouldRespectMazeBoundaries() {
            // In the implemented version, this should create a valid Maze
            Maze maze = MazeBuilder.builder()
                    .height(5)
                    .width(8)
                    .start(1, 1)
                    .end(3, 6)
                    .withRandomPath()
                    .withPerimeterWalls()
                    .build();

            assertNotNull(maze);
            assertEquals(5, maze.getHeight());
            assertEquals(8, maze.getWidth());

            // Find special cells (start, end, path)
            List<Position> specialCells = new java.util.ArrayList<>();
            specialCells.addAll(maze.findCellsWithValue('s')); // Start
            specialCells.addAll(maze.findCellsWithValue('e')); // End
            specialCells.addAll(maze.findCellsWithValue('p')); // Path

            // Check perimeter walls
            for (int row = 0; row < 5; row++) {
                if (!specialCells.contains(new Position(row, 0))) {
                    assertEquals('w', maze.getCell(row, 0), "Left wall missing at row " + row);
                }
                if (!specialCells.contains(new Position(row, 7))) {
                    assertEquals('w', maze.getCell(row, 7), "Right wall missing at row " + row);
                }
            }

            for (int col = 0; col < 8; col++) {
                if (!specialCells.contains(new Position(0, col))) {
                    assertEquals('w', maze.getCell(0, col), "Top wall missing at column " + col);
                }
                if (!specialCells.contains(new Position(4, col))) {
                    assertEquals('w', maze.getCell(4, col), "Bottom wall missing at column " + col);
                }
            }
        }
    }
}
