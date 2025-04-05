package io.jistud.mazesolver.server.service;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.jistud.mazesolver.server.repository.MazeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Position start = mazeService.getStartPosition(maze);
        Position end = mazeService.getEndPosition(maze);
        
        assertNotNull(start);
        assertNotNull(end);
        assertTrue(maze.isValidPosition(start));
        assertTrue(maze.isValidPosition(end));
        
        // Verify the maze was saved to the repository
        verify(mazeRepository).save(any(MazeEntity.class));
    }
    
    @Test
    void testGetMazeById() {
        // Given
        UUID id = UUID.randomUUID();
        MazeEntity mazeEntity = new MazeEntity();
        mazeEntity.setId(id);
        mazeEntity.setMazeData("###\n#.#\n###");
        mazeEntity.setWidth(3);
        mazeEntity.setHeight(3);
        mazeEntity.setStartX(1);
        mazeEntity.setStartY(1);
        mazeEntity.setEndX(1);
        mazeEntity.setEndY(1);
        
        when(mazeRepository.findById(id)).thenReturn(Optional.of(mazeEntity));
        
        // When
        Optional<Maze> result = mazeService.getMazeById(id);
        
        // Then
        assertTrue(result.isPresent());
        Maze maze = result.get();
        assertEquals(3, maze.getWidth());
        assertEquals(3, maze.getHeight());
        
        // Verify positions
        Position start = mazeService.getStartPosition(maze);
        Position end = mazeService.getEndPosition(maze);
        
        assertEquals(1, start.getX());
        assertEquals(1, start.getY());
        assertEquals(1, end.getX());
        assertEquals(1, end.getY());
    }
    
    @Test
    void testGetMazeById_NotFound() {
        // Given
        UUID id = UUID.randomUUID();
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
        maze1.setId(UUID.randomUUID());
        maze1.setMazeData("###\n#.#\n###");
        maze1.setWidth(3);
        maze1.setHeight(3);
        maze1.setStartX(1);
        maze1.setStartY(1);
        maze1.setEndX(1);
        maze1.setEndY(1);
        
        MazeEntity maze2 = new MazeEntity();
        maze2.setId(UUID.randomUUID());
        maze2.setMazeData("####\n#..#\n####");
        maze2.setWidth(4);
        maze2.setHeight(3);
        maze2.setStartX(1);
        maze2.setStartY(1);
        maze2.setEndX(2);
        maze2.setEndY(1);
        
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
        UUID id = UUID.randomUUID();
        
        // When
        mazeService.deleteMaze(id);
        
        // Then
        verify(mazeRepository).deleteById(id);
    }
    
    @Test
    void testSolveMaze() {
        // Given
        Maze maze = new Maze(5, 5);
        // Create a simple maze with a clear path
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                maze.setCell(x, y, Maze.WALL);
            }
        }
        
        // Create a path from (1,1) to (3,3)
        maze.setCell(1, 1, Maze.EMPTY);
        maze.setCell(1, 2, Maze.EMPTY);
        maze.setCell(1, 3, Maze.EMPTY);
        maze.setCell(2, 3, Maze.EMPTY);
        maze.setCell(3, 3, Maze.EMPTY);
        
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        
        // Mock the repository to return our maze for a specific ID
        UUID id = UUID.randomUUID();
        MazeEntity mazeEntity = MazeEntity.fromDomain(maze, start, end);
        mazeEntity.setId(id);
        when(mazeRepository.findById(id)).thenReturn(Optional.of(mazeEntity));
        
        // When
        Optional<List<Position>> solutionOpt = mazeService.solveMaze(id);
        
        // Then
        assertTrue(solutionOpt.isPresent());
        List<Position> solution = solutionOpt.get();
        assertFalse(solution.isEmpty());
        
        // Verify start and end positions
        assertEquals(start, solution.get(0));
        assertEquals(end, solution.get(solution.size() - 1));
        
        // Verify all positions in the solution are valid and empty cells
        for (Position pos : solution) {
            assertTrue(maze.isValidPosition(pos));
            assertEquals(Maze.EMPTY, maze.getCell(pos.getX(), pos.getY()));
        }
        
        // Verify adjacent positions in the solution are neighbors
        for (int i = 0; i < solution.size() - 1; i++) {
            Position current = solution.get(i);
            Position next = solution.get(i + 1);
            assertTrue(isNeighbor(current, next));
        }
    }
    
    private boolean isNeighbor(Position p1, Position p2) {
        int dx = Math.abs(p1.getX() - p2.getX());
        int dy = Math.abs(p1.getY() - p2.getY());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
}
