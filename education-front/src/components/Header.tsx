import React from 'react';
import {Flex, Button, Link, Spacer, Box} from '@chakra-ui/react';
import {useAuth} from '../contexts/AuthContext';
import {getAxiosInstance} from '../utils/axiosConfig';
import {NavLink, useNavigate} from 'react-router-dom';

const Header: React.FC = () => {
    const navigate = useNavigate();

    const {isAuthenticated, setAuthenticated, setUser} = useAuth();

    const handleLogout = async () => {
        try {
            const response = await getAxiosInstance().post('/api/v1/auth/signout');
        } catch (e) {
            console.error(e)
        }
        setAuthenticated(false);
        setUser(null);
        navigate('/login'); // перенаправляем на страницу входа
    };

    return (
        <Flex bg="#1877F2" p={4} color="white">
            {!isAuthenticated && (<>
                <Box p="2">
                    <Link as={NavLink} to="/login" m={2} _hover={{textDecoration: 'underline'}}>
                        Войти
                    </Link>
                </Box>
                <Box p="2">
                    <Link as={NavLink} to="/register" m={2} _hover={{textDecoration: 'underline'}}>
                        Зарегистрироваться
                    </Link>
                </Box>
            </>)}
            <Box p="2">
                <Link as={NavLink} to="/persons" m={2} _hover={{textDecoration: 'underline'}}>
                    Персоналии
                </Link>
            </Box>
            <Spacer/>
            {isAuthenticated && (
                <Button colorScheme="facebook" variant="solid" onClick={handleLogout}>
                    Выйти
                </Button>
            )}
        </Flex>
    );
};

export default Header;
