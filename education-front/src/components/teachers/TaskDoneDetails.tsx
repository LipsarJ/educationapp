import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {instanceAxios} from "../../utils/axiosConfig";
import {
    Box,
    Button,
    Flex,
    FormControl,
    Heading,
    Input,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text,
} from "@chakra-ui/react";
import {FiCheckSquare, FiEdit2} from "react-icons/fi";
import {Oval} from "react-loader-spinner";
import {useAuth} from "../../contexts/AuthContext";
import {Field, Form, Formik} from "formik";

interface TeacherChecked {
    grade: number;
    teacherFeedback: string;
}

interface TaskDone {
    id: number;
    submissionDate: string;
    grade: number;
    studentDescription: string;
    teacherFeedback: string;
    teacherInfoDto: UserData;
    studentInfoDto: UserData;
}

interface UserData {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
}

const TaskDoneDetails = () => {
    const {courseId, lessonId, homeworkTaskId, homeworkDoneId} = useParams();
    const [taskDone, setTaskDone] = useState<TaskDone | null>(null);
    const [isLoading, setLoading] = useState(false);
    const [globalError, setGlobalError] = useState("");
    const [isRatingModalOpen, setIsRatingModalOpen] = useState(false);
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const [gradeError, setGradeError] = useState("");
    const [teacherFeedbackError, setTeacherFeedbackError] = useState("");
    const navigate = useNavigate();

    const fetchTaskDone = async () => {
        if (user) {
            if (user.roles.includes("TEACHER")) {
                try {
                    const response = await instanceAxios.get<TaskDone>(
                        `/teacher/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/${homeworkDoneId}`
                    );
                    setTaskDone(response.data);
                } catch (error) {
                    console.error(error);
                }
            } else if (user.roles.includes("STUDENT")) {
                try {
                    const response = await instanceAxios.get<TaskDone>(
                        `/student/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/my-homework`
                    );
                    setTaskDone(response.data);
                } catch (error) {
                    console.error(error);
                }
            }
        }
    };

    useEffect(() => {
        fetchTaskDone();
    }, [courseId, lessonId, homeworkTaskId, homeworkDoneId]);

    const validateGrade = (value: string) => {
        if (!value || isNaN(parseFloat(value)) || parseFloat(value) < 2 || parseFloat(value) > 5) {
            setGradeError("Оценка должна быть числом от 2 до 5");
        } else {
            setGradeError("");
        }
    };

    const validateTeacherFeedback = (value: string) => {
        if (!value || value.length < 10 || value.length > 1000) {
            setTeacherFeedbackError("Ответ учителя должен содержать от 10 до 1000 символов");
        } else {
            setTeacherFeedbackError("");
        }
    };

    const openRatingModal = () => {
        setIsRatingModalOpen(true);
    };

    const closeRatingModal = () => {
        setIsRatingModalOpen(false);
    };

    const handleSetRating = async (values: TeacherChecked) => {
        if (!gradeError && !teacherFeedbackError) {
            try {
                const response = await instanceAxios.put(
                    `/teacher/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}/${homeworkDoneId}`,
                    values
                );
                fetchTaskDone();
            } catch (error) {
                console.error(error);
            }
        }
        closeRatingModal();
    };

    if (!taskDone) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Heading size="lg" textAlign="center" mb={4} padding="16px">
                Решение
            </Heading>
            <Flex
                alignItems="center"
                justifyContent="center"
                mt={3}
                borderRadius={8}
                borderWidth="1px"
                borderColor="grey.500"
                w="500px"
                mx="auto"
                bg="#F9F9F9" boxShadow="sm" border="1px solid #ccc"
            >
                <Flex alignItems="center" textAlign="center" flexDir="column" mx="auto" mb={1} mt={3}>
                    <Heading ml={2} mr={2} mb={3}>
                        <Text>{!taskDone.studentInfoDto ? "-" : `${taskDone.studentInfoDto.firstname} ${taskDone.studentInfoDto.middlename} ${taskDone.studentInfoDto.lastname}`}</Text>
                    </Heading>
                    <Heading size="lg" textAlign="center" mb={1}>
                        Описание решения:
                    </Heading>
                    <Text fontSize="lg"> {taskDone.studentDescription}</Text>
                    <Heading size="lg" textAlign="center" mb={1}>
                        Ответ учителя:
                    </Heading>
                    <Text fontSize="lg"> {taskDone.teacherFeedback}</Text>
                    <Heading size="lg" textAlign="center" mb={1}>
                        Оценка:
                    </Heading>
                    <Text fontSize="lg"> {taskDone.grade}</Text>
                    <Heading size="lg" textAlign="center" mb={1}>
                        Дата загрузки:
                    </Heading>
                    <Text fontSize="lg"> {taskDone.submissionDate}</Text>
                    <Heading size="lg" textAlign="center" mb={1} mt={4}>
                        Проверил:
                    </Heading>
                    <Text
                        mb={4}>{!taskDone.teacherInfoDto ? "-" : `${taskDone.teacherInfoDto.firstname} ${taskDone.teacherInfoDto.middlename} ${taskDone.teacherInfoDto.lastname}`}</Text>
                    {user && user.roles.includes("TEACHER") && (
                        <Button mt={4} mb={3} colorScheme="blue" onClick={openRatingModal} width="70%"
                                leftIcon={<FiEdit2/>}>
                            {taskDone.grade !== null ? "Изменить оценку" : "Поставить оценку"}
                        </Button>
                    )}
                </Flex>
            </Flex>
            {user && user.roles.includes("STUDENT") && (
                <Flex justifyContent="center" mt={3}>
                    <Button leftIcon={<FiEdit2/>} w="500px" colorScheme="blue" onClick={() => {
                        navigate(`/task-done/edit-solution/${courseId}/${lessonId}/${homeworkTaskId}/${homeworkDoneId}`);
                    }}>
                        Изменить решение
                    </Button>
                </Flex>
            )}

            <Modal isOpen={isRatingModalOpen} onClose={closeRatingModal}>
                <ModalOverlay/>
                <ModalContent>
                    <ModalHeader>Поставить оценку</ModalHeader>
                    <Formik
                        initialValues={{
                            grade: taskDone.grade !== null ? taskDone.grade.toString() : "",
                            teacherFeedback: taskDone.teacherFeedback,
                        }}
                        validateOnChange={false}
                        validateOnBlur={false}
                        onSubmit={(values, {resetForm}) => {
                            const teacherChecked: TeacherChecked = {
                                grade: parseFloat(values.grade),
                                teacherFeedback: values.teacherFeedback,
                            };
                            handleSetRating(teacherChecked);
                            resetForm();
                        }}
                    >
                        <Form>
                            <ModalBody>
                                <Field name="grade">
                                    {({field}: { field: any }) => (
                                        <FormControl>
                                            <Input
                                                {...field}
                                                type="number"
                                                placeholder="Оценка"
                                                value={field.value || ""}
                                                onBlur={(e) => validateGrade(e.target.value)}
                                            />
                                            <Text color="red.500">{gradeError}</Text>
                                        </FormControl>
                                    )}
                                </Field>
                                <Field name="teacherFeedback">
                                    {({field}: { field: any }) => (
                                        <FormControl>
                                            <Input
                                                {...field}
                                                type="string"
                                                placeholder="Ответ учителя"
                                                value={field.value || ""}
                                                onBlur={(e) => validateTeacherFeedback(e.target.value)}
                                            />
                                            <Text color="red.500">{teacherFeedbackError}</Text>
                                        </FormControl>
                                    )}
                                </Field>
                            </ModalBody>
                            <ModalFooter>
                                <Button colorScheme="green" type="submit" mr={3} leftIcon={<FiCheckSquare/>}>
                                    Сохранить
                                </Button>
                                <Button onClick={closeRatingModal}>Отмена</Button>
                            </ModalFooter>
                        </Form>
                    </Formik>
                </ModalContent>
            </Modal>
        </Box>
    );
};

export default TaskDoneDetails;
