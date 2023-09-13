import React, {useState} from 'react';
import {Button, Container, Heading, Input} from '@chakra-ui/react';
import axios from 'axios';
import {useAuth} from '../../contexts/AuthContext';
import {useNavigate} from 'react-router-dom';
import {ErrorCodes} from './ErrorCodes';

interface LoginData {
    username: string;
    password: string;
}

const Login: React.FC = () => {
    const navigate = useNavigate();
    const {setAuthenticated, setUser} = useAuth();
    const [loginData, setLoginData] = useState<LoginData>({username: '', password: ''});
    const [error, setError] = useState<string | null>(null);

    const handleLogin = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/signin', loginData, {
                withCredentials: true
            });
            setAuthenticated(true);
            setUser(response.data);
            console.log(response.data);
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
    };

    return (
        <Container centerContent mt="15" maxWidth="lg">
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
            <Button colorScheme="blue" size="md" pr={20} pl={20} onClick={handleLogin}>
                Войти
            </Button>
        </Container>
    );
};

export default Login;
