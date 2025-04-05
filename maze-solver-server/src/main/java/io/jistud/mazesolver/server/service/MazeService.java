package io.jistud.mazesolver.server.service;

import java.util.List;
import java.util.Optional;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

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
     * Retrieve a maze by its ID
     *
     * @param id the ID of the maze
     * @return an Optional containing the maze if found, or empty if not found
     */
    Optional<Maze> getMazeById(Integer id);

    /**
     * Get all stored mazes
     *
     * @return a list of all mazes
     */
    List<Maze> getAllMazes();

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
     * @return an Optional containing the solution path if a solution exists, or empty if not
     */
    Optional<List<Position>> solveMaze(Integer id);
}
