import client from './axiosClient'
export const assignRole = (userId, roleName) => client.put(`/api/roles/users/${userId}`, null, { params: { roleName } })
