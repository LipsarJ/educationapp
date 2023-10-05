import React, {useEffect, useState} from 'react';
import {Field, Form, Formik} from 'formik';
import {Button, Container, Flex, FormControl, FormErrorMessage, Heading, Input} from '@chakra-ui/react';
import {instanceAxios} from '../../utils/axiosConfig';
import {useNavigate, useParams} from 'react-router-dom';
import {Oval, ThreeDots} from 'react-loader-spinner';
import {FiCheckSquare} from 'react-icons/fi'

const EditHomeworkDone: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState('');
    const [taskDone, setTaskDone] = useState<string>('');
    const [errorDescription, setErrorDescription] = useState('');
    const {courseId, lessonId, homeworkTaskId, homeworkDoneId} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTaskDoneData = async () => {
            setLoading(true);
            try {
                const response = await instanceAxios.get(`/student/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/my-homework`);
                setTaskDone(response.data.studentDescription);
                console.log(taskDone);
            } catch (error) {
                console.error(error);
            }
            setLoading(false);
        };

        fetchTaskDoneData();
    }, [courseId, lessonId, homeworkTaskId, homeworkDoneId]);

    const validateDescription = (value: string) => {
        if (!value) {
            setErrorDescription('Описание задания обязательно');
        } else if (value.length < 10 || value.length > 1000) {
            setErrorDescription('Описание задания должно быть от 10 до 1000 символов');
        }
        return errorDescription;
    };

    const handleEditTaskDone = async (values: { studentDescription: string }) => {
        if (!errorDescription) {
            setGlobalError('');
            setLoading(true);
            try {
                await instanceAxios.put(`/student/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/my-homework`, {
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

    if (!taskDone) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Container mt="5" centerContent>
            <Heading mb={4} size="lg">
                Редактировать решение
            </Heading>
            {globalError && <div style={{color: 'red'}}>{globalError}</div>}
            {!isLoading && (
                <Formik
                    initialValues={{studentDescription: taskDone}}
                    validateOnChange={false}
                    validateOnBlur={false}
                    onSubmit={(values) => handleEditTaskDone(values)}
                >
                    {() => (
                        <Form style={{minWidth: '100%', minHeight:"30%"}}>
                            <Field name="studentDescription" validate={validateDescription}>
                                {({field, form}: { field: any; form: any }) => (
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
                                colorScheme = "green"
                                size="lg"
                                type="submit"
                                mx="auto"
                                display="block"
                                leftIcon={<FiCheckSquare/>}
                            >
                                {isLoading ? (
                                    <ThreeDots height={'10px'} color="white"/>
                                ) : (
                                    <>Сохранить изменения</>
                                )}
                            </Button>
                        </Form>
                    )}
                </Formik>
            )}
        </Container>
    );
};

export default EditHomeworkDone;
