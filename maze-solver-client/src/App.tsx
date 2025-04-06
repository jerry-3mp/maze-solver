import './App.css'
import { useState } from 'react';
import ButtonAppBar from "./components/layout/ButtonAppBar.tsx";
import MazesDrawer from "./components/layout/MazesDrawer.tsx";

function App() {
    const [drawerOpen, setDrawerOpen] = useState(false);

    const toggleDrawer = () => {
        setDrawerOpen((prevState) => !prevState);
    };
  return (
    <div className="app-container">
        <ButtonAppBar toggleDrawer={toggleDrawer}/>
        <MazesDrawer open={drawerOpen} toggleDrawer={toggleDrawer} />
    </div>
  )
}

export default App
