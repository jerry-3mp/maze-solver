package io.jistud.mazesolver.server.repository;

import io.jistud.mazesolver.server.entity.MazeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MazeRepositoryTest {

    @Autowired
    private MazeRepository mazeRepository;

    @Test
    void testSaveAndFindById() {
        // Given
        MazeEntity maze = createTestMazeEntity();
        
        // When
        MazeEntity savedMaze = mazeRepository.save(maze);
        Optional<MazeEntity> foundMaze = mazeRepository.findById(savedMaze.getId());
        
        // Then
        assertTrue(foundMaze.isPresent());
        assertEquals(savedMaze.getId(), foundMaze.get().getId());
        assertEquals(savedMaze.getMazeData(), foundMaze.get().getMazeData());
        assertEquals(savedMaze.getWidth(), foundMaze.get().getWidth());
        assertEquals(savedMaze.getHeight(), foundMaze.get().getHeight());
        assertEquals(savedMaze.getStartX(), foundMaze.get().getStartX());
        assertEquals(savedMaze.getStartY(), foundMaze.get().getStartY());
        assertEquals(savedMaze.getEndX(), foundMaze.get().getEndX());
        assertEquals(savedMaze.getEndY(), foundMaze.get().getEndY());
    }

    @Test
    void testFindAll() {
        // Given
        mazeRepository.deleteAll(); // Clear any existing data
        MazeEntity maze1 = createTestMazeEntity();
        MazeEntity maze2 = createTestMazeEntity();
        mazeRepository.save(maze1);
        mazeRepository.save(maze2);
        
        // When
        List<MazeEntity> mazes = mazeRepository.findAll();
        
        // Then
        assertEquals(2, mazes.size());
    }

    @Test
    void testDelete() {
        // Given
        MazeEntity maze = createTestMazeEntity();
        MazeEntity savedMaze = mazeRepository.save(maze);
        
        // When
        mazeRepository.delete(savedMaze);
        Optional<MazeEntity> foundMaze = mazeRepository.findById(savedMaze.getId());
        
        // Then
        assertFalse(foundMaze.isPresent());
    }
    
    @Test
    void testFindByCreatedAtBefore() {
        // Given
        mazeRepository.deleteAll(); // Clear any existing data
        
        // Create mazes with different creation times
        MazeEntity oldMaze = createTestMazeEntity();
        oldMaze.setCreatedAt(Instant.now().minusSeconds(3600)); // 1 hour ago
        
        MazeEntity newMaze = createTestMazeEntity();
        newMaze.setCreatedAt(Instant.now());
        
        mazeRepository.save(oldMaze);
        mazeRepository.save(newMaze);
        
        // When
        Instant threshold = Instant.now().minusSeconds(1800); // 30 minutes ago
        List<MazeEntity> oldMazes = mazeRepository.findByCreatedAtBefore(threshold);
        
        // Then
        assertEquals(1, oldMazes.size());
        assertEquals(oldMaze.getId(), oldMazes.get(0).getId());
    }

    private MazeEntity createTestMazeEntity() {
        MazeEntity maze = new MazeEntity();
        maze.setMazeData("###\n#.#\n###");
        maze.setWidth(3);
        maze.setHeight(3);
        maze.setStartX(1);
        maze.setStartY(1);
        maze.setEndX(1);
        maze.setEndY(1);
        maze.setCreatedAt(Instant.now());
        return maze;
    }
}
