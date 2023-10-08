import React, {useState} from 'react';
import {ChakraProvider, CSSReset, Box, Flex, Text, useMediaQuery} from '@chakra-ui/react';
import {AuthProvider} from './contexts/AuthContext';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import CreateCourse from './components/authors/CreateCourse'
import Home from './components/Home';
import Courses from './components/authors/Courses'
import EmployeeSearch from './components/EmployeeSearch';
import Header from './components/Header'; // Импортируйте компонент Header
import Sidebar from './components/Sidebar';
import theme from './theme';
import SidebarOverlay from './components/SidebarOverlay';
import CourseDetails from './components/CourseDetails'
import CreateLesson from './components/authors/CreateLesson'
import LessonDetails from './components/LessonDetails'
import CreateTask from './components/authors/CreateTask'
import TaskDetails from './components/TaskDetails'
import Users from './components/Users'
import TasksDone from './components/teachers/TasksDone'
import CreateTaskDone from './components/student/CreateTaskDone'
import TaskDoneDetails from './components/teachers/TaskDoneDetails'
import EditTaskDone from './components/student/EditTaskDone'
import UsersControl from './components/admin/UsersControl'
import StudentsControle from './components/StudentsControle'

function App() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };
    const [isMobile] = useMediaQuery('(max-width: 768px)');

    return (
        <ChakraProvider theme={theme}>
            <AuthProvider>
                <CSSReset/>
                <BrowserRouter>
                    <Flex>
                        <Sidebar isSidebarOpen={isSidebarOpen} isMobile = {isMobile}/>
                        {isMobile &&
                            <SidebarOverlay isOpen={isSidebarOpen} onClose={toggleSidebar}/>
                        }
                    </Flex>
                    <Flex flexDir="column" position = "relative" marginLeft={isSidebarOpen && !isMobile ? '250px' : '0'} flex="1">
                        <Header onToggleSidebar={toggleSidebar} isSidebarOpen={isSidebarOpen} isMobile={isMobile}/>
                        <Routes>
                            <Route path="/" element={<Home/>}/>
                            <Route path="/persons" element={<EmployeeSearch/>}/>
                            <Route path="/login" element={<Login/>}/>
                            <Route path="/register" element={<Register/>}/>
                            <Route path="/courses" element={<Courses/>}/>
                            <Route path="/courses/create" element={<CreateCourse/>}/>
                            <Route path="/courses/:courseId" element={<CourseDetails/>}/>
                            <Route path="/courses/:courseId/journal" element={<StudentsControle/>}/>
                            <Route path="/lessons/create/:courseId" element={<CreateLesson/>}/>
                            <Route path="/lessons/:courseId/:lessonId" element={<LessonDetails/>}/>
                            <Route path="/tasks/create/:courseId/:lessonId" element={<CreateTask/>}/>
                            <Route path="/tasks/:courseId/:lessonId/:homeworkTaskId" element={<TaskDetails/>}/>
                            <Route path="/users/:id/:roleName" element={<Users/>}/>
                            <Route path="/courses/:courseId/:lessonId/:studentId/tasks" element={<TasksDone/>}/>
                            <Route path="/tasks-done/:courseId/:lessonId/:homeworkTaskId/:homeworkDoneId" element={<TaskDoneDetails/>}/>
                            <Route path="/task-done/edit-solution/:courseId/:lessonId/:homeworkTaskId/:homeworkDoneId" element={<EditTaskDone/>}/>
                            <Route path="/task-done/add-solution/:courseId/:lessonId/:homeworkTaskId" element={<CreateTaskDone/>}/>
                            <Route path="/admin/users" element={<UsersControl/>}/>
                        </Routes>
                    </Flex>
                </BrowserRouter>
            </AuthProvider>
        </ChakraProvider>
    );
}

export default App;
