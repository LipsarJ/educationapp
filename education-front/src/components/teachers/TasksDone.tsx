import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {instanceAxios} from '../../utils/axiosConfig';
import {useAuth} from '../../contexts/AuthContext';
import {Box, Button, Flex, Heading, Icon} from "@chakra-ui/react";
import {FiArrowLeftCircle, FiArrowRightCircle} from "react-icons/fi";
import {Oval} from "react-loader-spinner";
import TaskDoneCard from './TaskDoneCard'

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

const TasksDone = () => {
    const {courseId, lessonId, homeworkTaskId} = useParams();
    const [isLoading, setLoading] = useState(false);
    const [tasksDone, setTasksDone] = useState<TaskDone[]>([]);
    const [isDeleted, setIsDeleted] = useState(false);
    const [page, setPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(2);
    const [checked, setCheked] = useState(null);
    const [total, setTotal] = useState<number>(0);
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();
    const navigate = useNavigate();

    const fetchData = async (currentPage: number) => {
        try {
            const response = await instanceAxios.get(`/teacher/course/${courseId}/lessons/${lessonId}/homeworks/${homeworkTaskId}`, {
                params: {
                    checked: checked,
                    page: currentPage,
                    size: pageSize,
                }
            },);
            setTasksDone(response.data.homeworkDoneInfo);
            setTotal(response.data.totalCount);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchData(0);
    }, []);

    const handleNext = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchData(nextPage);
    };

    const handlePrev = () => {
        const prevPage = Math.max(page - 1, 0);
        setPage(prevPage);
        fetchData(prevPage);
    };

    const isPrevDisabled = page === 0;
    const isNextDisabled = total <= (page + 1) * pageSize;

    if (!tasksDone) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Box>
            <Heading mt={4} mb={4} size="lg" textAlign="center">
                Решения к домашнему заданию
            </Heading>
            <Flex
                gap={3}
                flexWrap="wrap"
                flexDir="row"
                justifyContent="center"
            >
                {tasksDone && tasksDone.map((taskDone: TaskDone) => (
                    <TaskDoneCard taskDone={taskDone} key={taskDone.id}/>
                ))}
            </Flex>
            <Flex justifyContent="center" alignItems="center" ml={2} mr={2} mb={2} gap={5}>
                <Button
                    onClick={() => {
                        handlePrev();
                    }}
                    isDisabled={isPrevDisabled}
                    cursor={isPrevDisabled ? "not-allowed" : "pointer"}
                >
                    <Icon
                        as={FiArrowLeftCircle}
                        color={isPrevDisabled ? "gray.300" : "black"}
                    />
                </Button>
                <Button
                    onClick={() => {
                        handleNext();
                    }}
                    isDisabled={isNextDisabled}
                    cursor={isNextDisabled ? "not-allowed" : "pointer"}
                >
                    <Icon
                        as={FiArrowRightCircle}
                        color={isNextDisabled ? "gray.300" : "black"}
                    />
                </Button>
            </Flex>
        </Box>
    );
};

export default TasksDone;
