import React, {useState} from 'react';
import {Box, ChakraProvider, CSSReset, IconButton} from '@chakra-ui/react';
import {FiMenu} from 'react-icons/fi'
import {AuthProvider} from './contexts/AuthContext';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import Home from "./components/Home";
import EmployeeSearch from "./components/EmployeeSearch";
import Sidebar from "./components/Sidebar"

function App() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    return (
        <ChakraProvider>
            <AuthProvider>
                <CSSReset/>
                <BrowserRouter>
                    <Box display="flex" position="relative">
                        <Sidebar isSidebarOpen={isSidebarOpen}/>
                        <IconButton
                            aria-label="Open Sidebar"
                            background="none"
                            position="relative"
                            top="10px"
                            left="10px"
                            zIndex="1001"
                            _hover={{background: 'none'}}
                            icon={<FiMenu/>}
                            onClick={() => {
                                setIsSidebarOpen(!isSidebarOpen);
                            }}
                        />
                        <Routes>
                            <Route path="/" element={<Home/>}/>
                            <Route path="/persons" element={<EmployeeSearch/>}/>
                            <Route path="/login" element={<Login/>}/>
                            <Route path="/register" element={<Register/>}/>
                        </Routes>
                    </Box>
                </BrowserRouter>
            </AuthProvider>
        </ChakraProvider>
    );
}

export default App;
