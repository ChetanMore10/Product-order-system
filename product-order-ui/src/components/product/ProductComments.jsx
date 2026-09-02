import { useCallback, useEffect, useState } from 'react'
import { Alert, Box, Button, Card, CardContent, CircularProgress, Divider, IconButton, Snackbar, Stack, TextField, Typography } from '@mui/material'
import { Delete, EditOutlined, ReplyOutlined, SendOutlined } from '@mui/icons-material'
import ConfirmDialog from '../common/ConfirmDialog'
import * as commentApi from '../../api/commentApi'
import { useAuth } from '../../context/AuthContext'

const errorMessage = error => {
  const status = error.response?.status
  if (status === 401) return 'Please login to comment.'
  if (status === 403) return 'You do not have permission to change this comment.'
  if (status === 404) return 'This comment or product could not be found.'
  return error.response?.data?.message || error.message || 'Unable to complete the request.'
}

const formatDate = value => value ? new Date(value).toLocaleString() : 'Recently'

function CommentItem({ comment, productId, user, onRefresh, onFeedback }) {
  const [replyOpen, setReplyOpen] = useState(false)
  const [reply, setReply] = useState('')
  const [editing, setEditing] = useState(false)
  const [editText, setEditText] = useState(comment.comment)
  const [deleting, setDeleting] = useState(false)
  const [saving, setSaving] = useState(false)
  const isOwner = user && String(user.id) === String(comment.userId)

  const submit = (request, success, clear) => {
    setSaving(true)
    request.then(() => { clear?.(); onRefresh(); onFeedback(success) }).catch(error => onFeedback(errorMessage(error))).finally(() => setSaving(false))
  }

  const postReply = () => {
    if (!reply.trim()) return
    submit(commentApi.addComment(productId, { comment: reply.trim(), parentCommentId: comment.id }), 'Reply posted.', () => { setReply(''); setReplyOpen(false) })
  }

  const saveEdit = () => {
    if (!editText.trim()) return
    submit(commentApi.updateComment(comment.id, { comment: editText.trim() }), 'Comment updated.', () => setEditing(false))
  }

  return <Box sx={{ ml: comment.parentCommentId ? { xs: 1.5, sm: 4 } : 0, mt: 2 }}>
    <Card variant="outlined" sx={{ borderColor: '#dfe7e1', borderRadius: 1.5, boxShadow: 'none' }}>
      <CardContent sx={{ p: 2, '&:last-child': { pb: 2 } }}>
        <Stack direction="row" justifyContent="space-between" gap={2} alignItems="flex-start">
          <Box sx={{ minWidth: 0 }}><Typography fontWeight={800} sx={{ color: '#17221f' }}>{comment.username || 'Customer'}</Typography><Typography variant="caption" color="text.secondary">{formatDate(comment.createdAt)}</Typography></Box>
          {isOwner && <Stack direction="row"><IconButton aria-label="Edit comment" size="small" onClick={() => setEditing(true)}><EditOutlined fontSize="small" /></IconButton><IconButton aria-label="Delete comment" size="small" color="error" onClick={() => setDeleting(true)}><Delete fontSize="small" /></IconButton></Stack>}
        </Stack>
        {editing ? <Stack direction={{ xs: 'column', sm: 'row' }} gap={1} sx={{ mt: 1.5 }}><TextField fullWidth autoFocus value={editText} onChange={event => setEditText(event.target.value)} inputProps={{ maxLength: 1000 }} /><Button variant="contained" disabled={saving || !editText.trim()} onClick={saveEdit}>Save</Button><Button disabled={saving} onClick={() => { setEditText(comment.comment); setEditing(false) }}>Cancel</Button></Stack> : <Typography sx={{ mt: 1.5, whiteSpace: 'pre-wrap', overflowWrap: 'anywhere' }}>{comment.comment}</Typography>}
        {user && !editing && <Button size="small" startIcon={<ReplyOutlined />} onClick={() => setReplyOpen(value => !value)} sx={{ mt: 1.25, px: 0.5 }}>Reply</Button>}
        {replyOpen && <Stack direction={{ xs: 'column', sm: 'row' }} gap={1} sx={{ mt: 1 }}><TextField fullWidth autoFocus placeholder="Write a reply..." value={reply} onChange={event => setReply(event.target.value)} inputProps={{ maxLength: 1000 }} /><Button variant="contained" startIcon={<SendOutlined />} disabled={saving || !reply.trim()} onClick={postReply}>Post</Button></Stack>}
      </CardContent>
    </Card>
    {comment.replies?.map(replyComment => <CommentItem key={replyComment.id} comment={replyComment} productId={productId} user={user} onRefresh={onRefresh} onFeedback={onFeedback} />)}
    <ConfirmDialog open={deleting} title="Delete this comment?" onClose={() => setDeleting(false)} onConfirm={() => { setDeleting(false); submit(commentApi.deleteComment(comment.id), 'Comment deleted.') }} />
  </Box>
}

export default function ProductComments({ productId }) {
  const { user, isAuthenticated } = useAuth()
  const [comments, setComments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')
  const [comment, setComment] = useState('')
  const [posting, setPosting] = useState(false)

  const loadComments = useCallback(() => {
    setLoading(true)
    commentApi.getComments(productId).then(({ data }) => { setComments(Array.isArray(data) ? data : []); setError('') }).catch(requestError => setError(errorMessage(requestError))).finally(() => setLoading(false))
  }, [productId])

  useEffect(() => { loadComments() }, [loadComments])

  const postComment = () => {
    if (!comment.trim()) return
    setPosting(true)
    commentApi.addComment(productId, { comment: comment.trim(), parentCommentId: null }).then(() => { setComment(''); loadComments(); setMessage('Comment posted.') }).catch(requestError => setError(errorMessage(requestError))).finally(() => setPosting(false))
  }

  return <Box sx={{ mt: 2.5 }}>
    <Divider sx={{ mb: 2 }} />
    <Typography variant="h6" fontWeight={800} sx={{ color: '#17221f' }}>Customer Comments</Typography>
    {isAuthenticated ? <Stack direction={{ xs: 'column', sm: 'row' }} gap={1} sx={{ mt: 1.5 }}><TextField fullWidth multiline maxRows={4} placeholder="Write a comment..." value={comment} onChange={event => setComment(event.target.value)} inputProps={{ maxLength: 1000 }} /><Button variant="contained" disabled={posting || !comment.trim()} onClick={postComment} sx={{ minWidth: 136, alignSelf: { sm: 'stretch' } }}>Post Comment</Button></Stack> : <Alert severity="info" sx={{ mt: 1.5 }}>Please login to comment.</Alert>}
    {loading ? <Stack alignItems="center" sx={{ py: 3 }}><CircularProgress size={26} /></Stack> : error ? <Alert severity="error" action={<Button color="inherit" size="small" onClick={loadComments}>Retry</Button>} sx={{ mt: 2 }}>{error}</Alert> : comments.length ? <Stack sx={{ mt: 1 }}>{comments.map(item => <CommentItem key={item.id} comment={item} productId={productId} user={user} onRefresh={loadComments} onFeedback={setMessage} />)}</Stack> : <Alert severity="info" sx={{ mt: 2 }}>No comments yet. Be the first to comment.</Alert>}
    <Snackbar open={Boolean(message)} autoHideDuration={4500} onClose={() => setMessage('')} message={message} />
  </Box>
}