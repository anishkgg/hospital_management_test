// Constants
const API_BASE = '/api/v1';

// App State
const state = {
    hospitals: [],
    doctors: [],
    appointments: [],
    analytics: null,
    userCity: 'Khagaria', // Default simulated city for "Near Me"
    selectedSlot: null,
    selectedDate: null,
    role: 'guest', // 'guest', 'client', or 'admin'
    username: null,
    authenticated: false,
    authMode: 'login' // 'login' or 'register'
};

// Toast Notifications Helper
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span>${type === 'success' ? '✓' : '✗'}</span>
        <div>${message}</div>
    `;
    container.appendChild(toast);
    
    // Animate and Remove
    setTimeout(() => {
        toast.style.opacity = '1';
        toast.style.transform = 'translateY(0)';
    }, 10);
    
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(20px)';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Modal & Drawer Helpers
function openModal(modalId) {
    document.getElementById(modalId).classList.add('active');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
    // Clear forms inside modal
    const form = document.querySelector(`#${modalId} form`);
    if (form) form.reset();
    
    // Specific cleanup
    if (modalId === 'modal-appointment') {
        state.selectedSlot = null;
        state.selectedDate = null;
        document.getElementById('apt-slots-container').style.display = 'none';
        document.getElementById('btn-submit-appointment').disabled = true;
    }
    if (modalId === 'modal-reschedule') {
        document.getElementById('reschedule-slots-container').style.display = 'none';
        document.getElementById('btn-submit-reschedule').disabled = true;
    }
    if (modalId === 'modal-auth') {
        document.getElementById('auth-error-msg').style.display = 'none';
        document.getElementById('auth-error-msg').innerText = '';
    }
}

// Loading States
function toggleBtnLoading(btnId, isLoading, text = 'Save') {
    const btn = document.getElementById(btnId);
    if (!btn) return;
    if (isLoading) {
        btn.disabled = true;
        btn.innerHTML = `<span class="spinner" style="width:14px; height:14px; border-width:2px; vertical-align:middle; margin-right:6px;"></span> Processing...`;
    } else {
        btn.disabled = false;
        btn.innerHTML = text;
    }
}

// Format Datetime
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '-';
    const date = new Date(dateTimeStr);
    return date.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    });
}

// Render star ratings helper
function getStarsHTML(rating) {
    const r = rating != null ? rating : 5.0;
    const percentage = (r / 5) * 100;
    return `
        <div class="rating-container">
            <div class="stars-outer">
                <div class="stars-inner" style="width: ${percentage}%"></div>
            </div>
            <span>${Number(r).toFixed(1)}</span>
        </div>
    `;
}

// Initial Setup
document.addEventListener('DOMContentLoaded', () => {
    initAuthModule();
    initNavigation();
    initModalEvents();
    initFormSubmissions();
    initNearMeFilter();
    initPersonalDrawer();
    
    // Determine user status first, then load catalogs
    checkAuthStatus();
});

// AUTH MODULE
function initAuthModule() {
    const btnShowLogin = document.getElementById('btn-show-login');
    const btnLogout = document.getElementById('btn-logout');
    const toggleAuthLink = document.getElementById('link-toggle-auth');
    const btnSubmitAuth = document.getElementById('btn-submit-auth');
    
    if (btnShowLogin) {
        btnShowLogin.addEventListener('click', () => {
            state.authMode = 'login';
            updateAuthModalUI();
            openModal('modal-auth');
        });
    }
    
    if (btnLogout) {
        btnLogout.addEventListener('click', logoutUser);
    }
    
    if (toggleAuthLink) {
        toggleAuthLink.addEventListener('click', (e) => {
            e.preventDefault();
            state.authMode = state.authMode === 'login' ? 'register' : 'login';
            updateAuthModalUI();
        });
    }
    
    if (btnSubmitAuth) {
        btnSubmitAuth.addEventListener('click', handleAuthSubmit);
    }
}

function setupGoogleIdentity() {
    const btnGoogleLogin = document.getElementById('btn-google-login');
    const googleNativeContainer = document.getElementById('g_id_signin');
    
    if (!state.googleClientId) return;
    
    // Check if the Client ID is configured with a real value
    if (state.googleClientId.startsWith('YOUR_')) {
        // Simulated local fallback
        if (btnGoogleLogin) {
            btnGoogleLogin.style.display = 'flex';
            // Avoid duplicate listeners
            btnGoogleLogin.replaceWith(btnGoogleLogin.cloneNode(true));
            document.getElementById('btn-google-login').addEventListener('click', handleSimulatedGoogleLogin);
        }
        if (googleNativeContainer) googleNativeContainer.style.display = 'none';
    } else {
        // Safe check for GIS loading
        if (typeof google === 'undefined' || !google.accounts || !google.accounts.id) {
            console.log("Google Identity Services script not ready yet, retrying in 100ms...");
            setTimeout(setupGoogleIdentity, 100);
            return;
        }

        // Real Google Identity Services popup
        if (btnGoogleLogin) btnGoogleLogin.style.display = 'none';
        if (googleNativeContainer) {
            googleNativeContainer.style.display = 'flex';
            
            try {
                google.accounts.id.initialize({
                    client_id: state.googleClientId,
                    callback: handleGoogleCredentialResponse
                });
                google.accounts.id.renderButton(
                    googleNativeContainer,
                    { theme: "outline", size: "large", width: 350 }
                );
            } catch (err) {
                console.error("Google Identity Services render failed", err);
            }
        }
    }
}

async function handleGoogleCredentialResponse(response) {
    const credential = response.credential;
    await postGoogleAuth(credential);
}

async function handleSimulatedGoogleLogin() {
    const email = prompt("Enter simulated Google Email Address:", "anishraj@gmail.com");
    if (!email) return;
    
    if (!email.includes('@') || email.length < 5) {
        showToast("Please enter a valid email address", "error");
        return;
    }
    
    await postGoogleAuth(`mock-token:${email}`);
}

async function postGoogleAuth(credential) {
    try {
        const res = await fetch(`${API_BASE}/auth/google`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ credential })
        });
        
        const data = await res.json();
        
        if (res.ok) {
            showToast(`Logged in successfully! Welcome, ${data.username}`, "success");
            closeModal('modal-auth');
            await checkAuthStatus();
        } else {
            showToast(data.error || "Google Authentication failed", "error");
        }
    } catch (e) {
        showToast("Connection to Google login endpoint failed", "error");
    }
}

