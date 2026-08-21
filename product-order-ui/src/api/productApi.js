import client from './axiosClient'
export const getProducts = () => client.get('/api/products')
export const getProductsByCategory = id => client.get(`/api/products/category/${id}`)
export const createProduct = payload => client.post('/api/products', payload)
export const updateProduct = (id, payload) => client.put(`/api/products/${id}`, payload)
export const enableProduct = id => client.patch(`/api/products/${id}/enable`)
export const disableProduct = id => client.patch(`/api/products/${id}/disable`)
