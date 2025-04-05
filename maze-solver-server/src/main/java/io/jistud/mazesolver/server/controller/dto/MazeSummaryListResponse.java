package io.jistud.mazesolver.server.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import io.jistud.mazesolver.server.entity.MazeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for a paginated list of maze summaries.
 */
@Schema(description = "Paginated list of maze summaries")
public class MazeSummaryListResponse {

    @Schema(description = "List of maze summaries in the current page")
    private final List<MazeSummaryDTO> content;

    @Schema(description = "Total number of mazes")
    private final long totalElements;

    @Schema(description = "Total number of pages")
    private final int totalPages;

    @Schema(description = "Number of items per page")
    private final int size;

    @Schema(description = "Current page number (0-based)")
    private final int number;

    public MazeSummaryListResponse(
            List<MazeSummaryDTO> content, long totalElements, int totalPages, int size, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

    /**
     * Creates a MazeSummaryListResponse from a Page of MazeEntities.
     *
     * @param page The Page of MazeEntities
     * @return The corresponding MazeSummaryListResponse
     */
    public static MazeSummaryListResponse fromPage(Page<MazeEntity> page) {
        List<MazeSummaryDTO> content =
                page.getContent().stream().map(MazeSummaryDTO::fromEntity).collect(Collectors.toList());

        return new MazeSummaryListResponse(
                content, page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
    }

    public List<MazeSummaryDTO> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }
}
