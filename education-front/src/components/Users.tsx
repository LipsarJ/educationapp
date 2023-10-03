import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Button, Divider, Flex, Heading, Icon, Input, List, ListItem} from "@chakra-ui/react";
import {instanceAxios} from '../utils/axiosConfig';
import {FiArrowLeft, FiArrowLeftCircle, FiArrowRight, FiArrowRightCircle} from 'react-icons/fi';
import {Oval} from "react-loader-spinner";

interface UserData {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
}

const UserList = () => {
    const {roleName, id} = useParams();
    const [usersLeft, setUsersLeft] = useState<UserData[]>([]);
    const [usersRight, setUsersRight] = useState<UserData[]>([]);
    const [page, setPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(10);
    const [searchText, setSearchText] = useState<string>('');
    const [selectedLeftUsers, setSelectedLeftUsers] = useState<number[]>([]);
    const [selectedRightUsers, setSelectedRightUsers] = useState<number[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [searchResultsLeft, setSearchResultsLeft] = useState<UserData[]>([]);
    const [searchResultsRight, setSearchResultsRight] = useState<UserData[]>([]);
    const [total, setTotal] = useState<number>(0);

    const loadUsers = async () => {
        try {
            setIsLoading(true);
            let url = "";
            if (roleName === "AUTHOR") {
                url = `${process.env.REACT_APP_API_URL}/author/courses/${id}/authors`;
            } else if (roleName === "TEACHER") {
                url = `${process.env.REACT_APP_API_URL}/author/courses/${id}/teachers`;
            }

            const response = await instanceAxios.get(url, {
                withCredentials: true
            });
            setIsLoading(false);
            setUsersRight(response.data);
        } catch (error) {
            console.error("Ошибка при загрузке пользователей:", error);
        }
    };

    const filterUsers = (text: string) => {
        setSearchText(text);

        const filteredLeftUsers = usersLeft.filter((user) => {
            const fullName = `${user.firstname} ${user.middlename} ${user.lastname}`;
            return (
                (user.username.toLowerCase().includes(text.toLowerCase()) || fullName.toLowerCase().includes(text.toLowerCase()))
            );
        });

        // Фильтрация пользователей на правой стороне
        const filteredRightUsers = usersRight.filter((user) => {
            const fullName = `${user.firstname} ${user.middlename} ${user.lastname}`;
            return (
                (user.username.toLowerCase().includes(text.toLowerCase()) || fullName.toLowerCase().includes(text.toLowerCase()))
            );
        });

        setSearchResultsLeft(filteredLeftUsers);
        setSearchResultsRight(filteredRightUsers);
    };

    const fetchUsersLeft = async (page: number) => {
        try {
            setIsLoading(true);
            const response = await instanceAxios.get(`${process.env.REACT_APP_API_URL}/users/${roleName}`, {
                params: {
                    filterText: searchText,
                    page: page,
                    size: pageSize,
                },
            });
            const filteredUsers = response.data.userInfo.filter((user: UserData) => !usersRight.some(u => u.id === user.id));
            setUsersLeft(filteredUsers);
            setTotal(filteredUsers.length);
            setIsLoading(false);
        } catch (error) {
            console.error("Ошибка при загрузке пользователей:", error);
        }
    };

    useEffect(() => {
        fetchUsersLeft(0);
    }, [roleName, searchText, page, pageSize, usersRight]);

    useEffect(() => {
        loadUsers();
    }, [roleName, id]);

    const handleLeftUserClick = (userId: number) => {
        if (selectedLeftUsers.includes(userId)) {
            setSelectedLeftUsers((prevSelected) =>
                prevSelected.filter((id) => id !== userId)
            );
        } else {
            setSelectedLeftUsers((prevSelected) => [...prevSelected, userId]);
        }
    };

    const handleRightUserClick = (userId: number) => {
        if (selectedRightUsers.includes(userId)) {
            setSelectedRightUsers((prevSelected) =>
                prevSelected.filter((id) => id !== userId)
            );
        } else {
            setSelectedRightUsers((prevSelected) => [...prevSelected, userId]);
        }
    };

    const moveUsersToLeft = () => {
        const usersToMove = usersRight.filter((user) => selectedRightUsers.includes(user.id));
        setUsersLeft((prevUsers) => [...prevUsers, ...usersToMove]);
        setUsersRight((prevUsers) => prevUsers.filter((user) => !selectedRightUsers.includes(user.id)));
        setSelectedRightUsers([]);
    };

    const moveUsersToRight = () => {
        const usersToMove = usersLeft.filter((user) => selectedLeftUsers.includes(user.id));
        setUsersRight((prevUsers) => [...prevUsers, ...usersToMove]);
        setUsersLeft((prevUsers) => prevUsers.filter((user) => !selectedLeftUsers.includes(user.id)));
        setSelectedLeftUsers([]);
    };

    const handleAddAuthors = async (userIds: number[]) => {
        try {
            // Отправить запрос на добавление авторов
            const response = await instanceAxios.put(`${process.env.REACT_APP_API_URL}/author/courses/${id}/add-authors`, {
                ids: userIds,
            }, {withCredentials: true});
        } catch (error) {
            console.error("Ошибка при добавлении авторов:", error);
        }
    };

    const handleRemoveAuthors = async (userIds: number[]) => {
        try {
            // Отправить запрос на удаление авторов
            const response = await instanceAxios.put(`${process.env.REACT_APP_API_URL}/author/courses/${id}/remove-authors`, {
                ids: userIds,
            }, {withCredentials: true});

        } catch (error) {
            console.error("Ошибка при удалении авторов:", error);
        }
    };

    const handleAddTeachers = async (userIds: number[]) => {
        try {
            // Отправить запрос на добавление учителей
            const response = await instanceAxios.put(`${process.env.REACT_APP_API_URL}/author/courses/${id}/add-teachers`, {
                ids: userIds,
            }, {withCredentials: true});
        } catch (error) {
            console.error("Ошибка при добавлении учителей:", error);
        }
    };

    const handleRemoveTeachers = async (userIds: number[]) => {
        try {
            const response = await instanceAxios.put(`${process.env.REACT_APP_API_URL}/author/courses/${id}/remove-teachers`, {
                ids: userIds,
            }, {withCredentials: true});
        } catch (error) {
            console.error("Ошибка при удалении учителей:", error);
        }
    };

    const handleNext = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchUsersLeft(nextPage);
    };

    const handlePrev = () => {
        const prevPage = Math.max(page - 1, 0);
        setPage(prevPage);
        fetchUsersLeft(prevPage);
    };

    const isPrevDisabled = page === 0;
    const isNextDisabled = total <= (page + 1) * pageSize;

    return (
        <Flex flexDirection="column" alignItems="center" justifyContent="center">
            <Heading size="lg" textAlign="center" mb={4} padding="16px">
                {roleName === 'AUTHOR' ? (<>Изменить авторов</>) : (<>Изменить учителей</>)}
            </Heading>
            <Input
                mx="auto"
                w="40%"
                type="text"
                placeholder="Поиск по Username или ФИО"
                value={searchText}
                onChange={(e) => {
                    const text = e.target.value;
                    setSearchText(text);
                    filterUsers(text);
                }}
                mb={5}
            />
            <Flex justifyContent="center" alignItems="flex-start" gap={8} minWidth="800px">
                <List spacing={4} textAlign="center" w="50%" borderWidth="1px" borderRadius="8">
                    <Heading size="sm" mb={2} mt={2}>
                        {roleName === "AUTHOR" ? (<>Доступные авторы</>) : (<>Доступные учителя</>)}
                    </Heading>
                    {isLoading ? (<Flex justifyContent="center" alignItems="center" height="30vh">
                        <Oval color="#295C48" secondaryColor="#2B415B"/>
                    </Flex>) : (
                        searchText ? (searchResultsLeft.map((user) => (
                            <ListItem
                                key={user.id}
                                onClick={() => handleLeftUserClick(user.id)}
                                style={{
                                    cursor: "pointer",
                                    backgroundColor: selectedLeftUsers.includes(user.id) ? "#5F5" : "transparent",
                                }}
                                padding="5px"
                                borderRadius="8"
                                mb={3}
                                ml={1}
                                mr={1}
                            >
                                {user.username} - {user.firstname} {user.middlename} {user.lastname}
                                <Divider w="100%"></Divider>
                            </ListItem>
                        ))) : (
                            usersLeft.map((user) => (
                                <ListItem
                                    key={user.id}
                                    onClick={() => handleLeftUserClick(user.id)}
                                    style={{
                                        cursor: "pointer",
                                        backgroundColor: selectedLeftUsers.includes(user.id) ? "#5F5" : "transparent",
                                    }}
                                    padding="5px"
                                    borderRadius="8"
                                    mb={3}
                                    ml={1}
                                    mr={1}
                                >
                                    {user.username} - {user.firstname} {user.middlename} {user.lastname}
                                    <Divider w="100%"></Divider>
                                </ListItem>
                            ))))}
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
                </List>
                <Flex justifyContent="center" alignItems="center" flexDirection="column">
                    <Button
                        onClick={() => {
                            roleName === "AUTHOR" ? handleAddAuthors(selectedLeftUsers) : handleAddTeachers(selectedLeftUsers);
                            moveUsersToRight();
                        }}
                        mt={4}
                        isDisabled={selectedLeftUsers.length === 0}
                        cursor={selectedLeftUsers.length > 0 ? "pointer" : "not-allowed"}
                    >
                        <Icon
                            as={FiArrowRight}
                            color={selectedLeftUsers.length > 0 ? "#5F5" : "gray.300"}
                        />
                    </Button>
                    <Button
                        onClick={() => {
                            roleName === "AUTHOR" ? handleRemoveAuthors(selectedRightUsers) : handleRemoveTeachers(selectedRightUsers);
                            moveUsersToLeft();
                        }}
                        mt={2}
                        isDisabled={selectedRightUsers.length === 0}
                        cursor={selectedRightUsers.length > 0 ? "pointer" : "not-allowed"}
                    >
                        <Icon
                            as={FiArrowLeft}
                            color={selectedRightUsers.length > 0 ? "#F55" : "gray.300"}
                        />
                    </Button>
                </Flex>
                <List spacing={4} textAlign="center" w="50%" borderWidth="1px" borderRadius="8">
                    <Heading size="sm" mb={2} mt={2}>
                        {roleName === "AUTHOR" ? (<>Авторы курса</>) : (<>Учителя курса</>)}
                    </Heading>
                    {isLoading ? (<Flex justifyContent="center" alignItems="center" height="30vh">
                        <Oval color="#295C48" secondaryColor="#2B415B"/>
                    </Flex>) : (
                        searchText ? (
                            searchResultsRight.map((user) => (
                                <ListItem
                                    key={user.id}
                                    onClick={() => handleRightUserClick(user.id)}
                                    style={{
                                        cursor: "pointer",
                                        backgroundColor: selectedRightUsers.includes(user.id) ? "#F55" : "transparent"
                                    }}
                                    padding="5px"
                                    borderRadius="8"
                                    mb={3}
                                    ml={1}
                                    mr={1}
                                >
                                    {user.username} - {user.firstname} {user.middlename} {user.lastname}
                                    <Divider w="100%"></Divider>
                                </ListItem>
                            ))) : (usersRight.map((user) => (
                            <ListItem
                                key={user.id}
                                onClick={() => handleRightUserClick(user.id)}
                                style={{
                                    cursor: "pointer",
                                    backgroundColor: selectedRightUsers.includes(user.id) ? "#F55" : "transparent"
                                }}
                                padding="5px"
                                borderRadius="8"
                                mb={3}
                                ml={1}
                                mr={1}
                            >
                                {user.username} - {user.firstname} {user.middlename} {user.lastname}
                                <Divider w="100%"></Divider>
                            </ListItem>
                        ))))}
                </List>
            </Flex>
        </Flex>
    );
};

export default UserList;
