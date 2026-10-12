/**
 * LAXMI CLINIC - HOME VISIT BOOKING & TRACKING (PATIENT SIDE)
 */
const HomeVisitManager = (() => {

    function init() {
        setupBookingForm();
        setupTrackingForm();
        loadAvailableSlots();
        setDefaultDate();
    }

    function setDefaultDate() {
        const dateInput = document.getElementById('hvPreferredDate');
        if (dateInput) {
            const today = new Date().toISOString().split('T')[0];
            dateInput.min = today;
            dateInput.value = today;
        }
    }

    async function loadAvailableSlots() {
        const slotSelect = document.getElementById('hvTimeSlot');
        if (!slotSelect) return;

        try {
            const res = await API.public.getAvailableSlots();
            if (res.data && res.data.length > 0) {
                slotSelect.innerHTML = '<option value="">Select Time Slot</option>' +
                    res.data.map(slot => `<option value="${slot}">${slot}</option>`).join('');
            }
        } catch (err) {
            console.error('Failed to load home visit slots:', err);
        }
    }

    function setupBookingForm() {
        const form = document.getElementById('homeVisitBookingForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = form.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;

            const name = document.getElementById('hvPatientName').value.trim();
            const mobile = document.getElementById('hvMobile').value.trim();
            const email = document.getElementById('hvEmail').value.trim();
            const address = document.getElementById('hvAddress').value.trim();
            const landmark = document.getElementById('hvLandmark').value.trim();
            const pincode = document.getElementById('hvPincode').value.trim();
            const preferredDate = document.getElementById('hvPreferredDate').value.trim();
            const preferredTimeSlot = document.getElementById('hvTimeSlot').value.trim();
            const reason = document.getElementById('hvReason').value.trim();

            if (!name || !mobile || !address || !pincode || !preferredDate || !preferredTimeSlot) {
                showToast('Please fill all required fields.', 'warning');
                return;
            }

            try {
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner"></span> Submitting Booking Request...';

                const res = await API.public.bookHomeVisit({
                    patientName: name,
                    mobile,
                    email: email || null,
                    address,
                    landmark: landmark || null,
                    pincode,
                    preferredDate,
                    preferredTimeSlot,
                    reason: reason || null
                });

                showToast('Home visit requested successfully! Our team will contact you.', 'success');
                form.reset();
                setDefaultDate();

                renderBookingConfirmation(res.data);
            } catch (err) {
                showToast(err.message || 'Failed to book home visit.', 'error');
            } finally {
                btn.disabled = false;
                btn.innerHTML = originalText;
            }
        });
    }

    function setupTrackingForm() {
        const form = document.getElementById('hvTrackForm');
        if (!form) return;

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const mobile = document.getElementById('hvTrackMobile').value.trim();
            const resultsContainer = document.getElementById('hvTrackResults');
            if (!mobile || !resultsContainer) return;

            try {
                resultsContainer.innerHTML = '<p style="text-align:center; padding:1rem;">Searching requests...</p>';
                const res = await API.public.trackHomeVisits(mobile);

                if (!res.data || res.data.length === 0) {
                    resultsContainer.innerHTML = `
                        <div style="background:#f8fafc; padding:1.5rem; border-radius:12px; text-align:center; border:1px solid #e2e8f0;">
                            <p style="margin:0; color:#64748b;">No home visit requests found for mobile: <strong>${mobile}</strong></p>
                        </div>
                    `;
                    return;
                }

                resultsContainer.innerHTML = res.data.map(visit => {
                    let badgeClass = 'badge-pending';
                    if (visit.status === 'ACCEPTED') badgeClass = 'badge-accepted';
                    if (visit.status === 'REJECTED') badgeClass = 'badge-rejected';
                    if (visit.status === 'COMPLETED') badgeClass = 'badge-completed';
                    if (visit.status === 'CANCELLED') badgeClass = 'badge-cancelled';

                    return `
                        <div class="card" style="margin-bottom:1rem; padding:1.25rem; border-left:4px solid var(--primary);">
                            <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:0.75rem;">
                                <div>
                                    <h4 style="margin:0 0 0.25rem 0;">${visit.patientName}</h4>
                                    <span style="font-size:0.85rem; color:#64748b;">📅 ${visit.preferredDate} | ⏰ ${visit.preferredTimeSlot}</span>
                                </div>
                                <span class="badge ${badgeClass}">${visit.status}</span>
                            </div>
                            <p style="font-size:0.85rem; margin-bottom:0.5rem; color:#475569;">📍 <strong>Address:</strong> ${visit.address} (PIN: ${visit.pincode})</p>
                            ${visit.reason ? `<p style="font-size:0.85rem; margin-bottom:0.5rem; color:#64748b;">📝 <strong>Reason:</strong> ${visit.reason}</p>` : ''}
                            ${visit.adminRemark ? `<div style="background:#f0fdfa; padding:0.6rem 0.8rem; border-radius:6px; font-size:0.85rem; color:#0d9488; margin-top:0.5rem;"><strong>Clinic Note:</strong> ${visit.adminRemark}</div>` : ''}
                        </div>
                    `;
                }).join('');
            } catch (err) {
                resultsContainer.innerHTML = `<p style="color:var(--danger); text-align:center;">Failed to load requests: ${err.message}</p>`;
            }
        });
    }

    function renderBookingConfirmation(visit) {
        const modal = document.getElementById('hvConfirmationModal');
        const content = document.getElementById('hvConfirmationContent');
        if (!modal || !content) return;

        content.innerHTML = `
            <div style="text-align:center; padding:1rem 0;">
                <div style="font-size:3rem; margin-bottom:0.5rem;">🏠✅</div>
                <h3 style="color:#0f172a; margin-bottom:0.5rem;">Home Visit Request Received!</h3>
                <p style="color:#64748b; font-size:0.95rem; margin-bottom:1.5rem;">
                    Thank you, <strong>${visit.patientName}</strong>. Your home visit request has been recorded with status <span class="badge badge-pending">PENDING</span>.
                </p>

                <div class="token-slip-details" style="text-align:left;">
                    <div class="token-slip-row"><span class="label">Date:</span><span class="val">${visit.preferredDate}</span></div>
                    <div class="token-slip-row"><span class="label">Time Slot:</span><span class="val">${visit.preferredTimeSlot}</span></div>
                    <div class="token-slip-row"><span class="label">Mobile:</span><span class="val">${visit.mobile}</span></div>
                    <div class="token-slip-row"><span class="label">Address:</span><span class="val">${visit.address}</span></div>
                </div>

                <p style="font-size:0.85rem; color:#0d9488; font-weight:600; margin-top:1rem;">
                    Our clinic coordinator will call you to confirm doctor availability.
                </p>

                <div style="margin-top:1.5rem;">
                    <button class="btn btn-primary btn-sm" onclick="HomeVisitManager.closeConfirmationModal()">Got It</button>
                </div>
            </div>
        `;

        modal.classList.add('active');
    }

    function closeConfirmationModal() {
        const modal = document.getElementById('hvConfirmationModal');
        if (modal) modal.classList.remove('active');
    }

    return {
        init,
        closeConfirmationModal
    };
})();
