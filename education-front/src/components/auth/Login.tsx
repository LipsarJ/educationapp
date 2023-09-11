import React, {useState} from 'react';
import {Button, Container, Heading, Input} from '@chakra-ui/react';
import axios from '../../utils/axiosConfig';
import {useAuth} from '../../contexts/AuthContext';
import {useNavigate} from 'react-router-dom';


const Login: React.FC = () => {
    const navigate = useNavigate();
    const {setAuthenticated, setUser} = useAuth();
    const [loginData, setLoginData] = useState({username: '', password: ''});


    const handleLogin = async () => {
        let error;
        try {
            const response = await axios.post('/api/v1/auth/signin', loginData);
            setAuthenticated(true);
            setUser(response.data);
            console.log(response.data);
            navigate('/'); // перенаправляем на главную страницу
        } catch (error: any) {
            console.error(error);
            if (error.response.status == 401) {
                alert("Неверное имя пользователя или пароль.")
            }
        }
    };

    return (
        <Container centerContent mt="15" maxWidth="lg">
            <Heading mb={4} size="lg">Войти</Heading>
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
