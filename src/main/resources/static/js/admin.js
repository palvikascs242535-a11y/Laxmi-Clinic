/**
 * LAXMI CLINIC - ADMIN & DOCTOR DASHBOARD CONTROLLER
 */
document.addEventListener('DOMContentLoaded', () => {
    AdminApp.init();
});

const AdminApp = (() => {
    let currentTab = 'overview';
    let queuePollTimer = null;
    let selectedVisitId = null;

    function init() {
        if (!API.isAuthenticated()) {
            window.location.href = '/index.html';
            return;
        }

        setupAuthUser();
        setupNavigation();
        setupQueueControls();
        setupSiteInfoForm();
        setupHomeVisitModal();
        setupNotificationsDrawer();
        setupSettingsForm();

        // Load initial tab
        handleHashChange();
        window.addEventListener('hashchange', handleHashChange);

        // Start queue polling
        startQueuePolling();
    }

    function setupAuthUser() {
        const user = API.getUser();
        if (user) {
            const nameEls = document.querySelectorAll('.admin-user-name');
            nameEls.forEach(el => el.textContent = user.fullName || user.username);
        }

        const logoutBtn = document.getElementById('adminLogoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                API.clearAuth();
                showToast('Logged out successfully', 'info');
                setTimeout(() => window.location.href = '/index.html', 500);
            });
        }
    }

    function setupNavigation() {
        const sidebar = document.getElementById('adminSidebar');
        const sidebarToggle = document.getElementById('adminSidebarToggle');
        const sidebarBackdrop = document.getElementById('adminSidebarBackdrop');

        function toggleMobileSidebar() {
            if (!sidebar) return;
            const isOpen = sidebar.classList.toggle('mobile-open');
            if (sidebarBackdrop) {
                sidebarBackdrop.classList.toggle('active', isOpen);
            }
        }

        function closeMobileSidebar() {
            if (!sidebar) return;
            sidebar.classList.remove('mobile-open');
            if (sidebarBackdrop) {
                sidebarBackdrop.classList.remove('active');
            }
        }

        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleMobileSidebar();
            });
        }

        if (sidebarBackdrop) {
            sidebarBackdrop.addEventListener('click', closeMobileSidebar);
        }

        const links = document.querySelectorAll('.sidebar-link[data-tab]');
        links.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const tab = link.getAttribute('data-tab');
                window.location.hash = tab;
                closeMobileSidebar();
            });
        });
    }

    function handleHashChange() {
        const hash = window.location.hash.replace('#', '') || 'overview';
        currentTab = hash;

        // Update active sidebar link
        document.querySelectorAll('.sidebar-link').forEach(link => {
            link.classList.toggle('active', link.getAttribute('data-tab') === currentTab);
        });

        // Hide all views, show active
        document.querySelectorAll('.admin-view').forEach(view => {
            view.style.display = view.id === `view-${currentTab}` ? 'block' : 'none';
        });

        const viewTitle = document.getElementById('currentViewTitle');
        if (viewTitle) {
            const formatted = currentTab.charAt(0).toUpperCase() + currentTab.slice(1);
            viewTitle.textContent = formatted === 'Overview' ? 'Dashboard Overview' : formatted;
        }

        // Trigger view load
        switch (currentTab) {
            case 'overview':
                loadDashboardStats();
                loadLiveQueueBoard();
                break;
            case 'queue':
                loadLiveQueueBoard();
                loadTokensTable();
                break;
            case 'homevisits':
                loadHomeVisitsTable();
                break;
            case 'shifts':
                loadShiftsList();
                break;
            case 'siteinfo':
                loadSiteInfoSettings();
                break;
            case 'inquiries':
                loadInquiriesTable();
                break;
            case 'notifications':
                loadNotificationsList();
                break;
        }
    }

    function startQueuePolling() {
        if (queuePollTimer) clearInterval(queuePollTimer);
        queuePollTimer = setInterval(() => {
            if (currentTab === 'overview' || currentTab === 'queue') {
                loadDashboardStats();
                loadLiveQueueBoard();
                if (currentTab === 'queue') loadTokensTable();
            }
            updateUnreadBadge();
        }, 5000);
    }

    /* ----------------------------------------------------
       DASHBOARD OVERVIEW & LIVE METRICS
    ---------------------------------------------------- */
    async function loadDashboardStats() {
        try {
            const res = await API.admin.getDashboardStats();
            const stats = res.data;
            if (!stats) return;

            document.getElementById('statTotalTokens').textContent = stats.todayTotalTokens;
            document.getElementById('statCurrentToken').textContent = stats.currentTokenNumber > 0 ? `#${stats.currentTokenNumber}` : '--';
            document.getElementById('statWaitingPatients').textContent = stats.waitingPatientsCount;
            document.getElementById('statCompletedPatients').textContent = stats.completedPatientsCount;
            document.getElementById('statSkippedPatients').textContent = stats.skippedPatientsCount;
            document.getElementById('statPendingHomeVisits').textContent = stats.pendingHomeVisitsCount;
            document.getElementById('statAcceptedVisitsToday').textContent = stats.acceptedHomeVisitsTodayCount;

            const operationalBadge = document.getElementById('statOperationalStatus');
            if (operationalBadge) {
                operationalBadge.className = stats.clinicOperational ? 'badge badge-completed' : 'badge badge-cancelled';
                operationalBadge.textContent = stats.clinicOperational ? 'Clinic Open' : 'Clinic Closed';
            }

            // Update unread notification count
            const notifBadge = document.getElementById('adminNotifBadge');
            if (notifBadge) {
                notifBadge.textContent = stats.unreadNotificationsCount;
                notifBadge.style.display = stats.unreadNotificationsCount > 0 ? 'inline-flex' : 'none';
            }
        } catch (err) {
            console.error('Failed to load dashboard stats:', err);
        }
    }

    async function updateUnreadBadge() {
        try {
            const res = await API.admin.getNotifications();
            const unread = res.data.filter(n => !n.isRead).length;
            const notifBadge = document.getElementById('adminNotifBadge');
            if (notifBadge) {
                notifBadge.textContent = unread;
                notifBadge.style.display = unread > 0 ? 'inline-flex' : 'none';
            }
        } catch (err) {}
    }

    /* ----------------------------------------------------
       QUEUE CONTROLLER (HIGHEST PRIORITY FEATURE)
    ---------------------------------------------------- */
    function setupQueueControls() {
        const btnCallNext = document.getElementById('btnCallNextToken');
        if (btnCallNext) {
            btnCallNext.addEventListener('click', async () => {
                try {
                    const res = await API.admin.callNextToken();
                    showToast(`Token #${res.data.tokenNumber} (${res.data.patientName}) CALLED!`, 'success');
                    loadLiveQueueBoard();
                    loadTokensTable();
                    loadDashboardStats();
                } catch (err) {
                    showToast(err.message || 'No waiting patients in queue', 'warning');
                }
            });
        }

        const filterSelect = document.getElementById('queueStatusFilter');
        if (filterSelect) {
            filterSelect.addEventListener('change', () => loadTokensTable());
        }
    }

    async function loadLiveQueueBoard() {
        try {
            const res = await API.public.getLiveQueueStatus();
            const data = res.data;
            if (!data) return;

            const bigNum = document.getElementById('operatorCurrentTokenNum');
            const patientName = document.getElementById('operatorCurrentPatientName');
            const shiftName = document.getElementById('operatorCurrentShift');

            if (bigNum) {
                const newText = data.currentTokenNumber > 0 ? `#${data.currentTokenNumber}` : '--';
                if (bigNum.textContent !== newText && data.currentTokenNumber > 0) {
                    bigNum.classList.remove('animate-pop');
                    void bigNum.offsetWidth;
                    bigNum.classList.add('animate-pop');
                }
                bigNum.textContent = newText;
            }
            if (patientName) patientName.textContent = data.currentTokenNumber > 0 ? data.currentPatientMaskedName : 'No Active Patient';
            if (shiftName) shiftName.textContent = `Shift: ${data.currentShiftName || 'General'}`;
        } catch (err) {
            console.error('Failed to load queue board:', err);
        }
    }

    async function loadTokensTable() {
        const tbody = document.getElementById('tokensTableBody');
        if (!tbody) return;

        const filter = document.getElementById('queueStatusFilter') ? document.getElementById('queueStatusFilter').value : '';

        try {
            const res = await API.admin.getTodayTokens(null, filter || null);
            if (!res.data || res.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align:center; padding:2rem; color:#64748b;">No tokens recorded for today.</td></tr>';
                return;
            }

            tbody.innerHTML = res.data.map(token => {
                let badgeClass = 'badge-waiting';
                let rowClass = '';
                if (token.status === 'CALLED') {
                    badgeClass = 'badge-called';
                    rowClass = 'row-pulse';
                }
                if (token.status === 'COMPLETED') badgeClass = 'badge-completed';
                if (token.status === 'SKIPPED') badgeClass = 'badge-skipped';

                return `
                    <tr class="${rowClass}">
                        <td><strong style="font-size:1.15rem; color:#0d9488;">#${token.tokenNumber}</strong></td>
                        <td>
                            <strong>${token.patientName}</strong>
                            <div style="font-size:0.8rem; color:#64748b;">${token.gender || 'N/A'}, Age: ${token.age || 'N/A'}</div>
                        </td>
                        <td>${token.patientPhone}</td>
                        <td><span class="badge ${badgeClass}">${token.status}</span></td>
                        <td>${token.shiftName || 'General'}</td>
                        <td>${token.symptoms || '<span style="color:#94a3b8;">None</span>'}</td>
                        <td class="actions-cell">
                            ${token.status === 'WAITING' ? `
                                <button class="btn btn-primary btn-sm" onclick="AdminApp.callToken('${token.id}')">Call</button>
                                <button class="btn btn-warning btn-sm" onclick="AdminApp.skipToken('${token.id}')">Skip</button>
                            ` : ''}
                            ${token.status === 'CALLED' ? `
                                <button class="btn btn-accent btn-sm" onclick="AdminApp.completeToken('${token.id}')">Mark Complete</button>
                                <button class="btn btn-warning btn-sm" onclick="AdminApp.skipToken('${token.id}')">Skip</button>
                            ` : ''}
                            ${token.status === 'SKIPPED' || token.status === 'COMPLETED' ? `
                                <button class="btn btn-outline btn-sm" onclick="AdminApp.recallToken('${token.id}')">Recall</button>
                            ` : ''}
                        </td>
                    </tr>
                `;
            }).join('');
        } catch (err) {
            tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">Failed to load tokens: ${err.message}</td></tr>`;
        }
    }

    async function callToken(id) {
        try {
            const res = await API.admin.callSpecificToken(id);
            showToast(`Calling Token #${res.data.tokenNumber}`, 'success');
            loadLiveQueueBoard();
            loadTokensTable();
            loadDashboardStats();
        } catch (err) {
            showToast(err.message || 'Failed to call token', 'error');
        }
    }

    async function completeToken(id) {
        try {
            const res = await API.admin.markComplete(id);
            showToast(`Token #${res.data.tokenNumber} marked as COMPLETED`, 'success');
            loadLiveQueueBoard();
            loadTokensTable();
            loadDashboardStats();
        } catch (err) {
            showToast(err.message || 'Failed to complete token', 'error');
        }
    }

    async function skipToken(id) {
        try {
            const res = await API.admin.skipToken(id);
            showToast(`Token #${res.data.tokenNumber} marked as SKIPPED`, 'info');
            loadLiveQueueBoard();
            loadTokensTable();
            loadDashboardStats();
        } catch (err) {
            showToast(err.message || 'Failed to skip token', 'error');
        }
    }

    async function recallToken(id) {
        try {
            const res = await API.admin.recallToken(id);
            showToast(`Token #${res.data.tokenNumber} recalled to queue`, 'success');
            loadLiveQueueBoard();
            loadTokensTable();
            loadDashboardStats();
        } catch (err) {
            showToast(err.message || 'Failed to recall token', 'error');
        }
    }

    /* ----------------------------------------------------
       HOME VISITS MANAGEMENT
    ---------------------------------------------------- */
    async function loadHomeVisitsTable() {
        const tbody = document.getElementById('homeVisitsTableBody');
        if (!tbody) return;

        const statusFilter = document.getElementById('hvStatusFilter') ? document.getElementById('hvStatusFilter').value : '';

        try {
            const res = await API.admin.getHomeVisits(statusFilter || null);
            if (!res.data || res.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align:center; padding:2rem; color:#64748b;">No home visit requests found.</td></tr>';
                return;
            }

            tbody.innerHTML = res.data.map(visit => {
                let badgeClass = 'badge-pending';
                if (visit.status === 'ACCEPTED') badgeClass = 'badge-accepted';
                if (visit.status === 'REJECTED') badgeClass = 'badge-rejected';
                if (visit.status === 'COMPLETED') badgeClass = 'badge-completed';
                if (visit.status === 'CANCELLED') badgeClass = 'badge-cancelled';

                return `
                    <tr>
                        <td>
                            <strong>${visit.patientName}</strong>
                            <div style="font-size:0.8rem; color:#64748b;">📞 ${visit.mobile}</div>
                        </td>
                        <td>${visit.preferredDate}</td>
                        <td>${visit.preferredTimeSlot}</td>
                        <td>
                            <div style="max-width:220px; font-size:0.85rem;">
                                ${visit.address} (PIN: ${visit.pincode})
                                ${visit.landmark ? `<br><small style="color:#64748b;">Near ${visit.landmark}</small>` : ''}
                            </div>
                        </td>
                        <td><span class="badge ${badgeClass}">${visit.status}</span></td>
                        <td><small>${visit.reason || 'Routine'}</small></td>
                        <td class="actions-cell">
                            <button class="btn btn-outline btn-sm" onclick="AdminApp.openHomeVisitModal('${visit.id}')">Manage</button>
                        </td>
                    </tr>
                `;
            }).join('');
        } catch (err) {
            tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:red;">Failed to load home visits: ${err.message}</td></tr>`;
        }
    }

    function setupHomeVisitModal() {
        const modal = document.getElementById('homeVisitModal');
        const form = document.getElementById('homeVisitActionForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!selectedVisitId) return;

            const status = document.getElementById('modalHvStatus').value;
            const adminRemark = document.getElementById('modalHvRemark').value.trim();
            const doctorNotes = document.getElementById('modalHvDoctorNotes').value.trim();

            try {
                await API.admin.updateHomeVisitStatus(selectedVisitId, status, adminRemark, doctorNotes);
                showToast('Home visit updated successfully', 'success');
                closeHomeVisitModal();
                loadHomeVisitsTable();
                loadDashboardStats();
            } catch (err) {
                showToast(err.message || 'Failed to update home visit', 'error');
            }
        });
    }

    async function openHomeVisitModal(id) {
        selectedVisitId = id;
        const modal = document.getElementById('homeVisitModal');
        if (!modal) return;

        try {
            const res = await API.admin.getHomeVisits();
            const visit = res.data.find(v => v.id === id);
            if (!visit) return;

            document.getElementById('modalHvPatient').textContent = `${visit.patientName} (${visit.mobile})`;
            document.getElementById('modalHvSchedule').textContent = `${visit.preferredDate} - ${visit.preferredTimeSlot}`;
            document.getElementById('modalHvAddress').textContent = `${visit.address}, PIN: ${visit.pincode}`;
            document.getElementById('modalHvStatus').value = visit.status;
            document.getElementById('modalHvRemark').value = visit.adminRemark || '';
            document.getElementById('modalHvDoctorNotes').value = visit.doctorNotes || '';

            modal.classList.add('active');
        } catch (err) {
            showToast('Failed to open details', 'error');
        }
    }

    function closeHomeVisitModal() {
        const modal = document.getElementById('homeVisitModal');
        if (modal) modal.classList.remove('active');
    }

    /* ----------------------------------------------------
       5 SHIFTS MANAGEMENT
    ---------------------------------------------------- */
    async function loadShiftsList() {
        const container = document.getElementById('adminShiftsContainer');
        if (!container) return;

        try {
            const res = await API.admin.getAllShifts();
            if (!res.data || res.data.length === 0) {
                container.innerHTML = '<p>No shifts configured.</p>';
                return;
            }

            container.innerHTML = res.data.map(shift => `
                <div class="card" style="margin-bottom:1.5rem; padding:1.5rem;">
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem; border-bottom:1px solid #e2e8f0; padding-bottom:0.75rem;">
                        <div style="display:flex; align-items:center; gap:0.75rem;">
                            <span class="shift-num-badge">${shift.shiftNumber}</span>
                            <h4 style="margin:0;">Shift #${shift.shiftNumber}: ${shift.name}</h4>
                        </div>
                        <div style="display:flex; align-items:center; gap:1rem;">
                            <label class="switch">
                                <input type="checkbox" ${shift.active ? 'checked' : ''} onchange="AdminApp.toggleShiftActive(${shift.shiftNumber}, this.checked)">
                                <span class="slider"></span>
                            </label>
                            <span style="font-weight:600; font-size:0.85rem; color:${shift.active ? '#0d9488' : '#94a3b8'};">
                                ${shift.active ? 'ACTIVE' : 'INACTIVE'}
                            </span>
                        </div>
                    </div>

                    <form onsubmit="AdminApp.saveShift(event, ${shift.shiftNumber})" class="form-grid-3">
                        <div class="form-group">
                            <label class="form-label">Shift Name</label>
                            <input type="text" class="form-control" id="shiftName_${shift.shiftNumber}" value="${shift.name}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Start Time</label>
                            <input type="text" class="form-control" id="shiftStart_${shift.shiftNumber}" value="${shift.startTime}" placeholder="e.g. 09:00 AM" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">End Time</label>
                            <input type="text" class="form-control" id="shiftEnd_${shift.shiftNumber}" value="${shift.endTime}" placeholder="e.g. 11:30 AM" required>
                        </div>
                        <div class="form-group" style="grid-column: 1 / 3;">
                            <label class="form-label">Description / Clinic Area</label>
                            <input type="text" class="form-control" id="shiftDesc_${shift.shiftNumber}" value="${shift.description || ''}">
                        </div>
                        <div class="form-group" style="display:flex; align-items:flex-end;">
                            <button type="submit" class="btn btn-primary btn-sm" style="width:100%;">Save Shift #${shift.shiftNumber}</button>
                        </div>
                    </form>
                </div>
            `).join('');
        } catch (err) {
            container.innerHTML = `<p style="color:red;">Failed to load shifts: ${err.message}</p>`;
        }
    }

    async function toggleShiftActive(shiftNum, active) {
        try {
            await API.admin.toggleShiftActive(shiftNum, active);
            showToast(`Shift #${shiftNum} active status set to ${active ? 'Active' : 'Inactive'}`, 'success');
            loadShiftsList();
        } catch (err) {
            showToast('Failed to toggle shift status', 'error');
        }
    }

    async function saveShift(e, shiftNum) {
        e.preventDefault();
        const name = document.getElementById(`shiftName_${shiftNum}`).value.trim();
        const startTime = document.getElementById(`shiftStart_${shiftNum}`).value.trim();
        const endTime = document.getElementById(`shiftEnd_${shiftNum}`).value.trim();
        const description = document.getElementById(`shiftDesc_${shiftNum}`).value.trim();

        try {
            await API.admin.updateShift(shiftNum, {
                shiftNumber: shiftNum,
                name,
                startTime,
                endTime,
                description,
                active: true
            });
            showToast(`Shift #${shiftNum} updated successfully!`, 'success');
            loadShiftsList();
        } catch (err) {
            showToast(err.message || 'Failed to update shift', 'error');
        }
    }

    /* ----------------------------------------------------
       SITE INFO & OPERATIONAL STATUS SETTINGS
    ---------------------------------------------------- */
    async function loadSiteInfoSettings() {
        try {
            const res = await API.admin.getSiteInfo();
            const info = res.data;
            if (!info) return;

            document.getElementById('settingClinicName').value = info.clinicName || '';
            document.getElementById('settingTagline').value = info.tagline || '';
            document.getElementById('settingDoctorName').value = info.doctorName || '';
            document.getElementById('settingDoctorQual').value = info.doctorQualification || '';
            document.getElementById('settingDoctorSpecialty').value = info.doctorSpecialty || '';
            document.getElementById('settingDoctorExp').value = info.doctorExperience || '';
            document.getElementById('settingDoctorBio').value = info.doctorBio || '';
            document.getElementById('settingStreet').value = info.streetAddress || '';
            document.getElementById('settingCity').value = info.city || '';
            document.getElementById('settingState').value = info.state || '';
            document.getElementById('settingPincode').value = info.pincode || '';
            document.getElementById('settingPhone').value = info.primaryPhone || '';
            document.getElementById('settingEmergency').value = info.emergencyPhone || '';
            document.getElementById('settingEmail').value = info.email || '';
            document.getElementById('settingTimings').value = info.generalTimings || '';
            document.getElementById('settingAbout').value = info.aboutDescription || '';
            document.getElementById('settingNoticeBanner').value = info.noticeBanner || '';
            document.getElementById('settingOperational').checked = info.operational;
            document.getElementById('settingOperationalMsg').value = info.operationalStatusMessage || '';
        } catch (err) {
            showToast('Failed to load clinic settings', 'error');
        }
    }

    function setupSiteInfoForm() {
        const form = document.getElementById('siteInfoForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = form.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;

            const payload = {
                clinicName: document.getElementById('settingClinicName').value.trim(),
                tagline: document.getElementById('settingTagline').value.trim(),
                doctorName: document.getElementById('settingDoctorName').value.trim(),
                doctorQualification: document.getElementById('settingDoctorQual').value.trim(),
                doctorSpecialty: document.getElementById('settingDoctorSpecialty').value.trim(),
                doctorExperience: document.getElementById('settingDoctorExp').value.trim(),
                doctorBio: document.getElementById('settingDoctorBio').value.trim(),
                streetAddress: document.getElementById('settingStreet').value.trim(),
                city: document.getElementById('settingCity').value.trim(),
                state: document.getElementById('settingState').value.trim(),
                pincode: document.getElementById('settingPincode').value.trim(),
                primaryPhone: document.getElementById('settingPhone').value.trim(),
                emergencyPhone: document.getElementById('settingEmergency').value.trim(),
                email: document.getElementById('settingEmail').value.trim(),
                generalTimings: document.getElementById('settingTimings').value.trim(),
                aboutDescription: document.getElementById('settingAbout').value.trim(),
                noticeBanner: document.getElementById('settingNoticeBanner').value.trim(),
                operational: document.getElementById('settingOperational').checked,
                operationalStatusMessage: document.getElementById('settingOperationalMsg').value.trim()
            };

            try {
                btn.disabled = true;
                btn.innerHTML = 'Saving Changes...';

                await API.admin.updateSiteInfo(payload);
                showToast('Clinic site information updated successfully!', 'success');
            } catch (err) {
                showToast(err.message || 'Failed to save site information', 'error');
            } finally {
                btn.disabled = false;
                btn.innerHTML = originalText;
            }
        });
    }

    /* ----------------------------------------------------
       NOTIFICATIONS & INQUIRIES
    ---------------------------------------------------- */
    function setupNotificationsDrawer() {
        const toggleBtn = document.getElementById('toggleNotifDrawerBtn');
        const drawer = document.getElementById('notificationDrawer');
        const closeBtn = document.getElementById('closeNotifDrawerBtn');
        const markAllBtn = document.getElementById('markAllNotifsReadBtn');

        if (toggleBtn && drawer) {
            toggleBtn.addEventListener('click', () => {
                drawer.classList.toggle('active');
                if (drawer.classList.contains('active')) {
                    loadNotificationsList();
                }
            });
        }

        if (closeBtn && drawer) {
            closeBtn.addEventListener('click', () => drawer.classList.remove('active'));
        }

        if (markAllBtn) {
            markAllBtn.addEventListener('click', async () => {
                try {
                    await API.admin.markAllNotificationsRead();
                    showToast('All notifications marked as read', 'info');
                    loadNotificationsList();
                    updateUnreadBadge();
                } catch (err) {
                    showToast('Failed to mark notifications', 'error');
                }
            });
        }
    }

    async function loadNotificationsList() {
        const container = document.getElementById('drawerNotifList');
        if (!container) return;

        try {
            const res = await API.admin.getNotifications();
            if (!res.data || res.data.length === 0) {
                container.innerHTML = '<p style="text-align:center; padding:1.5rem; color:#64748b;">No notifications.</p>';
                return;
            }

            container.innerHTML = res.data.map(n => `
                <div class="notif-item ${!n.isRead ? 'unread' : ''}">
                    <div class="notif-title">${n.title}</div>
                    <div style="font-size:0.85rem; color:#334155;">${n.message}</div>
                    <div class="notif-time">${new Date(n.createdAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})} - ${new Date(n.createdAt).toLocaleDateString()}</div>
                    ${!n.isRead ? `<button class="btn btn-sm btn-outline" style="margin-top:0.5rem; font-size:0.75rem; padding:0.2rem 0.5rem;" onclick="AdminApp.markNotifRead('${n.id}')">Mark Read</button>` : ''}
                </div>
            `).join('');
        } catch (err) {
            container.innerHTML = `<p style="color:red;">Failed to load notifications: ${err.message}</p>`;
        }
    }

    async function markNotifRead(id) {
        try {
            await API.admin.markNotificationRead(id);
            loadNotificationsList();
            updateUnreadBadge();
        } catch (err) {}
    }

    async function loadInquiriesTable() {
        const tbody = document.getElementById('inquiriesTableBody');
        if (!tbody) return;

        try {
            const res = await API.admin.getContactSubmissions();
            if (!res.data || res.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:2rem;">No patient inquiries recorded.</td></tr>';
                return;
            }

            tbody.innerHTML = res.data.map(inq => `
                <tr>
                    <td><strong>${inq.name}</strong></td>
                    <td>📞 ${inq.mobile} ${inq.email ? `<br><small>${inq.email}</small>` : ''}</td>
                    <td>${inq.subject}</td>
                    <td><small>${inq.message}</small></td>
                    <td><span class="badge ${inq.status === 'RESOLVED' ? 'badge-completed' : 'badge-pending'}">${inq.status}</span></td>
                    <td class="actions-cell">
                        ${inq.status !== 'RESOLVED' ? `
                            <button class="btn btn-accent btn-sm" onclick="AdminApp.resolveInquiry('${inq.id}')">Resolve</button>
                        ` : '<span style="color:#10b981; font-weight:600;">✓ Done</span>'}
                    </td>
                </tr>
            `).join('');
        } catch (err) {
            tbody.innerHTML = `<tr><td colspan="6" style="color:red; text-align:center;">Failed to load inquiries: ${err.message}</td></tr>`;
        }
    }

    async function resolveInquiry(id) {
        try {
            await API.admin.updateContactStatus(id, 'RESOLVED', 'Resolved by clinic staff');
            showToast('Inquiry marked as resolved', 'success');
            loadInquiriesTable();
            loadDashboardStats();
        } catch (err) {
            showToast('Failed to update inquiry', 'error');
        }
    }

    function setupSettingsForm() {
        const form = document.getElementById('changePasswordForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const curPass = document.getElementById('settingCurrentPassword').value;
            const newPass = document.getElementById('settingNewPassword').value;
            const confPass = document.getElementById('settingConfirmPassword').value;

            if (newPass !== confPass) {
                showToast('New passwords do not match', 'warning');
                return;
            }

            try {
                await API.admin.changePassword(curPass, newPass);
                showToast('Password changed successfully!', 'success');
                form.reset();
            } catch (err) {
                showToast(err.message || 'Failed to change password', 'error');
            }
        });
    }

    return {
        init,
        callToken,
        completeToken,
        skipToken,
        recallToken,
        openHomeVisitModal,
        closeHomeVisitModal,
        toggleShiftActive,
        saveShift,
        markNotifRead,
        resolveInquiry
    };
})();
