import { extendTheme } from '@chakra-ui/react';

const theme = extendTheme({
    styles: {
        global: {
            body: {
                bgSize: "cover",
                bgPosition: "top",
                bgImage: "https://img.freepik.com/free-vector/flat-design-english-school-background_23-2149487419.jpg?t=st=1733907190~exp=1733910790~hmac=99dc2e769fe98e21c5905952bce866ff7139bb45d574e48035d2cb86b1dc4ef9&w=1800", // Установите серый фон здесь, например, gray.100
            },
        },
    },
    fonts: {
        body: 'Montserrat, sans-serif',
        heading: 'Montserrat, sans-serif'
    },
});

export default theme;