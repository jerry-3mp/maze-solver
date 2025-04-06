import { useState } from 'react';
import { ApiFactory } from '../api/apiFactory';
import { MazeGenerationRequestDTO, MazeResponseDTO } from '../api/api';

interface UseMazeGenerationResult {
  generating: boolean;
  error: string | null;
  generatedMaze: MazeResponseDTO | null;
  generateMaze: (width: number, height: number) => Promise<MazeResponseDTO | null>;
}

/**
 * Custom hook for generating a new maze
 */
export const useMazeGeneration = (onSuccess?: () => void): UseMazeGenerationResult => {
  const [generating, setGenerating] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [generatedMaze, setGeneratedMaze] = useState<MazeResponseDTO | null>(null);

  const mazeApi = ApiFactory.getMazeApi();

  const generateMaze = async (width: number, height: number): Promise<MazeResponseDTO | null> => {
    setGenerating(true);
    setError(null);
    
    try {
      const request: MazeGenerationRequestDTO = {
        width,
        height
      };
      
      const response = await mazeApi.generateRandomMaze(request);
      const data = response.data as MazeResponseDTO;
      
      setGeneratedMaze(data);
      
      // Call the success callback if provided
      if (onSuccess) {
        onSuccess();
      }
      
      return data;
    } catch (err) {
      console.error('Error generating maze:', err);
      setError('Failed to generate maze. Please try again later.');
      return null;
    } finally {
      setGenerating(false);
    }
  };

  return {
    generating,
    error,
    generatedMaze,
    generateMaze
  };
};
