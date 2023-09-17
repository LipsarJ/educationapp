import React from 'react';
import {Flex, IconButton, Text} from '@chakra-ui/react';
import {FiMenu} from 'react-icons/fi';
import {useLocation} from 'react-router-dom';

interface HeaderProps {
    onToggleSidebar: () => void;
    isSidebarOpen: boolean;
    isMobile: boolean;
}

const Header: React.FC<HeaderProps> = ({onToggleSidebar, isSidebarOpen, isMobile}) => {
    const location = useLocation();
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
        default:
            currentPage = '';
    }
    return (
        <Flex alignItems="center" justifyContent="space-between" bg="blue.500" p={3} w="100%" flexDir="row">
            {isMobile && isSidebarOpen ? <Flex h ={10} w={10}/> :
            <IconButton
                aria-label="Open Sidebar"
                background="none"
                color = "white"
                zIndex="1001"
                icon={<FiMenu/>}
                onClick={onToggleSidebar}
            />
            }
            <Text fontWeight="bold" fontSize="lg" color="white" textAlign="center" mr={10}>
                {currentPage}
            </Text>
            <Flex></Flex>
        </Flex>
    );
};

export default Header;
