import React, { useState } from 'react';
import { Field, Form, Formik } from 'formik';
import {
    Button,
    Container,
    FormControl,
    FormErrorMessage,
    Heading,
    Input,
    FormLabel
} from '@chakra-ui/react';
import { instanceAxios } from '../../utils/axiosConfig';
import { useNavigate, useParams } from 'react-router-dom';
import { ThreeDots } from 'react-loader-spinner';

interface TaskDoneData {
    studentDescription: string;
}

const CreateTaskDone: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');
    const [errorDescription, setErrorDescription] = useState('');
    const { courseId, lessonId, homeworkTaskId } = useParams();
    const navigate = useNavigate();

    const validateDescription = (value: string) => {
        if (!value) {
            setErrorDescription('Описание задания обязательно');
        } else if (value.length < 10 || value.length > 1000) {
            setErrorDescription('Описание задания должно быть от 10 до 1000 символов');
        }
        return errorDescription;
    };

    const handleCreateTaskDone = async (values: TaskDoneData) => {
        if (!errorDescription) {
            setGlobalError('');
            setLoading(true);
            try {
                // Send a POST request to create a task done
                const response = await instanceAxios.post(`/student/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/my-homework`, {
                    studentDescription: values.studentDescription,
                });
                navigate(`/tasks/${courseId}/${lessonId}/${homeworkTaskId}`);
            } catch (error: any) {
                console.error(error);
                setGlobalError('Что-то пошло не так, попробуйте позже.');
            }
        }
        setLoading(false);
    };

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">
                Добавить решение
            </Heading>
            {globalError && <div style={{ color: 'red' }}>{globalError}</div>}
            <Formik
                initialValues={{
                    studentDescription: '',
                }}
                validateOnChange={false}
                validateOnBlur={false}
                onSubmit={handleCreateTaskDone}
            >
                {() => (
                    <Form style={{ minWidth: '100%' }}>
                        <Field name="studentDescription" validate={validateDescription}>
                            {({ field, form }: { field: any; form: any }) => (
                                <FormControl
                                    width="100%"
                                    mt={0}
                                    mb={2}
                                    isInvalid={errorDescription && form.touched.studentDescription}
                                    onChange={() => {
                                        if (errorDescription) {
                                            setErrorDescription('');
                                        }
                                        field.onChange(field.name);
                                    }}
                                >
                                    <FormLabel>Решение</FormLabel>
                                    <Input
                                        {...field}
                                        mb={2}
                                        width="100%"
                                        placeholder="Описание задания"
                                    />
                                    <FormErrorMessage mt={0} mb={2}>
                                        {errorDescription}
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
                                <>Создать решение</>
                            )}
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default CreateTaskDone;
