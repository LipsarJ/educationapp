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
    useColorModeValue,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton,
    ModalBody,
    ModalFooter,
    FormControl,
    FormLabel,
    FormErrorMessage,
    Input as ChakraInput,
    Icon, Select, Flex, Checkbox
} from '@chakra-ui/react';
import {Formik, Field, Form, ErrorMessage} from 'formik';
import {instanceAxios} from '../../utils/axiosConfig';
import {FiArrowRightCircle, FiArrowLeftCircle, FiEdit2} from 'react-icons/fi'
import {Oval, ThreeDots} from 'react-loader-spinner';


interface UpdateUserDto {
    username: string;
    roles: RoleDto[]; // Здесь можно указать тип, который соответствует вашей модели ERole.
    email: string;
    firstname?: string;
    middlename?: string;
    lastname?: string;
    userStatus: string; // Здесь можно указать тип, который соответствует вашей модели UserStatus.
}

interface User {
    id: number;
    username: string;
    firstname: string;
    middlename: string;
    lastname: string;
    email: string;
    roles: RoleDto[];
    userStatus: string;
}

interface RoleDto {
    roleName: string;
}

const UsersControl: React.FC = () => {
    const [isLoading, setLoading] = useState(false);
    const [users, setUsers] = useState<User[]>([]);
    const [searchText, setSearchText] = useState<string>('');
    const [page, setPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(10);
    const [total, setTotal] = useState<number>(0);
    const borderColor = useColorModeValue('gray.200', 'gray.900');
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const rolesOptions = ["ADMIN", "AUTHOR", "TEACHER", "STUDENT", "MODERATOR"];
    const [selectedUser, setSelectedUser] = useState<User | null>({
        id: 0,
        username: '',
        firstname: '',
        middlename: '',
        lastname: '',
        email: '',
        roles: [],
        userStatus: '',
    });
    const [enteredRoles, setEnteredRoles] = useState<string[]>(
        selectedUser?.roles.map(role => role.roleName) || []
    );

    useEffect(() => {
        fetchUsers(0);
    }, [searchText]);

    const fetchUsers = async (currentPage: number) => {
        try {
            const response = await instanceAxios.get('/admin/user', {
                params: {
                    filterText: searchText,
                    page: currentPage,
                    size: pageSize,
                },
            });
            setUsers(response.data.userInfo);
            setTotal(response.data.totalCount);
        } catch (error) {
            console.error('Error fetching data', error);
        }
    };

    const handleSearch = () => {
        setPage(0);
        fetchUsers(0);
    };

    const handleNext = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchUsers(nextPage);
    };

    const handlePrev = () => {
        const prevPage = Math.max(page - 1, 0);
        setPage(prevPage);
        fetchUsers(prevPage);
    };

    const isPrevDisabled = page === 0;
    const isNextDisabled = total <= (page + 1) * pageSize;

    const handleEditUser = (user: User) => {
        setSelectedUser(user);
        const rolesArray = user.roles.map(role => role.roleName);
        setEnteredRoles(rolesArray);
        setIsEditing(true);
    };

    const handleCloseEditUser = () => {
        setSelectedUser(null);
        setIsEditing(false);
    };

    const handleSaveEditUser = async (values: any) => {
        if (selectedUser) {
            let rolesArray: RoleDto[] = [];

            if (typeof values.roles === 'string') {
                rolesArray = values.roles.split(',').map((roleName: string) => ({
                    roleName: roleName.trim(),
                }));
            } else if (Array.isArray(values.roles)) {
                rolesArray = values.roles.map((roleName: string) => ({
                    roleName: roleName.trim(),
                }));
            }

            const updateUserDto: UpdateUserDto = {
                username: values.username,
                roles: rolesArray,
                email: values.email,
                firstname: values.firstname,
                middlename: values.middlename,
                lastname: values.lastname,
                userStatus: values.userStatus,
            };
            setLoading(true);
            try {
                console.log(updateUserDto);
                const response = await instanceAxios.put(`/admin/user/${selectedUser.id}`, updateUserDto);
                setIsEditing(false);
                fetchUsers(0);
            } catch (error) {
                console.error('Error saving user', error);
            }
        }
        setLoading(false);
    };

    const validateEmail = (value: string) => {
        if (!value || !isValidEmail(value)) {
            return 'Некорректный email';
        }
        return '';
    };

    const isValidEmail = (email: string) => {
        const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
        return emailPattern.test(email);
    };

    if (!users) {
        return (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Oval color="#295C48" secondaryColor="#2B415B"/>
            </Flex>
        );
    }

    return (
        <Container mt={5} maxW="container.xl" centerContent>
            <Box w="full" p={4} borderWidth={1} borderRadius="md" borderColor={borderColor}>
                <VStack spacing={4}>
                    <Input
                        placeholder="Введите имя пользователя..."
                        value={searchText}
                        onChange={(e) => {
                            setSearchText(e.target.value);
                            handleSearch(); // Вызывать поиск при каждом изменении текста
                        }}
                    />
                </VStack>
            </Box>
            <Divider/>
            <Flex mt={2} flexDir="column" gap={2} alignItems="flex-start" w="100%">
                {users.map((user) => (
                    <Box key={user.id} w="100%" p={2} borderWidth={1} borderRadius="md" borderColor={borderColor}
                         h="100%">
                        <HStack spacing={4} align="center">
                            <Icon as={FiEdit2} cursor="pointer" onClick={() => handleEditUser(user)}/>
                            <Text>
                                <strong>Имя пользователя:</strong> {user.username}
                                <strong> Имя:</strong> {user.firstname} <strong> Отчество:</strong> {user.middlename}
                                <strong> Фамилия:</strong> {user.lastname} <strong> Почта:</strong> {user.email}
                                <strong> Роли:</strong> {user.roles.map((role) => role.roleName).join(', ')}
                                <strong> Статус:</strong> {user.userStatus}
                            </Text>
                        </HStack>
                    </Box>
                ))}
            </Flex>
            <HStack mt={4}>
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
            </HStack>
            {selectedUser && (
                <Modal isOpen={isEditing} onClose={handleCloseEditUser}>
                    <ModalOverlay/>
                    <Formik
                        initialValues={{
                            username: selectedUser.username || '',
                            firstname: selectedUser.firstname || '',
                            middlename: selectedUser.middlename || '',
                            lastname: selectedUser.lastname || '',
                            email: selectedUser.email || '',
                            roles: enteredRoles || '',
                            userStatus: selectedUser.userStatus || ''
                        }}
                        enableReinitialize
                        onSubmit={async (values, {setSubmitting}) => {
                            handleSaveEditUser(values);
                            setSubmitting(false);
                        }}
                    >
                        <Form>
                            <ModalContent>
                                <ModalHeader>Редактировать пользователя</ModalHeader>
                                <ModalCloseButton/>
                                <ModalBody>
                                    <Field name="username">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>Username</FormLabel>
                                                <ChakraInput {...field} />
                                                <ErrorMessage name="username" component={FormErrorMessage}/>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="firstname">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>First Name</FormLabel>
                                                <ChakraInput {...field} />
                                                <ErrorMessage name="firstname" component={FormErrorMessage}/>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="middlename">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>Middle Name</FormLabel>
                                                <ChakraInput {...field} />
                                                <ErrorMessage name="middlename" component={FormErrorMessage}/>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="lastname">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>Last Name</FormLabel>
                                                <ChakraInput {...field} />
                                                <ErrorMessage name="lastname" component={FormErrorMessage}/>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="email">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>Email</FormLabel>
                                                <ChakraInput {...field} />
                                                <ErrorMessage name="email" component={FormErrorMessage}/>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="roles">
                                        {({field, form}: { field: any; form: any }) => (

                                            <FormControl mb={4}>
                                                <HStack alignItems="flex-start" flexDir = "column">
                                                    <FormLabel>Roles</FormLabel>
                                                    {rolesOptions.map((role) => (
                                                        <Checkbox
                                                            ml={8}
                                                            key={role}
                                                            name={field.name}
                                                            value={role}
                                                            isChecked={field.value.includes(role)}
                                                            onChange={() => {
                                                                const roles = [...field.value]; // Создаем копию текущего значения
                                                                if (roles.includes(role)) {
                                                                    // Если роль уже выбрана, убираем её
                                                                    roles.splice(roles.indexOf(role), 1);
                                                                } else {
                                                                    // Иначе добавляем роль
                                                                    roles.push(role);
                                                                }
                                                                form.setFieldValue(field.name, roles);
                                                                setEnteredRoles(roles); // Обновляем enteredRoles
                                                            }}
                                                        >
                                                            {role}
                                                        </Checkbox>
                                                    ))}
                                                    <ErrorMessage name="roles" component={FormErrorMessage}/>
                                                </HStack>
                                            </FormControl>
                                        )}
                                    </Field>
                                    <Field name="userStatus">
                                        {({field, form}: { field: any; form: any }) => (
                                            <FormControl mb={4}>
                                                <FormLabel>UserStatus</FormLabel>
                                                <Input
                                                    {...field}
                                                    placeholder="Статус пользователя"
                                                />
                                            </FormControl>
                                        )}
                                    </Field>
                                </ModalBody>
                                <ModalFooter>
                                    <Button colorScheme="blue" mr={3} onClick={handleCloseEditUser}>
                                        Отмена
                                    </Button>
                                    <Button colorScheme="green" type="submit">
                                        {isLoading ? <ThreeDots height={'10px'} color="white"/> : <>Сохранить
                                            изменения</>}
                                    </Button>
                                </ModalFooter>
                            </ModalContent>
                        </Form>
                    </Formik>
                </Modal>
            )}
        </Container>
    );
};

export default UsersControl;
