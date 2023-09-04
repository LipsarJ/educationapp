import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Box, Heading, Text, VStack } from '@chakra-ui/react';

const Home: React.FC = () => {
    const { isAuthenticated, user } = useAuth();

    return (
        <VStack pl={5} pr={5} spacing={8} alignItems="center">
            {isAuthenticated ? (
                <>
                    <Heading mt={5} as="h1" size="2xl">
                        Добро пожаловать, {user?.username}!
                    </Heading>
                    <Text fontSize="xl">
                        Наше приложение — это образовательная платформа, где ученики могут проходить курсы, а учителя — создавать и проверять их.
                    </Text>
                </>
            ) : (
                <>
                    <Heading mt={5} as="h1" size="2xl">
                        Добро пожаловать!
                    </Heading>
                    <Text fontSize="xl">
                        Пожалуйста, зарегистрируйтесь или войдите в приложение для дальнейшего использования.
                    </Text>
                </>
            )}
        </VStack>
    );
};

export default Home;
