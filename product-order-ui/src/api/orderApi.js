import client from './axiosClient'
export const checkout = (userId, payload) => client.post('/api/orders/checkout', payload, { params: { userId } })
export const getMyOrders = userId => client.get('/api/orders/my-orders', { params: { userId } })
export const getAllOrders = () => client.get('/api/orders')
export const getOrder = (userId, orderId) => client.get(`/api/orders/${orderId}`, { params: { userId } })
