package io.jistud.mazesolver.server.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.model.Position;

import jakarta.persistence.*;

@Entity
@Table(name = "mazes", schema = "maze_solver")
@EntityListeners(AuditingEntityListener.class)
public class MazeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "maze_data", nullable = false, columnDefinition = "TEXT")
    private String mazeData;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "start_row", nullable = false)
    private int startRow;

    @Column(name = "start_col", nullable = false)
    private int startCol;

    @Column(name = "end_row", nullable = false)
    private int endRow;

    @Column(name = "end_col", nullable = false)
    private int endCol;

    @Column(name = "is_solved", nullable = false)
    private boolean solved;

    @Column(name = "solution_path", columnDefinition = "TEXT")
    private String solutionPath;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Default constructor for JPA
    public MazeEntity() {}

    // Constructor with all fields
    public MazeEntity(
            Integer id,
            String mazeData,
            int width,
            int height,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean solved,
            String solutionPath,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.mazeData = mazeData;
        this.width = width;
        this.height = height;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.solved = solved;
        this.solutionPath = solutionPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Static factory method to create entity from domain model
    public static MazeEntity fromDomain(Maze maze) {
        MazeEntity entity = new MazeEntity();
        entity.setWidth(maze.getWidth());
        entity.setHeight(maze.getHeight());

        // Find start and end positions
        List<Position> startPositions = maze.findCellsWithValue(Maze.START);
        List<Position> endPositions = maze.findCellsWithValue(Maze.END);

        if (startPositions.size() == 1) {
            Position start = startPositions.getFirst();
            entity.setStartRow(start.row());
            entity.setStartCol(start.col());
        } else {
            throw new IllegalArgumentException("Maze must have exactly one start position");
        }

        if (endPositions.size() == 1) {
            Position end = endPositions.getFirst();
            entity.setEndRow(end.row());
            entity.setEndCol(end.col());
        } else {
            throw new IllegalArgumentException("Maze must have exactly one end position");
        }

        // Set solved status and solution path
        entity.setSolved(maze.isSolved());
        if (maze.isSolved() && maze.getSolvedPath() != null) {
            entity.setSolutionPath(serializePositions(maze.getSolvedPath()));
        }

        // Serialize maze data
        entity.setMazeData(maze.toString());

        // Note: createdAt and updatedAt are now managed by JPA auditing
        return entity;
    }

    // Convert entity to domain model
    public Maze toDomain() {
        // Create a new char grid
        char[][] grid = new char[height][width];

        // Parse the maze data
        String[] rows = mazeData.split("\n");
        for (int row = 0; row < rows.length && row < height; row++) {
            String rowData = rows[row];
            for (int col = 0; col < rowData.length() && col < width; col++) {
                grid[row][col] = rowData.charAt(col);
            }
        }

        // Create the maze from the grid
        Maze maze = new Maze(height, width, grid);

        // Set solution path if available
        if (solved && solutionPath != null && !solutionPath.isEmpty()) {
            List<Position> path = deserializePositions(solutionPath);
            maze.setSolvedPath(path);
        }

        return maze;
    }

    // Get start position as domain object
    public Position getStartPosition() {
        return new Position(startRow, startCol);
    }

    // Get end position as domain object
    public Position getEndPosition() {
        return new Position(endRow, endCol);
    }

    // Serialize a list of positions to a string
    private static String serializePositions(List<Position> positions) {
        if (positions == null || positions.isEmpty()) {
            return "";
        }

        return positions.stream().map(p -> p.row() + "," + p.col()).collect(Collectors.joining(";"));
    }

    // Deserialize a string to a list of positions
    private static List<Position> deserializePositions(String serialized) {
        if (serialized == null || serialized.isEmpty()) {
            return new ArrayList<>();
        }

        List<Position> positions = new ArrayList<>();
        String[] pairs = serialized.split(";");

        for (String pair : pairs) {
            String[] coords = pair.split(",");
            if (coords.length == 2) {
                try {
                    int row = Integer.parseInt(coords[0]);
                    int col = Integer.parseInt(coords[1]);
                    positions.add(new Position(row, col));
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                }
            }
        }

        return positions;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMazeData() {
        return mazeData;
    }

    public void setMazeData(String mazeData) {
        this.mazeData = mazeData;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(String solutionPath) {
        this.solutionPath = solutionPath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
