package io.jistud.mazesolver.server.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;

/**
 * Service for managing maze generation, storage, and solving.
 */
public interface MazeService {

    /**
     * Generate a random maze with the specified dimensions
     *
     * @param width the width of the maze
     * @param height the height of the maze
     * @return the generated maze
     */
    Maze generateRandomMaze(int width, int height);

    /**
     * Generate a random perfect maze with the specified dimensions
     *
     * @param width the width of the maze
     * @param height the height of the maze
     * @return the generated maze
     */
     Maze generateRandomPerfectMaze(int width, int height);

    /**
     * Converts a maze model to a maze entity and saves it to the database
     *
     * @param maze the maze model to save
     * @return the saved maze entity
     */
    MazeEntity saveEntityFromMaze(Maze maze);

    /**
     * Find a maze entity by its ID
     *
     * @param id the ID of the maze
     * @return an Optional containing the maze entity if found, or empty if not found
     */
    Optional<MazeEntity> findById(Integer id);

    /**
     * Find all maze entities with pagination
     *
     * @param pageable the pagination information
     * @return a Page of maze entities
     */
    Page<MazeEntity> findAll(Pageable pageable);

    /**
     * Convert a maze entity to a maze model
     *
     * @param entity the maze entity
     * @return the maze model
     */
    Maze convertToModel(MazeEntity entity);

    /**
     * Delete a maze by its ID
     *
     * @param id the ID of the maze to delete
     */
    void deleteMaze(Integer id);

    /**
     * Solve a maze by finding a path from start to end
     *
     * @param id the ID of the maze to solve
     * @return an Optional containing the solved maze entity if a solution exists, or empty if not
     */
    Optional<MazeEntity> solveMaze(Integer id);
}
