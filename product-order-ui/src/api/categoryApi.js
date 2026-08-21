import client from './axiosClient'
export const getCategories = () => client.get('/api/categories')
export const createCategory = payload => client.post('/api/categories', payload)
export const updateCategory = (id, payload) => client.put(`/api/categories/${id}`, payload)
export const deleteCategory = id => client.delete(`/api/categories/${id}`)