function updateAuthModalUI() {
    const title = document.getElementById('auth-modal-title');
    const btn = document.getElementById('btn-submit-auth');
    const togglePrompt = document.getElementById('auth-toggle-prompt');
    const toggleLink = document.getElementById('link-toggle-auth');
    const errorMsg = document.getElementById('auth-error-msg');
    const googleLoginContainer = document.getElementById('btn-google-login').parentNode;
    const separator = document.querySelector('.or-separator');
    
    errorMsg.style.display = 'none';
    errorMsg.innerText = '';
    
    if (state.authMode === 'login') {
        title.innerText = 'Sign In';
        btn.innerText = 'Sign In';
        togglePrompt.innerText = 'New to the platform?';
        toggleLink.innerText = 'Register Client';
        
        // Show google login for sign in mode only
        googleLoginContainer.style.display = 'block';
        separator.style.display = 'flex';
    } else {
        title.innerText = 'Register Client';
        btn.innerText = 'Register Account';
        togglePrompt.innerText = 'Already have an account?';
        toggleLink.innerText = 'Sign In';
        
        // Hide google login for manual client registrations
        googleLoginContainer.style.display = 'none';
        separator.style.display = 'none';
    }
}

async function checkAuthStatus() {
    // Load Google OAuth Config once
    if (!state.googleClientId) {
        try {
            const configRes = await fetch(`${API_BASE}/auth/config`);
            if (configRes.ok) {
                const configData = await configRes.json();
                state.googleClientId = configData.googleClientId;
                setupGoogleIdentity();
            }
        } catch (e) {
            console.error("Failed to load Google OAuth config", e);
        }
    }
    
    try {
        const res = await fetch(`${API_BASE}/auth/status`);
        if (!res.ok) throw new Error("Auth status failed");
        
        const data = await res.json();
        
        const userInfo = document.getElementById('auth-user-info');
        const roleBadge = document.getElementById('auth-role-badge');
        const btnShowLogin = document.getElementById('btn-show-login');
        const btnLogout = document.getElementById('btn-logout');
        
        if (data.authenticated) {
            state.authenticated = true;
            state.username = data.username;
            state.role = data.role.replace('ROLE_', '').toLowerCase(); // e.g. 'admin' or 'client'
            
            // Set User Details Info
            userInfo.innerHTML = `<span>👤</span> ${data.username}`;
            roleBadge.innerText = state.role;
            roleBadge.className = `badge ${state.role}`;
            
            btnShowLogin.style.display = 'none';
            btnLogout.style.display = 'block';
            
            // Set body classes
            document.body.className = `${state.role}-mode`;
        } else {
            state.authenticated = false;
            state.username = null;
            state.role = 'guest';
            
            userInfo.innerHTML = `<span>👤</span> Guest User`;
            roleBadge.className = 'badge';
            roleBadge.style.display = 'none';
            
            btnShowLogin.style.display = 'block';
            btnLogout.style.display = 'none';
            
            document.body.className = 'guest-mode';
            
            // Redirect to public tab if logged out
            const activeTab = document.querySelector('.nav-item.active');
            if (activeTab && (activeTab.id === 'tab-dashboard' || activeTab.id === 'tab-appointments')) {
                document.getElementById('tab-hospitals').click();
            }
        }
        
        // Refresh grids and visibility
        applyViewRestrictions();
        
        // Fetch core catalogs
        await fetchHospitals();
        await fetchDoctors();
        renderHospitals();
        renderDoctors();
        
        if (state.role === 'admin') {
            await fetchAppointments();
            loadDashboardData();
            renderAppointments();
        } else if (state.role === 'client') {
            await fetchAppointments();
            renderAppointments();
        }
        
    } catch (e) {
        console.error("Auth status query error:", e);
        document.body.className = 'guest-mode';
    }
}

function applyViewRestrictions() {
    // Show/hide admin tabs and admin buttons
    const adminNav = document.querySelectorAll('.admin-only');
    const adminSection = document.querySelectorAll('.admin-only-section');
    
    if (state.role === 'admin') {
        adminNav.forEach(el => el.style.display = 'block');
        adminSection.forEach(el => el.style.display = 'block');
        document.querySelectorAll('.admin-only').forEach(el => el.style.display = 'inline-block');
    } else {
        adminNav.forEach(el => el.style.display = 'none');
        adminSection.forEach(el => el.style.display = 'none');
        document.querySelectorAll('.admin-only').forEach(el => el.style.display = 'none');
    }
}

async function handleAuthSubmit(e) {
    e.preventDefault();
    
    const form = document.getElementById('form-auth');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    
    const usernameInput = document.getElementById('auth-username').value.trim();
    const passwordInput = document.getElementById('auth-password').value;
    const errorMsg = document.getElementById('auth-error-msg');
    
    toggleBtnLoading('btn-submit-auth', true, state.authMode === 'login' ? 'Sign In' : 'Register Account');
    errorMsg.style.display = 'none';
    
    const payload = { username: usernameInput, password: passwordInput };
    const url = state.authMode === 'login' ? `${API_BASE}/auth/login` : `${API_BASE}/auth/register`;
    
    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        const data = await res.json();
        
        if (res.ok) {
            if (state.authMode === 'login') {
                showToast(`Logged in successfully! Welcome, ${data.username}`, "success");
                closeModal('modal-auth');
                await checkAuthStatus();
            } else {
                showToast("Registration successful! Please log in now.", "success");
                state.authMode = 'login';
                updateAuthModalUI();
                toggleBtnLoading('btn-submit-auth', false, 'Sign In');
            }
        } else {
            errorMsg.innerText = data.error || "Authentication operation failed.";
            errorMsg.style.display = 'block';
            toggleBtnLoading('btn-submit-auth', false, state.authMode === 'login' ? 'Sign In' : 'Register Account');
        }
    } catch (err) {
        errorMsg.innerText = "Network error. Make sure your server is online.";
        errorMsg.style.display = 'block';
        toggleBtnLoading('btn-submit-auth', false, state.authMode === 'login' ? 'Sign In' : 'Register Account');
    }
}

async function logoutUser() {
    try {
        const res = await fetch(`${API_BASE}/auth/logout`, { method: 'POST' });
        if (res.ok) {
            showToast("Successfully signed out", "success");
            await checkAuthStatus();
        } else {
            showToast("Logout request rejected", "error");
        }
    } catch (e) {
        showToast("Error connection during logout", "error");
    }
}

