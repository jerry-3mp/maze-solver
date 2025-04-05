package io.jistud.mazesolver.server.controller.dto;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import io.jistud.mazesolver.server.entity.MazeEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MazeSummaryListResponseTest {

    @Test
    void fromPage_ShouldTransformPageCorrectly() {
        // Given
        MazeEntity entity1 = new MazeEntity();
        entity1.setId(1);
        entity1.setCreatedAt(Instant.now().minusSeconds(7200));
        entity1.setUpdatedAt(Instant.now().minusSeconds(3600));
        entity1.setSolved(false);

        MazeEntity entity2 = new MazeEntity();
        entity2.setId(2);
        entity2.setCreatedAt(Instant.now().minusSeconds(3600));
        entity2.setUpdatedAt(Instant.now());
        entity2.setSolved(true);
        entity2.setSolutionPath("[(0,0), (1,0), (1,1)]");

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MazeEntity> entityPage = new PageImpl<>(List.of(entity1, entity2), pageRequest, 2);

        // When
        MazeSummaryListResponse response = MazeSummaryListResponse.fromPage(entityPage);

        // Then
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(1, response.getContent().get(0).getId());
        assertEquals(2, response.getContent().get(1).getId());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(10, response.getSize());
        assertEquals(0, response.getNumber());
    }

    @Test
    void fromPage_WithEmptyPage_ShouldReturnEmptyList() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MazeEntity> entityPage = new PageImpl<>(List.of(), pageRequest, 0);

        // When
        MazeSummaryListResponse response = MazeSummaryListResponse.fromPage(entityPage);

        // Then
        assertNotNull(response);
        assertEquals(0, response.getContent().size());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertEquals(10, response.getSize());
        assertEquals(0, response.getNumber());
    }
}
