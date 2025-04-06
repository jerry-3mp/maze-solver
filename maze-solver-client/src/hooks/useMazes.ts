import { useState, useEffect, useCallback } from 'react';
import { ApiFactory } from '../api/apiFactory';
import { MazeSummaryDTO, MazeSummaryListResponse } from '../api/api';

interface UseMazesResult {
  mazes: MazeSummaryDTO[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  totalElements: number;
  currentPage: number;
  pageSize: number;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  refresh: () => void;
}

/**
 * Custom hook for fetching and managing maze data with pagination
 */
export const useMazes = (initialPage: number = 0, initialPageSize: number = 10): UseMazesResult => {
  const [mazes, setMazes] = useState<MazeSummaryDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [totalElements, setTotalElements] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(initialPage);
  const [pageSize, setPageSize] = useState<number>(initialPageSize);

  const mazeApi = ApiFactory.getMazeApi();

  const fetchMazes = useCallback(async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.getMazes(currentPage, pageSize);
      const data = response.data as MazeSummaryListResponse;
      
      setMazes(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
    } catch (err) {
      console.error('Error fetching mazes:', err);
      setError('Failed to load mazes. Please try again later.');
      setMazes([]);
    } finally {
      setLoading(false);
    }
  }, [currentPage, pageSize, mazeApi]);

  // Fetch mazes when component mounts or when pagination params change
  useEffect(() => {
    fetchMazes();
  }, [fetchMazes]);

  const setPage = useCallback((page: number) => {
    setCurrentPage(page);
  }, []);

  const setPageSizeAndResetPage = useCallback((size: number) => {
    setPageSize(size);
    setCurrentPage(0); // Reset to first page when changing page size
  }, []);

  return {
    mazes,
    loading,
    error,
    totalPages,
    totalElements,
    currentPage,
    pageSize,
    setPage,
    setPageSize: setPageSizeAndResetPage,
    refresh: fetchMazes
  };
};
