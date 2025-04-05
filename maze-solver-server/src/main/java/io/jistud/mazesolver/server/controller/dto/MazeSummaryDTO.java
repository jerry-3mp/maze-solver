package io.jistud.mazesolver.server.controller.dto;

import java.time.Instant;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for summarized maze information.
 */
@Schema(description = "Summary information about a maze")
public class MazeSummaryDTO {

    @Schema(description = "Unique identifier for the maze")
    private final Integer id;

    @Schema(description = "Timestamp when the maze was created")
    private final Instant createdAt;

    @Schema(description = "Timestamp when the maze was last updated")
    private final Instant updatedAt;

    @Schema(description = "Whether the maze has been solved")
    private final boolean solved;

    public MazeSummaryDTO(Integer id, Instant createdAt, Instant updatedAt, boolean solved) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.solved = solved;
    }

    /**
     * Creates a MazeSummaryDTO from a MazeEntity.
     *
     * @param entity The MazeEntity
     * @return The corresponding MazeSummaryDTO
     */
    public static MazeSummaryDTO fromEntity(MazeEntity entity) {
        return new MazeSummaryDTO(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.isSolved());
    }

    public Integer getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isSolved() {
        return solved;
    }
}
