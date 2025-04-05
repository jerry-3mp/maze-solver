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

            // Check perimeter walls
            for (int row = 0; row < 5; row++) {
                assertEquals('w', maze.getCell(row, 0), "Left wall missing at row " + row);
                assertEquals('w', maze.getCell(row, 7), "Right wall missing at row " + row);
            }

            for (int col = 0; col < 8; col++) {
                assertEquals('w', maze.getCell(0, col), "Top wall missing at column " + col);
                assertEquals('w', maze.getCell(4, col), "Bottom wall missing at column " + col);
            }
        }
    }
}
