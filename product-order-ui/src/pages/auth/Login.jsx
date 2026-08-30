import { useState } from 'react'
import { Alert, Box, Button, FormControl, FormHelperText, InputLabel, Link, MenuItem, Paper, Select, Stack, TextField, Typography } from '@mui/material'
import { useAuth } from '../../context/AuthContext'

const ROLE_LABELS = {
  USER: 'User',
  ADMIN: 'Admin',
  SUPER_ADMIN: 'Super Admin',
}

export default function Login() {
  const { login } = useAuth()
  const [form, setForm] = useState({ username: '', password: '', role: 'USER' })
  const [apiError, setApiError] = useState('')
  const [loading, setLoading] = useState(false)
  const [touched, setTouched] = useState({ username: false, password: false, role: false })

  // Clear API error when user modifies form
  const clearErrorOnChange = () => {
    setApiError('')
  }

  const handleUsernameChange = e => {
    setForm({ ...form, username: e.target.value })
    clearErrorOnChange()
  }

  const handlePasswordChange = e => {
    setForm({ ...form, password: e.target.value })
    clearErrorOnChange()
  }

  const handleRoleChange = e => {
    setForm({ ...form, role: e.target.value })
    clearErrorOnChange()
  }

  const handleFieldBlur = fieldName => {
    setTouched({ ...touched, [fieldName]: true })
  }

  // Validation functions
  const getValidationError = fieldName => {
    if (!touched[fieldName]) return ''
    if (fieldName === 'username') return form.username ? '' : 'Username is required'
    if (fieldName === 'password') return form.password ? '' : 'Password is required'
    if (fieldName === 'role') return form.role ? '' : 'Role is required'
    return ''
  }

  const isFormValid = () => {
    return form.username && form.password && form.role
  }

  const submit = async e => {
    e.preventDefault()
    setApiError('')

    // Mark all fields as touched
    setTouched({ username: true, password: true, role: true })

    // Validate form
    if (!isFormValid()) {
      return
    }

    setLoading(true)
    try {
      await login(form)
    } catch (err) {
      // Display backend error or role mismatch error
      const errorMessage = err.response?.data?.message || err.message || 'Login failed. Check your credentials and backend connection.'
      setApiError(errorMessage)
    } finally {
      setLoading(false)
    }
  }

  const usernameError = getValidationError('username')
  const passwordError = getValidationError('password')
  const roleError = getValidationError('role')

  return (
    <Box sx={{ minHeight: '100vh', display: 'grid', placeItems: 'center', p: 2, bgcolor: '#f7f8f5' }}>
      <Paper elevation={0} sx={{ width: '100%', maxWidth: 440, p: { xs: 3, sm: 5 }, border: '1px solid #e2e8e2', borderRadius: 2 }}>
        <Stack direction="row" alignItems="center" gap={1.5} sx={{ mb: 4 }}>
          <Box sx={{ width: 40, height: 40, bgcolor: '#1b5e3f', borderRadius: 1, display: 'grid', placeItems: 'center', color: '#fff', fontWeight: 800, fontSize: 16 }}>
            PO
          </Box>
          <Typography fontWeight={800} sx={{ fontFamily: 'Manrope', fontSize: 20, color: '#17221f' }}>
            Parcelly
          </Typography>
        </Stack>

        <Typography variant="h5" sx={{ fontWeight: 700, color: '#17221f', mb: 1 }}>
          Welcome back
        </Typography>
        <Typography color="text.secondary" sx={{ mb: 3, fontSize: '.95rem' }}>
          Sign in to access your account
        </Typography>

        {apiError && (
          <Alert severity="error" sx={{ mb: 2.5, borderRadius: 1 }}>
            {apiError}
          </Alert>
        )}

        <form onSubmit={submit}>
          <Stack gap={2.5}>
            <TextField
              label="Username"
              value={form.username}
              onChange={handleUsernameChange}
              onBlur={() => handleFieldBlur('username')}
              required
              fullWidth
              size="small"
              disabled={loading}
              error={Boolean(usernameError)}
              helperText={usernameError}
            />

            <TextField
              label="Password"
              type="password"
              value={form.password}
              onChange={handlePasswordChange}
              onBlur={() => handleFieldBlur('password')}
              required
              fullWidth
              size="small"
              disabled={loading}
              error={Boolean(passwordError)}
              helperText={passwordError}
            />

            <FormControl fullWidth size="small" required error={Boolean(roleError)}>
              <InputLabel>Role</InputLabel>
              <Select
                value={form.role}
                onChange={handleRoleChange}
                onBlur={() => handleFieldBlur('role')}
                label="Role"
                disabled={loading}
              >
                <MenuItem value="USER">{ROLE_LABELS.USER}</MenuItem>
                <MenuItem value="ADMIN">{ROLE_LABELS.ADMIN}</MenuItem>
                <MenuItem value="SUPER_ADMIN">{ROLE_LABELS.SUPER_ADMIN}</MenuItem>
              </Select>
              <FormHelperText>{roleError || 'Select your account role'}</FormHelperText>
            </FormControl>

            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              sx={{ mt: 1 }}
            >
              {loading ? 'Signing in...' : 'Sign in'}
            </Button>
          </Stack>
        </form>

        <Typography textAlign="center" color="text.secondary" sx={{ mt: 3, fontSize: '.9rem' }}>
          Don't have an account?{' '}
          <Link href="/register" underline="none" sx={{ color: '#1b5e3f', fontWeight: 600 }}>
            Create one
          </Link>
        </Typography>
      </Paper>
    </Box>
  )
}
