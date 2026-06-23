// guest-dashboard.js

document.addEventListener('DOMContentLoaded', initializeGuestDashboard);

let currentUser = null;

function initializeGuestDashboard() {
    const userStr = localStorage.getItem('currentUser');
    if (!userStr) {
        window.location.href = 'index.html'; // Redirect if not logged in
        return;
    }
    currentUser = JSON.parse(userStr);

    loadDashboardStats();
    loadProfileDetails();
}

// Load key stats for the dashboard cards
function loadDashboardStats() {
    // Example: Fetch guest's bookings to calculate stats
    fetch(`http://localhost:8080/api/bookings/guest/${currentUser.guestId}`)
        .then(response => response.json())
        .then(bookings => {
            document.getElementById('totalBookings').textContent = bookings.length;
            
            const upcoming = bookings.find(b => new Date(b.checkInDate) > new Date() && b.status === 'CONFIRMED');
            document.getElementById('nextCheckIn').textContent = upcoming 
                ? formatDate(upcoming.checkInDate) 
                : 'None';
        });

    // Example: Fetch payments to find pending amount
    fetch(`http://localhost:8080/api/payments/guest/${currentUser.guestId}`)
        .then(response => response.json())
        .then(payments => {
            const pending = payments
                .filter(p => p.status === 'PENDING')
                .reduce((sum, p) => sum + p.amount, 0);
            document.getElementById('pendingPayments').textContent = `$${pending.toFixed(2)}`;
        });
}


// Load guest's profile information
function loadProfileDetails() {
    // Assuming currentUser object from login has the required details
    document.getElementById('guestName').textContent = currentUser.fullName;
    document.getElementById('guestEmail').textContent = currentUser.email;
    document.getElementById('guestPhone').textContent = currentUser.phone || 'Not provided';
}

// Handle profile updates
function updateProfile() {
    const form = document.getElementById('editProfileForm'); // Assumes a form in a modal
    const updatedData = {
        fullName: form.querySelector('#editName').value,
        phone: form.querySelector('#editPhone').value,
        // Include password field if you have a password change feature
    };

    fetch(`http://localhost:8080/api/users/${currentUser.guestId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedData)
    })
    .then(response => response.json())
    .then(updatedUser => {
        // Update localStorage and UI
        localStorage.setItem('currentUser', JSON.stringify(updatedUser));
        currentUser = updatedUser;
        loadProfileDetails();
        bootstrap.Modal.getInstance(document.getElementById('editProfileModal')).hide();
        alert('Profile updated successfully!');
    })
    .catch(error => {
        console.error('Error updating profile:', error);
        alert('Failed to update profile.');
    });
}

function logout() {
    localStorage.removeItem('currentUser');
    window.location.href = 'index.html';
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    return new Date(dateStr).toLocaleDateString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}