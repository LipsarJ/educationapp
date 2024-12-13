import React from 'react';
import {Box} from '@chakra-ui/react';

interface SidebarOverlayProps {
    isOpen: boolean;
    onClose: () => void;
}

const SidebarOverlay: React.FC<SidebarOverlayProps> = ({isOpen, onClose}) => {
    return isOpen ? (
        <Box
            position="fixed"
            top="0"
            left="0"
            width="100%"
            height="100%"
            background="rgba(0, 0, 0, 0.5)" // Полупрозрачный черный фон
            zIndex="999"
            onClick={onClose}
        />
    ) : null;
};

export default SidebarOverlay;
