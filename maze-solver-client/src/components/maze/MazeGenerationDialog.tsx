import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box,
  Typography,
  CircularProgress,
  Alert
} from '@mui/material';
import { useMazeContext } from '../../context/MazeContext';

interface MazeGenerationDialogProps {
  open: boolean;
  perfect: boolean
  onClose: () => void;
}

const MazeGenerationDialog: React.FC<MazeGenerationDialogProps> = ({
  open,
  perfect,
  onClose
}) => {
  // Form state
  const [width, setWidth] = useState<number>(10);
  const [height, setHeight] = useState<number>(10);
  const [widthError, setWidthError] = useState<string>('');
  const [heightError, setHeightError] = useState<string>('');

  const { 
    loading: generating, 
    error, 
    generateMaze 
  } = useMazeContext();

  const validateInputs = (): boolean => {
    let isValid = true;

    // Validate width
    if (!width || width < 5 || width > 30) {
      setWidthError('Width must be between 5 and 30');
      isValid = false;
    } else {
      setWidthError('');
    }

    // Validate height
    if (!height || height < 5 || height > 30) {
      setHeightError('Height must be between 5 and 30');
      isValid = false;
    } else {
      setHeightError('');
    }

    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent, perfect: boolean) => {
    e.preventDefault();

    if (!validateInputs()) {
      return;
    }

    await generateMaze(width, height, perfect);
    onClose();
  };

  const handleClose = () => {
    // Reset form when closing
    setWidthError('');
    setHeightError('');
    onClose();
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose}
      maxWidth="sm"
      fullWidth
    >
      <DialogTitle>Generate New Maze</DialogTitle>
      <form onSubmit={(e)=>handleSubmit(e, perfect)}>
        <DialogContent>
          <Box sx={{ mb: 2 }}>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Specify dimensions for your new maze. For best results, use values between 5 and 30.
            </Typography>
            
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}

            <TextField
              label="Width"
              type="number"
              fullWidth
              value={width}
              onChange={(e) => setWidth(parseInt(e.target.value, 10))}
              error={!!widthError}
              helperText={widthError}
              variant="outlined"
              InputProps={{ inputProps: { min: 5, max: 30 } }}
              sx={{ mb: 2 }}
              disabled={generating}
            />
            
            <TextField
              label="Height"
              type="number"
              fullWidth
              value={height}
              onChange={(e) => setHeight(parseInt(e.target.value, 10))}
              error={!!heightError}
              helperText={heightError}
              variant="outlined"
              InputProps={{ inputProps: { min: 5, max: 30 } }}
              disabled={generating}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} disabled={generating}>
            Cancel
          </Button>
          <Button 
            type="submit" 
            variant="contained" 
            color="primary"
            disabled={generating}
            startIcon={generating ? <CircularProgress size={20} /> : null}
          >
            {generating ? 'Generating...' : 'Generate Maze'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default MazeGenerationDialog;
