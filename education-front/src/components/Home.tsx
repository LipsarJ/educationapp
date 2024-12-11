import React from 'react';
import {useAuth} from '../contexts/AuthContext';
import {useNavigate, Link} from "react-router-dom";
import {Button, Flex, Heading, Text, VStack} from '@chakra-ui/react';

const Home: React.FC = () => {
    const {isAuthenticated, user} = useAuth();
    const navigate = useNavigate();

    return (
        <Flex w="50%" margin="0 auto" mt={500} textAlign="center" flexDir="column">
            <VStack spacing={8} alignItems="center" mt={5}>
                {isAuthenticated ? (
                    <>
                        <Heading as="h1" size="xl">
                            Добро пожаловать, {user?.username}!
                        </Heading>
                        <Text fontSize="xl" color="#3D3D3D">
                            Наше приложение — это образовательная платформа, где ученики могут проходить курсы, а
                            учителя — создавать и проверять их.
                        </Text>
                    </>
                ) : (
                    <>
                        <Heading as="h1" size="xl">
                            Добро пожаловать!
                        </Heading>
                        <Text fontSize="xl" color="#3D3D3D">
                            Пожалуйста, зарегистрируйтесь или войдите в приложение для дальнейшего использования.
                        </Text>
                        <Text fontSize="xl" color="#3D3D3D">
                            Для навигации используйте выдвижное меню слева.
                        </Text>
                        <Flex mx="auto" mt={3} w="20%" justifyContent="center">
                            <Button as={Link} to="/login" bg="facebook.400" ml={2} size="md" flex="1" color="white">
                                Регистрация / Вход
                            </Button>
                        </Flex>
                    </>
                )}
            </VStack>
        </Flex>
    );
};

export default Home;
