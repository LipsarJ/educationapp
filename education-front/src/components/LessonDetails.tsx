import React, {useEffect, useState} from "react";
import {NavLink, useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {
    Box,
    Button,
    Divider,
    Flex,
    FormControl,
    FormErrorMessage,
    Heading,
    Input,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text,
} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";
import {FiArrowLeftCircle, FiCheckSquare, FiEdit2, FiPlus, FiX} from "react-icons/fi";
import {ErrorCodes} from "./auth/ErrorCodes";
import {Oval, ThreeDots} from "react-loader-spinner";
import TaskCard from './authors/TaskCard'

interface Task {
    id: number;
    title: string;
    description: string;
    deadlineDate: string;
    createDate: string;
    updateDate: string;
}

interface Lesson {
    id: number;
    lessonName: string;
    content: string;
    lessonStatus: string;
    createDate: string;
    updateDate: string;
}

interface LessonDto {
    lessonName: string;
    content: string;
}

const LessonDetails = () => {
    const {id, lessonId} = useParams();
    const [lesson, setLesson] = useState<Lesson | null>(null);
    const [isLoading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState("");
    const [globalError, setGlobalError] = useState('');
    const [isChanged, setChanged] = useState(false);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [isDeleted, setIsDeleted] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get<Task[]>(
                    `${process.env.REACT_APP_API_URL}/author/homework-tasks/${id}/${lessonId}`,
                    {withCredentials: true}
                );
                setTasks(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, [isDeleted]);

    const handleDeleteTask = async () => {
        setIsDeleted(!isDeleted);
    };

    const fetchLesson = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_API_URL}/author/lessons/${id}/${lessonId}`,
                {withCredentials: true}
            );
            setLesson(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchLesson();
    }, []);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const validateLessonName = (value: string) => {
        if (!value) {
            setError("Имя урока обязательно");
        } else if (value.length < 3 || value.length > 50) {
            setError("Имя урока должно быть от 3 до 50 символов");
        }
        return error;
    };

    const handleSaveClick = async (values: LessonDto) => {
        if (!error) {
            let error;
            setGlobalError('');
            setLoading(true);
            try {
                const response = await axios.put(
                    `${process.env.REACT_APP_API_URL}/author/lessons/${id}/${lessonId}`,
                    values,
                    {withCredentials: true}
                );
                setLesson(response.data);
                setChanged(false);
                fetchLesson();
            } catch (error: any) {
                console.error(error);
                if (
                    error &&
                    error.response &&
                    error.response.data &&
                    error.response.data.errorCode
                ) {
                    if (error.response.data.errorCode == ErrorCodes.LessonNameTaken) {
                        setError("Имя урока уже существует.");
                    }
                } else {
                    setGlobalError("Что-то пошло не так, попробуйте позже.");
                }
            }
            setLoading(false);
        }
    };

    if (!lesson) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Heading size="lg" textAlign="center" mb={4} padding="16px">
                {isEditing ? "Редактирование урока" : lesson.lessonName}
            </Heading>
            {isEditing ? (
                <Flex justifyContent="center" alignItems="center" flexDir="column">
                    {globalError && <div style={{color: 'red'}}>{globalError}</div>}
                    <Formik
                        initialValues={{lessonName: lesson.lessonName, content: lesson.content}}
                        onSubmit={handleSaveClick}
                        validateOnChange={false}
                        validateOnBlur={false}
                    >
                        {() => (
                            <Form style={{minWidth: '35%'}}>
                                <Field name="lessonName" validate={validateLessonName}>
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl
                                            isInvalid={error && form.touched.lessonName}
                                            mb={2}
                                            onChange={() => {
                                                if (error) {
                                                    setError("");
                                                }
                                                field.onChange(field.name);
                                            }}
                                        >
                                            <Input {...field} placeholder="Имя урока"/>
                                            <FormErrorMessage mt={0} mb={2}>{error}</FormErrorMessage>
                                        </FormControl>
                                    )}
                                </Field>
                                <Field name="content">
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl width="100%" mt={0} mb={2}>
                                            <Input {...field} placeholder="Содержимое урока"/>
                                        </FormControl>
                                    )}
                                </Field>
                                <Flex flexDir="row" justifyContent="space-between" gap={4}>
                                    <Button mt={4} w="100%" color="white"
                                            size="lg"
                                            bg="facebook.400"
                                            mx="auto"
                                            display="block"
                                            onClick={() => {
                                                setIsEditing(false);
                                                setGlobalError('');
                                            }}
                                    >
                                        <Flex flexDir="row" justifyContent="center" alignItems="center">
                                            <FiArrowLeftCircle style={{alignSelf: "flex-start", marginRight: "10px"}}/>
                                            <Text style={{alignSelf: "center"}}>Вернуться </Text>
                                        </Flex>
                                    </Button>
                                    <Button mt={4} w="100%" colorScheme="green"
                                            size="lg" type='submit'
                                            mx="auto"
                                            display="block"
                                    >
                                        {isLoading ? (
                                                <Flex alignItems="center" justifyContent="center">
                                                    <ThreeDots height={"10px"} color="white"/>
                                                </Flex>
                                            )
                                            :
                                            (<Flex flexDir="row" justifyContent="center" alignItems="center">
                                                    <FiCheckSquare
                                                        style={{alignSelf: "flex-start", marginRight: "10px"}}/>
                                                    <Text style={{alignSelf: "center"}}>Сохранить</Text>
                                                </Flex>
                                            )}
                                    </Button>
                                </Flex>
                            </Form>
                        )}
                    </Formik>
                </Flex>
            ) : (
                <Flex alignItems="center" justifyContent="center" mt={3}>
                    <Flex alignItems="center" textAlign="center" flexDir="column" mx="auto">
                        <Text fontSize="lg" mb={2}>
                            Статус:{" "}
                            {lesson.lessonStatus === "ACTIVE"
                                ? "Активен" : "Закончен"}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Содержание урока: {lesson.content}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Дата создания: {lesson.createDate}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Дата обновления: {lesson.updateDate}
                        </Text>
                        <Button
                            leftIcon={<FiEdit2/>}
                            mt={4}
                            color="white"
                            colorScheme="green"
                            onClick={() => {
                                setIsEditing(true);
                            }}
                            width="100%"
                        >
                            Редактировать
                        </Button>
                    </Flex>
                </Flex>
            )}

            {isEditing && !tasks && (
                <Flex justifyContent="center" alignItems="center" height="100vh">
                    <Oval color="#295C48" secondaryColor="#2B415B"/>
                </Flex>
            )}

            {isEditing && (
                <Box mt={4}>
                    <Heading size="lg" textAlign="center" mb={4}>
                        Задания урока
                    </Heading>
                    <Flex gap={4} flexWrap="wrap" justifyContent="center">
                        {tasks && tasks.map((task: Task) => (
                            <TaskCard task={task} key={task.id} onDelete={handleDeleteTask}/>
                        ))}
                        <Flex
                            key="create-task"
                            padding="16px"
                            borderRadius="8"
                            flexBasis="400px"
                            h="300px"
                            gap={3}
                            alignItems="center"
                            justifyContent="center"
                            cursor="pointer"
                            color="gray"
                            boxShadow="md"
                            onClick={() => navigate(`/tasks/create/${id}/${lessonId}`)}
                            _hover={{
                                bg: "#F9F9F9",
                            }}
                        >
                            <FiPlus size={32}/>
                        </Flex>
                    </Flex>
                </Box>
            )}
        </Box>
    );
};

export default LessonDetails;
