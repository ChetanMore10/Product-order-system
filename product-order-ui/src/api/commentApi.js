import client from './axiosClient'

export const getComments = productId => client.get(`/api/products/${productId}/comments`)
export const addComment = (productId, payload) => client.post(`/api/products/${productId}/comments`, payload)
export const updateComment = (commentId, payload) => client.put(`/api/comments/${commentId}`, payload)
export const deleteComment = commentId => client.delete(`/api/comments/${commentId}`)