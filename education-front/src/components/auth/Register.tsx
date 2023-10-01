import React, { useState } from 'react';
import { Field, Form, Formik } from 'formik';
import { Button, Container, FormControl, FormErrorMessage, Heading, Input } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { ErrorCodes } from './ErrorCodes';
import axios from 'axios';
import { ThreeDots } from 'react-loader-spinner';

interface SignupData {
    username: string;
    email: string;
    password: string;
    middlename: string;
    firstname: string;
    lastname: string;
}

const Register: React.FC = () => {
    const navigate = useNavigate();

    const [errorUsername, setErrorUsername] = useState('');
    const [errorEmail, setErrorEmail] = useState('');
    const [errorPassword, setErrorPassword] = useState('');
    const [errorMiddlename, setErrorMiddlename] = useState('');
    const [errorFirstname, setErrorFirstname] = useState('');
    const [errorLastname, setErrorLastname] = useState('');
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');

    const validateUsername = (value: string) => {
        if (!value) {
            setErrorUsername('Имя пользователя обязательно');
        } else if (value.length < 3 || value.length > 20) {
            setErrorUsername('Имя пользователя должно быть от 3 до 20 символов');
        } else {
            setErrorUsername(''); // Сброс ошибки при успешной валидации
        }
    };

    const validatePassword = (value: string) => {
        if (!value) {
            setErrorPassword('Пароль обязателен');
        } else if (value.length < 6 || value.length > 40) {
            setErrorPassword('Пароль должен быть от 6 до 40 символов');
        } else {
            setErrorPassword(''); // Сброс ошибки при успешной валидации
        }
    };

    const validateMiddlename = (value: string) => {
        if (!value) {
            setErrorMiddlename('Отчество обязательно');
        } else {
            setErrorMiddlename(''); // Сброс ошибки при успешной валидации
        }
    };

    const validateFirstname = (value: string) => {
        if (!value) {
            setErrorFirstname('Имя обязательно');
        } else {
            setErrorFirstname(''); // Сброс ошибки при успешной валидации
        }
    };

    const validateLastname = (value: string) => {
        if (!value) {
            setErrorLastname('Фамилия обязательна');
        } else {
            setErrorLastname(''); // Сброс ошибки при успешной валидации
        }
    };

    const validateEmail = (value: string) => {
        if (!value) {
            setErrorEmail('Почта обязательна');
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
            setErrorEmail('Неверный формат электронной почты');
        } else {
            setErrorEmail(''); // Сброс ошибки при успешной валидации
        }
    };

    const handleRegister = async (values: SignupData) => {
        // Проверка на наличие ошибок валидации
        if (!errorUsername && !errorEmail && !errorPassword && !errorMiddlename && !errorFirstname && !errorLastname) {
            setGlobalError('');
            setLoading(true);
            try {
                const response = await axios.post(process.env.REACT_APP_API_URL + '/auth/signup', values, {
                    withCredentials: true,
                });
                navigate('/login');
            } catch (error: any) {
                console.error(error);
                if (error && error.response && error.response.data && error.response.data.errorCode) {
                    const errorCode = error.response.data.errorCode;
                    if (errorCode === ErrorCodes.UsernameTaken) {
                        setErrorUsername('Имя пользователя занято');
                    }
                    if (errorCode === ErrorCodes.EmailTaken) {
                        setErrorEmail('E-mail уже используется');
                    }
                    // Другие обработки ошибок
                } else {
                    setGlobalError('Что-то пошло не так, попробуйте позже.');
                }
            }
            setLoading(false);
        }
    };

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">
                Зарегистрироваться
            </Heading>
            {globalError && <div style={{ color: 'red' }}>{globalError}</div>}
            <Formik
                initialValues={{
                    username: '',
                    email: '',
                    password: '',
                    middlename: '',
                    firstname: '',
                    lastname: '',
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleRegister}
            >
                {() => (
                    <Form style={{ minWidth: '100%' }}>
                        <Field name="username" validate={validateUsername}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorUsername && form.touched.username}
                                    width="100%"
                                    onChange={() => {
                                        if (errorUsername) {
                                            setErrorUsername('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Имя пользователя"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorUsername}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>

                        <Field name="email" validate={validateEmail}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorEmail && form.touched.email}
                                    width="100%"
                                    onChange={() => {
                                        if (errorEmail) {
                                            setErrorEmail('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Email"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorEmail}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="password" validate={validatePassword}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorPassword && form.touched.password}
                                    width="100%"
                                    onChange={() => {
                                        if (errorPassword) {
                                            setErrorPassword('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        type="password"
                                        placeholder="Пароль"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorPassword}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="firstname" validate={validateFirstname}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorFirstname && form.touched.firstname}
                                    width="100%"
                                    onChange={() => {
                                        if (errorFirstname) {
                                            setErrorFirstname('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Имя"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorFirstname}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="lastname" validate={validateLastname}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorLastname && form.touched.lastname}
                                    width="100%"
                                    onChange={() => {
                                        if (errorLastname) {
                                            setErrorLastname('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Фамилия"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorLastname}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="middlename" validate={validateMiddlename}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorMiddlename && form.touched.middlename}
                                    width="100%"
                                    onChange={() => {
                                        if (errorMiddlename) {
                                            setErrorMiddlename('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Отчество"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorMiddlename}
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
                                <ThreeDots height={'10px'} color="white" />
                            ) : (
                                <>Зарегистрироваться</>
                            )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default Register;
