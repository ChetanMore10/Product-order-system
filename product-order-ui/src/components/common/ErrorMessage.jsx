import { Alert, Button, Stack } from '@mui/material'
export default function ErrorMessage({ error, onRetry }) { return <Stack alignItems="center" sx={{ py: 5 }}><Alert severity="error" sx={{ mb: 2 }}>{error?.response?.data?.message || error?.message || 'Unable to load data from the server.'}</Alert>{onRetry && <Button onClick={onRetry}>Retry</Button>}</Stack> }
