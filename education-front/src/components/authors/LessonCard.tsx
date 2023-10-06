import React, {useState} from "react";
import {NavLink, useParams} from "react-router-dom";
import {instanceAxios} from '../../utils/axiosConfig';
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
} from "@chakra-ui/react";
import {FiX} from "react-icons/fi";
import {ErrorCodes} from "../auth/ErrorCodes";
import {ThreeDots} from "react-loader-spinner";
import {useAuth} from '../../contexts/AuthContext';

const LessonCard: React.FC<{ lesson: any, onDelete: () => void, provided: any }> = ({lesson, onDelete, provided}) => {
    const [isDeleting, setIsDeleting] = useState(false);
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const [isHover, setHover] = useState(false);
    const {id} = useParams();

    const toggleDeleteConfirmation = () => {
        setIsDeleting(!isDeleting);
        if (error) setError('');
    };

    const handleDelete = async () => {
        setIsDeleting(true);
        setLoading(true);
        try {
            await instanceAxios.delete(`/author/lessons/${id}/${lesson.id}`);
            toggleDeleteConfirmation();
            setIsDeleting(false);
            onDelete();
        } catch (error: any) {
            console.error(error);
            if (error && error.response && error.response.data && error.response.data.errorCode) {
                if (error.response.data.errorCode == ErrorCodes.StatusIsInvalid) {
                    setError("Урок может быть удалён только в статусе NOT_ACTIVE.")
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
            position="relative"
            marginBottom="16px"
            borderRadius="8"
            flexBasis="400px"
            h="300px"
            flexDir="column"
            alignItems="center"
            gap={3}
            boxShadow="sm"
            border="1px solid #ccc"
            _hover={{
                bg: "#F9F9F9",
            }}
            onMouseEnter={() => setHover(true)}
            onMouseLeave={() => setHover(false)}
            ref={provided ? provided.innerRef : undefined}
            {...(provided ? provided.draggableProps : {})}
            {...(provided ? provided.dragHandleProps : {})}
        >
            <Heading mt={1} size="md" textAlign="center" cursor="pointer" textDecoration={isHover ?"underline" : "none"}>
                <Text as={NavLink} to={`/lessons/${id}/${lesson.id}`}>{lesson.lessonName}</Text>
            </Heading>
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
            <Box position="absolute" left="2" top="2">
                {lesson.order}
            </Box>

            <Divider w="100%"></Divider>
            <Box _hover={{
                textDecoration: "none"
            }}
            >
                <Heading size="md" textAlign="center">Статус: </Heading>
                <Text textAlign="center">
                    {lesson.lessonStatus === 'ACTIVE'
                        ? 'Активен'
                        : 'Закончен'}
                </Text>
                <Heading size="md" textAlign="center">Дата создания:</Heading>
                <Text textAlign="center">
                    {lesson.createDate}
                </Text>
                <Heading size="md" textAlign="center">Дата обновления:</Heading>
                <Text textAlign="center">
                    {lesson.updateDate}
                </Text>
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
                            "Вы уверены, что хотите удалить этот урок?"
                        )}
                    </ModalBody>
                    <ModalFooter>
                        <Button colorScheme="red" onClick={handleDelete}>
                            {isLoading && !error ? <ThreeDots color="white"/> : "Да"}
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

export default LessonCard;