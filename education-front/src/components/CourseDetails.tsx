import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {Box, Button, Flex, FormControl, FormErrorMessage, Heading, Input, Text,} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";
import {FiArrowLeftCircle, FiCheckSquare, FiEdit2, FiPlus, FiUserPlus} from "react-icons/fi";
import {ErrorCodes} from "./auth/ErrorCodes";
import {Oval} from "react-loader-spinner";
import LessonCard from './authors/LessonCard'

interface Course {
    id: number;
    courseName: string;
    courseStatus: string;
    createDate: string;
    updateDate: string;
    countStd: number;
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

const CourseDetails = () => {
    const {id} = useParams();
    const [course, setCourse] = useState<Course | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState("");
    const [globalError, setGlobalError] = useState('');
    const [isChanged, setChanged] = useState(false);
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [isDeleted, setIsDeleted] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get<Lesson[]>(
                    `${process.env.REACT_APP_API_URL}/author/lessons/${id}`,
                    {withCredentials: true}
                );
                setLessons(response.data);
            } catch (error) {
                console.error(error);
            }
        }

        fetchData();
    }, [isDeleted]);

    const handleDeleteLesson = async () => {
        setIsDeleted(!isDeleted);
    };

    const fetchCourse = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_API_URL}/author/courses/${id}`,
                {withCredentials: true}
            );
            setCourse(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchCourse();
    }, []);

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
                const response = await axios.put(
                    `${process.env.REACT_APP_API_URL}/author/courses/${id}`,
                    values,
                    {withCredentials: true}
                );
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
                                                disabled={!isChanged}
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
                                                disabled={!isChanged || !!error}
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
                <Flex justifyContent="center" mt={3}>
                    <Flex alignItems="center" flexDir="column">
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
                        <Text fontSize="lg" mb={2}>
                            Количество учеников: {course.countStd}
                        </Text>
                        <Button
                            leftIcon={<FiEdit2/>}
                            mt={4}
                            color="white"
                            colorScheme="green"
                            w="100%"
                            onClick={() => {
                                setIsEditing(true);
                            }}
                        >
                            Редактировать
                        </Button>
                        <Flex justifyContent="space-between" mt={4} gap={5} borderRadius={8}>
                            <Button
                                color="white"
                                bg="blue.500"
                                leftIcon={<FiUserPlus/>}
                                onClick={() => {
                                    // Добавьте логику для перехода на страницу добавления авторов
                                }}
                                width="50%"
                            >
                                Добавить авторов
                            </Button>
                            <Button
                                color="white"
                                leftIcon={<FiUserPlus/>}
                                bg="blue.500"
                                onClick={() => {
                                    // Добавьте логику для перехода на страницу добавления учителей
                                }}
                                width="50%"
                            >
                                Добавить учителей
                            </Button>
                        </Flex>
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

        </Box>
    );
};

export default CourseDetails;
