import React from 'react';
import { ListItem, ListItemButton, ListItemText, Chip, Box, Typography } from '@mui/material';
import { Grid3x3 as MazeIcon } from '@mui/icons-material';
import { MazeSummaryDTO } from '../../api/api';

interface MazeItemProps {
  maze: MazeSummaryDTO;
  selected: boolean;
  onClick: (id: number) => void;
}

/**
 * Component for displaying a single maze item in the drawer list
 */
const MazeItem: React.FC<MazeItemProps> = ({ maze, selected, onClick }) => {
  const handleClick = () => {
    onClick(maze.id!);
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return 'Unknown';
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  return (
    <ListItem disablePadding>
      <ListItemButton 
        selected={selected}
        onClick={handleClick}
        sx={{ 
          borderRadius: 1,
          my: 0.5,
          '&.Mui-selected': {
            backgroundColor: 'rgba(0, 0, 0, 0.08)',
            '&:hover': {
              backgroundColor: 'rgba(0, 0, 0, 0.12)',
            },
          }
        }}
      >
        <Box sx={{ display: 'flex', flexDirection: 'column', width: '100%' }}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <MazeIcon sx={{ mr: 1 }} />
            <ListItemText 
              primary={`Maze #${maze.id}`}
              sx={{ my: 0 }}
            />
            <Chip 
              label={maze.solved ? "Solved" : "Unsolved"} 
              size="small" 
              color={maze.solved ? "success" : "default"}
              sx={{ ml: 1 }}
            />
          </Box>
          <Typography variant="caption" color="text.secondary" sx={{ mt: 0.5 }}>
            Created: {formatDate(maze.createdAt)}
          </Typography>
        </Box>
      </ListItemButton>
    </ListItem>
  );
};

export default MazeItem;
