import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {instanceAxios} from '../utils/axiosConfig';
import {
    Box,
    Button,
    Flex,
    FormControl,
    FormErrorMessage,
    Heading,
    Input,
    Text,
    Table,
    Thead,
    Tbody,
    Tr,
    Th,
    Td
} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";
import {FiArrowLeftCircle, FiCheckSquare, FiEdit2, FiPlus, FiUsers} from "react-icons/fi";
import {ErrorCodes} from "./auth/ErrorCodes";
import {Oval} from "react-loader-spinner";
import LessonCard from './authors/LessonCard';
import Statuses from './authors/Statuses';
import {useAuth} from '../contexts/AuthContext';

interface Course {
    id: number;
    courseName: string;
    courseStatus: string;
    createDate: string;
    updateDate: string;
    countStd: number;
}

interface UserData {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
}

interface Lesson {
    id: number;
    lessonName: string;
    content: string;
    lessonStatus: string;
    createDate: string;
    updateDate: string;
}

interface CourseDto {
    courseName: string;
}

interface CourseStatusDto {
    courseName: string;
    courseStatus: string;
}

const CourseDetails = () => {
    const {id} = useParams();
    const [course, setCourse] = useState<Course | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState("");
    const [globalError, setGlobalError] = useState('');
    const [isChanged, setChanged] = useState(false);
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [isDeleted, setIsDeleted] = useState(false);
    const [authors, setAuthors] = useState<UserData[]>([]);
    const [teachers, setTeachers] = useState<UserData[]>([]);
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const navigate = useNavigate();

    const fetchData = async () => {
        if (user) {
            if (user.roles.includes('AUTHOR')) {
                try {
                    const response = await instanceAxios.get<Lesson[]>(`/author/lessons/${id}`);
                    setLessons(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes('TEACHER')) {
                try {
                    const response = await instanceAxios.get<Lesson[]>(`/teacher/lessons/${id}`);
                    setLessons(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes('STUDENT')) {
                try {
                    const response = await instanceAxios.get<Lesson[]>(`/student/course/${id}/lessons`);
                    setLessons(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
        }
    }

    useEffect(() => {

        fetchData();
    }, [isDeleted]);

    const handleDeleteLesson = async () => {
        setIsDeleted(!isDeleted);
    };

    const fetchCourse = async () => {
        if (user) {
            if (user.roles.includes('AUTHOR')) {
                try {
                    const response = await instanceAxios.get(`/author/courses/${id}`);
                    setCourse(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes('TEACHER')) {
                try {
                    const response = await instanceAxios.get(`/teacher/courses/${id}`);
                    setCourse(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes('STUDENT')) {
                try {
                    const response = await instanceAxios.get(`/student/course/${id}`);
                    setCourse(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
        }
    };

    useEffect(() => {
        fetchCourse();
    }, []);

    useEffect(() => {
        const fetchAuthorsAndTeachers = async () => {
            try {
                const authorsResponse = await instanceAxios.get<UserData[]>(`/author/courses/${id}/authors`);
                setAuthors(authorsResponse.data);

                const teachersResponse = await instanceAxios.get<UserData[]>(`/author/courses/${id}/teachers`);
                setTeachers(teachersResponse.data);
            } catch (error) {
                console.error(error);
            }
        };

        if (course) {
            fetchAuthorsAndTeachers();
        }
    }, [id, course]);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const validateCourseName = (value: string) => {
        if (!value) {
            setError("Имя курса обязательно");
        } else if (value.length < 3 || value.length > 50) {
            setError("Имя курса должно быть от 3 до 50 символов");
        }
        return error;
    };

    const handleSaveClick = async (values: CourseDto) => {
        if (!error) {
            try {
                const response = await instanceAxios.put(`/author/courses/${id}`, values);
                setCourse(response.data);
                setChanged(false);
                fetchCourse();
            } catch (error: any) {
                console.error(error);
                if (
                    error &&
                    error.response &&
                    error.response.data &&
                    error.response.data.errorCode
                ) {
                    if (error.response.data.errorCode == ErrorCodes.CourseNameTaken) {
                        setError("Имя курса уже существует.");
                    } else if (
                        error.response.data.errorCode == ErrorCodes.StatusIsInvalid
                    ) {
                        setError("Урок может быть создан только в статусе TEMPLATE");
                    }
                } else {
                    setGlobalError("Что-то пошло не так, попробуйте позже.");
                }
            }
        }
    };

    const handleStatusChange = async (values: CourseStatusDto) => {
        try {
            const response = await instanceAxios.put(
                `/author/courses/${id}`, values);
            setCourse(response.data);
            setChanged(false);
            fetchCourse();
        } catch (error: any) {
            console.error(error);
        }
    }

    if (!course) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Heading size="lg" textAlign="center" mb={4} padding="16px">
                {isEditing ? "Редактирование курса" : course.courseName}
            </Heading>
            {isEditing ? (
                <Flex justifyContent="center" alignItems="center">
                    <Formik
                        initialValues={{courseName: course.courseName}}
                        onSubmit={handleSaveClick}
                        validateOnChange={false}
                        validateOnBlur={false}
                    >
                        {({handleSubmit}) => (
                            <Form>
                                <Field name="courseName" validate={validateCourseName}>
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl
                                            isInvalid={error && form.touched.courseName}
                                            display='flex'
                                            onChange={() => {
                                                if (error) {
                                                    setError("");
                                                }
                                                field.onChange(field.name);
                                                setChanged(true);
                                            }}
                                        >
                                            <Button
                                                bg="white"
                                                style={{
                                                    backgroundColor: "transparent",
                                                    border: "none",
                                                    cursor: "pointer"
                                                }}
                                                onClick={() => {
                                                    setIsEditing(false);
                                                    setChanged(false);
                                                }}
                                            >
                                                <FiArrowLeftCircle
                                                    color="black"
                                                    size="40px"
                                                />
                                            </Button>
                                            <Flex flexDir="column" alignItems="center">
                                                <Input style={{width: "350px"}} {...field} placeholder="Имя курса"/>
                                                <FormErrorMessage textAlign="center">{error}</FormErrorMessage>
                                            </Flex>

                                            <Button
                                                bg="white"
                                                style={{
                                                    backgroundColor: "transparent",
                                                    border: "none",
                                                    cursor: isChanged && !error ? "pointer" : "not-allowed",
                                                }}
                                                isDisabled={!isChanged || !!error}
                                                onClick={() => {
                                                    if (isChanged) {
                                                        handleSubmit();
                                                    }
                                                }}
                                            >
                                                <FiCheckSquare
                                                    color={isChanged && !error ? "green" : "gray"}
                                                    size="40px"
                                                />
                                            </Button>
                                        </FormControl>
                                    )}
                                </Field>
                            </Form>
                        )}
                    </Formik>
                </Flex>
            ) : (
                <Flex justifyContent="center" mt={3} borderRadius={8}
                      borderWidth="1px"
                      borderColor="grey.500"
                      w="500px"
                      mx="auto"
                >
                    <Flex alignItems="center" flexDir="column" mt={3}>
                        <Text fontSize="lg" mb={2}>
                            Статус:{" "}
                            {course.courseStatus === "TEMPLATE"
                                ? "Подготавливается"
                                : course.courseStatus === "ONGOING"
                                    ? "Идёт"
                                    : "Закончен"}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Дата создания: {course.createDate}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Дата обновления: {course.updateDate}
                        </Text>
                        {user && !user.roles.includes('STUDENT') &&(
                        <Text fontSize="lg" mb={2}>
                            Количество учеников: {course.countStd}
                        </Text>
                        )}
                        {user && user.roles.includes('AUTHOR') && (
                            <Flex mt={4} flexDir="row" justifyContent="space-between" w="100%" gap={5}>
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
                                {course.courseStatus !== Statuses.CourseStatusOngoing && (
                                    <Button
                                        leftIcon={<FiCheckSquare/>}
                                        color="white"
                                        colorScheme="red"
                                        onClick={() => {
                                            handleStatusChange({
                                                courseName: course.courseName,
                                                courseStatus: Statuses.CourseStatusOngoing
                                            });
                                        }}
                                        width="100%"
                                    >
                                        Запустить курс
                                    </Button>
                                )}
                            </Flex>
                        )}
                        {user && user.roles.includes('TEACHER') && user.roles.includes('STUDENT') && (
                            <Flex justifyContent="center" w = "100%" mt={4} gap={5} borderRadius={8}>
                                <Button
                                    color="white"
                                    bg="blue.500"
                                    leftIcon={<FiUsers/>}
                                    onClick={() => {
                                        navigate(`/users/${id}/${'STUDENT'}`)
                                    }}
                                    width="50%"
                                >
                                    Изменить студентов
                                </Button>
                            </Flex>
                        )}
                        {user && user.roles.includes('AUTHOR') && (
                            <Flex justifyContent="space-between" mt={4} gap={5} borderRadius={8}>
                                <Button
                                    color="white"
                                    bg="blue.500"
                                    leftIcon={<FiUsers/>}
                                    onClick={() => {
                                        navigate(`/users/${id}/${'AUTHOR'}`)
                                    }}
                                    width="50%"
                                >
                                    Изменить авторов
                                </Button>
                                <Button
                                    color="white"
                                    leftIcon={<FiUsers/>}
                                    bg="blue.500"
                                    onClick={() => {
                                        navigate(`/users/${id}/${'TEACHER'}`)
                                    }}
                                    width="50%"
                                >
                                    Изменить учителей
                                </Button>
                            </Flex>
                        )}
                        {!authors || !teachers ? (
                            <Flex justifyContent="center" alignItems="center" height="100vh">
                                <Oval color="#295C48" secondaryColor="#2B415B"/>
                            </Flex>
                        ) : (
                            <Flex
                                mt={10}
                                mb={4}
                                gap={5}
                                flexDir="row"
                                justifyContent="space-between"
                                alignItems="flex-start"
                                w="100%"
                            >
                                <Flex flexDir="column" alignItems="center" flex="1">
                                    <Heading size="md" mb={2}>
                                        Авторы курса
                                    </Heading>
                                    {authors.map((author) => (
                                        <Text key={author.id}>{author.username}</Text>
                                    ))}
                                </Flex>
                                <Flex flexDir="column" alignItems="center" flex="1">
                                    <Heading size="md" mb={2}>
                                        Учителя курса
                                    </Heading>
                                    {teachers.map((teacher) => (
                                        <Text key={teacher.id}>{teacher.username}</Text>
                                    ))}
                                </Flex>
                            </Flex>
                        )}
                    </Flex>
                </Flex>
            )}
            {isEditing && !lessons && (
                <Flex justifyContent="center" alignItems="center" height="100vh">
                    <Oval color="#295C48" secondaryColor="#2B415B"/>
                </Flex>
            )}

            {isEditing && (
                <>
                    <Heading size="lg" textAlign="center" padding="20px">
                        Уроки курса
                    </Heading>
                    <Flex
                        gap={3}
                        flexWrap="wrap"
                        flexDir="row"
                        justifyContent="center"
                    >
                        {lessons && lessons.map((lesson: Lesson) => (
                            <LessonCard lesson={lesson} key={lesson.id} onDelete={handleDeleteLesson}/>
                        ))}
                        <Flex
                            key="create-course"
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
                            onClick={() => navigate(`/lessons/create/${course.id}`)}
                            _hover={{
                                bg: "#F9F9F9"
                            }}
                        >
                            <FiPlus size={32}/>
                        </Flex>
                    </Flex>

                </>
            )}
            {user && !user.roles.includes('AUTHOR') && (
                <Flex
                    gap={3}
                    mt={3}
                    flexWrap="wrap"
                    flexDir="row"
                    justifyContent="center"
                >
                    {lessons && lessons.map((lesson: Lesson) => (
                        <LessonCard lesson={lesson} key={lesson.id} onDelete={handleDeleteLesson}/>
                    ))
                    }
                </Flex>
            )}
        </Box>
    );
};

export default CourseDetails;
