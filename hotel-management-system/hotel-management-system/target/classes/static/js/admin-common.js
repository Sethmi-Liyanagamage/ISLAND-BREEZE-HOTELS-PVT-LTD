// Admin Common JavaScript Functions
function getAdminToken() {
    return localStorage.getItem('adminToken');
}

// Check authentication
function checkAuth() {
    if (!getAdminToken()) {
        window.location.href = 'admin-login.html';
        return false;
    }
    return true;
}

// Logout function
function logout() {
    localStorage.removeItem('adminToken');
    localStorage.removeItem('adminInfo');
    window.location.href = 'admin-login.html';
}

// Display admin info
function displayAdminInfo() {
    const adminInfo = JSON.parse(localStorage.getItem('adminInfo') || '{}');
    if (adminInfo.username) {
        const nameEl = document.getElementById('adminName');
        const avatarEl = document.getElementById('adminAvatar');
        if (nameEl) nameEl.textContent = adminInfo.username;
        if (avatarEl) avatarEl.textContent = adminInfo.username.charAt(0).toUpperCase();
    }
}

// Initialize admin page
function initAdminPage() {
    if (!checkAuth()) return;
    displayAdminInfo();
    
    // Setup logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            logout();
        });
    }
}

// Call init on page load
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initAdminPage);
} else {
    initAdminPage();
}
