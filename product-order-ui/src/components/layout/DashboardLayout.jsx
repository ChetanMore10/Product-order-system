import { useState } from 'react'
import { Box } from '@mui/material'
import Navbar from './Navbar'
import Sidebar from './Sidebar'
import { useAuth } from '../../context/AuthContext'
export default function DashboardLayout({ children }) { const { roles, logout } = useAuth(); const [open, setOpen] = useState(false); const role = roles.includes('SUPER_ADMIN') ? 'SUPER_ADMIN' : roles.includes('ADMIN') ? 'ADMIN' : 'USER'; return <Box className="app-shell"><Sidebar role={role} mobileOpen={open} onClose={() => setOpen(false)} /><Box sx={{ ml: { md: '248px' } }}><Navbar role={role} onMenu={() => setOpen(true)} onLogout={logout} /><main className="dashboard-content">{children}</main></Box></Box> }
