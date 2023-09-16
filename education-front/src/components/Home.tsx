import React from 'react';
import {useAuth} from '../contexts/AuthContext';
import {Flex, Heading, Text, VStack} from '@chakra-ui/react';

const Home: React.FC = () => {
    const {isAuthenticated, user} = useAuth();

    return (
        <Flex w="50%" margin="0 auto" textAlign="center" flexDir="column">
            <VStack spacing={8} alignItems="center" mt={5}>
                {isAuthenticated ? (
                    <>
                        <Heading as="h1" size="xl">
                            Добро пожаловать, {user?.username}!
                        </Heading>
                        <Text fontSize="xl" color = "#3D3D3D">
                            Наше приложение — это образовательная платформа, где ученики могут проходить курсы, а
                            учителя — создавать и проверять их.
                        </Text>
                    </>
                ) : (
                    <>
                        <Heading as="h1" size="xl">
                            Добро пожаловать!
                        </Heading>
                        <Text fontSize="xl" color = "#3D3D3D">
                            Пожалуйста, зарегистрируйтесь или войдите в приложение для дальнейшего использования.
                        </Text>
                    </>
                )}
            </VStack>
        </Flex>
    );
};

export default Home;
