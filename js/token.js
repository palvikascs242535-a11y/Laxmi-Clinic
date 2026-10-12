/**
 * LAXMI CLINIC - TOKEN & QUEUE MANAGEMENT (PATIENT SIDE)
 * Enhanced with Live Number Flip/Pop, Celebration Confetti & Visual Polishing
 */
const TokenManager = (() => {
    let pollingInterval = null;
    let myActiveToken = null;
    let lastKnownCurrentToken = null;

    function init() {
        setupTokenForm();
        setupTokenSearch();
        fetchLiveQueueStatus();
        startQueuePolling();
        loadActiveShifts();
    }

    async function loadActiveShifts() {
        const select = document.getElementById('tokenShiftSelect');
        if (!select) return;

        try {
            const res = await API.public.getActiveShifts();
            if (res.data && res.data.length > 0) {
                select.innerHTML = '<option value="">Select Preferred Shift</option>' + 
                    res.data.map(s => {
                        const isActive = s.active !== false;
                        if (isActive) {
                            return `<option value="${s.shiftNumber}">${s.name} (${s.startTime} - ${s.endTime})</option>`;
                        } else {
                            return `<option value="" disabled style="color:#e11d48; font-weight:700; background:#fff1f2;">🔴 Shift ${s.shiftNumber}: ${s.name} - INACTIVE / CLOSED</option>`;
                        }
                    }).join('');
            }
        } catch (err) {
            console.error('Failed to load shifts for token form:', err);
        }
    }

    async function fetchLiveQueueStatus() {
        try {
            const res = await API.public.getLiveQueueStatus();
            const data = res.data;
            if (!data) return;

            // Update Hero live display
            const curTokenEl = document.getElementById('heroCurrentToken');
            const curShiftEl = document.getElementById('heroCurrentShift');
            const waitingCountEl = document.getElementById('heroWaitingCount');
            const totalCountEl = document.getElementById('heroTotalCount');
            const clinicStatusEl = document.getElementById('heroClinicStatus');
            const heroBadgeBox = document.getElementById('heroBadgeContainer');
            const heroDot = document.getElementById('heroPulseDot');
            const queueBadge = document.getElementById('heroQueueLiveBadge');
            const queueSoundwave = document.getElementById('heroQueueSoundwave');
            const queueLiveText = document.getElementById('heroQueueLiveText');
            const tokenSubEl = document.querySelector('.current-token-sub');
            const noticeBannerEl = document.getElementById('noticeBannerContainer');
            const liveCard = document.getElementById('heroLiveTokenCard');

            // Clinic Operational Status (Open vs Closed)
            const isOperational = data.clinicOperational !== false;

            if (heroBadgeBox) {
                heroBadgeBox.classList.toggle('badge-closed', !isOperational);
            }
            if (heroDot) {
                heroDot.classList.toggle('dot-closed', !isOperational);
            }

            if (clinicStatusEl) {
                if (isOperational) {
                    clinicStatusEl.className = 'live-badge';
                    clinicStatusEl.innerHTML = '<div class="soundwave-anim"><span class="soundwave-bar"></span><span class="soundwave-bar"></span><span class="soundwave-bar"></span></div> <span>Clinic Open & Accepting Patients</span>';
                } else {
                    clinicStatusEl.className = 'live-badge badge-closed';
                    clinicStatusEl.innerHTML = `<div class="soundwave-anim soundwave-closed"><span class="soundwave-bar"></span><span class="soundwave-bar"></span><span class="soundwave-bar"></span></div> <span>🔴 Clinic Closed - ${data.statusMessage || 'Not Accepting OPD'}</span>`;
                }
            }

            if (queueBadge) {
                queueBadge.classList.toggle('badge-closed', !isOperational);
            }
            if (queueSoundwave) {
                queueSoundwave.classList.toggle('soundwave-closed', !isOperational);
            }
            if (queueLiveText) {
                queueLiveText.textContent = isOperational ? 'LIVE' : 'CLOSED';
            }

            if (tokenSubEl) {
                tokenSubEl.textContent = isOperational 
                    ? "Please proceed to Doctor's Consultation Room"
                    : "OPD is currently closed. Tokens will resume in next active shift.";
            }

            // Check if current token updated -> trigger visual pop
            if (curTokenEl) {
                if (!isOperational && (!data.currentTokenNumber || data.currentTokenNumber <= 0)) {
                    curTokenEl.textContent = 'CLOSED';
                } else {
                    const newNum = data.currentTokenNumber > 0 ? `#${data.currentTokenNumber}` : '--';
                    if (lastKnownCurrentToken !== null && lastKnownCurrentToken !== data.currentTokenNumber && data.currentTokenNumber > 0) {
                        curTokenEl.classList.remove('animate-pop');
                        void curTokenEl.offsetWidth; // trigger reflow
                        curTokenEl.classList.add('animate-pop');
                        if (liveCard) {
                            liveCard.style.boxShadow = '0 0 35px rgba(13, 148, 136, 0.45)';
                            setTimeout(() => { liveCard.style.boxShadow = ''; }, 1200);
                        }
                    }
                    curTokenEl.textContent = newNum;
                    lastKnownCurrentToken = data.currentTokenNumber;
                }
            }

            if (curShiftEl) curShiftEl.textContent = data.currentShiftName || (isOperational ? 'General' : 'Closed');
            if (waitingCountEl) waitingCountEl.textContent = data.waitingCount;
            if (totalCountEl) totalCountEl.textContent = data.totalTokensToday;

            if (noticeBannerEl && data.noticeBanner) {
                noticeBannerEl.innerHTML = `<div style="background:#fef3c7; color:#92400e; padding:0.85rem 1.25rem; border-radius:12px; margin-bottom:1.5rem; text-align:center; font-weight:700; border:1px solid #fde68a; box-shadow:0 4px 12px rgba(245, 158, 11, 0.15);">📢 ${data.noticeBanner}</div>`;
            }

            // Update my token tracker if active
            if (myActiveToken) {
                updateMyTokenDisplay(myActiveToken);
            }
        } catch (err) {
            console.warn('Queue live status poll error:', err);
        }
    }

    function startQueuePolling() {
        if (pollingInterval) clearInterval(pollingInterval);
        pollingInterval = setInterval(fetchLiveQueueStatus, 6000); // 6s poll
    }

    function setupTokenForm() {
        const form = document.getElementById('generateTokenForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = form.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;

            const name = document.getElementById('tokenPatientName').value.trim();
            const phone = document.getElementById('tokenPatientPhone').value.trim();
            const age = document.getElementById('tokenPatientAge').value;
            const gender = document.getElementById('tokenPatientGender').value;
            const shiftNumber = document.getElementById('tokenShiftSelect').value;
            const symptoms = document.getElementById('tokenSymptoms').value.trim();

            if (!name || !phone) {
                showToast('Please enter patient name and mobile number', 'warning');
                return;
            }

            try {
                btn.disabled = true;
                btn.innerHTML = '⚡ Generating Your Token...';

                const res = await API.public.generateToken({
                    patientName: name,
                    patientPhone: phone,
                    age: age ? parseInt(age) : null,
                    gender: gender || null,
                    shiftNumber: shiftNumber ? parseInt(shiftNumber) : null,
                    symptoms: symptoms || null
                });

                myActiveToken = res.data;
                showToast('🎉 Token #' + res.data.tokenNumber + ' generated successfully!', 'success');
                form.reset();

                // Open Token Slip Modal with Celebration Burst
                renderTokenSlip(res.data, true);
                fetchLiveQueueStatus();
            } catch (err) {
                showToast(err.message || 'Failed to generate token', 'error');
            } finally {
                btn.disabled = false;
                btn.innerHTML = originalText;
            }
        });
    }

    function setupTokenSearch() {
        const searchForm = document.getElementById('tokenSearchForm');
        if (!searchForm) return;

        searchForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const query = document.getElementById('tokenSearchInput').value.trim();
            if (!query) return;

            try {
                // If numeric, check token number
                if (/^\d{1,4}$/.test(query)) {
                    const res = await API.public.checkToken(parseInt(query));
                    myActiveToken = res.data;
                    renderTokenSlip(res.data, false);
                } else {
                    // Mobile search
                    const res = await API.public.getMyTokens(query);
                    if (res.data && res.data.length > 0) {
                        myActiveToken = res.data[0];
                        renderTokenSlip(res.data[0], false);
                    } else {
                        showToast('No tokens found for mobile ' + query + ' today', 'info');
                    }
                }
            } catch (err) {
                showToast(err.message || 'Token not found', 'error');
            }
        });
    }

    /* ----------------------------------------------------
       CELEBRATION CONFETTI BURST
    ---------------------------------------------------- */
    function launchConfetti(container) {
        const confettiBox = document.createElement('div');
        confettiBox.className = 'confetti-container';
        
        const colors = ['#0d9488', '#10b981', '#0284c7', '#f59e0b', '#ec4899', '#8b5cf6'];
        const numPieces = 40;

        for (let i = 0; i < numPieces; i++) {
            const piece = document.createElement('div');
            piece.className = 'confetti-piece';
            
            const color = colors[Math.floor(Math.random() * colors.length)];
            const angle = Math.random() * Math.PI * 2;
            const velocity = 80 + Math.random() * 180;
            const tx = Math.cos(angle) * velocity;
            const ty = Math.sin(angle) * velocity;
            const rot = (Math.random() * 720 - 360) + 'deg';

            piece.style.backgroundColor = color;
            piece.style.setProperty('--tx', `${tx}px`);
            piece.style.setProperty('--ty', `${ty}px`);
            piece.style.setProperty('--tr', rot);
            piece.style.width = `${6 + Math.random() * 8}px`;
            piece.style.height = `${8 + Math.random() * 10}px`;

            confettiBox.appendChild(piece);
        }

        container.appendChild(confettiBox);
        setTimeout(() => confettiBox.remove(), 1500);
    }

    function renderTokenSlip(token, isCelebration = false) {
        const modal = document.getElementById('tokenSlipModal');
        const content = document.getElementById('tokenSlipContent');
        if (!modal || !content) return;

        let statusBadgeClass = 'badge-waiting';
        if (token.status === 'CALLED') statusBadgeClass = 'badge-called';
        if (token.status === 'COMPLETED') statusBadgeClass = 'badge-completed';
        if (token.status === 'SKIPPED') statusBadgeClass = 'badge-skipped';

        content.innerHTML = `
            <div class="token-slip-card" id="printableTokenSlip">
                <div class="token-slip-badge">🩺 Laxmi Clinic OPD Token</div>
                <div style="font-size: 0.88rem; font-weight: 700; color: #64748b; margin-bottom: 0.25rem; text-transform: uppercase; letter-spacing: 0.05em;">YOUR TOKEN NUMBER</div>
                <div class="token-slip-number">#${token.tokenNumber}</div>
                
                <div style="margin: 0.75rem 0;">
                    <span class="badge ${statusBadgeClass}" style="font-size: 1rem; padding: 0.45rem 1.25rem;">
                        ${token.status}
                    </span>
                </div>

                <p style="font-weight: 700; color: #0d9488; margin: 0.5rem 0; font-size: 1rem;">${token.statusMessage || ''}</p>

                <div class="token-slip-details">
                    <div class="token-slip-row">
                        <span class="label">Patient Name:</span>
                        <span class="val">${token.patientName}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Mobile Number:</span>
                        <span class="val">${token.patientPhone}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Consultation Shift:</span>
                        <span class="val">${token.shiftName || 'General OPD'}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Token Date:</span>
                        <span class="val">${token.tokenDate}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Currently Serving:</span>
                        <span class="val" style="color: #0d9488; font-weight: 800; font-size: 1.1rem;">${token.currentTokenNumber > 0 ? '#' + token.currentTokenNumber : 'Starting Soon'}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Patients Ahead:</span>
                        <span class="val" style="color: #b45309; font-weight: 800; font-size: 1.1rem;">${token.patientsAhead}</span>
                    </div>
                    <div class="token-slip-row">
                        <span class="label">Est. Wait Time:</span>
                        <span class="val">~${token.estimatedWaitMinutes} Mins</span>
                    </div>
                </div>

                <div style="display: flex; gap: 0.75rem; justify-content: center; margin-top: 1.75rem; flex-wrap: wrap;">
                    <button class="btn btn-primary btn-sm" onclick="window.print()">🖨️ Print Slip</button>
                    <button class="btn btn-outline btn-sm" onclick="TokenManager.refreshMyToken()">🔄 Refresh Status</button>
                    <button class="btn btn-secondary btn-sm" onclick="TokenManager.closeSlipModal()">Close</button>
                </div>
            </div>
        `;

        modal.classList.add('active');

        if (isCelebration) {
            const card = document.getElementById('printableTokenSlip');
            if (card) launchConfetti(card);
        }
    }

    function closeSlipModal() {
        const modal = document.getElementById('tokenSlipModal');
        if (modal) modal.classList.remove('active');
    }

    async function refreshMyToken() {
        if (!myActiveToken) return;
        try {
            const res = await API.public.checkToken(myActiveToken.tokenNumber, myActiveToken.tokenDate);
            myActiveToken = res.data;
            renderTokenSlip(res.data, false);
            showToast('Token status refreshed!', 'info');
        } catch (err) {
            showToast('Failed to refresh token', 'error');
        }
    }

    function updateMyTokenDisplay(token) {
        const banner = document.getElementById('myTokenFloatingBanner');
        if (!banner) return;

        if (token && (token.status === 'WAITING' || token.status === 'CALLED')) {
            banner.style.display = 'block';
            banner.innerHTML = `
                <div class="floating-token-inner">
                    <div class="floating-token-left">
                        <span class="floating-token-badge">Token #${token.tokenNumber}</span>
                        <div>
                            <strong>Currently Serving: #${token.currentTokenNumber > 0 ? token.currentTokenNumber : '--'}</strong> &bull; 
                            Patients Ahead: <strong>${token.patientsAhead}</strong> &bull; 
                            Status: <span class="badge badge-${token.status.toLowerCase()}">${token.status}</span>
                        </div>
                    </div>
                    <button class="btn btn-sm btn-primary" onclick="TokenManager.renderTokenSlip(TokenManager.getMyActiveToken(), false)">🎟️ View Live Slip</button>
                </div>
            `;
        } else {
            banner.style.display = 'none';
        }
    }

    return {
        init,
        fetchLiveQueueStatus,
        renderTokenSlip,
        closeSlipModal,
        refreshMyToken,
        getMyActiveToken: () => myActiveToken
    };
})();

