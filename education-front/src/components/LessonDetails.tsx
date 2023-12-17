import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {instanceAxios} from '../utils/axiosConfig';
import {useAuth} from '../contexts/AuthContext';
import {Box, Button, Flex, FormControl, FormErrorMessage, FormLabel, Heading, Input, Text} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";
import {FiArrowLeftCircle, FiCheckSquare, FiEdit2, FiPlus} from "react-icons/fi";
import {ErrorCodes} from "./auth/ErrorCodes";
import {Oval, ThreeDots} from "react-loader-spinner";
import TaskCard from './authors/TaskCard'
import Statuses from './authors/Statuses'

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
    num: number;
    lessonStatus: string;
    createDate: string;
    updateDate: string;
}

interface LessonDto {
    lessonName: string;
    content: string;
    num: number;
}

interface LessonStatusDto {
    lessonName: string;
    content: string;
    lessonStatus: string;
}

const LessonDetails = () => {
    const {courseId, lessonId} = useParams();
    const [lesson, setLesson] = useState<Lesson | null>(null);
    const [isLoading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState("");
    const [errorNum, setErrorNum] = useState("");
    const [globalError, setGlobalError] = useState('');
    const [isChanged, setChanged] = useState(false);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [isAuthor, setIsAuthor] = useState(false);
    const [isDeleted, setIsDeleted] = useState(false);
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            if (user) {
                if (user.roles.includes('AUTHOR')) {
                    if (user.roles.includes('TEACHER')) {
                        try {
                            const response = await instanceAxios.get<Task[]>(`/teacher/homework-tasks/${courseId}/${lessonId}`);
                            setTasks(response.data);
                        } catch (error) {
                            console.error(error);
                        }
                    } else {
                        try {
                            const response = await instanceAxios.get<Task[]>(`/author/homework-tasks/${courseId}/${lessonId}`);
                            setTasks(response.data);
                            setIsAuthor(true);
                        } catch (error) {
                            console.error(error);
                        }
                    }
                } else if (user.roles.includes('TEACHER')) {
                    try {
                        const response = await instanceAxios.get<Task[]>(`/teacher/homework-tasks/${courseId}/${lessonId}`);
                        setTasks(response.data);
                    } catch (error) {
                        console.error(error);
                    }
                } else if (user.roles.includes('STUDENT')) {
                    try {
                        const response = await instanceAxios.get<Task[]>(`/student/course/${courseId}/lessons/${lessonId}/homeworks`);
                        setTasks(response.data);
                    } catch (error) {
                        console.error(error);
                    }
                }
            }
        };

        fetchData();
    }, [isDeleted]);

    const handleDeleteTask = async () => {
        setIsDeleted(!isDeleted);
    };

    const fetchLesson = async () => {
        if (user) {
            if (user.roles.includes('AUTHOR')) {
                if (user.roles.includes('TEACHER')) {
                    try {
                        const response = await instanceAxios.get(`/teacher/lessons/${courseId}/${lessonId}`);
                        setLesson(response.data);
                    } catch (error) {
                        console.error(error);
                    }
                } else {
                    try {
                        const response = await instanceAxios.get(`/author/lessons/${courseId}/${lessonId}`);
                        setLesson(response.data);
                        setIsAuthor(true);
                    } catch (error) {
                        console.error(error);
                    }
                }
            } else if (user.roles.includes('TEACHER')) {
                try {
                    const response = await instanceAxios.get(`/teacher/lessons/${courseId}/${lessonId}`);
                    setLesson(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes('STUDENT')) {
                try {
                    const response = await instanceAxios.get(`/student/course/${courseId}/lessons/${lessonId}`);
                    setLesson(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
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

    const validateLessonNum = (value: number) => {
        if (value === null || value === undefined) {
            setErrorNum("Номер урока обязателен");
        } else if (value <= 0) {
            setErrorNum('Урок нумеруется с 1')
        }
        return errorNum;
    };

    const handleSaveClick = async (values: LessonDto) => {
        if (!error) {
            let error;
            setGlobalError('');
            setLoading(true);
            try {
                const response = await instanceAxios.put(
                    `/author/lessons/${courseId}/${lessonId}`, values);
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
                    } else if (error.response.data.errorCode == ErrorCodes.LessonNumIsTaken) {
                        setErrorNum("Урок с таким номером уже существует")
                    }
                } else {
                    setGlobalError("Что-то пошло не так, попробуйте позже.");
                }
            }
            setLoading(false);
        }
    };

    const handleStatusChange = async (values: LessonStatusDto) => {
        try {
            const response = await instanceAxios.put(`/author/lessons/${courseId}/${lessonId}`, values);
            setLesson(response.data);
            setChanged(false);
            fetchLesson();
        } catch (error: any) {
            console.error(error);
        }
    }

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
                        initialValues={{lessonName: lesson.lessonName, content: lesson.content, num: lesson.num}}
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
                                            <FormLabel>Название урока</FormLabel>
                                            <Input {...field} placeholder="Имя урока"/>
                                            <FormErrorMessage mt={0} mb={2}>{error}</FormErrorMessage>
                                        </FormControl>
                                    )}
                                </Field>
                                <Field name="content">
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl width="100%" mt={0} mb={2}>
                                            <FormLabel>Содержимое урока</FormLabel>
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
                <Flex alignItems="center" justifyContent="center" mt={3} borderWidth="1px"
                      borderColor="grey.500"
                      w="500px"
                      mx="auto"
                      borderRadius={8}
                >
                    <Flex alignItems="center" textAlign="center" flexDir="column" mx="auto" mt={3}>
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
                        {user && user.roles.includes('AUTHOR') && isAuthor && (
                            <Flex mt={4} mb={4} flexDir="row" justifyContent="space-between" w="90%" gap={5}>
                                <Button
                                    leftIcon={<FiEdit2/>}
                                    color="white"
                                    colorScheme="green"
                                    onClick={() => {
                                        setIsEditing(true);
                                    }}
                                    width="100%"
                                >
                                    Редактировать
                                </Button>
                                {lesson.lessonStatus !== Statuses.LessonStatusActive && (<Button
                                        leftIcon={<FiCheckSquare/>}
                                        color="white"
                                        colorScheme="red"
                                        onClick={() => {
                                            handleStatusChange({
                                                lessonName: lesson.lessonName,
                                                content: lesson.content,
                                                lessonStatus: Statuses.LessonStatusActive
                                            });
                                        }}
                                        width="100%"
                                    >
                                        Запустить урок
                                    </Button>
                                )}
                            </Flex>
                        )}
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
                            onClick={() => navigate(`/tasks/create/${courseId}/${lessonId}`)}
                            _hover={{
                                bg: "#F9F9F9",
                            }}
                        >
                            <FiPlus size={32}/>
                        </Flex>
                    </Flex>
                </Box>
            )}
            {user && !isEditing && (user.roles.includes('TEACHER') || user.roles.includes('STUDENT')) && (
                <Flex gap={4} flexWrap="wrap" justifyContent="center" mt={3}>
                    {tasks && tasks.map((task: Task) => (
                        <TaskCard task={task} key={task.id} onDelete={handleDeleteTask}/>
                    ))}
                </Flex>
            )}
        </Box>
    );
};

export default LessonDetails;
