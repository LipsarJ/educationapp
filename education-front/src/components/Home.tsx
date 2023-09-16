import React from 'react';
import {useAuth} from '../contexts/AuthContext';
import {Flex, Heading, Text, VStack} from '@chakra-ui/react';

const Home: React.FC = () => {
    const {isAuthenticated, user} = useAuth();

    return (
        <Flex w="50%" margin="0 auto" textAlign="center">
            <VStack spacing={8} alignItems="center"  >
                {isAuthenticated ? (
                    <>
                        <Heading as="h1" size="2xl">
                            Добро пожаловать, {user?.username}!
                        </Heading>
                        <Text fontSize="xl">
                            Наше приложение — это образовательная платформа, где ученики могут проходить курсы, а
                            учителя — создавать и проверять их.
                        </Text>
                    </>
                ) : (
                    <>
                        <Heading as="h1" size="2xl">
                            Добро пожаловать!
                        </Heading>
                        <Text fontSize="xl">
                            Пожалуйста, зарегистрируйтесь или войдите в приложение для дальнейшего использования.
                        </Text>
                    </>
                )}
            </VStack>
        </Flex>
    );
};

export default Home;
