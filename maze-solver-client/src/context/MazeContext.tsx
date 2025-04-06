import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { ApiFactory } from '../api/apiFactory';
import { MazeResponseDTO, MazeSummaryDTO, MazeSummaryListResponse } from '../api/api';

interface MazeContextType {
  // Selected maze
  currentMaze: MazeResponseDTO | null;
  setCurrentMazeById: (id: number) => Promise<void>;
  
  // Pagination
  mazes: MazeSummaryDTO[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  
  // Status
  loading: boolean;
  error: string | null;
  
  // Actions
  refresh: () => Promise<void>;
  generateMaze: (width: number, height: number) => Promise<void>;
  solveMaze: (id: number) => Promise<void>;
}

const MazeContext = createContext<MazeContextType | undefined>(undefined);

export const useMazeContext = () => {
  const context = useContext(MazeContext);
  if (context === undefined) {
    throw new Error('useMazeContext must be used within a MazeProvider');
  }
  return context;
};

interface MazeProviderProps {
  children: ReactNode;
  initialPage?: number;
  initialPageSize?: number;
}

export const MazeProvider: React.FC<MazeProviderProps> = ({
  children,
  initialPage = 0,
  initialPageSize = 5
}) => {
  // API clients
  const mazeApi = ApiFactory.getMazeApi();
  
  // State
  const [mazes, setMazes] = useState<MazeSummaryDTO[]>([]);
  const [currentMaze, setCurrentMaze] = useState<MazeResponseDTO | null>(null);
  const [currentPage, setCurrentPage] = useState(initialPage);
  const [pageSize, setPageSize] = useState(initialPageSize);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [hasInitialized, setHasInitialized] = useState(false);

  // Load a specific maze by ID
  const setCurrentMazeById = useCallback(async (id: number) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.getMaze(id);
      setCurrentMaze(response.data);
    } catch (err) {
      console.error('Error fetching maze details:', err);
      setError('Failed to load maze details. Please try again later.');
    } finally {
      setLoading(false);
    }
  }, [mazeApi]);

  // Fetch mazes for the current page
  const fetchMazes = useCallback(async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.getMazes(currentPage, pageSize);
      const data = response.data as MazeSummaryListResponse;
      
      setMazes(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      
      // Ensure we're not on a page beyond the total pages
      if (data.totalPages && currentPage >= data.totalPages && data.totalPages > 0) {
        setCurrentPage(Math.max(0, data.totalPages - 1));
      }
      
      // Auto-select the most recent maze on initial load
      if (!hasInitialized && !currentMaze && (data.content?.length || 0) > 0) {
        setHasInitialized(true);
        
        // Find the maze with the highest ID or most recent creation date
        const sortedMazes = [...(data.content || [])].sort((a, b) => {
          // Sort by creation date if available
          if (a.createdAt && b.createdAt) {
            return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
          }
          // Fallback to ID-based sorting
          return (b.id ?? 0) - (a.id ?? 0);
        });
        
        const mostRecentMaze = sortedMazes[0];
        if (mostRecentMaze?.id) {
          setCurrentMazeById(mostRecentMaze.id);
        }
      }
      
    } catch (err) {
      console.error('Error fetching mazes:', err);
      setError('Failed to load mazes. Please try again later.');
      setMazes([]);
    } finally {
      setLoading(false);
    }
  }, [currentPage, pageSize, mazeApi, currentMaze, hasInitialized, setCurrentMazeById]);

  // Generate a new maze
  const generateMaze = useCallback(async (width: number, height: number) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.generateRandomMaze({ width, height });
      const generatedMaze = response.data;
      await fetchMazes(); // Refresh the list
      if (generatedMaze?.id) {
        setCurrentMazeById(generatedMaze.id);
      }
      setCurrentPage(0);
    } catch (err) {
      console.error('Error generating maze:', err);
      setError('Failed to generate maze. Please try again later.');
    } finally {
      setLoading(false);
    }
  }, [mazeApi, fetchMazes]);

  // Solve a maze
  const solveMaze = useCallback(async (id: number) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.solveMaze(id);
      setCurrentMaze(response.data);
      await fetchMazes(); // Refresh the list to update the solved status
    } catch (err) {
      console.error('Error solving maze:', err);
      setError('Failed to solve maze. Please try again later.');
    } finally {
      setLoading(false);
    }
  }, [mazeApi, fetchMazes]);

  // Handle page size change
  const handlePageSizeChange = useCallback((size: number) => {
    setPageSize(size);
    setCurrentPage(0); // Reset to first page
  }, []);

  // Initial load and when dependencies change
  useEffect(() => {
    fetchMazes();
  }, [fetchMazes]);

  const value = {
    // Selected maze
    currentMaze,
    setCurrentMazeById,
    
    // Pagination
    mazes,
    currentPage,
    pageSize,
    totalPages,
    setPage: setCurrentPage,
    setPageSize: handlePageSizeChange,
    
    // Status
    loading,
    error,
    
    // Actions
    refresh: fetchMazes,
    generateMaze,
    solveMaze
  };

  return <MazeContext.Provider value={value}>{children}</MazeContext.Provider>;
};
