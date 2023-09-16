import React from 'react'
import {Avatar, Divider, Flex, Heading, useMediaQuery} from '@chakra-ui/react'
import {FiHome, FiLogIn, FiLogOut,} from 'react-icons/fi'
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
            h={isAuthenticated ? "95vh" : "100vh"}
            position={isMobile ? "fixed" : "relative"}
            display={isSidebarOpen ? "flex" : "none"}
            backgroundColor="blue.500"
            width="250px"
            transition="left 0.3s ease-in-out"
            zIndex="1000"
            flexDir="column"
        >
            <Flex
                p="5%"
                flexDir="column"
                w="100%"
                alignItems="flex-start"
                as="nav"
            >
                <NavItem title="Домашняя страница" icon={FiHome} description="Домашняя страница" url="/"/>
                {!isAuthenticated ? (
                    <NavItem title="Войти" icon={FiLogIn} description="Вход" url="/login"/>
                ) : (
                    <>
                        <NavItem title="Поиск сотрудников" icon={IoPeople} description="Поиск сотрудников"
                                 url="/persons"/>
                        <div onClick={handleLogout} style={{cursor: 'pointer', width:"100%"}}>
                            <NavItem
                                title="Выйти"
                                icon={FiLogOut}
                                description="Выход"
                                url=""
                            />
                        </div>
                    </>
                )}
            </Flex>

            <Flex
                p="5%"
                bottom="100%"
                w="100%"
                alignItems="flex-start"
                mb={4}
                flexDir = "column"
            >
                <Divider display={isSidebarOpen && user ? "flex" : "none"}/> {/* Изменили navSize на isSidebarOpen */}
                {user && (
                    <Flex mt={4} align="center">
                        <Avatar size="sm"/>
                        <Flex flexDir="column" ml={4}
                              display={isSidebarOpen ? "flex" : "none"}> {/* Изменили navSize на isSidebarOpen */}
                            <Heading as="h3" size="sm" textColor="white">
                                {user.username}
                            </Heading>
                        </Flex>
                    </Flex>
                )}
            </Flex>
        </Flex>
    )
}
