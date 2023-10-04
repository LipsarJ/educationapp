import React from 'react'
import {Avatar, Divider, Flex, Heading} from '@chakra-ui/react'
import {FiBook, FiHome, FiLogIn, FiLogOut} from 'react-icons/fi'
import {IoPeople} from 'react-icons/io5'
import NavItem from './NaviItem'
import {useAuth} from '../contexts/AuthContext';
import {instanceAxios} from '../utils/axiosConfig';
import {useNavigate} from 'react-router-dom';

export default function Sidebar({isSidebarOpen, isMobile}: { isSidebarOpen: boolean, isMobile: boolean }) {
    const navigate = useNavigate();

    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const handleLogout = async () => {
        try {
            const response = await instanceAxios.post('/auth/signout');
        } catch (e) {
            console.error(e)
        }
        setAuthenticated(false);
        setUser(null);
        navigate('/login');
    };

    return (
        <Flex
            h="100%"
            position="fixed"
            display={isSidebarOpen ? "flex" : "none"}
            width="250px"
            zIndex="1000"
            backgroundColor="white"
            flexDir="column"
            borderRight={isMobile ? "none" : "5px solid #ccc"}
        >

            <Heading as="h1" size="xl" textAlign="center" color="black" mt={4}>
                Education
                App
            </Heading>

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
                        <NavItem title="Мои курсы" icon={FiBook} description="Список курсов" url="/courses"/>
                        <NavItem title="Поиск сотрудников" icon={IoPeople} description="Поиск сотрудников"
                                 url="/persons"/>
                        <div onClick={handleLogout} style={{cursor: 'pointer', width: "100%"}}>
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
            <Flex></Flex>

            <Flex
                p="5%"
                w="100%"
                ml={3}
                alignItems="flex-start"
                bottom="100%"
                mb={4}
                flexDir="column"
            >
                <Divider
                    display={isSidebarOpen && user ? "flex" : "none"}/> {/* Изменили navSize на isSidebarOpen */}
                {user && (
                    <Flex mt={4} align="center">
                        <Avatar size="sm"/>
                        <Flex flexDir="column" ml={4}
                              display={isSidebarOpen ? "flex" : "none"}> {/* Изменили navSize на isSidebarOpen */}
                            <Heading as="h3" size="sm" textColor="#3D3D3D">
                                {user.username}
                            </Heading>
                        </Flex>
                    </Flex>
                )}
            </Flex>
        </Flex>
    )
}
