import './App.css'
import { useState, useCallback } from 'react';
import { Box, CssBaseline, Toolbar } from '@mui/material';
import ButtonAppBar from "./components/layout/ButtonAppBar.tsx";
import MazesDrawer from "./components/layout/MazesDrawer.tsx";
import MazeVisualization from './components/maze/MazeVisualization.tsx';
import MazeGenerationDialog from './components/maze/MazeGenerationDialog.tsx';
import { MazeProvider } from './context/MazeContext.tsx';

function App() {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [generationDialogOpen, setGenerationDialogOpen] = useState(false);
  const [perfectGeneration, setPerfectGeneration] = useState(false);
  
  const toggleDrawer = () => {
    setDrawerOpen((prevState) => !prevState);
  };

  const handleOpenGenerationDialog = useCallback((perfect: boolean) => {
    setGenerationDialogOpen(true);
    setPerfectGeneration(perfect);
  }, []);

  const handleCloseGenerationDialog = useCallback(() => {
    setGenerationDialogOpen(false);
  }, []);

  return (
    <MazeProvider>
      <Box sx={{ display: 'flex', height: '100vh' }}>
        <CssBaseline />
        <ButtonAppBar toggleDrawer={toggleDrawer}/>
        <MazesDrawer 
          open={drawerOpen} 
          toggleDrawer={setDrawerOpen}
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
            onGenerateMaze={handleOpenGenerationDialog}
          />
          <MazeGenerationDialog 
            open={generationDialogOpen}
            perfect={perfectGeneration}
            onClose={handleCloseGenerationDialog}
          />
        </Box>
      </Box>
    </MazeProvider>
  )
}

export default App
