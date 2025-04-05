package io.jistud.mazesolver.server.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for maze generation requests.
 */
@Schema(description = "Request parameters for generating a new maze")
public class MazeGenerationRequestDTO {

    @Schema(description = "Width of the maze (number of columns)", example = "10", minimum = "5")
    private final int width;

    @Schema(description = "Height of the maze (number of rows)", example = "10", minimum = "5")
    private final int height;

    public MazeGenerationRequestDTO(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
