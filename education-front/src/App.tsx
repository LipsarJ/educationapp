import React from 'react';
import {ChakraProvider, Switch} from '@chakra-ui/react';
import {AuthProvider} from './contexts/AuthContext';
import Header from "./components/Header";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import Home from "./components/Home";
import EmployeeSearch from "./components/EmployeeSearch";

function App() {
    return (
        <ChakraProvider>
            <AuthProvider>
                <BrowserRouter>
                    <Header/>
                    <Routes>
                        <Route path="/" element={<Home/>}/>
                        <Route path="/persons" element={<EmployeeSearch/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/register" element={<Register/>}/>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </ChakraProvider>
    );
}

export default App;