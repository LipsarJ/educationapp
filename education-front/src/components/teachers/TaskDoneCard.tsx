import React, {useState} from "react";
import {NavLink, useParams} from "react-router-dom";
import {Flex, Heading, Text,} from "@chakra-ui/react";
import {useAuth} from '../../contexts/AuthContext';

const TaskDoneCard: React.FC<{ taskDone: any }> = ({taskDone}) => {
    const {courseId, lessonId, homeworkTaskId} = useParams();
    const [isDeleting, setIsDeleting] = useState(false);
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();

    const toggleDeleteConfirmation = () => {
        setIsDeleting(!isDeleting);
        if (error) setError('');
    };

    return (
        <Flex
            padding="16px"
            position="relative"
            marginBottom="16px"
            borderRadius="8"
            flexBasis="300px"
            h="300px"
            flexDir="column"
            alignItems="center"
            gap={3}
            boxShadow="sm"
            w="10%"
            border="1px solid #ccc"
            background="#F9F9F9"
            _hover={{
                bg: "gray.200"
            }}
            as={NavLink} to={`/tasks-done/${courseId}/${lessonId}/${homeworkTaskId}/${taskDone.id}`}
        >
            <Heading mt={1} size="sm" textAlign="center" cursor="pointer">
                <Text>{!taskDone.studentInfoDto ? "-" : `${taskDone.studentInfoDto.firstname} ${taskDone.studentInfoDto.middlename} ${taskDone.studentInfoDto.lastname}`}</Text>
            </Heading>
            <Heading mt={1} size="sm" textAlign="center" cursor="pointer">
                <Text>Дата загрузки: </Text>
            </Heading>
            <Text> {taskDone.submissionDate}</Text>
            <Heading mt={1} size="sm" textAlign="center" cursor="pointer">
                <Text>Оценка: </Text>
            </Heading>
            <Text>{taskDone.grade ? taskDone.grade : '-'}</Text>
            <Heading mt={1} size="sm" textAlign="center" cursor="pointer">
                <Text>Проверил: </Text>
            </Heading>
            <Text>{!taskDone.teacherInfoDto ? "-" : `${taskDone.teacherInfoDto.firstname} ${taskDone.teacherInfoDto.middlename} ${taskDone.teacherInfoDto.lastname}`}</Text>
        </Flex>
    );
};

export default TaskDoneCard;