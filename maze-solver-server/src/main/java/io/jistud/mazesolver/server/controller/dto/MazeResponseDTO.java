package io.jistud.mazesolver.server.controller.dto;

import java.util.ArrayList;
import java.util.List;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for detailed maze information.
 */
@Schema(description = "Detailed information about a maze including its grid and solution path if solved")
public class MazeResponseDTO {

    @Schema(description = "Unique identifier for the maze")
    private final Integer id;

    @Schema(description = "2D grid representation of the maze. 'S'=start, 'E'=end, 'w'=wall, 'p'=path, ' '=empty")
    private final char[][] grid;

    @Schema(description = "Whether the maze has been solved")
    private final boolean solved;

    @Schema(description = "Sequence of positions forming the solution path (null if not solved)")
    private final List<PositionDTO> solvedPath;

    public MazeResponseDTO(Integer id, char[][] grid, boolean solved, List<PositionDTO> solvedPath) {
        this.id = id;
        this.grid = grid;
        this.solved = solved;
        this.solvedPath = solvedPath;
    }

    /**
     * Creates a MazeResponseDTO from a Maze model object.
     *
     * @param maze The Maze model object
     * @return The corresponding MazeResponseDTO
     */
    public static MazeResponseDTO fromMaze(Integer id, Maze maze) {

        List<PositionDTO> solvedPath = null;
        if (maze.isSolved() && maze.getSolvedPath() != null) {
            solvedPath = new ArrayList<>();
            for (Position position : maze.getSolvedPath()) {
                solvedPath.add(PositionDTO.fromPosition(position));
            }
        }

        return new MazeResponseDTO(id, maze.getGrid(), maze.isSolved(), solvedPath);
    }

    public Integer getId() {
        return id;
    }

    public char[][] getGrid() {
        return grid;
    }

    public boolean isSolved() {
        return solved;
    }

    public List<PositionDTO> getSolvedPath() {
        return solvedPath;
    }
}
