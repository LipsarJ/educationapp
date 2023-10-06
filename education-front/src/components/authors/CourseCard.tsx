import React, {useState} from "react";
import {FiX} from 'react-icons/fi';
import {instanceAxios} from '../../utils/axiosConfig';
import {useAuth} from '../../contexts/AuthContext';
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
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const [isHover, setHover] = useState(false);

    const toggleDeleteConfirmation = () => {
        setIsDeleting(!isDeleting);
        if (error) setError('');
    };

    const handleDelete = async () => {
        try {
            setIsDeleting(true);
            setLoading(true);
            await instanceAxios.delete(`/author/courses/${course.id}`);
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
            position="relative"
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
            onMouseEnter={() => setHover(true)}
            onMouseLeave={() => setHover(false)}
        >
            <Heading mt={1} size="md" textAlign="center" cursor="pointer" textDecoration={isHover ?"underline" : "none"}>
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
                {user && !user.roles.includes('STUDENT') && (
                    <>
                        <Heading size="md" textAlign="center">Количество учеников:</Heading>
                        <Text textAlign="center">
                            {course.countStd}
                        </Text>
                    </>
                )}
            </Box>
            {user && user.roles.includes('AUTHOR') && (
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
            )}

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