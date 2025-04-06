package io.jistud.mazesolver.server.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jistud.mazesolver.server.controller.dto.MazeGenerationRequestDTO;
import io.jistud.mazesolver.server.controller.dto.MazeResponseDTO;
import io.jistud.mazesolver.server.controller.dto.MazeSummaryListResponse;
import io.jistud.mazesolver.server.entity.MazeEntity;
import io.jistud.mazesolver.server.model.Maze;
import io.jistud.mazesolver.server.service.MazeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/mazes")
@CrossOrigin(origins = "*")
@Tag(name = "Maze Controller", description = "API for managing mazes")
public class MazeController {

    private final MazeService mazeService;

    public MazeController(MazeService mazeService) {
        this.mazeService = mazeService;
    }

    @PostMapping
    @Operation(
            summary = "Generate a random maze",
            description = "Creates a new random maze with the specified dimensions")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully generated maze",
                        content = @Content(schema = @Schema(implementation = MazeResponseDTO.class))),
                @ApiResponse(responseCode = "400", description = "Invalid dimensions provided")
            })
    public ResponseEntity<MazeResponseDTO> generateRandomMaze(
            @Parameter(description = "Maze generation parameters") @RequestBody MazeGenerationRequestDTO request) {

        // Validate dimensions
        if (request.getWidth() < 5 || request.getHeight() < 5 || request.getWidth() > 30 || request.getHeight() > 30) {
            return ResponseEntity.badRequest().build();
        }

        // Generate the maze
        Maze generatedMaze = mazeService.generateRandomMaze(request.getWidth(), request.getHeight());

        // Save the maze to database and get entity with ID
        MazeEntity savedEntity = mazeService.saveEntityFromMaze(generatedMaze);

        // Get the ID of the saved maze
        Integer mazeId = savedEntity.getId();

        // Create the response DTO
        MazeResponseDTO response = MazeResponseDTO.fromMaze(mazeId, generatedMaze);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/perfect")
    @Operation(
            summary = "Generate a random perfect maze",
            description = "Creates a new random perfect maze with the specified dimensions")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully generated maze",
                        content = @Content(schema = @Schema(implementation = MazeResponseDTO.class))),
                @ApiResponse(responseCode = "400", description = "Invalid dimensions provided")
            })
    public ResponseEntity<MazeResponseDTO> generateRandomPerfectMaze(
            @Parameter(description = "Maze generation parameters") @RequestBody MazeGenerationRequestDTO request) {

        // Validate dimensions
        if (request.getWidth() < 5 || request.getHeight() < 5 || request.getWidth() > 30 || request.getHeight() > 30) {
            return ResponseEntity.badRequest().build();
        }

        // Generate the maze
        Maze generatedMaze = mazeService.generateRandomPerfectMaze(request.getWidth(), request.getHeight());

        // Save the maze to database and get entity with ID
        MazeEntity savedEntity = mazeService.saveEntityFromMaze(generatedMaze);

        // Get the ID of the saved maze
        Integer mazeId = savedEntity.getId();

        // Create the response DTO
        MazeResponseDTO response = MazeResponseDTO.fromMaze(mazeId, generatedMaze);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all mazes with pagination", description = "Returns a paginated list of maze summaries")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved maze list",
                        content = @Content(schema = @Schema(implementation = MazeSummaryListResponse.class)))
            })
    public ResponseEntity<MazeSummaryListResponse> getMazes(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<MazeEntity> mazePage = mazeService.findAll(pageRequest);
        MazeSummaryListResponse response = MazeSummaryListResponse.fromPage(mazePage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get maze by ID", description = "Returns detailed information about a specific maze")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved maze",
                        content = @Content(schema = @Schema(implementation = MazeResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Maze not found")
            })
    public ResponseEntity<MazeResponseDTO> getMaze(
            @Parameter(description = "ID of maze to retrieve") @PathVariable Integer id) {

        Optional<MazeEntity> mazeEntity = mazeService.findById(id);

        if (mazeEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Maze maze = mazeService.convertToModel(mazeEntity.get());
        MazeResponseDTO response = MazeResponseDTO.fromMaze(id, maze);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/solve")
    @Operation(summary = "Solve a maze", description = "Attempts to solve the specified maze and returns the solution")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Maze successfully solved",
                        content = @Content(schema = @Schema(implementation = MazeResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Maze not found")
            })
    public ResponseEntity<MazeResponseDTO> solveMaze(
            @Parameter(description = "ID of maze to solve") @PathVariable Integer id) {

        if (mazeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<MazeEntity> solvedMazeEntity = mazeService.solveMaze(id);

        if (solvedMazeEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Maze solvedMaze = mazeService.convertToModel(solvedMazeEntity.get());
        MazeResponseDTO response = MazeResponseDTO.fromMaze(id, solvedMaze);

        return ResponseEntity.ok(response);
    }
}
