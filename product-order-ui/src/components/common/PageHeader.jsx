import { Stack, Typography, Button } from '@mui/material'

export default function PageHeader({ title, subtitle, action, onAction }) {
  return <Stack direction={{ xs: 'column', sm: 'row' }} justifyContent="space-between" alignItems={{ sm: 'center' }} gap={2} sx={{ mb: 3 }}>
    <div><Typography variant="h4" className="page-title">{title}</Typography>{subtitle && <Typography color="text.secondary" sx={{ mt: .5 }}>{subtitle}</Typography>}</div>
    {action && <Button variant="contained" onClick={onAction} sx={{ alignSelf: 'flex-start' }}>{action}</Button>}
  </Stack>
}
