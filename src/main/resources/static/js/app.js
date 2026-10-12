/**
 * LAXMI CLINIC - MAIN PATIENT PORTAL SCRIPT
 * Enhanced with Scroll-triggered Reveals, 3D Tilt Physics & Counter Rollups
 */
document.addEventListener('DOMContentLoaded', () => {
    App.init();
});

const App = (() => {
    let clinicData = null;
    let observer = null;

    function init() {
        setupNavigation();
        setupNavbarScroll();
        setupScrollReveal();
        setup3DCardTilt();
        
        loadSiteInformation();
        loadShiftsSchedule();
        setupAdminLoginModal();
        
        // Initialize sub-managers
        TokenManager.init();
        HomeVisitManager.init();
    }

    /* ----------------------------------------------------
       SCROLL-TRIGGERED REVEAL & STAGGER OBSERVER
    ---------------------------------------------------- */
    function setupScrollReveal() {
        if (!('IntersectionObserver' in window)) {
            // Fallback for older browsers
            document.querySelectorAll('.reveal-init, .reveal-left, .reveal-right, .reveal-scale, .stagger-container').forEach(el => {
                el.classList.add('revealed');
            });
            return;
        }

        const options = {
            root: null,
            rootMargin: '0px 0px -50px 0px',
            threshold: 0.12
        };

        if (observer) observer.disconnect();

        observer = new IntersectionObserver((entries, obs) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('revealed');
                    
                    // Trigger counters inside this element
                    const counters = entry.target.querySelectorAll('.counter-val');
                    counters.forEach(c => runCounter(c));
                    if (entry.target.classList.contains('counter-val')) {
                        runCounter(entry.target);
                    }

                    obs.unobserve(entry.target);
                }
            });
        }, options);

        document.querySelectorAll('.reveal-init, .reveal-left, .reveal-right, .reveal-scale, .stagger-container').forEach(el => {
            observer.observe(el);
        });
    }

    /* ----------------------------------------------------
       ANIMATED NUMERIC COUNTER ROLLUP
    ---------------------------------------------------- */
    function runCounter(el) {
        if (el.dataset.counterDone === 'true') return;
        const target = parseInt(el.dataset.target || el.textContent.replace(/\D/g, ''), 10);
        if (isNaN(target) || target <= 0) return;
        
        el.dataset.counterDone = 'true';
        const suffix = el.dataset.suffix || '';
        const duration = 1400;
        const startTime = performance.now();

        function updateCount(currentTime) {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);
            // Easing: ease-out cubic
            const easeProgress = 1 - Math.pow(1 - progress, 3);
            const currentVal = Math.floor(easeProgress * target);

            el.textContent = `${currentVal}${suffix}`;

            if (progress < 1) {
                requestAnimationFrame(updateCount);
            } else {
                el.textContent = `${target}${suffix}`;
            }
        }

        requestAnimationFrame(updateCount);
    }

    /* ----------------------------------------------------
       INTERACTIVE 3D PERSPECTIVE CARD TILT
    ---------------------------------------------------- */
    function setup3DCardTilt() {
        const tiltElements = document.querySelectorAll('.live-token-card');
        tiltElements.forEach(card => {
            card.addEventListener('mousemove', (e) => {
                const rect = card.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                
                const centerX = rect.width / 2;
                const centerY = rect.height / 2;
                
                const rotateX = ((y - centerY) / centerY) * -5;
                const rotateY = ((x - centerX) / centerX) * 5;

                card.style.transform = `perspective(1000px) rotateX(${rotateX.toFixed(2)}deg) rotateY(${rotateY.toFixed(2)}deg) translateY(-4px)`;
            });

            card.addEventListener('mouseleave', () => {
                card.style.transform = '';
            });
        });
    }

    /* ----------------------------------------------------
       NAVBAR SCROLL ELEVATION
    ---------------------------------------------------- */
    function setupNavbarScroll() {
        const navbar = document.querySelector('.navbar');
        if (!navbar) return;

        window.addEventListener('scroll', () => {
            if (window.scrollY > 20) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        }, { passive: true });
    }

    function setupNavigation() {
        const mobileToggle = document.getElementById('mobileMenuToggle');
        const navMenu = document.getElementById('navMenu');

        if (mobileToggle && navMenu) {
            mobileToggle.addEventListener('click', (e) => {
                e.stopPropagation();
                navMenu.classList.toggle('active');
                mobileToggle.textContent = navMenu.classList.contains('active') ? '✕' : '☰';
            });

            // Close mobile menu on outside click
            document.addEventListener('click', (e) => {
                if (navMenu.classList.contains('active') && !navMenu.contains(e.target) && e.target !== mobileToggle) {
                    navMenu.classList.remove('active');
                    mobileToggle.textContent = '☰';
                }
            });
        }

        // Smooth scroll nav links & auto-close menu on link click
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                const href = this.getAttribute('href');
                if (href === '#' || href === '') return;
                const target = document.querySelector(href);
                if (target) {
                    e.preventDefault();
                    target.scrollIntoView({ behavior: 'smooth' });
                    if (navMenu && navMenu.classList.contains('active')) {
                        navMenu.classList.remove('active');
                        if (mobileToggle) mobileToggle.textContent = '☰';
                    }
                }
            });
        });
    }

    async function loadSiteInformation() {
        try {
            const res = await API.public.getSiteInfo();
            clinicData = res.data;
            if (!clinicData) return;

            // Populate all site text elements
            document.querySelectorAll('.clinic-name-text').forEach(el => el.textContent = clinicData.clinicName || 'Laxmi Clinic');
            document.querySelectorAll('.doctor-name-text').forEach(el => el.textContent = clinicData.doctorName || 'Dr. [Name]');
            document.querySelectorAll('.doctor-qualification-text').forEach(el => el.textContent = clinicData.doctorQualification || 'MBBS, MD');
            document.querySelectorAll('.doctor-specialty-text').forEach(el => el.textContent = clinicData.doctorSpecialty || 'General Physician');
            document.querySelectorAll('.doctor-experience-text').forEach(el => {
                el.textContent = clinicData.doctorExperience || '30+ Years Clinical Experience';
                const match = (clinicData.doctorExperience || '').match(/\d+/);
                if (match) el.dataset.target = match[0];
            });
            document.querySelectorAll('.doctor-bio-text').forEach(el => el.textContent = clinicData.doctorBio || '');
            document.querySelectorAll('.clinic-address-text').forEach(el => {
                el.textContent = `${clinicData.streetAddress}, ${clinicData.city}, ${clinicData.state} - ${clinicData.pincode}`;
            });
            document.querySelectorAll('.clinic-phone-text').forEach(el => el.textContent = clinicData.primaryPhone || '[Phone Number]');
            document.querySelectorAll('.clinic-email-text').forEach(el => el.textContent = clinicData.email || 'clinic@email.com');
            document.querySelectorAll('.clinic-timings-text').forEach(el => el.textContent = clinicData.generalTimings || '9:00 AM - 8:00 PM');
            document.querySelectorAll('.clinic-tagline-text').forEach(el => el.textContent = clinicData.tagline || '');
            document.querySelectorAll('.clinic-about-desc').forEach(el => el.textContent = clinicData.aboutDescription || '');

            // Clinic Operational Badge
            const heroBadgeBox = document.getElementById('heroBadgeContainer');
            const heroDot = document.getElementById('heroPulseDot');
            const clinicStatusEl = document.getElementById('heroClinicStatus');
            const isOperational = clinicData.operational !== false;

            if (heroBadgeBox) heroBadgeBox.classList.toggle('badge-closed', !isOperational);
            if (heroDot) heroDot.classList.toggle('dot-closed', !isOperational);
            if (clinicStatusEl) {
                if (isOperational) {
                    clinicStatusEl.className = 'live-badge';
                    clinicStatusEl.innerHTML = '<div class="soundwave-anim"><span class="soundwave-bar"></span><span class="soundwave-bar"></span><span class="soundwave-bar"></span></div> <span>Clinic Open & Accepting Patients</span>';
                } else {
                    clinicStatusEl.className = 'live-badge badge-closed';
                    clinicStatusEl.innerHTML = `<div class="soundwave-anim soundwave-closed"><span class="soundwave-bar"></span><span class="soundwave-bar"></span><span class="soundwave-bar"></span></div> <span>🔴 Clinic Closed - ${clinicData.operationalStatusMessage || 'Not Accepting OPD'}</span>`;
                }
            }

            // Highlights
            const highlightsContainer = document.getElementById('aboutHighlightsList');
            if (highlightsContainer && clinicData.highlights) {
                highlightsContainer.innerHTML = clinicData.highlights.map(h => `
                    <div style="display:flex; align-items:center; gap:0.75rem; padding:0.6rem 0; font-weight:600; color:#334155;">
                        <span style="color:#0d9488; font-size:1.25rem;">✔</span> ${h}
                    </div>
                `).join('');
            }
        } catch (err) {
            console.error('Failed to load clinic site info:', err);
        }
    }

    async function loadShiftsSchedule() {
        const container = document.getElementById('shiftsTimelineContainer');
        if (!container) return;

        try {
            const res = await API.public.getActiveShifts();
            if (!res.data || res.data.length === 0) {
                container.innerHTML = '<p style="text-align:center; grid-column:1/-1;">Shifts schedule being updated.</p>';
                return;
            }

            container.innerHTML = res.data.map(shift => {
                const isActive = shift.active !== false;
                const statusBadge = isActive 
                    ? `<span class="badge badge-active" style="font-size:0.75rem; padding:0.2rem 0.6rem; margin-top:0.35rem;">🟢 ACTIVE</span>`
                    : `<span class="badge badge-inactive" style="font-size:0.75rem; padding:0.2rem 0.6rem; margin-top:0.35rem;">🔴 INACTIVE / CLOSED</span>`;

                return `
                    <div class="shift-card stagger-item ${isActive ? 'active-shift' : 'inactive-shift'}">
                        <div class="shift-num-badge">${shift.shiftNumber}</div>
                        <div class="shift-info-mobile">
                            <div class="shift-name">${shift.name}</div>
                            <div class="shift-time">⏰ ${shift.startTime} - ${shift.endTime}</div>
                            <div style="margin-top:0.3rem;">${statusBadge}</div>
                            <p style="font-size:0.82rem; color:${isActive ? '#64748b' : '#9f1239'}; margin-top:0.35rem; margin-bottom:0;">
                                ${shift.description || (isActive ? 'General Consultations' : 'Shift Currently Closed')}
                            </p>
                        </div>
                    </div>
                `;
            }).join('');

            setupScrollReveal();
        } catch (err) {
            console.error('Failed to load shifts:', err);
        }
    }

    function setupAdminLoginModal() {
        const loginModal = document.getElementById('adminLoginModal');
        const loginTrigger = document.getElementById('adminLoginTrigger');
        const loginForm = document.getElementById('adminLoginForm');

        if (loginTrigger && loginModal) {
            loginTrigger.addEventListener('click', (e) => {
                e.preventDefault();
                if (API.isAuthenticated()) {
                    window.location.href = '/admin.html';
                } else {
                    loginModal.classList.add('active');
                }
            });
        }

        if (loginForm) {
            loginForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const username = document.getElementById('adminUsername').value.trim();
                const password = document.getElementById('adminPassword').value;
                const btn = loginForm.querySelector('button[type="submit"]');
                const originalText = btn.innerHTML;

                try {
                    btn.disabled = true;
                    btn.innerHTML = 'Verifying Credentials...';

                    const res = await API.public.login({ username, password });
                    API.setAuth(res.data.token, res.data);

                    showToast('Login successful! Redirecting to Doctor/Admin Dashboard...', 'success');
                    setTimeout(() => {
                        window.location.href = '/admin.html';
                    }, 800);
                } catch (err) {
                    showToast(err.message || 'Invalid username or password', 'error');
                } finally {
                    btn.disabled = false;
                    btn.innerHTML = originalText;
                }
            });
        }
    }

    function closeLoginModal() {
        const modal = document.getElementById('adminLoginModal');
        if (modal) modal.classList.remove('active');
    }

    return {
        init,
        closeLoginModal,
        setupScrollReveal
    };
})();

