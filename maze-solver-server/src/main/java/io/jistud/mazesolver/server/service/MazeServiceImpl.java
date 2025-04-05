package io.jistud.mazesolver.server.service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
        MazeEntity entity = MazeEntity.fromDomain(maze);
        mazeRepository.save(entity);

        return maze;
    }

    @Override
    public Optional<Maze> getMazeById(Integer id) {
        return mazeRepository.findById(id).map(MazeEntity::toDomain);
    }

    @Override
    public List<Maze> getAllMazes() {
        return mazeRepository.findAll().stream().map(MazeEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteMaze(Integer id) {
        mazeRepository.deleteById(id);
    }

    @Override
    public Optional<List<Position>> solveMaze(Integer id) {
        Optional<MazeEntity> mazeEntityOpt = mazeRepository.findById(id);

        if (mazeEntityOpt.isPresent()) {
            MazeEntity entity = mazeEntityOpt.get();

            // If already solved, return the existing solution
            if (entity.isSolved()
                    && entity.getSolutionPath() != null
                    && !entity.getSolutionPath().isEmpty()) {
                List<Position> positions = Arrays.stream(
                                entity.getSolutionPath().split(";"))
                        .map(pair -> {
                            String[] coords = pair.split(",");
                            return new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        })
                        .collect(Collectors.toList());
                return Optional.of(positions);
            }

            // Otherwise, solve the maze
            Maze maze = entity.toDomain();
            boolean solved = maze.solve();

            if (solved) {
                // Update the entity with the solution
                entity.setSolved(true);
                entity.setSolutionPath(maze.getSolvedPath().stream()
                        .map(p -> p.row() + "," + p.col())
                        .collect(Collectors.joining(";")));
                entity.setUpdatedAt(Instant.now());

                // Save the updated entity
                mazeRepository.save(entity);

                return Optional.of(maze.getSolvedPath());
            }

            return Optional.empty();
        }

        return Optional.empty();
    }
}