// Navigation Controller
function initNavigation() {
    const navItems = document.querySelectorAll('.sidebar-nav .nav-item');
    const sections = document.querySelectorAll('.section');
    const pageTitle = document.getElementById('page-title');
    const pageSubtitle = document.getElementById('page-subtitle');
    
    const titles = {
        'dashboard-section': { title: 'Dashboard Overview', subtitle: 'Real-time clinical metrics and coordination hub.' },
        'hospitals-section': { title: 'Hospitals Catalog', subtitle: 'Manage medical centers and system locations.' },
        'doctors-section': { title: 'Doctors Directory', subtitle: 'Physician registry, shift schedules, and availability.' },
        'appointments-section': { title: 'Appointments Schedule', subtitle: 'Patient consultation management and rescheduling.' }
    };

    navItems.forEach(item => {
        if (item.id === 'btn-personal-drawer') return; // Skip history click
        
        item.addEventListener('click', () => {
            const target = item.getAttribute('data-target');
            
            navItems.forEach(n => n.classList.remove('active'));
            sections.forEach(s => s.classList.remove('active'));
            
            item.classList.add('active');
            document.getElementById(target).classList.add('active');
            
            // Update Title
            pageTitle.innerText = titles[target].title;
            pageSubtitle.innerText = titles[target].subtitle;
            
            // Refresh target data
            if (target === 'dashboard-section') {
                loadDashboardData();
            } else if (target === 'hospitals-section') {
                renderHospitals();
            } else if (target === 'doctors-section') {
                renderDoctors();
            } else if (target === 'appointments-section') {
                renderAppointments();
            }
        });
    });

    // Quick Book Button
    document.getElementById('btn-quick-appointment').addEventListener('click', () => {
        if (!state.authenticated) {
            state.authMode = 'login';
            updateAuthModalUI();
            openModal('modal-auth');
            showToast("Please log in first to book an appointment.", "error");
            return;
        }
        openModal('modal-appointment');
    });
}

// Modal Toggle Event Listeners
function initModalEvents() {
    const openBtnHospital = document.getElementById('btn-add-hospital');
    const openBtnDoctor = document.getElementById('btn-add-doctor');
    const openBtnAppointment = document.getElementById('btn-create-appointment');
    
    if (openBtnHospital) openBtnHospital.addEventListener('click', () => openModal('modal-hospital'));
    if (openBtnDoctor) openBtnDoctor.addEventListener('click', () => openModal('modal-doctor'));
    
    if (openBtnAppointment) {
        openBtnAppointment.addEventListener('click', () => {
            if (!state.authenticated) {
                state.authMode = 'login';
                updateAuthModalUI();
                openModal('modal-auth');
                showToast("Please log in first to book an appointment.", "error");
                return;
            }
            openModal('modal-appointment');
        });
    }
    
    // Close buttons (X or Cancel)
    document.querySelectorAll('[data-close]').forEach(elem => {
        elem.addEventListener('click', (e) => {
            e.preventDefault();
            const modalId = elem.getAttribute('data-close');
            closeModal(modalId);
        });
    });
    
    // Handle Doctor register modal hospital dropdown populating on click
    if (openBtnDoctor) {
        openBtnDoctor.addEventListener('click', () => {
            populateHospitalDropdown('doctor-hospital');
        });
    }
    
    // Handle Appointment modal hospital select
    const aptHospitalSelect = document.getElementById('apt-hospital-select');
    const aptDoctorSelect = document.getElementById('apt-doctor-select');
    const aptDateSelect = document.getElementById('apt-date-select');
    
    if (openBtnAppointment) {
        openBtnAppointment.addEventListener('click', () => {
            populateHospitalDropdown('apt-hospital-select');
            aptDoctorSelect.disabled = true;
            aptDoctorSelect.innerHTML = '<option value="">Choose a hospital first...</option>';
            aptDateSelect.disabled = true;
            aptDateSelect.value = '';
            document.getElementById('apt-slots-container').style.display = 'none';
        });
    }

    if (aptHospitalSelect) {
        aptHospitalSelect.addEventListener('change', () => {
            const hospitalId = aptHospitalSelect.value;
            if (!hospitalId) {
                aptDoctorSelect.disabled = true;
                aptDoctorSelect.innerHTML = '<option value="">Choose a hospital first...</option>';
                aptDateSelect.disabled = true;
                aptDateSelect.value = '';
                document.getElementById('apt-slots-container').style.display = 'none';
                return;
            }
            
            // Filter doctors belonging to this hospital
            const filteredDoctors = state.doctors.filter(doc => doc.hospitalId && doc.hospitalId == hospitalId);
            
            if (filteredDoctors.length === 0) {
                aptDoctorSelect.disabled = true;
                aptDoctorSelect.innerHTML = '<option value="">No doctors registered here</option>';
                aptDateSelect.disabled = true;
                aptDateSelect.value = '';
                document.getElementById('apt-slots-container').style.display = 'none';
            } else {
                aptDoctorSelect.disabled = false;
                aptDoctorSelect.innerHTML = '<option value="">Select Doctor...</option>' + 
                    filteredDoctors.map(doc => `<option value="${doc.id}">${doc.name} (${doc.specialty})</option>`).join('');
                aptDateSelect.disabled = true;
                aptDateSelect.value = '';
                document.getElementById('apt-slots-container').style.display = 'none';
            }
        });
    }

    if (aptDoctorSelect) {
        aptDoctorSelect.addEventListener('change', () => {
            const docId = aptDoctorSelect.value;
            if (!docId) {
                aptDateSelect.disabled = true;
                aptDateSelect.value = '';
                document.getElementById('apt-slots-container').style.display = 'none';
                return;
            }
            
            // Enable date selector and set min date to today
            aptDateSelect.disabled = false;
            const today = new Date().toISOString().split('T')[0];
            aptDateSelect.min = today;
            aptDateSelect.value = '';
            document.getElementById('apt-slots-container').style.display = 'none';
        });
    }

    if (aptDateSelect) {
        aptDateSelect.addEventListener('change', () => {
            const docId = aptDoctorSelect.value;
            const dateStr = aptDateSelect.value;
            if (!docId || !dateStr) {
                document.getElementById('apt-slots-container').style.display = 'none';
                return;
            }
            generateBookingSlots(docId, dateStr, 'apt-slots-grid');
        });
    }

    // Reschedule date selector listener
    const reschDateSelect = document.getElementById('reschedule-date-select');
    if (reschDateSelect) {
        reschDateSelect.addEventListener('change', () => {
            const dateStr = reschDateSelect.value;
            const aptId = document.getElementById('reschedule-apt-id').value;
            const apt = state.appointments.find(a => a.id == aptId);
            if (!apt || !dateStr) return;
            
            generateBookingSlots(apt.doctorId, dateStr, 'reschedule-slots-grid');
        });
    }
}

