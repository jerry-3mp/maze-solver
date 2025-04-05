package io.jistud.mazesolver.server.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.jistud.mazesolver.server.repository.MazeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MazeServiceTest {

    @Mock
    private MazeRepository mazeRepository;

    private MazeService mazeService;

    @BeforeEach
    void setUp() {
        mazeService = new MazeServiceImpl(mazeRepository);
    }

    @Test
    void testGenerateRandomMaze() {
        // Given
        int width = 10;
        int height = 10;
        when(mazeRepository.save(any(MazeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Maze maze = mazeService.generateRandomMaze(width, height);

        // Then
        assertNotNull(maze);
        assertEquals(width, maze.getWidth());
        assertEquals(height, maze.getHeight());

        // Verify the maze has valid start and end positions
        List<Position> startPositions = maze.findCellsWithValue(Maze.START);
        List<Position> endPositions = maze.findCellsWithValue(Maze.END);

        assertEquals(1, startPositions.size(), "Maze should have exactly one start position");
        assertEquals(1, endPositions.size(), "Maze should have exactly one end position");

        // Verify the maze path cells has been removed
        List<Position> pathPositions = maze.findCellsWithValue(Maze.PATH);
        assertEquals(0, pathPositions.size(), "Maze should not have path cells");

        // Verify the maze was saved to the repository
        verify(mazeRepository).save(any(MazeEntity.class));
    }

    @Test
    void testGetMazeById() {
        // Given
        Integer id = new java.util.Random().nextInt();
        MazeEntity mazeEntity = new MazeEntity();
        mazeEntity.setId(id);
        mazeEntity.setMazeData("www\nsew\nwww");
        mazeEntity.setWidth(3);
        mazeEntity.setHeight(3);
        mazeEntity.setStartRow(1);
        mazeEntity.setStartCol(0);
        mazeEntity.setEndRow(1);
        mazeEntity.setEndCol(2);
        mazeEntity.setSolved(false);
        mazeEntity.setCreatedAt(Instant.now());

        when(mazeRepository.findById(id)).thenReturn(Optional.of(mazeEntity));

        // When
        Optional<Maze> result = mazeService.getMazeById(id);

        // Then
        assertTrue(result.isPresent());
        Maze maze = result.get();
        assertEquals(3, maze.getWidth());
        assertEquals(3, maze.getHeight());

        // Verify start and end positions in the maze
        List<Position> startPositions = maze.findCellsWithValue(Maze.START);
        List<Position> endPositions = maze.findCellsWithValue(Maze.END);

        assertEquals(1, startPositions.size(), "Maze should have exactly one start position");
        assertEquals(1, endPositions.size(), "Maze should have exactly one end position");
    }

    @Test
    void testGetMazeById_NotFound() {
        // Given
        Integer id = new java.util.Random().nextInt();
        when(mazeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Maze> result = mazeService.getMazeById(id);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllMazes() {
        // Given
        MazeEntity maze1 = new MazeEntity();
        maze1.setId(new java.util.Random().nextInt());
        maze1.setMazeData("www\nsew\nwww");
        maze1.setWidth(3);
        maze1.setHeight(3);
        maze1.setStartRow(1);
        maze1.setStartCol(0);
        maze1.setEndRow(1);
        maze1.setEndCol(2);
        maze1.setSolved(false);
        maze1.setCreatedAt(Instant.now());

        MazeEntity maze2 = new MazeEntity();
        maze2.setId(new java.util.Random().nextInt());
        maze2.setMazeData("wwww\ns  e\nwwww");
        maze2.setWidth(4);
        maze2.setHeight(3);
        maze2.setStartRow(1);
        maze2.setStartCol(0);
        maze2.setEndRow(1);
        maze2.setEndCol(3);
        maze2.setSolved(false);
        maze2.setCreatedAt(Instant.now());

        when(mazeRepository.findAll()).thenReturn(Arrays.asList(maze1, maze2));

        // When
        List<Maze> mazes = mazeService.getAllMazes();

        // Then
        assertEquals(2, mazes.size());
        assertEquals(3, mazes.get(0).getWidth());
        assertEquals(3, mazes.get(0).getHeight());
        assertEquals(4, mazes.get(1).getWidth());
        assertEquals(3, mazes.get(1).getHeight());
    }

    @Test
    void testDeleteMaze() {
        // Given
        Integer id = new java.util.Random().nextInt();

        // When
        mazeService.deleteMaze(id);

        // Then
        verify(mazeRepository).deleteById(id);
    }

    @Test
    void testSolveMaze_AlreadySolved() {
        // Given
        Integer id = new java.util.Random().nextInt();
        MazeEntity entity = new MazeEntity();
        entity.setId(id);
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

        when(mazeRepository.findById(id)).thenReturn(Optional.of(entity));

        // When
        Optional<List<Position>> solutionOpt = mazeService.solveMaze(id);

        // Then
        assertTrue(solutionOpt.isPresent());
        List<Position> solution = solutionOpt.get();
        assertEquals(3, solution.size());

        // Verify the solution matches the stored path
        assertEquals(new Position(1, 0), solution.get(0));
        assertEquals(new Position(1, 1), solution.get(1));
        assertEquals(new Position(1, 2), solution.get(2));

        // Verify the maze entity was not updated (already solved)
        verify(mazeRepository, never()).save(any(MazeEntity.class));
    }

    @Test
    void testSolveMaze_NotYetSolved() {
        // Given
        Integer id = new java.util.Random().nextInt();

        // Create a maze that can be solved
        char[][] grid = new char[5][5];
        // Fill with walls
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = Maze.WALL;
            }
        }

        // Create a path
        grid[1][1] = Maze.START;
        grid[1][2] = Maze.EMPTY;
        grid[1][3] = Maze.EMPTY;
        grid[2][3] = Maze.EMPTY;
        grid[3][3] = Maze.END;

        Maze maze = new Maze(5, 5, grid);

        MazeEntity entity = MazeEntity.fromDomain(maze);
        entity.setId(id);
        entity.setSolved(false);
        entity.setSolutionPath(null);

        when(mazeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mazeRepository.save(any(MazeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<List<Position>> solutionOpt = mazeService.solveMaze(id);

        // Then
        assertTrue(solutionOpt.isPresent());
        List<Position> solution = solutionOpt.get();
        assertFalse(solution.isEmpty());

        // Verify the solution path starts at START and ends at END
        List<Position> startPositions = maze.findCellsWithValue(Maze.START);
        List<Position> endPositions = maze.findCellsWithValue(Maze.END);

        assertEquals(startPositions.get(0), solution.get(0));
        assertEquals(endPositions.get(0), solution.get(solution.size() - 1));

        // Verify the maze entity was updated with the solution
        ArgumentCaptor<MazeEntity> entityCaptor = ArgumentCaptor.forClass(MazeEntity.class);
        verify(mazeRepository).save(entityCaptor.capture());

        MazeEntity savedEntity = entityCaptor.getValue();
        assertTrue(savedEntity.isSolved());
        assertNotNull(savedEntity.getSolutionPath());
        assertNotNull(savedEntity.getUpdatedAt());
    }
}
