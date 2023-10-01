import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {Box, Button, Flex, FormControl, FormErrorMessage, Heading, Input, Text,} from "@chakra-ui/react";
import {Field, Form, Formik} from "formik";
import {FiArrowLeftCircle, FiCheckSquare, FiEdit2} from "react-icons/fi";
import {ErrorCodes} from "./auth/ErrorCodes";
import {Oval, ThreeDots} from "react-loader-spinner";
import {format} from "date-fns-tz";

interface Task {
    id: number;
    title: string;
    description: string;
    deadlineDate: string;
    createDate: string;
    updateDate: string;
}

interface TaskDto {
    title: string;
    description: string;
    deadlineDate: string;
}

const TaskDetails = () => {
    const {courseId, lessonId, id} = useParams();
    const [task, setTask] = useState<Task | null>(null);
    const [isLoading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [errorTitle, setErrorTitle] = useState("");
    const [errorDesc, setErrorDesc] = useState("");
    const [errorDate, setErrorDate] = useState("");
    const [globalError, setGlobalError] = useState("");
    const [isChanged, setChanged] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTask = async () => {
            try {
                const response = await axios.get<Task>(
                    `${process.env.REACT_APP_API_URL}/author/homework-tasks/${courseId}/${lessonId}/${id}`,
                    {withCredentials: true}
                );
                setTask(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchTask();
    }, [courseId, lessonId, id]);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const validateTitle = (value: string) => {
        if (!value) {
            setErrorTitle("Заголовок задания обязателен");
        } else if (value.length < 3 || value.length > 100) {
            setErrorTitle("Заголовок задания должен быть от 3 до 100 символов");
        }
        return errorTitle;
    };

    const validateDescription = (value: string) => {
        if (!value) {
            setErrorDesc("Описание задания обязательно");
        } else if (value.length < 10 || value.length > 1000) {
            setErrorDesc("Описание задания должно быть от 10 до 1000 символов");
        }
        return errorDesc;
    };

    const validateDate = (value: string) => {
        const dateFormat = /^\d{4}.\d{2}.\d{2}$/;
        if (!value) {
            setErrorDate('Дата сдачи обязательна')
        } else if (!value.match(dateFormat)) {
            setErrorDate('Неверный формат даты, используйте ГГГГ-ММ-ДД');
        } else {

            const parts = value.split('-');
            const year = parseInt(parts[0], 10);
            const month = parseInt(parts[1], 10);
            const day = parseInt(parts[2], 10);

            if (
                month < 1 || month > 12 ||
                day < 1 || day > 31 ||
                year < 1900 || year > 2099
            ) {
                setErrorDate('Недопустимая дата');
            }
        }

        return errorDate;
    };

    const handleSaveClick = async (values: TaskDto) => {
        if (!errorDesc && !errorTitle) {
            setGlobalError("");
            setLoading(true);
            try {
                const inputDate = new Date(values.deadlineDate);
                values.deadlineDate = format(inputDate, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", {timeZone: 'UTC'});
                const response = await axios.put(
                    `${process.env.REACT_APP_API_URL}/author/homework-tasks/${courseId}/${lessonId}/${id}`,
                    values,
                    {withCredentials: true}
                );
                setTask(response.data);
                setChanged(false);
            } catch (error: any) {
                console.error(error);
                if (
                    error &&
                    error.response &&
                    error.response.data &&
                    error.response.data.errorCode
                ) {
                    if (error.response.data.errorCode === ErrorCodes.TaskNameTaken) {
                        setErrorTitle("Заголовок задания уже существует.");
                    }
                } else {
                    setGlobalError("Что-то пошло не так, попробуйте позже.");
                }
            }
            setLoading(false);
        }
    };

    if (!task) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Heading size="lg" textAlign="center" mb={4} padding="16px">
                {isEditing ? "Редактирование задания" : task.title}
            </Heading>
            {isEditing ? (
                <Flex justifyContent="center" alignItems="center" flexDir="column">
                    {globalError && <div style={{color: "red"}}>{globalError}</div>}
                    <Formik
                        initialValues={{
                            title: task.title,
                            description: task.description,
                            deadlineDate: task.deadlineDate,
                        }}
                        onSubmit={handleSaveClick}
                        validateOnChange={false}
                        validateOnBlur={false}
                    >
                        {() => (
                            <Form style={{minWidth: "35%"}}>
                                <Field name="title" validate={validateTitle}>
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl
                                            isInvalid={errorTitle && form.touched.title}
                                            mb={2}
                                            onChange={() => {
                                                if (errorTitle) {
                                                    setErrorTitle("");
                                                }
                                                field.onChange(field.name);
                                            }}
                                        >
                                            <Input {...field} placeholder="Заголовок задания"/>
                                            <FormErrorMessage mt={0} mb={2}>
                                                {errorTitle}
                                            </FormErrorMessage>
                                        </FormControl>
                                    )}
                                </Field>
                                <Field name="description" validate={validateDescription}>
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl
                                            width="100%"
                                            mt={0} mb={2}
                                            isInvalid={errorDesc && form.touched.description}
                                            onChange={() => {
                                                if (errorDesc) {
                                                    setErrorDesc("");
                                                }
                                                field.onChange(field.name);
                                            }}
                                        >
                                            <Input {...field} placeholder="Описание задания"/>
                                            <FormErrorMessage mt={0} mb={2}>
                                                {errorDesc}
                                            </FormErrorMessage>
                                        </FormControl>
                                    )}
                                </Field>
                                <Field name="deadlineDate" validate={validateDate}>
                                    {({field, form}: { field: any; form: any }) => (
                                        <FormControl
                                            width="100%"
                                            isInvalid={errorDate && form.touched.deadlineDate}
                                            onChange={() => {
                                                if (errorDate) {
                                                    setErrorDate('');
                                                }
                                                field.onChange(field.name);
                                            }}
                                        >
                                            <Input
                                                {...field}
                                                mb={2}
                                                width="100%"
                                                placeholder="Дата сдачи"
                                            />
                                            <FormErrorMessage mt={0} mb={2}>
                                                {errorDate}
                                            </FormErrorMessage>
                                        </FormControl>
                                    )}
                                </Field>
                                <Flex flexDir="row" justifyContent="space-between" gap={4}>
                                    <Button
                                        mt={4}
                                        w="100%"
                                        color="white"
                                        size="lg"
                                        bg="facebook.400"
                                        mx="auto"
                                        display="block"
                                        onClick={() => {
                                            setIsEditing(false);
                                            setGlobalError("");
                                        }}
                                    >
                                        <Flex flexDir="row" justifyContent="center" alignItems="center">
                                            <FiArrowLeftCircle
                                                style={{alignSelf: "flex-start", marginRight: "10px"}}
                                            />
                                            <Text style={{alignSelf: "center"}}>Вернуться </Text>
                                        </Flex>
                                    </Button>
                                    <Button
                                        mt={4}
                                        w="100%"
                                        colorScheme="green"
                                        size="lg"
                                        type="submit"
                                        mx="auto"
                                        display="block"
                                    >
                                        {isLoading ? (
                                            <Flex alignItems="center" justifyContent="center">
                                                <ThreeDots height={"10px"} color="white"/>
                                            </Flex>
                                        ) : (
                                            <Flex flexDir="row" justifyContent="center" alignItems="center">
                                                <FiCheckSquare
                                                    style={{alignSelf: "flex-start", marginRight: "10px"}}
                                                />
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
                        <Text fontSize="lg"> Заголовок задания: {task.title}</Text>
                        <Text fontSize="lg"> Описание задания: {task.description}</Text>
                        <Text fontSize="lg" mb={2}>
                            Дата создания: {task.createDate}
                        </Text>
                        <Text fontSize="lg" mb={2}>
                            Дата обновления: {task.updateDate}
                        </Text>
                        <Text fontSize="lg">Дата сдачи: {task.deadlineDate}</Text>
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
        </Box>
    );
};

export default TaskDetails;
