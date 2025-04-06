import { useState, useEffect } from 'react';
import { ApiFactory } from '../api/apiFactory';
import { MazeResponseDTO } from '../api/api';

interface UseMazeDetailsResult {
  maze: MazeResponseDTO | null;
  loading: boolean;
  error: string | null;
  fetchMaze: (id: number) => Promise<void>;
}

/**
 * Custom hook for fetching and managing detailed maze data
 */
export const useMazeDetails = (initialMazeId?: number): UseMazeDetailsResult => {
  const [maze, setMaze] = useState<MazeResponseDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(initialMazeId !== undefined);
  const [error, setError] = useState<string | null>(null);

  const mazeApi = ApiFactory.getMazeApi();

  const fetchMaze = async (id: number) => {
    if (!id) return;
    
    setLoading(true);
    setError(null);
    
    try {
      const response = await mazeApi.getMaze(id);
      setMaze(response.data);
    } catch (err) {
      console.error(`Error fetching maze with ID ${id}:`, err);
      setError('Failed to load maze details. Please try again later.');
      setMaze(null);
    } finally {
      setLoading(false);
    }
  };

  // Fetch initial maze if provided
  useEffect(() => {
    if (initialMazeId) {
      fetchMaze(initialMazeId);
    }
  }, [initialMazeId]);

  return {
    maze,
    loading,
    error,
    fetchMaze
  };
};
