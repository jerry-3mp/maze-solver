-- Set the search path to use the maze_solver schema
SET search_path TO maze_solver, public;

-- Create table in the maze_solver schema
CREATE TABLE maze_solver.mazes (
    id UUID PRIMARY KEY,
    maze_data TEXT NOT NULL,
    width INT NOT NULL,
    height INT NOT NULL,
    start_row INT NOT NULL,
    start_col INT NOT NULL,
    end_row INT NOT NULL,
    end_col INT NOT NULL,
    is_solved BOOLEAN NOT NULL DEFAULT FALSE,
    solution_path TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Index for querying by creation date
CREATE INDEX idx_mazes_created_at ON maze_solver.mazes(created_at);

-- Index for querying solved mazes
CREATE INDEX idx_mazes_is_solved ON maze_solver.mazes(is_solved);
