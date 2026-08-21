import { Box, Typography } from '@mui/material'
import Inventory2OutlinedIcon from '@mui/icons-material/Inventory2Outlined'
export default function EmptyState({ title = 'Nothing here yet', message = 'New activity will appear here.' }) { return <Box sx={{ py: 7, textAlign: 'center' }}><Inventory2OutlinedIcon sx={{ fontSize: 42, color: '#b9c7be' }} /><Typography fontWeight={700} sx={{ mt: 1 }}>{title}</Typography><Typography color="text.secondary">{message}</Typography></Box> }
