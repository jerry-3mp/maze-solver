import './App.css'
import { useState, useEffect } from 'react';
import { Box, CssBaseline, Toolbar } from '@mui/material';
import ButtonAppBar from "./components/layout/ButtonAppBar.tsx";
import MazesDrawer from "./components/layout/MazesDrawer.tsx";
import MazeVisualization from './components/maze/MazeVisualization.tsx';
import { useMazes } from './hooks/useMazes.ts';
import { useMazeDetails } from './hooks/useMazeDetails.ts';
import { ApiFactory } from './api/apiFactory.ts';

function App() {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [selectedMazeId, setSelectedMazeId] = useState<number | null>(null);
  
  // Get the list of mazes for the drawer
  const { mazes, loading: mazesLoading } = useMazes(0, 5);
  
  // Get the details of the selected maze
  const { 
    maze: selectedMaze, 
    loading: mazeDetailsLoading, 
    error: mazeDetailsError,
    fetchMaze 
  } = useMazeDetails();

  const toggleDrawer = () => {
    setDrawerOpen((prevState) => !prevState);
  };

  const handleMazeSelect = (mazeId: number) => {
    setSelectedMazeId(mazeId);
    fetchMaze(mazeId);
  };

  const handleSolveMaze = async (mazeId: number) => {
    try {
      const mazeApi = ApiFactory.getMazeApi();
      await mazeApi.solveMaze(mazeId);
      // Refetch maze details to get the solution
      fetchMaze(mazeId);
    } catch (error) {
      console.error('Error solving maze:', error);
    }
  };

  // On initial load, select the most recent maze (highest ID)
  useEffect(() => {
    if (!mazesLoading && mazes.length > 0 && !selectedMazeId) {
      // Find the maze with the highest ID
      const mostRecentMaze = [...mazes].sort((a, b) => 
        (b.id ?? 0) - (a.id ?? 0)
      )[0];
      
      if (mostRecentMaze?.id) {
        setSelectedMazeId(mostRecentMaze.id);
        fetchMaze(mostRecentMaze.id);
      }
    }
  }, [mazesLoading, mazes, selectedMazeId, fetchMaze]);

  return (
    <Box sx={{ display: 'flex', height: '100vh' }}>
      <CssBaseline />
      <ButtonAppBar toggleDrawer={toggleDrawer}/>
      <MazesDrawer 
        open={drawerOpen} 
        toggleDrawer={setDrawerOpen}
        onMazeSelect={handleMazeSelect}
      />
      <Box
        component="main"
        sx={{ 
          flexGrow: 1,
          p: 0,
          width: { sm: `calc(100% - 240px)` },
          height: '100vh',
          overflow: 'auto'
        }}
      >
        <Toolbar /> {/* Spacer for the AppBar */}
        <MazeVisualization 
          maze={selectedMaze}
          loading={mazeDetailsLoading}
          error={mazeDetailsError}
          onSolveMaze={handleSolveMaze}
        />
      </Box>
    </Box>
  )
}

export default App
