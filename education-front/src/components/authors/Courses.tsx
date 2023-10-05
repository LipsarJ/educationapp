import React, {useEffect, useState} from "react";
import {FiPlus, FiX} from 'react-icons/fi';
import {instanceAxios} from '../../utils/axiosConfig';
import {useAuth} from '../../contexts/AuthContext';
import {
    Box,
    Button,
    Divider,
    Flex,
    Heading,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text,
} from '@chakra-ui/react';
import {NavLink, useNavigate} from 'react-router-dom';
import {Oval, ThreeDots} from 'react-loader-spinner';
import {ErrorCodes} from '../auth/ErrorCodes'
import CourseCard from './CourseCard'

interface Course {
    id: number;
    courseName: string;
    courseStatus: string;
    createDate: string;
    updateDate: string;
    countStd: number;
}

const Courses: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [isDeleted, setIsDeleted] = useState(false);
    const navigate = useNavigate();
    const {isAuthenticated, setAuthenticated, setUser, user} = useAuth();

    const handleDeleteCourse = async () => {
        setIsDeleted(!isDeleted);
    };
    const fetchData = async () => {
        if (user) {
            const allCourses: Course[] = [];

            if (user.roles.includes('AUTHOR')) {
                try {
                    const response = await instanceAxios.get<Course[]>('/author/courses');
                    allCourses.push(...response.data);
                } catch (error) {
                    console.error(error);
                }
            }

            if (user.roles.includes('TEACHER')) {
                try {
                    const response = await instanceAxios.get<Course[]>('/teacher/courses');
                    allCourses.push(...response.data);
                } catch (error) {
                    console.error(error);
                }
            }

            if (user.roles.includes('STUDENT')) {
                try {
                    const response = await instanceAxios.get<Course[]>('/student/course');
                    allCourses.push(...response.data);
                } catch (error) {
                    console.error(error);
                }
            }

            // Удаление дубликатов по id с использованием Map
            const uniqueCoursesMap = new Map<number, Course>();
            allCourses.forEach(course => {
                uniqueCoursesMap.set(course.id, course);
            });

            // Преобразование Map обратно в массив уникальных курсов
            const uniqueCourses = Array.from(uniqueCoursesMap.values());

            setCourses(uniqueCourses);
        }
    };

    useEffect(() => {

        fetchData();
    }, [isDeleted]);


    return (
        <>
            {!courses ? (
                <Flex justifyContent="center" alignItems="center" height="100vh">
                    <Oval color="#295C48" secondaryColor="#2B415B"/>
                </Flex>) : (
                <>
                    <Heading padding="20px" size="lg" textAlign="center">Ваши курсы</Heading>
                    <Flex
                        gap={5}
                        flexDir="row"
                        flexWrap="wrap"
                        justifyContent="center"
                    >
                        {user && user.roles.includes("AUTHOR") && (
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
                                color="gray"
                                boxShadow="md"
                                onClick={() => navigate('/courses/create')}
                                _hover={{
                                    bg: "#F9F9F9"
                                }}
                            >
                                <FiPlus size={32}/>
                            </Flex>
                        )}
                        {courses && courses.map((course: Course) => (
                            <CourseCard course={course} key={course.id} onDelete={handleDeleteCourse}/>
                        ))}
                    </Flex>
                </>
            )}
        </>
    );
};

export default Courses;
