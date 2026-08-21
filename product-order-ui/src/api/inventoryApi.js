import client from './axiosClient'
export const getInventory = productId => client.get(`/api/inventory/${productId}`)
export const updateInventory = (productId, payload) => client.put(`/api/inventory/${productId}`, payload)
