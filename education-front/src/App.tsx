import React, {useState} from 'react';
import {ChakraProvider, CSSReset, Box, Flex, Text, useMediaQuery} from '@chakra-ui/react';
import {AuthProvider} from './contexts/AuthContext';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Home from './components/Home';
import EmployeeSearch from './components/EmployeeSearch';
import Header from './components/Header'; // Импортируйте компонент Header
import Sidebar from './components/Sidebar';
import theme from './themes';
import SidebarOverlay from './components/SidebarOverlay';

function App() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };
    const [isMobile] = useMediaQuery('(max-width: 768px)');

    return (
        <ChakraProvider theme = {theme}>
            <AuthProvider>
                <CSSReset/>
                <BrowserRouter>
                    <Flex transition="display 0.3s ease-in-out">
                        <Sidebar isSidebarOpen={isSidebarOpen}/>
                        <SidebarOverlay isOpen={isSidebarOpen} onClose={toggleSidebar} />
                    </Flex>
                    <Flex flexDir="column" position = "fixed" w="100%"marginLeft={isSidebarOpen && !isMobile ? '250px' : '0'} >
                        <Header onToggleSidebar={toggleSidebar}/>
                        <Routes>
                            <Route path="/" element={<Home/>}/>
                            <Route path="/persons" element={<EmployeeSearch/>}/>
                            <Route path="/login" element={<Login/>}/>
                            <Route path="/register" element={<Register/>}/>
                        </Routes>
                    </Flex>
                </BrowserRouter>
            </AuthProvider>
        </ChakraProvider>
    );
}

export default App;
