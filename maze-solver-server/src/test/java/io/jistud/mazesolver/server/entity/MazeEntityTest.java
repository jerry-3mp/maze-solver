package io.jistud.mazesolver.server.entity;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MazeEntityTest {

    @Test
    void testCreateMazeEntity() {
        // Given
        UUID id = UUID.randomUUID();
        String mazeData = "###\n#.#\n###";
        Position start = new Position(1, 0);
        Position end = new Position(1, 2);
        Instant createdAt = Instant.now();
        
        // When
        MazeEntity entity = new MazeEntity();
        entity.setId(id);
        entity.setMazeData(mazeData);
        entity.setStartX(start.getX());
        entity.setStartY(start.getY());
        entity.setEndX(end.getX());
        entity.setEndY(end.getY());
        entity.setWidth(3);
        entity.setHeight(3);
        entity.setCreatedAt(createdAt);
        
        // Then
        assertEquals(id, entity.getId());
        assertEquals(mazeData, entity.getMazeData());
        assertEquals(start.getX(), entity.getStartX());
        assertEquals(start.getY(), entity.getStartY());
        assertEquals(end.getX(), entity.getEndX());
        assertEquals(end.getY(), entity.getEndY());
        assertEquals(3, entity.getWidth());
        assertEquals(3, entity.getHeight());
        assertEquals(createdAt, entity.getCreatedAt());
    }
    
    @Test
    void testConvertFromDomainModel() {
        // Given
        Maze maze = new Maze(3, 3);
        // Simple maze with walls on the outside
        maze.setCell(0, 0, Maze.WALL);
        maze.setCell(1, 0, Maze.WALL);
        maze.setCell(2, 0, Maze.WALL);
        maze.setCell(0, 1, Maze.WALL);
        maze.setCell(1, 1, Maze.EMPTY);
        maze.setCell(2, 1, Maze.WALL);
        maze.setCell(0, 2, Maze.WALL);
        maze.setCell(1, 2, Maze.WALL);
        maze.setCell(2, 2, Maze.WALL);
        
        Position start = new Position(1, 1);
        Position end = new Position(1, 1); // Same for simplicity
        
        // When
        MazeEntity entity = MazeEntity.fromDomain(maze, start, end);
        
        // Then
        assertNotNull(entity.getId(), "ID should be generated");
        assertNotNull(entity.getCreatedAt(), "Created timestamp should be set");
        assertEquals(maze.getWidth(), entity.getWidth());
        assertEquals(maze.getHeight(), entity.getHeight());
        assertEquals(start.getX(), entity.getStartX());
        assertEquals(start.getY(), entity.getStartY());
        assertEquals(end.getX(), entity.getEndX());
        assertEquals(end.getY(), entity.getEndY());
        
        // Expect maze data to be properly serialized
        String expectedMazeData = "###\n#.#\n###";
        assertEquals(expectedMazeData, entity.getMazeData());
    }
    
    @Test
    void testConvertToDomainModel() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setId(UUID.randomUUID());
        entity.setMazeData("###\n#.#\n###");
        entity.setStartX(1);
        entity.setStartY(1);
        entity.setEndX(1);
        entity.setEndY(1);
        entity.setWidth(3);
        entity.setHeight(3);
        entity.setCreatedAt(Instant.now());
        
        // When
        Maze maze = entity.toDomain();
        Position start = entity.getStartPosition();
        Position end = entity.getEndPosition();
        
        // Then
        assertEquals(entity.getWidth(), maze.getWidth());
        assertEquals(entity.getHeight(), maze.getHeight());
        assertEquals(Maze.WALL, maze.getCell(0, 0));
        assertEquals(Maze.WALL, maze.getCell(1, 0));
        assertEquals(Maze.WALL, maze.getCell(2, 0));
        assertEquals(Maze.WALL, maze.getCell(0, 1));
        assertEquals(Maze.EMPTY, maze.getCell(1, 1));
        assertEquals(Maze.WALL, maze.getCell(2, 1));
        assertEquals(Maze.WALL, maze.getCell(0, 2));
        assertEquals(Maze.WALL, maze.getCell(1, 2));
        assertEquals(Maze.WALL, maze.getCell(2, 2));
        
        assertEquals(entity.getStartX(), start.getX());
        assertEquals(entity.getStartY(), start.getY());
        assertEquals(entity.getEndX(), end.getX());
        assertEquals(entity.getEndY(), end.getY());
    }
}
