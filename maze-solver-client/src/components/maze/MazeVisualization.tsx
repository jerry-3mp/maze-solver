import React from 'react';
import {
  Box,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  Button,
  Grid,
  Chip
} from '@mui/material';
import { useMazeContext } from '../../context/MazeContext';

interface MazeVisualizationProps {
  onGenerateMaze?: (perfect: boolean) => void;
}

const MazeVisualization: React.FC<MazeVisualizationProps> = ({
  onGenerateMaze
}) => {
  const { currentMaze: maze, loading, error, solveMaze } = useMazeContext();
  const handleSolveClick = () => {
    if (maze && maze.id) {
      solveMaze(maze.id);
    }
  };

  const handleGenerateClick = (perfect: boolean) => {
    if (onGenerateMaze) {
      onGenerateMaze(perfect);
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

  // Legend items with cell type and corresponding color
  const legendItems = [
    { type: 'Start', color: '#4CAF50' },
    { type: 'End', color: '#F44336' },
    { type: 'Wall', color: '#424242' },
    { type: 'Path', color: '#2196F3' },
    { type: 'Empty', color: '#FFFFFF' },
    { type: 'Solution', color: '#FF9800', description: maze?.solved ? 'with numbered steps' : '' }
  ];

  // Check if the current cell is part of the solution path and return its index
  const getSolutionCellIndex = (row: number, col: number) => {
    if (!maze?.solvedPath) return -1;
    
    return maze.solvedPath.findIndex(pos => 
      pos.row === row && pos.col === col
    );
  };

  // Check if the current cell is part of the solution path
  const isSolutionCell = (row: number, col: number) => {
    return getSolutionCellIndex(row, col) !== -1;
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
        <Grid size={12}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h5">
              Maze #{maze.id}
            </Typography>
            <Box sx={{ display: 'flex', gap: 2 }}>
              {!maze.solved && (
                <Button 
                  variant="contained" 
                  color="primary"
                  onClick={handleSolveClick}
                >
                  Solve Maze
                </Button>
              )}
              <Button
                variant="outlined"
                color="primary"
                onClick={()=>handleGenerateClick(false)}
              >
                Generate Maze
              </Button>
              <Button
                  variant="outlined"
                  color="primary"
                  onClick={()=>handleGenerateClick(true)}
              >
                Generate Perfect Maze
              </Button>
            </Box>
          </Box>
        </Grid>
        
        <Grid size={12}>
          <Paper elevation={1} sx={{ p: 2, mt: 2, mb: 2 }}>
            <Typography variant="subtitle1" sx={{ mb: 1 }}>
              Legend
            </Typography>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, justifyContent: 'center' }}>
              {legendItems.map((item) => (
                <Box key={item.type} sx={{ display: 'flex', alignItems: 'center', mr: 2 }}>
                  <Box 
                    sx={{
                      width: 20,
                      height: 20,
                      backgroundColor: item.color,
                      border: '1px solid #e0e0e0',
                      mr: 1,
                      borderRadius: item.type === 'Solution' ? '50%' : '0',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}
                  >
                    {item.type === 'Solution' && maze?.solved && (
                      <Typography variant="caption" sx={{ fontSize: '8px', color: '#000', fontWeight: 'bold' }}>
                        1
                      </Typography>
                    )}
                  </Box>
                  <Typography variant="caption">
                    {item.type}
                    {item.description && ` (${item.description})`}
                  </Typography>
                </Box>
              ))}
            </Box>
          </Paper>
        </Grid>
        
        <Grid size={12}>
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
                        width: '15px',
                        height: '15px',
                        borderRadius: '50%',
                        backgroundColor: '#FF9800',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
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
                    {maze.solved && isSolutionCell(rowIndex, colIndex) && !['S', 'E'].includes(cell) && (
                      <Typography 
                        variant="caption" 
                        sx={{ 
                          position: 'absolute', 
                          top: '50%', 
                          left: '50%', 
                          transform: 'translate(-50%, -50%)',
                          color: '#000',
                          fontWeight: 'bold',
                          fontSize: '10px',
                          zIndex: 2
                        }}
                      >
                        {getSolutionCellIndex(rowIndex, colIndex) + 1}
                      </Typography>
                    )}
                  </Box>
                ))
              ))}
            </Box>
          </Paper>
        </Grid>
        
        <Grid size={12}>
          <Box sx={{ mt: 2 }}>
            <Typography variant="body2" color="text.secondary" component="div">
              <strong>Status:</strong>
              <Chip
                label={maze.solved ? "Solved" : "Unsolved"}
                size="small"
                color={maze.solved ? "success" : "default"}
                sx={{ ml: 1 }}/>
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
