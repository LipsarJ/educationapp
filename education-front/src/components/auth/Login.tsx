import React, {useState} from 'react';
import {Button, Container, Flex, Heading, Input} from '@chakra-ui/react';
import axios from 'axios';
import {useAuth} from '../../contexts/AuthContext';
import {Link, useNavigate} from 'react-router-dom';
import {ErrorCodes} from './ErrorCodes';
import {ThreeDots} from 'react-loader-spinner'

interface LoginData {
    username: string;
    password: string;
}

const Login: React.FC = () => {
    const navigate = useNavigate();
    const {setAuthenticated, setUser} = useAuth();
    const [loginData, setLoginData] = useState<LoginData>({username: '', password: ''});
    const [error, setError] = useState<string | null>(null);
    const [isLoadingLogin, setLoadingLogin] = useState(false);
    const [isLoadingRegister, setLoadingRegister] = useState(false);

    const fetchDataRegister = () => {
        setLoadingRegister(true);
    }

    const handleLogin = async () => {
        setLoadingLogin(true);
        try {
            const response = await axios.post(process.env.REACT_APP_API_URL + '/auth/signin', loginData, {
                withCredentials: true
            });
            setAuthenticated(true);
            setUser(response.data);
            navigate('/');
        } catch (error: any) {
            const errorCode = error.response.data.errorCode;
            console.log(errorCode);
            if (errorCode == ErrorCodes.BadCredits) {
                setError('Неверный логин или пароль');
            } else {
                console.error(error);
            }
        }
        setLoadingLogin(false);
    };

    return (
        <Container centerContent mt="5" maxWidth="lg">
            <Heading mb={4} size="lg">Войти</Heading>
            {error && <div style={{color: 'red'}}>{error}</div>}
            <Input
                placeholder="Username"
                size="md"
                w="100%"
                onChange={(e) => setLoginData({...loginData, username: e.target.value})}
                mb={3}
            />
            <Input
                placeholder="Password"
                type="password"
                size="md"
                w="100%"
                mb={3}
                onChange={(e) => setLoginData({...loginData, password: e.target.value})}
            />
            <Flex
                w="100%"
            >
                <Button bg="facebook.400" size="md" flex="1" mr={2} color="white" onClick={handleLogin}>
                    {isLoadingLogin ? (<ThreeDots height={"10px"} color="white"/>) : (<>
                        Войти</>)}
                </Button>
                <Button as={Link} to="/register" bg="facebook.400" ml={2} size="md" flex="1" color="white"
                        onClick={fetchDataRegister}>
                    {isLoadingRegister ? (<ThreeDots height={"10px"} color="white"/>) : (<>
                        Регистрация</>)}
                </Button>
            </Flex>
        </Container>
    );
};

export default Login;
