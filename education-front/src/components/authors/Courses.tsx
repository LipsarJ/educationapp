import React, {useEffect, useState} from "react";
import {FiPlus} from 'react-icons/fi';
import axios from 'axios';
import {Box, Divider, Flex, Heading, Text, Link} from '@chakra-ui/react';
import {useNavigate, NavLink} from 'react-router-dom';

interface Course {
    id: number;
    courseName: string;
    courseStatus: string;
    createDate: string;
    updateDate: string;
    countStd: number;
}

const CourseCard = ({course}: { course: Course }) => {
    const [isExpanded, setIsExpanded] = useState(false);
    const toggleExpand = () => {
        setIsExpanded(!isExpanded);
    };

    return (
        <Flex
            padding="16px"
            marginBottom="16px"
            borderRadius="8"
            flexBasis="400px"
            h="300px"
            flexDir="column"
            gap={3}
            boxShadow="sm"
            border="1px solid #ccc"
            cursor = "pointer"
            _hover={{
                bg: "#F9F9F9"
            }}
        >
            <Heading mt={1} size="md" textAlign="center">{course.courseName}</Heading>
            <Divider w="100%"></Divider>
            <Box>
                <Heading size="md" textAlign="center">Статус: </Heading>
                <Text textAlign="center">
                    {course.courseStatus === 'TEMPLATE'
                        ? 'Подготавливается'
                        : course.courseStatus === 'ONGOING'
                            ? 'Идёт'
                            : 'Закончен'}
                </Text>
                <Heading size="md" textAlign="center">Дата создания:</Heading>
                <Text textAlign="center">
                    {course.createDate}
                </Text>
                <Heading size="md" textAlign="center">Дата обновления:</Heading>
                <Text textAlign="center">
                    {course.updateDate}
                </Text>
                <Heading size="md" textAlign="center">Количество учеников:</Heading>
                <Text textAlign="center">
                    {course.countStd}
                </Text>
            </Box>
        </Flex>
    );
};

const Courses: React.FC = () => {
    const [courses, setCourses] = useState([]);
    const [isLoading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(
                    process.env.REACT_APP_API_URL + '/author/courses',
                    {withCredentials: true}
                );
                setCourses(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, []);

    return (
        <>
            <Heading padding="20px" size="lg" textAlign="center">Ваши курсы</Heading>
            <Flex
                gap={5}
                flexDir="row"
                flexWrap="wrap"
                justifyContent="center"
            >
                <Flex
                    key="create-course"
                    padding="16px"
                    borderRadius="8"
                    flexBasis="400px"
                    h="300px"
                    gap={3}
                    alignItems="center"
                    justifyContent="center"
                    cursor="pointer"
                    color = "gray"
                    boxShadow="md"
                    onClick={() => navigate('/courses/create')} // Переход на страницу создания курса по клику
                    _hover={{
                        bg: "#F9F9F9"
                    }}
                >
                    <FiPlus size={32}/> {/* Значок "+" */}
                </Flex>
                {courses.map((course: Course) => (
                    <CourseCard course={course} key={course.id}/>
                ))}
            </Flex>

        </>
    );
};

export default Courses;
