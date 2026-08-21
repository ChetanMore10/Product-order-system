import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
export default function ProtectedRoute({ children, roles }) { const { isAuthenticated, roles: userRoles } = useAuth(); const location = useLocation(); if (!isAuthenticated) return <Navigate to="/login" state={{ from: location }} replace />; if (roles && !roles.some(role => userRoles.includes(role))) { const role = userRoles.includes('SUPER_ADMIN') ? 'super-admin' : userRoles.includes('ADMIN') ? 'admin' : 'user'; return <Navigate to={`/${role}/dashboard`} replace /> } return children }
