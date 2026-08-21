import client from './axiosClient'
export const login = payload => client.post('/api/auth/login', payload)
export const register = payload => client.post('/api/auth/register', payload)