// Generate Time Slots Logic
function generateBookingSlots(doctorId, dateStr, containerId) {
    const doc = state.doctors.find(d => d.id == doctorId);
    if (!doc) return;
    
    const container = document.getElementById(containerId);
    const parentContainerId = containerId === 'apt-slots-grid' ? 'apt-slots-container' : 'reschedule-slots-container';
    const submitBtnId = containerId === 'apt-slots-grid' ? 'btn-submit-appointment' : 'btn-submit-reschedule';
    
    container.innerHTML = '';
    
    // 1. Validate working day
    const date = new Date(dateStr);
    const dayName = date.toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase();
    const docDays = doc.workingDays ? doc.workingDays.toUpperCase() : "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY";
    
    if (!docDays.includes(dayName)) {
        container.innerHTML = `<div style="grid-column: span 4; color: var(--status-cancelled); font-size:12px; font-weight:600; padding:10px;">Doctor does not work on ${dayName}.</div>`;
        document.getElementById(parentContainerId).style.display = 'block';
        document.getElementById(submitBtnId).disabled = true;
        return;
    }
    
    // 2. Parse Shift times
    const shiftStart = doc.shiftStart ? doc.shiftStart : '09:00:00';
    const shiftEnd = doc.shiftEnd ? doc.shiftEnd : '17:00:00';
    
    const [startH, startM] = shiftStart.split(':').map(Number);
    const [endH, endM] = shiftEnd.split(':').map(Number);
    
    let current = new Date();
    current.setHours(startH, startM, 0, 0);
    
    const end = new Date();
    end.setHours(endH, endM, 0, 0);
    
    // 3. Get existing appointments for this doctor on this date (not cancelled)
    const existing = state.appointments.filter(apt => {
        if (apt.doctorId != doctorId) return false;
        if (apt.status === 'CANCELLED') return false;
        
        const aptDate = apt.appointmentTime.split('T')[0];
        return aptDate === dateStr;
    });

    // 4. Loop slots (30-min intervals)
    let slotsFound = 0;
    while (current < end) {
        const slotHour = String(current.getHours()).padStart(2, '0');
        const slotMin = String(current.getMinutes()).padStart(2, '0');
        const slotTimeStr = `${slotHour}:${slotMin}`;
        
        // Check if slot has booking
        const matchCount = existing.filter(apt => {
            const aptTime = apt.appointmentTime.split('T')[1].substring(0, 5);
            return aptTime === slotTimeStr;
        }).length;
        
        const slotBtn = document.createElement('button');
        slotBtn.type = 'button';
        slotBtn.className = 'slot-btn';
        slotBtn.textContent = slotTimeStr;
        
        // Slot Availability Status
        if (matchCount === 0) {
            slotBtn.setAttribute('data-status', 'available');
            slotBtn.style.borderColor = 'var(--status-completed)';
            slotBtn.style.color = 'var(--status-completed)';
        } else if (matchCount === 1) {
            slotBtn.setAttribute('data-status', 'waiting');
            slotBtn.style.borderColor = 'var(--status-pending)';
            slotBtn.style.color = 'var(--status-pending)';
            slotBtn.textContent += ' (Wait)';
        } else {
            slotBtn.setAttribute('data-status', 'full');
            slotBtn.disabled = true;
            slotBtn.textContent += ' (Full)';
        }
        
        // Prevent booking historical slots if selected date is today
        const todayStr = new Date().toISOString().split('T')[0];
        if (dateStr === todayStr) {
            const now = new Date();
            const nowTimeStr = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;
            if (slotTimeStr <= nowTimeStr) {
                slotBtn.disabled = true;
            }
        }
        
        slotBtn.addEventListener('click', () => {
            // Deselect others
            container.querySelectorAll('.slot-btn').forEach(b => b.classList.remove('selected'));
            slotBtn.classList.add('selected');
            
            // Save selection
            state.selectedSlot = slotTimeStr;
            state.selectedDate = dateStr;
            document.getElementById(submitBtnId).disabled = false;
        });
        
        container.appendChild(slotBtn);
        current.setMinutes(current.getMinutes() + 30);
        slotsFound++;
    }
    
    if (slotsFound === 0) {
        container.innerHTML = `<div style="grid-column: span 4; color: var(--text-muted); font-size:12px; padding:10px;">No slots available in shift parameters.</div>`;
        document.getElementById(submitBtnId).disabled = true;
    }
    
    document.getElementById(parentContainerId).style.display = 'block';
}

// Dropdown Helper
function populateHospitalDropdown(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;
    
    select.innerHTML = '<option value="">Select Hospital...</option>' + 
        state.hospitals.map(h => `<option value="${h.id}">${h.name} (${h.city})</option>`).join('');
}

// Fetch Core Data
async function loadAllData() {
    try {
        await Promise.all([
            fetchHospitals(),
            fetchDoctors()
        ]);
        
        if (state.authenticated) {
            await fetchAppointments();
            renderAppointments();
            if (state.role === 'admin') {
                loadDashboardData();
            }
        } else {
            renderHospitals();
        }
    } catch (error) {
        console.error("Error loading core details:", error);
    }
}

async function fetchHospitals() {
    const res = await fetch(`${API_BASE}/getAllHospitals`);
    if (!res.ok) throw new Error("Hospitals fetch failed");
    state.hospitals = await res.json();
}

async function fetchDoctors() {
    const res = await fetch(`${API_BASE}/getAllDoctors/schedule`);
    if (!res.ok) throw new Error("Doctors fetch failed");
    state.doctors = await res.json();
}

async function fetchAppointments() {
    // Only fetch if authenticated
    if (!state.authenticated) return;
    const res = await fetch(`${API_BASE}/getAllAppointment`);
    if (!res.ok) throw new Error("Appointments fetch failed");
    state.appointments = await res.json();
}

// DASHBOARD MODULE
async function loadDashboardData() {
    // Update Stats Card Numbers
    document.getElementById('metric-hospitals').innerText = state.hospitals.length;
    document.getElementById('metric-doctors').innerText = state.doctors.length;
    document.getElementById('metric-appointments').innerText = state.appointments.length;
    
    const completedCount = state.appointments.filter(a => a.status === 'COMPLETED').length;
    const rate = state.appointments.length > 0 
        ? Math.round((completedCount / state.appointments.length) * 100) 
        : 0;
    document.getElementById('metric-completion').innerText = `${rate}%`;
    
    // Populate Shifts Widget
    renderDashboardShifts();

    // Fetch and Draw Analytics
    try {
        const res = await fetch(`${API_BASE}/reports/summary`);
        if (res.ok) {
            state.analytics = await res.json();
            drawAnalyticsChart(state.analytics);
        }
    } catch (e) {
        console.error("Could not load report summary", e);
    }
}

