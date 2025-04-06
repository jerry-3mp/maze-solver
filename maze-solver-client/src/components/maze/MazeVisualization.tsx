import React from 'react';
import { 
  Box, 
  Typography, 
  Paper, 
  CircularProgress, 
  Alert,
  Button,
  Grid
} from '@mui/material';
import { MazeResponseDTO } from '../../api/api';

interface MazeVisualizationProps {
  maze: MazeResponseDTO | null;
  loading: boolean;
  error: string | null;
  onSolveMaze?: (mazeId: number) => void;
}

const MazeVisualization: React.FC<MazeVisualizationProps> = ({
  maze,
  loading,
  error,
  onSolveMaze
}) => {
  const handleSolveClick = () => {
    if (maze && maze.id && onSolveMaze) {
      onSolveMaze(maze.id);
    }
  };

  // Cell color mapping
  const getCellColor = (cell: string) => {
    switch (cell) {
      case 's': return '#4CAF50'; // Start - Green
      case 'e': return '#F44336'; // End - Red
      case 'w': return '#424242'; // Wall - Dark Grey
      case 'p': return '#2196F3'; // Path - Blue
      default: return '#FFFFFF';  // Empty - White
    }
  };

  // Check if the current cell is part of the solution path
  const isSolutionCell = (row: number, col: number) => {
    if (!maze?.solvedPath) return false;
    
    return maze.solvedPath.some(pos => 
      pos.row === row && pos.col === col
    );
  };

  // Rendering loading state
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
        <CircularProgress />
      </Box>
    );
  }

  // Rendering error state
  if (error) {
    return (
      <Alert severity="error" sx={{ m: 2 }}>
        {error}
      </Alert>
    );
  }

  // Rendering empty state
  if (!maze) {
    return (
      <Box sx={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '100%',
        flexDirection: 'column' 
      }}>
        <Typography variant="h6" color="text.secondary">
          No maze selected
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          Select a maze from the drawer or generate a new one
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3, maxWidth: '100%', overflow: 'auto' }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h5">
              Maze #{maze.id}
            </Typography>
            {!maze.solved && (
              <Button 
                variant="contained" 
                color="primary"
                onClick={handleSolveClick}
              >
                Solve Maze
              </Button>
            )}
          </Box>
        </Grid>
        
        <Grid item xs={12}>
          <Paper elevation={3} sx={{ p: 2, overflow: 'auto' }}>
            <Box sx={{ 
              display: 'grid',
              gridTemplateColumns: `repeat(${maze.grid?.[0]?.length || 0}, 25px)`,
              gap: 0,
              justifyContent: 'center'
            }}>
              { maze.grid?.map((row, rowIndex) => (
                row.split('').map((cell, colIndex) => (
                  <Box 
                    key={`${rowIndex}-${colIndex}`}
                    sx={{
                      width: 25,
                      height: 25,
                      backgroundColor: getCellColor(cell),
                      border: '1px solid #e0e0e0',
                      position: 'relative',
                      '&::after': isSolutionCell(rowIndex, colIndex) && !['S', 'E'].includes(cell) ? {
                        content: '""',
                        position: 'absolute',
                        top: '50%',
                        left: '50%',
                        transform: 'translate(-50%, -50%)',
                        width: '10px',
                        height: '10px',
                        borderRadius: '50%',
                        backgroundColor: '#FF9800',
                      } : {}
                    }}
                  >
                    {(cell === 'S' || cell === 'E') && (
                      <Typography 
                        variant="caption" 
                        sx={{ 
                          position: 'absolute', 
                          top: '50%', 
                          left: '50%', 
                          transform: 'translate(-50%, -50%)',
                          color: '#fff',
                          fontWeight: 'bold'
                        }}
                      >
                        {cell}
                      </Typography>
                    )}
                  </Box>
                ))
              ))}
            </Box>
          </Paper>
        </Grid>
        
        <Grid item xs={12}>
          <Box sx={{ mt: 2 }}>
            <Typography variant="body2" color="text.secondary" component="div">
              <strong>Status:</strong> {maze.solved ? 'Solved' : 'Not Solved'}
              {maze.solved && maze.solvedPath && (
                <Box component="span" sx={{ ml: 2 }}>
                  <strong>Solution length:</strong> {maze.solvedPath.length} steps
                </Box>
              )}
            </Typography>
          </Box>
        </Grid>
      </Grid>
    </Box>
  );
};

export default MazeVisualization;
