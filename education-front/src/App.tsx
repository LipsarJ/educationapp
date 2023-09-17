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
import theme from './theme';
import SidebarOverlay from './components/SidebarOverlay';

function App() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };
    const [isMobile] = useMediaQuery('(max-width: 768px)');

    return (
        <ChakraProvider theme={theme}>
            <AuthProvider>
                <CSSReset/>
                <BrowserRouter>
                    <Flex>
                        <Sidebar isSidebarOpen={isSidebarOpen} isMobile = {isMobile}/>
                        {isMobile &&
                            <SidebarOverlay isOpen={isSidebarOpen} onClose={toggleSidebar}/>
                        }
                    </Flex>
                    <Flex flexDir="column" position = "relative" marginLeft={isSidebarOpen && !isMobile ? '250px' : '0'} flex="1">
                        <Header onToggleSidebar={toggleSidebar} isSidebarOpen={isSidebarOpen} isMobile={isMobile}/>
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
