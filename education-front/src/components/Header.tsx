import React from 'react';
import { Flex, Text, IconButton } from '@chakra-ui/react';
import { FiMenu } from 'react-icons/fi';

interface HeaderProps {
    onToggleSidebar: () => void;
}

const Header: React.FC<HeaderProps> = ({ onToggleSidebar }) => {
    return (
        <Flex alignItems="center" justifyContent="space-between" bg="blue.500" p={3} w="100%">
            <IconButton
                aria-label="Open Sidebar"
                background="none"
                zIndex="1001"
                icon={<FiMenu />}
                onClick={onToggleSidebar}
            />
            <Text fontWeight="bold" fontSize="lg" color="white">
                EducationApp
            </Text>
            <Flex>
                {/* Дополнительные элементы справа */}
            </Flex>
        </Flex>
    );
};

export default Header;
