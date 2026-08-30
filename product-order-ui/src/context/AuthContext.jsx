import { createContext, useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as authApi from '../api/authApi'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const navigate = useNavigate()
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('user') || 'null'))
  const token = localStorage.getItem('token')

  const login = async payload => {
    const { data } = await authApi.login(payload)
    const selectedRole = payload.role

    // Backend is source of truth for roles
    const backendRoles = [...data.roles]

    // Validate: selected role must match one of the backend roles
    if (!backendRoles.includes(selectedRole)) {
      // Clear any partial state and throw error
      const error = new Error('Selected role does not match your account role.')
      error.response = { data: { message: 'Selected role does not match your account role.' } }
      throw error
    }

    // Role validation passed, store credentials
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify({ id: data.userId, username: data.username, roles: backendRoles }))
    setUser({ id: data.userId, username: data.username, roles: backendRoles })

    // Navigate based on selected role (which is now validated against backend)
    const roleRoute = selectedRole === 'SUPER_ADMIN' ? 'super-admin' : selectedRole.toLowerCase()
    navigate(`/${roleRoute}/dashboard`)
  }

  const logout = () => {
    localStorage.clear()
    setUser(null)
    navigate('/login')
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        roles: user?.roles || [],
        isAuthenticated: Boolean(token && user),
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
