import React, {useEffect, useState} from 'react';
import {
    Button,
    Container,
    Flex,
    Icon,
    Table,
    TableCaption,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    Tr,
    Input
} from '@chakra-ui/react';
import {instanceAxios} from '../utils/axiosConfig';
import {useNavigate, useParams} from 'react-router-dom';
import {FiArrowLeftCircle, FiArrowRightCircle} from 'react-icons/fi';
import {Oval} from "react-loader-spinner";

interface Lesson {
    id: number;
    lessonName: string;
}

interface User {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
}

interface RequestDto {
    studentId: number;
}

interface JournalDto {
    lessonId: number;
    studentId: number;
    percentage: number;
}

const StudentsControl = () => {
    const [students, setStudents] = useState<User[]>([]);
    const {courseId} = useParams();
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [homeworkPercentages, setHomeworkPercentages] = useState<JournalDto[]>([]);
    const [page, setPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(10);
    const [searchText, setSearchText] = useState<string>('');
    const [total, setTotal] = useState<number>(0);
    const navigate = useNavigate();

    const fetchStudents = async (page: number) => {
        try {
            const response = await instanceAxios.get(`/teacher/course/${courseId}/students`, {
                params: {
                    page: page,
                    size: pageSize,
                },
            });
            setStudents(response.data.userInfo);
        } catch (error) {
            console.error('Ошибка при получении списка учеников:', error);
        }
    };

    const fetchLessons = async () => {
        try {
            const response = await instanceAxios.get(`/teacher/lessons/${courseId}`);
            setLessons(response.data);
        } catch (error) {
            console.error('Ошибка при получении списка уроков:', error);
        }
    };

    useEffect(() => {
        fetchStudents(0);
        fetchLessons();
    }, [courseId, page, pageSize]);

    useEffect(() => {
        const fetchHomeworkPercentages = async () => {
            if (lessons) {
                try {
                    const response = await instanceAxios.get(`/teacher/course/${courseId}/hw-done-percentage`);
                    setHomeworkPercentages(response.data);
                } catch (error) {
                    console.error('Ошибка при получении процентов выполненных домашних работ:', error);
                }
            }
        };

        fetchHomeworkPercentages();
    }, [courseId, lessons, page, pageSize]);

    const handleNext = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchStudents(nextPage);
    };

    const handlePrev = () => {
        const prevPage = Math.max(page - 1, 0);
        setPage(prevPage);
        fetchStudents(prevPage);
    };

    const filterUsers = (text: string) => {
        if (!text) fetchStudents(0);

        const filteredStudents = students.filter((student) => {
            const fullName = `${student.firstname} ${student.middlename} ${student.lastname}`;
            return (
                (student.username.toLowerCase().includes(text.toLowerCase()) || fullName.toLowerCase().includes(text.toLowerCase()))
            );
        });

        setStudents(filteredStudents);
        setTotal(filteredStudents.length)
    };

    const isPrevDisabled = page === 0;
    const isNextDisabled = total <= (page + 1) * pageSize;

    if (!lessons || !students) return (
        <Flex justifyContent="center" alignItems="center" height="30vh">
            <Oval color="#295C48" secondaryColor="#2B415B"/>
        </Flex>)

    return (
        <Flex alignItems="center" justifyContent="center" flexDir="column">
            <Flex minW="container.lg" mt={3} mb={3}>
                <Input
                    type="text"
                    placeholder="Поиск студента..."
                    value={searchText}
                    onChange={(e) => {
                        const text = e.target.value;
                        setSearchText(text);
                        filterUsers(text);
                    }}
                />
            </Flex>
            <Container maxW="container.lg" p={4} style={{overflowX: 'auto'}}>
                <Table variant="striped" colorScheme="gray">
                    <TableCaption>Ученики и процент выполненных домашних работ</TableCaption>
                    <Thead>
                        <Tr>
                            <Th>Ученик</Th>
                            {lessons &&
                                lessons.map((lesson) => (
                                    <Th textAlign="center" key={lesson.id}>{lesson.lessonName}</Th>
                                ))}
                        </Tr>
                    </Thead>
                    <Tbody>
                        {students &&
                            students.map((student) => (
                                <Tr key={student.id}>
                                    <Td>{student.firstname} {student.middlename} {student.lastname}</Td>
                                    {lessons &&
                                        lessons.map((lesson) => {
                                            // Найдем соответствующий элемент в journalDto
                                            const journalEntry = homeworkPercentages.find(entry => entry.lessonId === lesson.id && entry.studentId === student.id);

                                            // Если элемент не найден, установим процент в 0
                                            const percentage = journalEntry ? journalEntry.percentage : 0;

                                            return (
                                                <Td key={lesson.id}>
                                                    <Flex justifyContent="center">
                                                        <Button
                                                            w="60px"
                                                            backgroundColor="blue.500"
                                                            color="white"
                                                            borderRadius="5px"
                                                            padding="5px 10px"
                                                            cursor="pointer"
                                                            onClick={() => {
                                                                navigate(`/courses/${courseId}/${lesson.id}/${student.id}/tasks`);
                                                            }}
                                                        >
                                                            <Text textAlign="center">{percentage}%</Text>
                                                        </Button>
                                                    </Flex>
                                                </Td>
                                            );
                                        })}
                                </Tr>
                            ))}
                    </Tbody>
                </Table>
            </Container>
            <Flex justifyContent="center" alignItems="center" mt={4} mb={2} gap={5}>
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
        </Flex>
    );
};

export default StudentsControl;
