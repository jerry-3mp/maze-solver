package io.jistud.mazesolver.server.service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.jistud.mazesolver.server.builder.MazeBuilder;
import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;
import io.jistud.mazesolver.server.repository.MazeRepository;

@Service
public class MazeServiceImpl implements MazeService {

    private final MazeRepository mazeRepository;

    @Autowired
    public MazeServiceImpl(MazeRepository mazeRepository) {
        this.mazeRepository = mazeRepository;
    }

    @Override
    public Maze generateRandomMaze(int width, int height) {
        // Use the existing MazeBuilder to generate a random maze
        Maze maze = MazeBuilder.builder()
                .height(height)
                .width(width)
                .randomStartAndEnd()
                .withRandomPath()
                .withPerimeterWalls()
                .withEmptyPath()
                .build();

        // Save the maze to the database
        MazeEntity entity = convertToEntity(maze);
        mazeRepository.save(entity);

        return maze;
    }

    @Override
    public Optional<MazeEntity> findById(Integer id) {
        return mazeRepository.findById(id);
    }

    @Override
    public Page<MazeEntity> findAll(Pageable pageable) {
        return mazeRepository.findAll(pageable);
    }

    @Override
    public Maze convertToModel(MazeEntity entity) {
        if (entity == null) {
            return null;
        }

        // Parse the maze data into a 2D grid
        String[] rows = entity.getMazeData().split("\n");
        int height = rows.length;
        int width = rows[0].length();

        char[][] grid = new char[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char cell = rows[row].charAt(col);
                grid[row][col] = cell;
            }
        }

        Maze maze = new Maze(height, width, grid);

        // Parse solution path if present
        if (entity.isSolved() && entity.getSolutionPath() != null) {
            // Expected format: "[(row,col), (row,col), ...]"
            String pathStr = entity.getSolutionPath().trim();

            // Remove the outer brackets and split by comma-space
            if (pathStr.startsWith("[") && pathStr.endsWith("]")) {
                pathStr = pathStr.substring(1, pathStr.length() - 1);
                String[] positions = pathStr.split(", ");

                List<Position> solutionPath = new ArrayList<>();
                for (String posStr : positions) {
                    // Format: (row,col)
                    if (posStr.startsWith("(") && posStr.endsWith(")")) {
                        posStr = posStr.substring(1, posStr.length() - 1);
                        String[] coords = posStr.split(",");
                        int row = Integer.parseInt(coords[0]);
                        int col = Integer.parseInt(coords[1]);
                        solutionPath.add(new Position(row, col));
                    }
                }

                maze.setSolvedPath(solutionPath);
            }
        }

        return maze;
    }

    /**
     * Convert a maze model to a maze entity
     *
     * @param maze the maze model
     * @return the maze entity
     */
    private MazeEntity convertToEntity(Maze maze) {
        if (maze == null) {
            return null;
        }

        MazeEntity entity = new MazeEntity();

        // Convert grid to string representation
        char[][] grid = maze.getGrid();
        StringBuilder mazeData = new StringBuilder();

        for (int row = 0; row < grid.length; row++) {
            if (row > 0) {
                mazeData.append("\n");
            }
            mazeData.append(new String(grid[row]));
        }

        entity.setMazeData(mazeData.toString());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setSolved(maze.isSolved());

        // Convert solution path if present
        if (maze.isSolved() && maze.getSolvedPath() != null) {
            String pathStr = maze.getSolvedPath().stream()
                    .map(pos -> "(" + pos.row() + "," + pos.col() + ")")
                    .collect(Collectors.joining(", "));

            entity.setSolutionPath("[" + pathStr + "]");
        }

        return entity;
    }

    @Override
    public void deleteMaze(Integer id) {
        mazeRepository.deleteById(id);
    }

    @Override
    public Optional<MazeEntity> solveMaze(Integer id) {
        Optional<MazeEntity> mazeEntityOpt = mazeRepository.findById(id);

        if (mazeEntityOpt.isPresent()) {
            MazeEntity entity = mazeEntityOpt.get();

            // If already solved, return the existing solution
            if (entity.isSolved() && entity.getSolutionPath() != null) {
                return Optional.of(entity);
            }

            // Otherwise, solve the maze
            Maze maze = convertToModel(entity);
            boolean solved = maze.solve();

            if (solved) {
                // Update the entity with the solution
                entity.setSolved(true);

                // Convert solution path to string representation
                String pathStr = maze.getSolvedPath().stream()
                        .map(pos -> "(" + pos.row() + "," + pos.col() + ")")
                        .collect(Collectors.joining(", "));

                entity.setSolutionPath("[" + pathStr + "]");
                entity.setUpdatedAt(Instant.now());

                // Save the updated entity
                return Optional.of(mazeRepository.save(entity));
            }
        }

        return Optional.empty();
    }
}
