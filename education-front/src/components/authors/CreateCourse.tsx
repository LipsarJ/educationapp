import React, {useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, FormControl, FormErrorMessage, FormLabel, Heading, Input} from '@chakra-ui/react';
import {instanceAxios} from '../../utils/axiosConfig';
import {useNavigate} from 'react-router-dom';
import {ThreeDots} from 'react-loader-spinner';
import {ErrorCodes} from '../auth/ErrorCodes'

interface CourseData {
    courseName: string;
    courseStatus: string;
}

const CreateCourse: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');
    const [errorCourseName, setErrorCourseName] = useState('');
    const [errorCourseStatus, setErrorCourseStatus] = useState('');
    const navigate = useNavigate();

    const validateCourseName = (value: string) => {
        if (!value) {
            setErrorCourseName("Имя курса обязательно");
        } else if (value.length < 3 || value.length > 50) {
            setErrorCourseName("Имя курса должно быть от 3 до 50 символов");
        }
        return errorCourseName;
    };

    const validateCourseStatus = (value: string) => {
        if (!value) {
            setErrorCourseStatus("Статус обязателен");
        }
        return errorCourseStatus;
    };

    const handleCreateCourse = async (values: CourseData) => {
        if (!errorCourseStatus && !errorCourseName) {
            let error;
            setGlobalError('');
            setLoading(true);
            try {
                const response = await instanceAxios.post('/author/courses', values);
                navigate("/courses");
            } catch (error: any) {
                console.error(error);
                if (error && error.response && error.response.data && error.response.data.errorCode) {
                    if (error.response.data.errorCode == ErrorCodes.CourseNameTaken) {
                        setErrorCourseName("Имя курса уже существует.")
                    } else if (error.response.data.errorCode == ErrorCodes.StatusIsInvalid) {
                        setErrorCourseStatus("Урок может быть создан только в статусе TEMPLATE")
                    }
                } else {
                    setGlobalError('Что-то пошло не так, попробуйте позже.');
                }
            }
        }
        setLoading(false);
    };

    return (
        <Container mt="5" bg="#F9F9F9" centerContent boxShadow="sm" border="1px solid #ccc" borderRadius="8">
            <Heading mt={4} mb={4} size="lg">
                Создание курса
            </Heading>
            {globalError && <div style={{color: 'red'}}>{globalError}</div>}
            <Formik
                initialValues={{
                    courseName: '',
                    courseStatus: 'TEMPLATE',
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleCreateCourse}
            >
                {() => (
                    <Form style={{minWidth: '100%'}}>
                        <Field name="courseName" validate={validateCourseName}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorCourseName && form.touched.courseName}
                                    width="100%"
                                    onChange={() => {
                                        if (errorCourseName) {
                                            setErrorCourseName('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <FormLabel>Название курса</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Имя курса"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorCourseName}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="courseStatus" validate={validateCourseStatus}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorCourseStatus && form.touched.courseStatus}
                                    width="100%"
                                    onChange={() => {
                                        if (errorCourseStatus) {
                                            setErrorCourseStatus('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <FormLabel>Статус курса</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Статус курса"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorCourseStatus}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Button
                            mt={5}
                            mb={5}
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
                                <>Создать курс</>
                            )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default CreateCourse;
