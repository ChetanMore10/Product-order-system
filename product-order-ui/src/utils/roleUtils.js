export const dashboardForRoles = roles => roles.includes('SUPER_ADMIN') ? '/super-admin/dashboard' : roles.includes('ADMIN') ? '/admin/dashboard' : '/user/dashboard'
export const hasRole = (roles, allowed) => allowed.some(role => roles.includes(role))
