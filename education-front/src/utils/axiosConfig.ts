import axios, {AxiosInstance} from 'axios';

let instance: AxiosInstance;

export const initializeAxios = (logout: () => void) => {
     instance = axios.create({
        baseURL: 'http://localhost:8080',
        withCredentials: true
    });

    instance.interceptors.response.use(
        (response) => {
            return response;
        },
        async function (error) {
            const originalRequest = error.config;
            if (
                (error.response.status === 403 || error.response.status === 401) &&
                error.config &&
                !error.config._retry
            ) {
                originalRequest._retry = true;
                try {
                    const response = await axios.post('http://localhost:8080/api/v1/auth/refreshtoken', {}, {withCredentials: true});
                    return instance(originalRequest);
                } catch (error) {
                    logout();
                    return Promise.reject(error);
                }
            }
            return Promise.reject(error);
        }
    );

    return instance;
};

export const getAxiosInstance = () => {
    if (!instance) {
        throw new Error('Axios has not been initialized. Initialize it in AuthProvider first.');
    }
    return instance;
};