function renderDashboardShifts() {
    const container = document.getElementById('dashboard-shifts-container');
    if (!container) return;
    
    const list = [...state.doctors].slice(0, 5);
    
    if (list.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">⏰</div>
                <h4>No shifts scheduled</h4>
                <p>Register doctors to view shift rosters.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = list.map(doc => `
        <div style="padding: 12px 0; border-bottom: 1px solid var(--border-color); display:flex; justify-content:space-between; align-items:center;">
            <div>
                <div style="font-weight: 600; font-size: 14px; color: var(--text-main);">${doc.name}</div>
                <div style="font-size: 12px; color: var(--text-muted);">${doc.specialty} &bull; ${doc.hospitalName || 'No hospital'}</div>
            </div>
            <div style="text-align: right;">
                <div style="font-weight: 500; font-size: 13px; color: var(--primary);">${doc.shiftStart ? doc.shiftStart.substring(0,5) : '09:00'} - ${doc.shiftEnd ? doc.shiftEnd.substring(0,5) : '17:00'}</div>
                <div style="font-size: 11px; color: var(--text-muted);">${doc.workingDays || 'Mon-Fri'}</div>
            </div>
        </div>
    `).join('');
}

// CUSTOM SVG CHART GENERATOR
function drawAnalyticsChart(analytics) {
    const svg = document.getElementById('analytics-svg');
    if (!svg || !analytics) return;
    svg.innerHTML = ''; 

    const reportData = analytics.hospitalReports || [];
    
    if (reportData.length === 0) {
        svg.innerHTML = `<text x="250" y="125" text-anchor="middle" fill="var(--text-muted)">No hospital data for charting.</text>`;
        return;
    }
    
    const maxApts = Math.max(...reportData.map(d => d.totalAppointments), 1);
    
    const margin = { top: 30, right: 20, bottom: 40, left: 60 };
    const width = 500;
    const height = 250;
    
    const chartWidth = width - margin.left - margin.right;
    const chartHeight = height - margin.top - margin.bottom;
    
    // Draw Y axis grids
    const gridCount = 4;
    for (let i = 0; i <= gridCount; i++) {
        const yVal = margin.top + (chartHeight / gridCount) * i;
        const aptCountLabel = Math.round(maxApts - (maxApts / gridCount) * i);
        
        // Grid Line
        const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
        line.setAttribute("x1", margin.left);
        line.setAttribute("y1", yVal);
        line.setAttribute("x2", width - margin.right);
        line.setAttribute("y2", yVal);
        line.setAttribute("class", "chart-grid-line");
        svg.appendChild(line);
        
        // Y Axis Label
        const label = document.createElementNS("http://www.w3.org/2000/svg", "text");
        label.setAttribute("x", margin.left - 10);
        label.setAttribute("y", yVal + 4);
        label.setAttribute("text-anchor", "end");
        label.setAttribute("class", "chart-axis-label");
        label.textContent = aptCountLabel;
        svg.appendChild(label);
    }
    
    // Draw Bars
    const barWidth = Math.min(45, (chartWidth / reportData.length) * 0.6);
    const spacing = (chartWidth - (barWidth * reportData.length)) / (reportData.length + 1);
    
    reportData.forEach((data, index) => {
        const xVal = margin.left + spacing + (barWidth + spacing) * index;
        const barHeight = (data.totalAppointments / maxApts) * chartHeight;
        const yVal = margin.top + chartHeight - barHeight;
        
        // Bar
        const bar = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        bar.setAttribute("x", xVal);
        bar.setAttribute("y", yVal);
        bar.setAttribute("width", barWidth);
        bar.setAttribute("height", Math.max(barHeight, 2));
        bar.setAttribute("class", "chart-bar");
        
        bar.addEventListener('click', () => {
            showToast(`${data.hospitalName}: ${data.totalAppointments} appointments`, 'success');
        });
        
        svg.appendChild(bar);
        
        // Value labels
        if (data.totalAppointments > 0) {
            const valLabel = document.createElementNS("http://www.w3.org/2000/svg", "text");
            valLabel.setAttribute("x", xVal + barWidth / 2);
            valLabel.setAttribute("y", yVal - 8);
            valLabel.setAttribute("class", "chart-value-label");
            valLabel.textContent = data.totalAppointments;
            svg.appendChild(valLabel);
        }
        
        // X Axis labels
        const nameLabel = document.createElementNS("http://www.w3.org/2000/svg", "text");
        nameLabel.setAttribute("x", xVal + barWidth / 2);
        nameLabel.setAttribute("y", margin.top + chartHeight + 20);
        nameLabel.setAttribute("class", "chart-axis-label");
        nameLabel.textContent = data.hospitalName.length > 12 ? data.hospitalName.substring(0, 10) + '..' : data.hospitalName;
        svg.appendChild(nameLabel);
    });
}

// "NEAR ME" HOSPITALS FILTER
function initNearMeFilter() {
    const chk = document.getElementById('chk-near-me');
    if (!chk) return;
    
    chk.addEventListener('change', () => {
        if (chk.checked) {
            // Ask simulated city if not set
            const city = prompt("Enter your current city to find nearby hospitals:", state.userCity);
            if (city) {
                state.userCity = city.trim();
                renderHospitals(true); // filter near me
            } else {
                chk.checked = false;
                renderHospitals(false);
            }
        } else {
            renderHospitals(false);
        }
    });
}

// HOSPITALS MODULE
function renderHospitals(filterNearMe = false) {
    const tbody = document.getElementById('hospitals-table-body');
    const emptyState = document.getElementById('hospitals-empty-state');
    
    let list = state.hospitals;
    if (filterNearMe) {
        list = state.hospitals.filter(h => h.city.toLowerCase() === state.userCity.toLowerCase());
    }
    
    if (list.length === 0) {
        tbody.innerHTML = '';
        emptyState.style.display = 'block';
        return;
    }
    
    emptyState.style.display = 'none';
    tbody.innerHTML = list.map(h => `
        <tr>
            <td style="font-weight:600; color:var(--bg-dark);">${h.name}</td>
            <td>${h.address}</td>
            <td>${h.city}</td>
            <td>${h.phone}</td>
            <td>${getStarsHTML(h.rating)}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="showHospitalDetails(${h.id})">🔍 View Doctors</button>
            </td>
        </tr>
    `).join('');
}

window.showHospitalDetails = (id) => {
    // Switch to Doctors tab and filter by hospital city/name
    const hospital = state.hospitals.find(h => h.id == id);
    if (!hospital) return;
    
    // Set doctor filter parameters and search
    document.getElementById('search-doctor-city').value = hospital.city;
    document.getElementById('search-doctor-specialty').value = '';
    document.getElementById('search-doctor-name').value = '';
    
    // Switch Tab to doctors
    document.getElementById('tab-doctors').click();
    
    // Filter doctors belonging to this hospital city
    const list = state.doctors.filter(d => d.hospitalId == id);
    renderDoctors(list);
    showToast(`Showing doctors registered at ${hospital.name}`, 'success');
};

// DOCTORS MODULE
function renderDoctors(doctorsList = state.doctors) {
    const grid = document.getElementById('doctors-grid');
    const emptyState = document.getElementById('doctors-empty-state');
    
    grid.innerHTML = '';
    
    if (doctorsList.length === 0) {
        emptyState.style.display = 'block';
        return;
    }
    
    emptyState.style.display = 'none';
    doctorsList.forEach(doc => {
        const card = document.createElement('div');
        card.className = 'info-card';
        card.innerHTML = `
            <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                <h4>${doc.name}</h4>
                ${getStarsHTML(doc.rating)}
            </div>
            <div style="font-size: 13px; font-weight:600; color: var(--primary); margin-bottom: 12px;">${doc.specialty}</div>
            
            <div class="info-card-detail">
                <span class="label">Hospital:</span>
                <span class="val">${doc.hospitalName || 'Not assigned'}</span>
            </div>
            <div class="info-card-detail">
                <span class="label">Shift Hours:</span>
                <span class="val">${doc.shiftStart ? doc.shiftStart.substring(0,5) : '09:00'} - ${doc.shiftEnd ? doc.shiftEnd.substring(0,5) : '17:00'}</span>
            </div>
            <div class="info-card-detail">
                <span class="label">Workdays:</span>
                <span class="val" style="font-size:11px;">${doc.workingDays || 'Mon-Fri'}</span>
            </div>
            <div class="info-card-detail">
                <span class="label">Phone:</span>
                <span class="val">${doc.phone || '-'}</span>
            </div>
            <div class="info-card-detail">
                <span class="label">License No:</span>
                <span class="val" style="font-family:monospace;">${doc.licenseNumber || '-'}</span>
            </div>
            
            <div class="info-card-footer">
                <button class="btn btn-secondary btn-sm" onclick="bookWithDoctor(${doc.id})">📅 Book Slot</button>
            </div>
        `;
        grid.appendChild(card);
    });
}

window.bookWithDoctor = (docId) => {
    if (!state.authenticated) {
        state.authMode = 'login';
        updateAuthModalUI();
        openModal('modal-auth');
        showToast("Please sign in to book appointment.", "error");
        return;
    }
    
    openModal('modal-appointment');
    const doc = state.doctors.find(d => d.id == docId);
    if (!doc) return;
    
    // Set Hospital Dropdown
    if (doc.hospitalId) {
        populateHospitalDropdown('apt-hospital-select');
        document.getElementById('apt-hospital-select').value = doc.hospitalId;
        
        // Trigger manual change to fetch doctors
        const select = document.getElementById('apt-doctor-select');
        select.disabled = false;
        
        const filteredDoctors = state.doctors.filter(d => d.hospitalId && d.hospitalId == doc.hospitalId);
        select.innerHTML = filteredDoctors.map(d => `<option value="${d.id}">${d.name} (${d.specialty})</option>`).join('');
        select.value = docId;
        
        // Enable date
        const dateSelect = document.getElementById('apt-date-select');
        dateSelect.disabled = false;
        dateSelect.min = new Date().toISOString().split('T')[0];
        dateSelect.value = '';
        document.getElementById('apt-slots-container').style.display = 'none';
    }
};

// APPOINTMENTS MODULE
function renderAppointments() {
    const tbody = document.getElementById('appointments-table-body');
    const emptyState = document.getElementById('appointments-empty-state');
    
    // If not authenticated, hide schedules completely
    if (!state.authenticated) {
        tbody.innerHTML = '';
        emptyState.style.display = 'block';
        emptyState.innerHTML = `
            <div class="empty-state-icon">🔒</div>
            <h4>Authentication Required</h4>
            <p>Please log in to view schedules and manage doctor coordinates.</p>
        `;
        return;
    }
    
    if (state.appointments.length === 0) {
        tbody.innerHTML = '';
        emptyState.style.display = 'block';
        emptyState.innerHTML = `
            <div class="empty-state-icon">📅</div>
            <h4>No appointments scheduled yet</h4>
            <p>Start booking appointments for registered doctors.</p>
        `;
        return;
    }
    
    emptyState.style.display = 'none';
    tbody.innerHTML = state.appointments.map(apt => `
        <tr>
            <td style="font-family: monospace; font-weight: 700; color:var(--bg-dark);">${apt.bookingCode}</td>
            <td style="font-weight: 500;">${apt.patientName}</td>
            <td>${apt.doctorName || 'Unknown Doctor'}</td>
            <td>${formatDateTime(apt.appointmentTime)}</td>
            <td><span class="badge ${apt.status.toLowerCase()}">${apt.status}</span></td>
            <td class="admin-only" style="${state.role === 'admin' ? '' : 'display:none;'}">
                <div class="action-buttons">
                    <button class="btn btn-secondary btn-sm" onclick="showAppointmentDetails('${apt.bookingCode}')">🔍 View</button>
                    ${apt.status === 'SCHEDULED' || apt.status === 'WAITING' ? `
                        <button class="btn btn-secondary btn-sm" onclick="openRescheduleModal(${apt.id}, '${apt.appointmentTime}')">✏️ Reschedule</button>
                        <button class="btn btn-primary btn-sm" onclick="openCompleteModal(${apt.id})">✓ Complete</button>
                    ` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

// Booking lookup details
async function showAppointmentDetails(bookingCode) {
    if (!state.authenticated) {
        showToast("Please log in to lookup detailed booking logs.", "error");
        return;
    }
    
    try {
        const res = await fetch(`${API_BASE}/appointment/details?bookingCode=${bookingCode}`);
        if (!res.ok) {
            showToast("Booking code not found", "error");
            return;
        }
        
        const apt = await res.json();
        
        // Show result card
        const card = document.getElementById('lookup-result-card');
        card.style.display = 'block';
        card.scrollIntoView({ behavior: 'smooth' });
        
        // Populate fields
        document.getElementById('lookup-status').className = `badge ${apt.status.toLowerCase()}`;
        document.getElementById('lookup-status').innerText = apt.status;
        document.getElementById('lookup-patient-name').innerText = apt.patientName;
        document.getElementById('lookup-patient-email').innerText = apt.patientEmail;
        document.getElementById('lookup-patient-phone').innerText = apt.patientPhone;
        document.getElementById('lookup-code').innerText = apt.bookingCode;
        document.getElementById('lookup-doctor-name').innerText = apt.doctorName || 'Unknown';
        document.getElementById('lookup-time').innerText = formatDateTime(apt.appointmentTime);
        
        // Medical Notes
        const notesContainer = document.getElementById('lookup-notes-container');
        if (apt.medicalNotes) {
            notesContainer.style.display = 'block';
            document.getElementById('lookup-notes').innerText = apt.medicalNotes;
        } else {
            notesContainer.style.display = 'none';
        }
        
        // Actions
        const actionsContainer = document.getElementById('lookup-actions');
        actionsContainer.innerHTML = '';
        
        if (apt.status === 'SCHEDULED' || apt.status === 'WAITING') {
            actionsContainer.innerHTML = `
                <button class="btn btn-secondary btn-sm" onclick="openRescheduleModal(${apt.id}, '${apt.appointmentTime}')">✏️ Reschedule</button>
                <button class="btn btn-secondary btn-sm" style="color:var(--status-cancelled); border-color:var(--status-cancelled-bg);" onclick="cancelAppointment(${apt.id})">❌ Cancel Booking</button>
            `;
        }
    } catch (e) {
        showToast("Error retrieving booking details", "error");
    }
}

// Reschedule modal toggle
window.openRescheduleModal = (aptId, currentTime) => {
    document.getElementById('reschedule-apt-id').value = aptId;
    
    // Set date min and value
    const select = document.getElementById('reschedule-date-select');
    const today = new Date().toISOString().split('T')[0];
    select.min = today;
    select.value = currentTime.split('T')[0];
    
    document.getElementById('reschedule-slots-container').style.display = 'none';
    document.getElementById('btn-submit-reschedule').disabled = true;
    
    openModal('modal-reschedule');
    
    // Trigger slots loading
    const apt = state.appointments.find(a => a.id == aptId);
    if (apt) {
        generateBookingSlots(apt.doctorId, select.value, 'reschedule-slots-grid');
    }
};

// Complete modal toggle
window.openCompleteModal = (aptId) => {
    document.getElementById('complete-apt-id').value = aptId;
    document.getElementById('complete-notes').value = '';
    openModal('modal-complete');
};

// Cancel action handler
window.cancelAppointment = async (id) => {
    if (!confirm("Are you sure you want to cancel this appointment?")) return;
    
    try {
        const res = await fetch(`${API_BASE}/appointment/cancel?id=${id}`, {
            method: 'PUT'
        });
        
        if (res.ok) {
            showToast("Appointment cancelled successfully", "success");
            await checkAuthStatus();
            
            // Hide/update lookup card if shown
            const lookupCode = document.getElementById('lookup-code').innerText;
            if (lookupCode && lookupCode !== '-') showAppointmentDetails(lookupCode);
            
            // Refresh personal bookings list if open
            refreshPersonalBookings();
        } else {
            showToast("Failed to cancel appointment", "error");
        }
    } catch (e) {
        showToast("Error cancelling appointment", "error");
    }
};

// PERSONAL DRAWER MODULE
function initPersonalDrawer() {
    const btn = document.getElementById('btn-personal-drawer');
    const lookupBtn = document.getElementById('btn-personal-lookup');
    
    if (btn) {
        btn.addEventListener('click', () => {
            openModal('drawer-personal');
            updatePersonalDrawerUI();
        });
    }
    
    if (lookupBtn) {
        lookupBtn.addEventListener('click', () => {
            refreshPersonalBookings();
        });
    }
}

function updatePersonalDrawerUI() {
    const drawerAuthMsg = document.getElementById('drawer-auth-required-msg');
    const inputControl = document.getElementById('personal-contact-input');
    const lookupBtn = document.getElementById('btn-personal-lookup');
    
    if (!state.authenticated) {
        drawerAuthMsg.style.display = 'block';
        inputControl.disabled = true;
        lookupBtn.disabled = true;
        document.getElementById('personal-bookings-results').innerHTML = '';
    } else {
        drawerAuthMsg.style.display = 'none';
        inputControl.disabled = false;
        lookupBtn.disabled = false;
        
        // Seed default search if contact info matches client username
        if (state.username && (state.username.match(/^\d+$/) || state.username.includes('@'))) {
            inputControl.value = state.username;
            refreshPersonalBookings();
        }
    }
}

function refreshPersonalBookings() {
    const input = document.getElementById('personal-contact-input').value.trim();
    const container = document.getElementById('personal-bookings-results');
    
    if (!input) {
        showToast("Please enter email or phone number", "error");
        return;
    }
    
    const matched = state.appointments.filter(apt => {
        return (apt.patientPhone && apt.patientPhone === input) || 
               (apt.patientEmail && apt.patientEmail.toLowerCase() === input.toLowerCase());
    });
    
    if (matched.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📭</div>
                <h4>No bookings found</h4>
                <p>No appointments match the contact details entered.</p>
            </div>
        `;
        return;
    }
    
    // Sort bookings by date descending
    matched.sort((a,b) => b.appointmentTime.localeCompare(a.appointmentTime));
    
    container.innerHTML = matched.map(apt => `
        <div class="personal-booking-card">
            <div class="personal-booking-card-header">
                <span style="font-family:monospace; font-weight:700; color:var(--bg-dark);">${apt.bookingCode}</span>
                <span class="badge ${apt.status.toLowerCase()}">${apt.status}</span>
            </div>
            <div style="font-size:13px; margin-bottom: 6px;"><strong style="color:var(--text-muted)">Doctor:</strong> ${apt.doctorName || 'N/A'}</div>
            <div style="font-size:13px; margin-bottom: 6px;"><strong style="color:var(--text-muted)">Schedule:</strong> ${formatDateTime(apt.appointmentTime)}</div>
            ${apt.medicalNotes ? `
                <div style="font-size:12px; margin-top:10px; background:#f8fafc; padding:8px; border-radius:4px; color:#475569;">
                    <strong>Notes:</strong> ${apt.medicalNotes}
                </div>
            ` : ''}
            
            ${apt.status === 'SCHEDULED' || apt.status === 'WAITING' ? `
                <div style="display:flex; justify-content:flex-end; gap:6px; margin-top:12px;">
                    <button class="btn btn-secondary btn-sm" onclick="closeModal('drawer-personal'); openRescheduleModal(${apt.id}, '${apt.appointmentTime}')">✏️ Reschedule</button>
                    <button class="btn btn-secondary btn-sm" style="color:var(--status-cancelled); border-color:var(--status-cancelled-bg);" onclick="cancelAppointment(${apt.id})">❌ Cancel</button>
                </div>
            ` : ''}
        </div>
    `).join('');
}

// FORM SUBMISSIONS
function initFormSubmissions() {
    // Add Hospital Form
    document.getElementById('btn-submit-hospital').addEventListener('click', async (e) => {
        const form = document.getElementById('form-hospital');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        toggleBtnLoading('btn-submit-hospital', true);
        
        const payload = {
            hospitalName: document.getElementById('hospital-name').value,
            address: document.getElementById('hospital-address').value,
            city: document.getElementById('hospital-city').value,
            phone: document.getElementById('hospital-phone').value,
            rating: parseFloat(document.getElementById('hospital-rating').value) || 5.0
        };
        
        try {
            const res = await fetch(`${API_BASE}/addHospital`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            
            if (res.ok) {
                showToast("Hospital saved successfully", "success");
                closeModal('modal-hospital');
                await checkAuthStatus();
            } else {
                showToast("Failed to save hospital details", "error");
            }
        } catch (err) {
            showToast("Network error occurred", "error");
        } finally {
            toggleBtnLoading('btn-submit-hospital', false, 'Save Hospital');
        }
    });

    // Add Doctor Form
    document.getElementById('btn-submit-doctor').addEventListener('click', async (e) => {
        const form = document.getElementById('form-doctor');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        toggleBtnLoading('btn-submit-doctor', true);
        
        const payload = {
            name: document.getElementById('doctor-name').value,
            specialty: document.getElementById('doctor-specialty').value,
            phone: document.getElementById('doctor-phone').value,
            email: document.getElementById('doctor-email').value,
            hospitalId: parseInt(document.getElementById('doctor-hospital').value),
            licenseNumber: document.getElementById('doctor-license').value,
            shiftStart: document.getElementById('doctor-shift-start').value + ':00',
            shiftEnd: document.getElementById('doctor-shift-end').value + ':00',
            workingDays: document.getElementById('doctor-working-days').value,
            rating: parseFloat(document.getElementById('doctor-rating').value) || 5.0
        };
        
        try {
            const res = await fetch(`${API_BASE}/addDoctors/schedule`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            
            if (res.ok) {
                showToast("Doctor registered successfully", "success");
                closeModal('modal-doctor');
                await checkAuthStatus();
            } else {
                showToast("Registration failed. Email or phone conflict.", "error");
            }
        } catch (err) {
            showToast("Network error occurred", "error");
        } finally {
            toggleBtnLoading('btn-submit-doctor', false, 'Register');
        }
    });

    // Search/Filter Doctors Button
    document.getElementById('btn-search-doctors').addEventListener('click', async () => {
        const name = document.getElementById('search-doctor-name').value;
        const specialty = document.getElementById('search-doctor-specialty').value;
        const city = document.getElementById('search-doctor-city').value;
        
        let query = [];
        if (name) query.push(`name=${encodeURIComponent(name)}`);
        if (specialty) query.push(`specialty=${encodeURIComponent(specialty)}`);
        if (city) query.push(`city=${encodeURIComponent(city)}`);
        
        const queryString = query.length > 0 ? '?' + query.join('&') : '';
        
        try {
            const res = await fetch(`${API_BASE}/doctors/search${queryString}`);
            if (res.ok) {
                const list = await res.json();
                renderDoctors(list);
            } else {
                showToast("Doctor filter failed", "error");
            }
        } catch (e) {
            showToast("Search network error", "error");
        }
    });

    // Book Appointment Form
    document.getElementById('btn-submit-appointment').addEventListener('click', async (e) => {
        const form = document.getElementById('form-appointment');
        if (!form.checkValidity() || !state.selectedSlot || !state.selectedDate) {
            form.reportValidity();
            return;
        }
        
        toggleBtnLoading('btn-submit-appointment', true);
        
        const payload = {
            patientName: document.getElementById('apt-patient-name').value,
            patientPhone: document.getElementById('apt-patient-phone').value,
            patientEmail: document.getElementById('apt-patient-email').value,
            appointmentTime: `${state.selectedDate}T${state.selectedSlot}:00`,
            doctorId: parseInt(document.getElementById('apt-doctor-select').value)
        };
        
        try {
            const res = await fetch(`${API_BASE}/book/appointment`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            
            const data = await res.json();
            
            if (res.ok) {
                showToast(`Appointment booked successfully! Booking Code: ${data.bookingCode}`, "success");
                closeModal('modal-appointment');
                await checkAuthStatus();
            } else {
                showToast(data.error || "Booking failed. Overlap or schedule limits exceeded.", "error");
            }
        } catch (err) {
            showToast("Network error occurred", "error");
        } finally {
            toggleBtnLoading('btn-submit-appointment', false, 'Confirm Booking');
        }
    });

    // Reschedule Form
    document.getElementById('btn-submit-reschedule').addEventListener('click', async (e) => {
        const form = document.getElementById('form-reschedule');
        if (!form.checkValidity() || !state.selectedSlot || !state.selectedDate) {
            form.reportValidity();
            return;
        }
        
        toggleBtnLoading('btn-submit-reschedule', true);
        const id = document.getElementById('reschedule-apt-id').value;
        const payload = {
            newTime: `${state.selectedDate}T${state.selectedSlot}:00`
        };
        
        try {
            const res = await fetch(`${API_BASE}/appointment/reschedule?id=${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            
            const data = await res.json();
            
            if (res.ok) {
                showToast("Appointment rescheduled successfully", "success");
                closeModal('modal-reschedule');
                
                await checkAuthStatus();
                
                // Refresh details card
                const lookupCode = document.getElementById('lookup-code').innerText;
                if (lookupCode && lookupCode !== '-') showAppointmentDetails(lookupCode);
                
                // Refresh personal bookings list if open
                refreshPersonalBookings();
            } else {
                showToast(data.error || "Rescheduling failed.", "error");
            }
        } catch (err) {
            showToast("Network error occurred", "error");
        } finally {
            toggleBtnLoading('btn-submit-reschedule', false, 'Confirm Reschedule');
        }
    });

    // Complete Form
    document.getElementById('btn-submit-complete').addEventListener('click', async (e) => {
        const form = document.getElementById('form-complete');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        toggleBtnLoading('btn-submit-complete', true);
        const id = document.getElementById('complete-apt-id').value;
        const payload = {
            medicalNotes: document.getElementById('complete-notes').value
        };
        
        try {
            const res = await fetch(`${API_BASE}/appointment/complete?id=${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            
            if (res.ok) {
                showToast("Appointment marked as completed!", "success");
                closeModal('modal-complete');
                await checkAuthStatus();
                
                // Refresh details card
                const lookupCode = document.getElementById('lookup-code').innerText;
                if (lookupCode && lookupCode !== '-') showAppointmentDetails(lookupCode);
            } else {
                showToast("Action failed", "error");
            }
        } catch (err) {
            showToast("Network error occurred", "error");
        } finally {
            toggleBtnLoading('btn-submit-complete', false, 'Complete Session');
        }
    });

    // Lookup Booking Code Action
    document.getElementById('btn-lookup-booking').addEventListener('click', () => {
        const code = document.getElementById('search-booking-code').value.trim();
        if (!code) {
            showToast("Please enter a booking code", "error");
            return;
        }
        showAppointmentDetails(code);
    });
}
