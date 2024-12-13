import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {instanceAxios} from "../../utils/axiosConfig";
import {useAuth} from "../../contexts/AuthContext";
import {Box, Flex, Heading} from "@chakra-ui/react";
import TaskCard from "../authors/TaskCard";
import TaskDoneCardWithProps from "./TaskDoneCardWithProps";
import {Oval} from "react-loader-spinner";

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

interface Task {
    id: number;
    title: string;
    description: string;
    deadlineDate: string;
    createDate: string;
    updateDate: string;
}

const TasksDone = () => {
    const {courseId, lessonId, studentId} = useParams();
    const [isLoading, setLoading] = useState(false);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [tasksDone, setTasksDone] = useState<TaskDone[]>([]);
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();

    const fetchTasksAndTasksDone = async () => {
        try {
            const tasksResponse = await instanceAxios.get<Task[]>(
                `/teacher/homework-tasks/${courseId}/${lessonId}`
            );

            const tasksDoneResponse = await Promise.all(
                tasksResponse.data.map(async (task) => {
                    const response = await instanceAxios.get<TaskDone>(
                        `/teacher/course/${courseId}/lessons/${lessonId}/homeworks/${task.id}/student/${studentId}`
                    );
                    return response.data;
                })
            );

            setTasks(tasksResponse.data);
            setTasksDone(tasksDoneResponse);
        } catch (error) {
            console.error(error);
        }
    };


    useEffect(() => {
        fetchTasksAndTasksDone();
    }, []);

    if (!tasks || !tasksDone) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Flex
                ml={10}
                mr={10}
                gap={3}
                flexDir="column"
                bg="#F9F9F9" boxShadow="sm" border="1px solid #ccc" borderRadius="8"
                margin="0 auto"
                mt={4}
                w="40%"
            >
                <Heading mt={4} mb={4} size="lg" textAlign="center">
                    Решения ученика
                </Heading>
                {tasks.map((task: Task, index) => (
                    <Flex key={task.id} alignItems="center" justifyContent="center" gap={50} mb={4}>
                        <TaskCard task={task} onDelete={() => {
                        }}/>
                        {tasksDone[index] ? (
                            <TaskDoneCardWithProps homeworkTaskId={task.id} taskDone={tasksDone[index]}/>
                        ) : (
                            <Heading w="300px" size="md" textAlign="center">Нет данных</Heading>
                        )}
                    </Flex>
                ))}
            </Flex>
        </Box>
    );
};

export default TasksDone;
