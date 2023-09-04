import React, {createContext, useState, useContext, useEffect} from 'react';
import axios from '../utils/axiosConfig';

const AuthContext = createContext<AuthContextProps | null>(null);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

interface AuthProviderProps {
    children: React.ReactNode;
}

interface AuthContextProps {
    isAuthenticated: boolean;
    user: User | null;
    setAuthenticated: React.Dispatch<React.SetStateAction<boolean>>;
    setUser: React.Dispatch<React.SetStateAction<User | null>>;
}

interface User {
    id: number;
    username: string;
    email: string;
    roles: string[];
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [isAuthenticated, setAuthenticated] = useState(() => {
        return JSON.parse(localStorage.getItem('isAuthenticated') || 'false');
    });

    const [user, setUser] = useState<User | null>(() => {
        const savedUser = localStorage.getItem('user');
        return savedUser ? JSON.parse(savedUser) : null;
    });

    useEffect(() => {
        localStorage.setItem('isAuthenticated', JSON.stringify(isAuthenticated));
        localStorage.setItem('user', JSON.stringify(user));
    }, [isAuthenticated, user]);

    return (
        <AuthContext.Provider value={{ isAuthenticated, setAuthenticated, user, setUser }}>
            {children}
        </AuthContext.Provider>
    );
};
