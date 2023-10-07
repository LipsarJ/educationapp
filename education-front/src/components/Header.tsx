import React from 'react';
import {Flex, IconButton, Text} from '@chakra-ui/react';
import {FiMenu, FiArrowLeft} from 'react-icons/fi';
import {useLocation, useNavigate} from 'react-router-dom';

interface HeaderProps {
    onToggleSidebar: () => void;
    isSidebarOpen: boolean;
    isMobile: boolean;
}

const Header: React.FC<HeaderProps> = ({onToggleSidebar, isSidebarOpen, isMobile}) => {
    const location = useLocation();
    const navigate = useNavigate();
    let currentPage = '';

    switch (location.pathname) {
        case '/':
            currentPage = 'Домашняя страница';
            break;
        case '/login':
            currentPage = 'Вход';
            break;
        case '/register':
            currentPage = 'Регистрация';
            break;
        case '/persons':
            currentPage = 'Пользователи';
            break;
        case '/courses':
            currentPage = 'Курсы';
            break;
        case '/courses/create':
            currentPage = "Создание курса"
            break;
        default:
            currentPage = 'EducationApp';
    }
    return (
        <Flex alignItems="center" justifyContent="center" bg="blue.500" p={3} w="100%" flexDir="row" h="50px">
            <Flex alignItems="center" position = "absolute" left={2} display="flex">
                {isMobile && isSidebarOpen ? <Flex h={10} w={10}/> :
                    <IconButton
                        aria-label="Open Sidebar"
                        background="none"
                        left={0}
                        color="white"
                        zIndex="1001"
                        icon={<FiMenu/>}
                        onClick={onToggleSidebar}
                    />
                }
                <IconButton
                    aria-label="Back"
                    background="none"
                    color="white"
                    zIndex="1001"
                    ml={5}
                    icon={<FiArrowLeft/>}
                    onClick={() => navigate(-1)}
                />
            </Flex>
            <Text fontWeight="bold" fontSize="lg" color="white" textAlign="center">
                {currentPage}
            </Text>
        </Flex>
    );
};

export default Header;
