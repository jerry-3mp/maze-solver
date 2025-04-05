package io.jistud.mazesolver.server.controller.dto;

import io.jistud.mazesolver.server.model.Position;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object representing a position in a maze.
 */
@Schema(description = "Represents a position in a maze grid")
public class PositionDTO {

    @Schema(description = "Row index (0-based)")
    private final int row;

    @Schema(description = "Column index (0-based)")
    private final int col;

    public PositionDTO(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Creates a PositionDTO from a Position model object.
     *
     * @param position The Position model object
     * @return The corresponding PositionDTO
     */
    public static PositionDTO fromPosition(Position position) {
        return new PositionDTO(position.row(), position.col());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
