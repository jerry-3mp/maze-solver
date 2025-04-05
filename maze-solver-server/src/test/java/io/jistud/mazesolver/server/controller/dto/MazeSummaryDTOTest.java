package io.jistud.mazesolver.server.controller.dto;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import io.jistud.mazesolver.server.entity.MazeEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeSummaryDTOTest {

    @Test
    void fromEntity_WithUnsolvedMaze_ShouldMapCorrectly() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setId(1);
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now();
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setSolved(false);
        entity.setMazeData("wwsww\nw   w\nw w w\nw   w\nwweww");

        // When
        MazeSummaryDTO dto = MazeSummaryDTO.fromEntity(entity);

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        assertFalse(dto.isSolved());
    }

    @Test
    void fromEntity_WithSolvedMaze_ShouldMapSolvedStatus() {
        // Given
        MazeEntity entity = new MazeEntity();
        entity.setId(1);
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now();
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setSolved(true);
        entity.setMazeData("wwsww\nw   w\nw w w\nw   w\nwweww");
        entity.setSolutionPath("[(2,0), (2,1), (1,1), (1,2), (1,3), (2,3), (2,4)]");

        // When
        MazeSummaryDTO dto = MazeSummaryDTO.fromEntity(entity);

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        assertTrue(dto.isSolved());
    }
}
