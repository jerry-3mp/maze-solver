-- Create a dedicated schema for the maze-solver application
CREATE SCHEMA IF NOT EXISTS maze_solver;

-- Set the search path to include the new schema
SET search_path TO maze_solver, public;
