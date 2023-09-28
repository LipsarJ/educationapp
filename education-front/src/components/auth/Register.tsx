import React, {useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, FormControl, FormErrorMessage, Heading, Input} from '@chakra-ui/react';
import {useNavigate} from 'react-router-dom';
import {ErrorCodes} from './ErrorCodes'
import axios from 'axios';
import {ThreeDots} from 'react-loader-spinner';

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

    const [errorUsername, setErrorUsername] = useState("");
    const [errorEmail, setErrorEmail] = useState("");
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState("");
    const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));

    const validateUsername = (value: string) => {
        if (!value) {
            setErrorUsername("Имя пользователя обязательно");
        } else if (value.length < 3 || value.length > 20) {
            setErrorUsername("Имя пользователя должно быть от 3 до 20 символов");
        }
        return errorUsername;
    };

    const validatePassword = (value: string) => {
        let error;
        if (!value) {
            error = "Пароль обязателен";
        } else if (value.length < 6 || value.length > 40) {
            error = "Пароль должен быть от 6 до 40 символов";
        }
        return error;
    };

    const validateMiddlename = (value: string) => {
        let error;
        if (!value) {
            error = "Отчество обязательно";
        }
        return error;
    };

    const validateFirstname = (value: string) => {
        let error;
        if (!value) {
            error = "Имя обязательно";
        }
        return error;
    };

    const validateLastname = (value: string) => {
        let error;
        if (!value) {
            error = "Фамилия обязательна";
        }
        return error;
    };

    const validateEmail = (value: string) => {
        if (!value) {
            setErrorEmail('Почта обязательна');
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
            setErrorEmail("Неверный формат электронной почты")
        }
        return errorEmail
    }

    const handleRegister = async (values: SignupData) => {
        setGlobalError("");
        let error;
        setLoading(true);
        try {
            const response = await axios.post(process.env.REACT_APP_API_URL + '/auth/signup', values, {
                withCredentials: true
            });
            console.log(response.data);
            navigate('/login');
        } catch (error: any) {
            console.error(error);
            if (error && error.response && error.response.data && error.response.data.errorCode) {
                const errorCode = error.response.data.errorCode;
                if (errorCode == ErrorCodes.UsernameTaken) {
                    setErrorUsername("Имя пользователя занято");
                }
                if (errorCode == ErrorCodes.EmailTaken) {
                    setErrorEmail("E-mail уже используется");
                }
            }
            else {
                setGlobalError("Что-то пошло не так, попробуйте позже.");
            }
        }
        setLoading(false);
    };

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">Зарегистрироваться</Heading>
            {globalError && <div style={{ color: 'red' }}>{globalError}</div>}
            <Formik
                initialValues={{
                    username: '',
                    email: '',
                    password: '',
                    middlename: '',
                    firstname: '',
                    lastname: ''
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleRegister}
            >
                {() => (
                    <Form style={{minWidth: '100%'}}>
                        <Field name='username' validate={validateUsername}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={errorUsername && form.touched.username && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (errorUsername) {
                                                     setErrorUsername('');
                                                 }
                                                 field.onChange(field.name);
                                             }}>
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Имя пользователя"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>{errorUsername}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>

                        <Field name='email' validate={validateEmail}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={errorEmail && form.touched.email && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (errorEmail) {
                                                     setErrorEmail('');
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Email"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>{errorEmail}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='password' validate={validatePassword}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.password && form.touched.password && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (form.errors.password) {
                                                     form.setFieldError('password', ''); // Удаление ошибки
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           type="password" placeholder="Пароль"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.password}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='firstname' validate={validateFirstname}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.firstname && form.touched.firstname && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (form.errors.firstname) {
                                                     form.setFieldError('firstname', ''); // Удаление ошибки
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Имя"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.firstname}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='lastname' validate={validateLastname}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.lastname && form.touched.lastname && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (form.errors.lastname) {
                                                     form.setFieldError('lastname', ''); // Удаление ошибки
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Фамилия"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.lastname}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='middlename' validate={validateMiddlename}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.middlename && form.touched.middlename && globalError == ""} width="100%"
                                             onChange={() => {
                                                 if (form.errors.middlename) {
                                                     form.setFieldError('middlename', ''); // Удаление ошибки
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Отчество"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.middlename}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Button mt={5} color="white" bg="facebook.400" size="lg" type='submit' mx="auto"
                                display="block" >
                            {isLoading ? (<ThreeDots height={"10px"} color="white"/>)
                                :
                                (<>
                                        Зарегистрироваться
                                    </>
                                )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
}

export default Register;
