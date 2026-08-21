import client from './axiosClient'
export const getUsers = () => client.get('/api/users')
export const getUser = id => client.get(`/api/users/${id}`)
export const deleteUser = id => client.delete(`/api/users/${id}`)
