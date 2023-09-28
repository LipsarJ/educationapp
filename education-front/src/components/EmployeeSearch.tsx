import React, {useState, useEffect} from 'react';
import {
    Box,
    Container,
    Input,
    Button,
    Text,
    VStack,
    Divider,
    HStack,
    useColorModeValue
} from '@chakra-ui/react';
import {instanceAxios} from '../utils/axiosConfig';

interface Employee {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
}

const EmployeeSearch: React.FC = () => {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [searchText, setSearchText] = useState<string>('');
    const [page, setPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(2);
    const [total, setTotal] = useState<number>(0);
    const borderColor = useColorModeValue('gray.200', 'gray.900');

    const fetchEmployees = async (currentPage: number) => {
        try {
            const response = await instanceAxios.get('/users', {
                params: {
                    filterText: searchText,
                    page: currentPage,
                    size: pageSize,
                },
            });
            setEmployees(response.data.userInfo);
            setTotal(response.data.totalCount);
        } catch (err) {
            console.error('Error fetching data', err);
        }
    };

    useEffect(() => {
        // Загрузка первой страницы при инициализации компонента
        fetchEmployees(0);
    }, []);

    const handleSearch = () => {
        setPage(0);
        fetchEmployees(0);
    };

    const handleNext = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchEmployees(nextPage);
    };

    const handlePrev = () => {
        const prevPage = Math.max(page - 1, 0);
        setPage(prevPage);
        fetchEmployees(prevPage);
    };

    const isPrevDisabled = page === 0;
    const isNextDisabled = total <= (page + 1) * pageSize;

    return (
        <Container mt={5} maxW="container.xl" centerContent>
            <Box w="full" p={4} borderWidth={1} borderRadius="md" borderColor={borderColor}>
                <VStack spacing={4}>
                    <Input
                        placeholder="Введите имя пользователя..."
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                    />
                    <Button color="white" bg="facebook.400" onClick={() => {
                        handleSearch();
                    }}>
                        Найти
                    </Button>
                </VStack>
            </Box>
            <Divider/>
            <VStack mt={2} spacing={4}>
                {employees.map((emp) => (
                    <Box key={emp.id} w="full" p={5} borderWidth={1} borderRadius="md" borderColor={borderColor}>
                        <Text>Username: {emp.username}</Text>
                        <Text>First Name: {emp.firstname}</Text>
                        <Text>Middle Name: {emp.middlename}</Text>
                        <Text>Last Name: {emp.lastname}</Text>
                    </Box>
                ))}
            </VStack>
            <HStack mt={4}>
                <Button
                    color="white"
                    bg="facebook.400"
                    onClick={() => {
                       handlePrev()
                    }}
                    isDisabled={isPrevDisabled}
                >
                    Назад
                </Button>
                <Button
                    color="white"
                    bg="facebook.400"
                    onClick={() => {
                        handleNext();
                    }}
                    isDisabled={isNextDisabled}
                >
                    Вперед
                </Button>
            </HStack>
        </Container>
    );
};

export default EmployeeSearch;
