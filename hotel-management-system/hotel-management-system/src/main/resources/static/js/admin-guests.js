// admin-guests.js

document.addEventListener('DOMContentLoaded', function() {
    loadGuests();
});

// Load all guests (users with 'GUEST' role)
function loadGuests() {
    fetch('http://localhost:8080/api/users/guests') // Assuming an endpoint that returns only guests
        .then(response => response.json())
        .then(guests => {
            const tbody = document.getElementById('guestsTableBody');
            tbody.innerHTML = '';

            if (!guests || guests.length === 0) {
                tbody.innerHTML = `<tr><td colspan="5" class="text-center py-4"><p>No guests found</p></td></tr>`;
                return;
            }

            updateGuestStats(guests);

            guests.forEach(guest => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${guest.fullName}</td>
                    <td>${guest.email}</td>
                    <td>${formatDate(guest.joinDate)}</td>
                    <td><span class="badge bg-${guest.isActive ? 'success' : 'secondary'}">${guest.isActive ? 'Active' : 'Inactive'}</span></td>
                    <td>
                        <div class="btn-group">
                            <button class="btn btn-sm btn-light" onclick="viewGuestDetails('${guest.guestId}')">View</button>
                            <button class="btn btn-sm btn-light text-danger" onclick="deleteGuest('${guest.guestId}')">Delete</button>
                        </div>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Error loading guests:', error);
            document.getElementById('guestsTableBody').innerHTML = `<tr><td colspan="5" class="text-center py-4 text-danger">Error loading guests</td></tr>`;
        });
}

// Update guest statistics cards
function updateGuestStats(guests) {
    const activeGuests = guests.filter(g => g.isActive).length;
    const newToday = guests.filter(g => isToday(new Date(g.joinDate))).length;

    document.getElementById('totalActiveGuests').textContent = activeGuests;
    document.getElementById('newGuestsToday').textContent = newToday;
    document.getElementById('inactiveGuests').textContent = guests.length - activeGuests;
}

// Placeholder for viewing guest details
function viewGuestDetails(guestId) {
    console.log('Viewing details for guest:', guestId);
    // You would typically open a modal with the guest's full booking history and profile info.
}

// Delete a guest
function deleteGuest(guestId) {
    if (!confirm('Are you sure you want to delete this guest? This cannot be undone.')) return;

    fetch(`http://localhost:8080/api/users/${guestId}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                loadGuests();
            } else {
                alert('Failed to delete guest.');
            }
        })
        .catch(error => {
            console.error('Error deleting guest:', error);
            alert('Failed to delete guest.');
        });
}

// Helper function to format a date
function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    return new Date(dateStr).toLocaleDateString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}

// Helper function to check if a date is today
function isToday(someDate) {
    const today = new Date();
    return someDate.getDate() === today.getDate() &&
           someDate.getMonth() === today.getMonth() &&
           someDate.getFullYear() === today.getFullYear();
}