import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import StatusComponent from "../status/StatusComponent.tsx";

interface ButtonAppBar {
    toggleDrawer: () => void;
}

export default function ButtonAppBar({ toggleDrawer }: ButtonAppBar) {
    return (
            <AppBar position="fixed">
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                    >
                        <MenuIcon onClick={() => toggleDrawer()}/>
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Maze Solver
                    </Typography>
                    <StatusComponent />
                </Toolbar>
            </AppBar>
    );
}