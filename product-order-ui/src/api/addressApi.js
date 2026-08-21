import client from './axiosClient'
export const getAddresses = userId => client.get('/api/addresses', { params: { userId } })
export const createAddress = (userId, payload) => client.post('/api/addresses', payload, { params: { userId } })
export const updateAddress = (userId, id, payload) => client.put(`/api/addresses/${id}`, payload, { params: { userId } })
export const deleteAddress = (userId, id) => client.delete(`/api/addresses/${id}`, { params: { userId } })
