import { BrowserRouter } from 'react-router-dom'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import AppRoutes from './routes/AppRoutes'
import { AuthProvider } from './context/AuthContext'
import './App.css'

const theme = createTheme({
  palette: { primary: { main: '#235c4b', contrastText: '#fff' }, secondary: { main: '#e76f51' }, background: { default: '#f4f6f3', paper: '#fff' }, text: { primary: '#17221f', secondary: '#718078' } },
  typography: { fontFamily: 'DM Sans, sans-serif', h1: { fontFamily: 'Manrope, sans-serif' }, h2: { fontFamily: 'Manrope, sans-serif' }, h3: { fontFamily: 'Manrope, sans-serif' }, h4: { fontFamily: 'Manrope, sans-serif' }, h5: { fontFamily: 'Manrope, sans-serif' }, h6: { fontFamily: 'Manrope, sans-serif' } },
  shape: { borderRadius: 10 },
  components: { MuiButton: { styleOverrides: { root: { borderRadius: 8, textTransform: 'none', fontWeight: 700, boxShadow: 'none' }, containedPrimary: { '&:hover': { backgroundColor: '#19483b', boxShadow: 'none' } } } }, MuiTextField: { defaultProps: { size: 'small' } }, MuiOutlinedInput: { styleOverrides: { root: { borderRadius: 8 } } }, MuiTableCell: { styleOverrides: { head: { backgroundColor: '#f1f5f1', color: '#52655c', fontWeight: 800, fontSize: 12, textTransform: 'uppercase', letterSpacing: '.06em' }, root: { borderColor: '#e2e8e2' } } }, MuiChip: { styleOverrides: { root: { fontWeight: 700 } } } }
})

export default function App() {
  return <ThemeProvider theme={theme}><BrowserRouter><AuthProvider><AppRoutes /></AuthProvider></BrowserRouter></ThemeProvider>
}
