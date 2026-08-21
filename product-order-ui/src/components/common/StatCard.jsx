import { Box, Card, CardContent, Typography } from '@mui/material'

export default function StatCard({ label, value, detail, icon, tone = '#e76f51' }) {
  return <Card className="soft-panel" sx={{ height: '100%' }}><CardContent sx={{ p: 2.5 }}><Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}><Typography color="text.secondary" variant="body2" fontWeight={600}>{label}</Typography><Box sx={{ color: tone }}>{icon}</Box></Box><Typography variant="h4" fontWeight={800} sx={{ fontFamily: 'Manrope' }}>{value}</Typography><Typography variant="body2" color="text.secondary" sx={{ mt: .5 }}>{detail}</Typography></CardContent></Card>
}
