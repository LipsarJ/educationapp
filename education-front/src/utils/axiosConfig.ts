import axios from 'axios';

const instance = axios.create({
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
            const response = await axios.post('http://localhost:8080/api/v1/auth/refreshtoken', {}, {withCredentials: true});
            return instance(originalRequest);
        }
        return Promise.reject(error);
    }
);

export default instance;
