import React, {useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, FormControl, FormErrorMessage, Heading, Input} from '@chakra-ui/react';
import axios from 'axios';
import {useNavigate, useParams} from 'react-router-dom';
import {ThreeDots} from 'react-loader-spinner';
import {ErrorCodes} from '../auth/ErrorCodes'
import {format} from "date-fns-tz";

interface TaskData {
    title: string;
    description: string;
    deadlineDate: string;
}

const CreateTask: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');
    const [errorTitle, setErrorTitle] = useState('');
    const [errorDate, setErrorDate] = useState('');
    const [errorDesc, setErrorDesc] = useState('');
    const {courseId, lessonId} = useParams();
    const navigate = useNavigate();

    const validateTitle = (value: string) => {
        if (!value) {
            setErrorTitle("Заголовок задания обязателен");
        } else if (value.length < 3 || value.length > 50) {
            setErrorTitle("Заголовок задания должен быть от 3 до 20 символов");
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

    const handleCreateTask = async (values: TaskData) => {
        if (!errorDate && !errorTitle) {
            let error;
            setGlobalError('');
            setLoading(true);
            try {
                const inputDate = new Date(values.deadlineDate);
                values.deadlineDate = format(inputDate, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", {timeZone: 'UTC'});
                const response = await axios.post(`${process.env.REACT_APP_API_URL}/author/homework-tasks/${courseId}/${lessonId}`, values, {
                    withCredentials: true,
                });
                navigate(`/lessons/${courseId}/${lessonId}`);
            } catch (error: any) {
                console.error(error);
                if (error && error.response && error.response.data && error.response.data.errorCode) {
                    console.log(error.response.data.errorCode);
                    if (error.response.data.errorCode == ErrorCodes.TaskNameTaken) {
                        setErrorTitle("Имя задания уже существует.")
                    }
                } else {
                    setGlobalError('Что-то пошло не так, попробуйте позже.');
                }
            }
        }
        setLoading(false);
    };

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">
                Создать Задание
            </Heading>
            {globalError && <div style={{color: 'red'}}>{globalError}</div>}
            <Formik
                initialValues={{
                    title: '',
                    description: '',
                    deadlineDate: ''
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleCreateTask}
            >
                {() => (
                    <Form style={{minWidth: '100%'}}>
                        <Field name="title" validate={validateTitle}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorTitle && form.touched.title}
                                    width="100%"
                                    onChange={() => {
                                        if (errorTitle) {
                                            setErrorTitle('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Заголовок задания"
                                    />
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
                        <Button
                            mt={5}
                            color="white"
                            bg="facebook.400"
                            size="lg"
                            type="submit"
                            mx="auto"
                            display="block"
                        >
                            {isLoading ? (
                                <ThreeDots height={'10px'} color="white"/>
                            ) : (
                                <>Создать задание</>
                            )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default CreateTask;
