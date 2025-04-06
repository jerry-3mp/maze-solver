import React, { useState } from 'react';
import {
  Box,
  Drawer,
  List,
  Typography,
  Divider,
  Pagination,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
  Alert,
  Toolbar
} from '@mui/material';
import MazeItem from '../maze/MazeItem';
import { useMazes } from '../../hooks/useMazes';

interface DrawerProps {
  open: boolean;
  toggleDrawer: (open: boolean) => void;
  onMazeSelect?: (mazeId: number) => void;
}

const MazesDrawer: React.FC<DrawerProps> = ({ open, toggleDrawer, onMazeSelect }) => {
  const [selectedMazeId, setSelectedMazeId] = useState<number | null>(null);
  
  const {
    mazes,
    loading,
    error,
    totalPages,
    currentPage,
    pageSize,
    setPage,
    setPageSize
  } = useMazes(0, 5); // Start with 5 items per page

  const handleMazeClick = (mazeId: number) => {
    setSelectedMazeId(mazeId);
    if (onMazeSelect) {
      onMazeSelect(mazeId);
    }
  };

  const handlePageChange = (_event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1); // API is 0-based, but Pagination component is 1-based
  };

  const handlePageSizeChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    setPageSize(event.target.value as number);
  };

  const drawerContent = (
    <Box sx={{ width: 280, overflow: 'hidden', display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Toolbar />
      <Typography variant="h6" sx={{ px: 2, py: 1 }}>
        Saved Mazes
      </Typography>
      <Divider />

      <Box sx={{ flexGrow: 1, overflow: 'auto' }}>
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', pt: 4 }}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ m: 2 }}>
            {error}
          </Alert>
        ) : mazes.length === 0 ? (
          <Typography sx={{ p: 2, color: 'text.secondary' }}>
            No mazes found. Generate a new one!
          </Typography>
        ) : (
          <List>
            {mazes.map((maze) => (
              <MazeItem
                key={maze.id}
                maze={maze}
                selected={selectedMazeId === maze.id}
                onClick={handleMazeClick}
              />
            ))}
          </List>
        )}
      </Box>

      <Box sx={{ p: 2, borderTop: 1, borderColor: 'divider' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <FormControl variant="outlined" size="small" sx={{ minWidth: 80 }}>
            <InputLabel id="page-size-label">Per Page</InputLabel>
            <Select
              labelId="page-size-label"
              value={pageSize}
              onChange={handlePageSizeChange}
              label="Per Page"
            >
              <MenuItem value={5}>5</MenuItem>
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={20}>20</MenuItem>
            </Select>
          </FormControl>
          
          <Typography variant="body2" color="text.secondary">
            {totalPages > 0 ? `Page ${currentPage + 1} of ${totalPages}` : ''}
          </Typography>
        </Box>

        <Pagination
          count={totalPages}
          page={currentPage + 1}
          onChange={handlePageChange}
          color="primary"
          size="medium"
          showFirstButton
          showLastButton
          disabled={loading}
          sx={{ display: 'flex', justifyContent: 'center' }}
        />
      </Box>
    </Box>
  );

  return (
    <Drawer
      anchor="left"
      open={open}
      onClose={() => toggleDrawer(false)}
    >
      {drawerContent}
    </Drawer>
  );
};

export default MazesDrawer;
