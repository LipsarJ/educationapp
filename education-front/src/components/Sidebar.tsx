import React, { useState } from 'react'
import {
    Flex,
    Text,
    IconButton,
    Divider,
    Avatar,
    Heading
} from '@chakra-ui/react'
import {
    FiMenu,
    FiHome,
    FiLogIn,
    FiUser,
    FiGlobe,
    FiLogOut,
} from 'react-icons/fi'
import {IoPeople } from 'react-icons/io5'
import NavItem from './NaviItem'
import {useAuth} from '../contexts/AuthContext';
import {instanceAxios} from '../utils/axiosConfig';
import {NavLink, useNavigate} from 'react-router-dom';

export default function Sidebar() {
    const [navSize, changeNavSize] = useState("large")
    const navigate = useNavigate();

    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();

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
            pos="sticky"
            left="5"
            h="95vh"
            marginTop="2.5vh"
            boxShadow="0 4px 12px 0 rgba(0, 0, 0, 0.05)"
            borderRadius={navSize == "small" ? "15px" : "30px"}
            w={navSize == "small" ? "75px" : "200px"}
            flexDir="column"
            justifyContent="space-between"
            position = "absolute"
        >
            <Flex
                p="5%"
                flexDir="column"
                w="100%"
                alignItems={navSize == "small" ? "center" : "flex-start"}
                as="nav"
            >
                <IconButton
                    aria-label="label"
                    background="none"
                    mt={5}
                    _hover={{ background: 'none' }}
                    icon={<FiMenu />}
                    onClick={() => {
                        if (navSize == "small")
                            changeNavSize("large")
                        else
                            changeNavSize("small")
                    }}
                />
                <NavItem navSize={navSize} title="Домашняя страница" icon={FiHome} active={navSize === "large"} description="Домашняя страница" url="/" />
                {!isAuthenticated ? (
                    <>
                        <NavItem navSize={navSize} title="Регистрация" icon={FiGlobe} active={navSize === "large"} description="Регистрация" url="/register" />
                        <NavItem navSize={navSize} title="Войти" icon={FiLogIn} active={navSize === "large"} description="Вход" url="/login" />
                        <NavItem navSize={navSize} title="Поиск сотрудников" icon={IoPeople} active={navSize === "large"} description="Поиск сотрудников" url="/persons" />
                    </>
                ) : (
                    <NavItem navSize={navSize} title="Выйти" icon={FiLogOut} active={navSize === "large"} description="Выход" url="" />
                )}
            </Flex>

            <Flex
                p="5%"
                flexDir="column"
                w="100%"
                alignItems={navSize == "small" ? "center" : "flex-start"}
                mb={4}
            >
                <Divider display={navSize == "small" ? "none" : "flex"} />
                {user && (
                    <Flex mt={4} align="center">
                        <Avatar size="sm" />
                        <Flex flexDir="column" ml={4} display={navSize === "small" ? "none" : "flex"}>
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