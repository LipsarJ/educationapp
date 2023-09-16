import React from 'react'
import {Avatar, Divider, Flex, Heading, useMediaQuery} from '@chakra-ui/react'
import {FiGlobe, FiHome, FiLogIn, FiLogOut,} from 'react-icons/fi'
import {IoPeople} from 'react-icons/io5'
import NavItem from './NaviItem'
import {useAuth} from '../contexts/AuthContext';
import {instanceAxios} from '../utils/axiosConfig';
import {useNavigate} from 'react-router-dom';

export default function Sidebar({isSidebarOpen}: { isSidebarOpen: boolean }) {
    const navigate = useNavigate();

    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const [isMobile] = useMediaQuery('(max-width: 768px)');

    const handleLogout = async () => {
        try {
            const response = await instanceAxios.post('/api/v1/auth/signout');
        } catch (e) {
            console.error(e)
        }
        setAuthenticated(false);
        setUser(null);
        navigate('/login');
    };

    return (
        <Flex
            h="100vh"
            position={isMobile ? "fixed" : "relative"}
            display={isSidebarOpen ? "flex" : "none"}
            backgroundColor="rgba(255, 155, 13, 1.0)"
            width="250px"
            transition="left 0.3s ease-in-out"
            zIndex="1000"
            left={isMobile ? (isSidebarOpen ? '0' : '-250px') : '0'}
        >
            <Flex
                p="5%"
                flexDir="column"
                w="100%"
                alignItems="flex-start"
                as="nav"
            >
                <NavItem navSize={isSidebarOpen ? "large" : "small"} title="Домашняя страница" icon={FiHome}
                         active={isSidebarOpen} description="Домашняя страница" url="/"/>
                {!isAuthenticated ? (
                    <>
                        <NavItem navSize={isSidebarOpen ? "large" : "small"} title="Регистрация" icon={FiGlobe}
                                 active={isSidebarOpen} description="Регистрация" url="/register"/>
                        <NavItem navSize={isSidebarOpen ? "large" : "small"} title="Войти" icon={FiLogIn}
                                 active={isSidebarOpen} description="Вход" url="/login"/>
                        <NavItem navSize={isSidebarOpen ? "large" : "small"} title="Поиск сотрудников" icon={IoPeople}
                                 active={isSidebarOpen} description="Поиск сотрудников" url="/persons"/>
                    </>
                ) : (
                    <NavItem navSize={isSidebarOpen ? "large" : "small"} title="Выйти" icon={FiLogOut}
                             active={isSidebarOpen} description="Выход" url=""/>
                )}
            </Flex>

            <Flex
                p="5%"
                flexDir="column"
                w="100%"
                alignItems={isSidebarOpen ? "center" : "flex-start"} // Изменили navSize на isSidebarOpen
                mb={4}
            >
                <Divider display={isSidebarOpen ? "flex" : "none"}/> {/* Изменили navSize на isSidebarOpen */}
                {user && (
                    <Flex mt={4} align="center">
                        <Avatar size="sm"/>
                        <Flex flexDir="column" ml={4}
                              display={isSidebarOpen ? "flex" : "none"}> {/* Изменили navSize на isSidebarOpen */}
                            <Heading as="h3" size="sm">
                                {user.username}
                            </Heading>
                        </Flex>
                    </Flex>
                )}
            </Flex>
        </Flex>
    )
}
