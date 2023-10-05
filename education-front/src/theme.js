import { extendTheme } from '@chakra-ui/react';

const theme = extendTheme({
    styles: {
        global: {
            body: {
                bg: "white", // Установите серый фон здесь, например, gray.100
            },
        },
    },
    fonts: {
        body: 'Montserrat, sans-serif',
        heading: 'Montserrat, sans-serif'
    },
});

export default theme;