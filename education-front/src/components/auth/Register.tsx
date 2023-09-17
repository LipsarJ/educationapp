import React, {useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, FormControl, FormErrorMessage, Heading, Input} from '@chakra-ui/react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {ErrorCodes} from './ErrorCodes'

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

    const [usernames, setUsernames] = useState<string[]>([]);
    const [emails, setEmails] = useState<string[]>([]);
    const addUsername = (newUsername: string) => {
        setUsernames([...usernames, newUsername]);
    };

    const addEmail = (newEmail: string) => {
        setEmails([...emails, newEmail]);
    };

    const validateUsername = (value: string) => {
        let error;
        if (!value) {
            error = "Имя пользователя обязательно";
        } else if (value.length < 3 || value.length > 20) {
            error = "Имя пользователя должно быть от 3 до 20 символов";
        } else if (usernames.includes(value)) {
            error = "Имя пользователя занято"
        }
        return error;
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
        let error
        if (!value) {
            error = 'Почта обязательна'
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
            error = "Неверный формат электронной почты"
        } else if (emails.includes(value)) {
            error = "Данный e-mail уже используется"
        }
        return error
    }

    const handleRegister = async (values: SignupData) => {
        let error;
        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/signup', values, {
                withCredentials: true
            });
            addUsername(values.username);
            console.log(response.data);
            navigate('/login');
        } catch (error: any) {
            console.error(error);
            const errorCode = error.response.data.errorCode;
            if (errorCode == ErrorCodes.UsernameTaken) {
                addUsername(values.username);
            }
            if (errorCode == ErrorCodes.EmailTaken) {
                addEmail(values.email);
            }
        }
    };

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">Зарегистрироваться</Heading>
            <Formik
                initialValues={{
                    username: '',
                    email: '',
                    password: '',
                    middlename: '',
                    firstname: '',
                    lastname: ''
                }}
                onSubmit={handleRegister}
            >
                {() => (
                    <Form style={{minWidth: '100%'}}>
                        <Field name='username' validate={validateUsername}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.username} width="100%">
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Имя пользователя"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.username}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>

                        <Field name='email' validate={validateEmail}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.email && form.touched.email} width="100%">
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Email"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.email}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name='password' validate={validatePassword}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl isInvalid={form.errors.password && form.touched.password} width="100%">
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
                                <FormControl isInvalid={form.errors.firstname && form.touched.firstname} width="100%">
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
                                <FormControl isInvalid={form.errors.lastname && form.touched.lastname} width="100%">
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
                                <FormControl isInvalid={form.errors.middlename && form.touched.middlename} width="100%">
                                    <Input {...field}
                                           mb={2}
                                           width="100%"
                                           placeholder="Отчество"/>
                                    <FormErrorMessage mt={0} mb={2}>{form.errors.middlename}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Button mt={5} color="white" bg="facebook.400" size="lg" type='submit' mx="auto" display="block">
                            Зарегистрироваться
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
}

export default Register;
