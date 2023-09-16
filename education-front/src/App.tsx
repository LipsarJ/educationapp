import React, { useState } from 'react';
import { ChakraProvider, CSSReset, Box, Flex, Text } from '@chakra-ui/react';
import { AuthProvider } from './contexts/AuthContext';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Home from './components/Home';
import EmployeeSearch from './components/EmployeeSearch';
import Header from './components/Header'; // Импортируйте компонент Header
import Sidebar from './components/Sidebar';

function App() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    return (
        <ChakraProvider>
            <AuthProvider>
                <CSSReset />
                <BrowserRouter>
                    <Header onToggleSidebar={toggleSidebar} />
                    <Flex>
                        <Sidebar isSidebarOpen={isSidebarOpen} />
                        <Routes>
                            <Route path="/" element={<Home />} />
                            <Route path="/persons" element={<EmployeeSearch />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                        </Routes>
                    </Flex>
                </BrowserRouter>
            </AuthProvider>
        </ChakraProvider>
    );
}

export default App;
