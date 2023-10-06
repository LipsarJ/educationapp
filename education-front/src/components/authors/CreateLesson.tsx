import React, {useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, FormControl, FormErrorMessage, Heading, Input, FormLabel} from '@chakra-ui/react';
import {instanceAxios} from '../../utils/axiosConfig';
import {useNavigate, useParams} from 'react-router-dom';
import {ThreeDots} from 'react-loader-spinner';
import {ErrorCodes} from '../auth/ErrorCodes'

interface LessonData {
    lessonName: string;
    lessonStatus: string;
    content: string;
    num: number;
}

const CreateCourse: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');
    const [errorLessonName, setErrorLessonName] = useState('');
    const [errorLessonStatus, setErrorLessonStatus] = useState('');
    const [errorLessonNum, setErrorLessonNum] = useState('');
    const {courseId} = useParams();
    const navigate = useNavigate();

    const validateLessonName = (value: string) => {
        if (!value) {
            setErrorLessonName("Имя урока обязательно");
        } else if (value.length < 3 || value.length > 50) {
            setErrorLessonName("Имя урока должно быть от 3 до 50 символов");
        }
        return errorLessonName;
    };

    const validateLessonStatus = (value: string) => {
        if (!value) {
            setErrorLessonStatus("Статус обязателен");
        }
        return errorLessonStatus;
    };

    const validateLessonNum = (value: number) => {
        if (value === null || value === undefined) {
            setErrorLessonNum("Номер урока обязателен");
        } else if (value <= 0) {
            setErrorLessonNum('Урок нумеруется с 1')
        }
        return errorLessonNum;
    };

    const handleCreateLesson = async (values: LessonData) => {
        if (!errorLessonName && !errorLessonStatus && !errorLessonNum) {
            let error;
            setGlobalError('');
            setLoading(true);
            try {
                const response = await instanceAxios.post(`/author/lessons/${courseId}`, values);
                navigate(`/lessons/${courseId}/${response.data.id}`);
            } catch (error: any) {
                console.error(error);
                if (error && error.response && error.response.data && error.response.data.errorCode) {
                    console.log(error.response.data.errorCode);
                    if (error.response.data.errorCode == ErrorCodes.LessonNameTaken) {
                        setErrorLessonName("Имя урока уже существует.")
                    } else if (error.response.data.errorCode == ErrorCodes.StatusIsInvalid) {
                        setErrorLessonStatus("Урок может быть создан только в статусе ACTIVE")
                    } else if (error.response.data.errorCode == ErrorCodes.LessonNumIsTaken) {
                        setErrorLessonNum("Урок с таким номером уже существует")
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
                Создать урок
            </Heading>
            {globalError && <div style={{color: 'red'}}>{globalError}</div>}
            <Formik
                initialValues={{
                    lessonName: '',
                    lessonStatus: 'NOT_ACTIVE',
                    content: '',
                    num: 0
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleCreateLesson}
            >
                {() => (
                    <Form style={{minWidth: '100%'}}>
                        <Field name="lessonName" validate={validateLessonName}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorLessonName && form.touched.lessonName}
                                    width="100%"
                                    onChange={() => {
                                        if (errorLessonName) {
                                            setErrorLessonName('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <FormLabel>Название урока</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Имя урока"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorLessonName}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="lessonStatus" validate={validateLessonStatus}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    isInvalid={errorLessonStatus && form.touched.lessonStatus}
                                    width="100%"
                                    onChange={() => {
                                        if (errorLessonStatus) {
                                            setErrorLessonStatus('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <FormLabel>Статус урока</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Статус урока"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorLessonStatus}
                                    </FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="content">
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl
                                    width="100%"
                                >
                                    <FormLabel>Содержание урока</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Содержимое урока"
                                    />
                                </FormControl>
                            )}
                        </Field>
                        <Field name="num" validate = {validateLessonNum}>
                            {({field, form}: { field: any; form: any }) => (
                                <FormControl width="100%" mt={0} mb={2}
                                             isInvalid={errorLessonNum && form.touched.num}
                                             onChange={() => {
                                                 if (errorLessonNum) {
                                                     setErrorLessonNum('');
                                                 }
                                                 field.onChange(field.name);
                                             }}
                                >
                                    <FormLabel>Порядковый номер урока</FormLabel>
                                    <Input {...field} placeholder="Порядковый номер урока"/>
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorLessonNum}
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
                                <>Создать урок</>
                            )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default CreateCourse;
