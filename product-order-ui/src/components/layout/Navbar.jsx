import { AppBar, Avatar, Box, IconButton, Stack, Toolbar, Typography, Badge } from '@mui/material'
import { NotificationsNone, Menu, Logout } from '@mui/icons-material'
import { useAuth } from '../../context/AuthContext'

export default function Navbar({ onMenu, role, onLogout }) {
  const { user } = useAuth()
  const username = user?.username || 'Account'
  return <AppBar position="sticky" elevation={0} sx={{ bgcolor: '#fff', color: '#17221f', borderBottom: '1px solid #e2e8e2' }}><Toolbar sx={{ minHeight: 72, px: { xs: 2, md: 4 } }}><IconButton onClick={onMenu} sx={{ display: { md: 'none' }, mr: 1 }}><Menu /></IconButton><Typography variant="h6" fontWeight={800} sx={{ fontFamily: 'Manrope', flexGrow: 1 }}>Overview</Typography><Stack direction="row" alignItems="center" gap={1.5}><IconButton><Badge color="error" variant="dot"><NotificationsNone /></Badge></IconButton><Avatar sx={{ bgcolor: '#dce9e3', color: '#235c4b', width: 36, height: 36 }}>{username.charAt(0).toUpperCase()}</Avatar><Box sx={{ display: { xs: 'none', sm: 'block' }, lineHeight: 1.2 }}><Typography variant="body2" fontWeight={700}>{username}</Typography><Typography variant="caption" color="text.secondary">{role}</Typography></Box><IconButton onClick={onLogout} title="Log out"><Logout fontSize="small" /></IconButton></Stack></Toolbar></AppBar>
}
