/**
 * LAXMI CLINIC - UNIFIED API CLIENT & UTILITIES
 */
const API = (() => {
    const TOKEN_KEY = 'laxmi_admin_token';
    const USER_KEY = 'laxmi_admin_user';

    function getAuthToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function setAuth(token, user) {
        localStorage.setItem(TOKEN_KEY, token);
        localStorage.setItem(USER_KEY, JSON.stringify(user));
    }

    function clearAuth() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
    }

    function getUser() {
        const user = localStorage.getItem(USER_KEY);
        return user ? JSON.parse(user) : null;
    }

    function isAuthenticated() {
        return !!getAuthToken();
    }

    async function request(endpoint, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        const token = getAuthToken();
        if (token && !options.skipAuth) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(endpoint, {
                ...options,
                headers
            });

            if (response.status === 401 && !endpoint.includes('/auth/login')) {
                clearAuth();
                if (window.location.pathname.includes('admin.html')) {
                    showToast('Session expired. Please log in again.', 'warning');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1200);
                }
            }

            const data = await response.json().catch(() => ({
                success: false,
                message: `Server returned status ${response.status}`
            }));

            if (!response.ok || data.success === false) {
                let errorMsg = data.message || 'An error occurred. Please try again.';
                if (data.errors) {
                    const validationMsgs = Object.values(data.errors).join(', ');
                    errorMsg += `: ${validationMsgs}`;
                }
                const error = new Error(errorMsg);
                error.status = response.status;
                error.data = data;
                throw error;
            }

            return data;
        } catch (err) {
            console.error(`API Error [${endpoint}]:`, err);
            throw err;
        }
    }

    // Public API calls
    const publicApi = {
        getSiteInfo: () => request('/api/public/site-info'),
        getActiveShifts: () => request('/api/public/shifts'),
        generateToken: (data) => request('/api/public/tokens/generate', { method: 'POST', body: JSON.stringify(data) }),
        getLiveQueueStatus: () => request('/api/public/tokens/live-status'),
        checkToken: (tokenNumber, date) => request(`/api/public/tokens/check?tokenNumber=${tokenNumber}${date ? `&date=${date}` : ''}`),
        getMyTokens: (phone, date) => request(`/api/public/tokens/my-tokens?phone=${encodeURIComponent(phone)}${date ? `&date=${date}` : ''}`),
        getAvailableSlots: () => request('/api/public/home-visits/slots'),
        bookHomeVisit: (data) => request('/api/public/home-visits/book', { method: 'POST', body: JSON.stringify(data) }),
        trackHomeVisits: (mobile) => request(`/api/public/home-visits/track/${encodeURIComponent(mobile)}`),
        submitContact: (data) => request('/api/public/contact', { method: 'POST', body: JSON.stringify(data) }),
        login: (credentials) => request('/api/public/auth/login', { method: 'POST', body: JSON.stringify(credentials), skipAuth: true })
    };

    // Admin API calls
    const adminApi = {
        getDashboardStats: () => request('/api/admin/dashboard/stats'),
        getTodayTokens: (date, status) => {
            let url = '/api/admin/tokens/today';
            const params = new URLSearchParams();
            if (date) params.append('date', date);
            if (status) params.append('status', status);
            const q = params.toString();
            return request(q ? `${url}?${q}` : url);
        },
        callNextToken: () => request('/api/admin/tokens/call-next', { method: 'POST' }),
        callSpecificToken: (id) => request(`/api/admin/tokens/${id}/call`, { method: 'POST' }),
        markComplete: (id, remark) => request(`/api/admin/tokens/${id}/complete`, { method: 'POST', body: JSON.stringify({ remark }) }),
        skipToken: (id, remark) => request(`/api/admin/tokens/${id}/skip`, { method: 'POST', body: JSON.stringify({ remark }) }),
        recallToken: (id) => request(`/api/admin/tokens/${id}/recall`, { method: 'POST' }),
        getHomeVisits: (status, date) => {
            let url = '/api/admin/home-visits';
            const params = new URLSearchParams();
            if (status) params.append('status', status);
            if (date) params.append('date', date);
            const q = params.toString();
            return request(q ? `${url}?${q}` : url);
        },
        updateHomeVisitStatus: (id, status, adminRemark, doctorNotes) => 
            request(`/api/admin/home-visits/${id}/status`, { method: 'PUT', body: JSON.stringify({ status, adminRemark, doctorNotes }) }),
        getSiteInfo: () => request('/api/admin/site-info'),
        updateSiteInfo: (data) => request('/api/admin/site-info', { method: 'PUT', body: JSON.stringify(data) }),
        toggleOperationalStatus: (operational, operationalStatusMessage) => 
            request('/api/admin/site-info/operational-status', { method: 'PUT', body: JSON.stringify({ operational, operationalStatusMessage }) }),
        getAllShifts: () => request('/api/admin/shifts'),
        updateShift: (shiftNumber, data) => request(`/api/admin/shifts/${shiftNumber}`, { method: 'PUT', body: JSON.stringify(data) }),
        toggleShiftActive: (shiftNumber, active) => request(`/api/admin/shifts/${shiftNumber}/toggle`, { method: 'PUT', body: JSON.stringify({ active }) }),
        resetDefaultShifts: () => request('/api/admin/shifts/reset-defaults', { method: 'POST' }),
        getNotifications: () => request('/api/admin/notifications'),
        markNotificationRead: (id) => request(`/api/admin/notifications/${id}/read`, { method: 'PUT' }),
        markAllNotificationsRead: () => request('/api/admin/notifications/mark-all-read', { method: 'PUT' }),
        broadcastAnnouncement: (title, message, priority) => 
            request('/api/admin/notifications/broadcast', { method: 'POST', body: JSON.stringify({ title, message, priority }) }),
        deleteNotification: (id) => request(`/api/admin/notifications/${id}`, { method: 'DELETE' }),
        getContactSubmissions: (status) => request(status ? `/api/admin/contact-submissions?status=${status}` : '/api/admin/contact-submissions'),
        updateContactStatus: (id, status, adminReply) => 
            request(`/api/admin/contact-submissions/${id}/status`, { method: 'PUT', body: JSON.stringify({ status, adminReply }) }),
        getMe: () => request('/api/admin/auth/me'),
        changePassword: (currentPassword, newPassword) => 
            request('/api/admin/auth/change-password', { method: 'PUT', body: JSON.stringify({ currentPassword, newPassword }) })
    };

    return {
        public: publicApi,
        admin: adminApi,
        setAuth,
        clearAuth,
        getAuthToken,
        getUser,
        isAuthenticated
    };
})();

// Global Toast System
function showToast(message, type = 'info', duration = 4000) {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    let icon = 'Ã¢â€žÂ¹Ã¯Â¸Â';
    if (type === 'success') icon = 'Ã¢Å“â€¦';
    if (type === 'error') icon = 'Ã¢ÂÅ’';
    if (type === 'warning') icon = 'Ã¢Å¡Â Ã¯Â¸Â';

    toast.innerHTML = `
        <span style="font-size: 1.25rem;">${icon}</span>
        <div style="flex-grow: 1; font-size: 0.9rem;">${message}</div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}
