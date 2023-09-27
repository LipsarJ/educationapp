import axios, {AxiosInstance} from 'axios';

export const instanceAxios = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    withCredentials: true
});
const logout = () => {

    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('user');
    window.location.href = '/login';
};
instanceAxios.interceptors.response.use(
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
                const response = await axios.post(process.env.REACT_APP_API_URL+'/auth/refreshtoken', {}, {withCredentials: true});
                return instanceAxios(originalRequest);
            } catch (error) {
                logout();
                return Promise.reject(error);
            }
        }
        return Promise.reject(error);
    }
);

