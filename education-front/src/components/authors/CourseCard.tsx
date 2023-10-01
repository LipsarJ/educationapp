import React, {useState} from "react";
import {FiX} from 'react-icons/fi';
import axios from 'axios';
import {
    Box,
    Button,
    Divider,
    Flex,
    Heading,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text,
} from '@chakra-ui/react';
import {NavLink} from 'react-router-dom';
import {ThreeDots} from 'react-loader-spinner';
import {ErrorCodes} from '../auth/ErrorCodes'

const CourseCard: React.FC<{ course: any, onDelete: () => void }> = ({course, onDelete}) => {
    const [isDeleting, setIsDeleting] = useState(false);
    const [isLodaing, setLoading] = useState(false);
    const [error, setError] = useState('');

    const toggleDeleteConfirmation = () => {
        setIsDeleting(!isDeleting);
        if (error) setError('');
    };

    const handleDelete = async () => {
        try {
            setIsDeleting(true);
            setLoading(true);
            await axios.delete(`${process.env.REACT_APP_API_URL}/author/courses/${course.id}`, {
                withCredentials: true,
            });
            setIsDeleting(false);
            toggleDeleteConfirmation();
            setLoading(false);
            onDelete();
        } catch (error: any) {
            console.error(error);
            if (error && error.response && error.response.data && error.response.data.errorCode) {
                if (error.response.data.errorCode == ErrorCodes.StatusIsInvalid) {
                    setError("Курс может быть удалён только в статусе TEMPLATE.")
                }
            } else {
                setError('Что-то пошло не так, попробуйте позже.');
            }
        }
        setLoading(false);
    };

    return (
        <Flex
            padding="16px"
            marginBottom="16px"
            position = "relative"
            borderRadius="8"
            flexBasis="400px"
            h="300px"
            flexDir="column"
            alignItems="center"
            gap={3}
            boxShadow="sm"
            border="1px solid #ccc"
            _hover={{
                bg: "#F9F9F9"
            }}
        >
            <Heading mt={1} size="md" textAlign="center" cursor="pointer">
                <Text as={NavLink} to={`/courses/${course.id}`}>{course.courseName}</Text>
            </Heading>

            <Divider w="100%"></Divider>
            <Box>
                <Heading size="md" textAlign="center">Статус: </Heading>
                <Text textAlign="center">
                    {course.courseStatus === 'TEMPLATE'
                        ? 'Подготавливается'
                        : course.courseStatus === 'ONGOING'
                            ? 'Идёт'
                            : 'Закончен'}
                </Text>
                <Heading size="md" textAlign="center">Дата создания:</Heading>
                <Text textAlign="center">
                    {course.createDate}
                </Text>
                <Heading size="md" textAlign="center">Дата обновления:</Heading>
                <Text textAlign="center">
                    {course.updateDate}
                </Text>
                <Heading size="md" textAlign="center">Количество учеников:</Heading>
                <Text textAlign="center">
                    {course.countStd}
                </Text>
            </Box>
            <Box position="absolute" right="2" top="2">
                <Box>
                    <FiX
                        color="red"
                        onClick={toggleDeleteConfirmation}
                        style={{
                            cursor: 'pointer',
                            fontSize: '18px'
                        }}
                    />
                </Box>
            </Box>

            <Modal isOpen={isDeleting} onClose={toggleDeleteConfirmation}>
                <ModalOverlay/>
                <ModalContent>
                    <ModalHeader>Подтверждение удаления</ModalHeader>
                    <ModalCloseButton/>
                    <ModalBody>
                        {error ? (
                            <Text color="red">{error}</Text>
                        ) : (
                            "Вы уверены, что хотите удалить этот курс?"
                        )}
                    </ModalBody>
                    <ModalFooter>
                        <Button colorScheme="red" onClick={handleDelete}>
                            {isLodaing && !error ? <ThreeDots color="white"/> : "Да"}
                        </Button>
                        <Button colorScheme="blue" ml={3} onClick={toggleDeleteConfirmation}>
                            Нет
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Flex>
    );
};

export default CourseCard;