import client from './axiosClient'
export const getCart = userId => client.get('/api/cart', { params: { userId } })
export const addCartItem = (userId, payload) => client.post('/api/cart/items', payload, { params: { userId } })
export const updateCartItem = (userId, productId, quantity) => client.put(`/api/cart/items/${productId}`, null, { params: { userId, quantity } })
export const removeCartItem = (userId, productId) => client.delete(`/api/cart/items/${productId}`, { params: { userId } })
export const clearCart = userId => client.delete('/api/cart', { params: { userId } })
