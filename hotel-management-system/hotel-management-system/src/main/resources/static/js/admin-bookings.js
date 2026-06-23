// admin-bookings.js

document.addEventListener('DOMContentLoaded', function() {
    loadBookings();
});

// Load Bookings from the backend
function loadBookings(filter = 'all') {
    fetch('http://localhost:8080/api/bookings/all')
        .then(response => response.json())
        .then(data => {
            const bookingsTableBody = document.getElementById('bookingsTableBody');
            bookingsTableBody.innerHTML = '';

            if (!data || data.length === 0) {
                bookingsTableBody.innerHTML = `<tr><td colspan="6" class="text-center py-4"><p>No bookings found</p></td></tr>`;
                return;
            }

            updateBookingStats(data);

            const filteredBookings = filter === 'all' 
                ? data 
                : data.filter(booking => booking.status.toLowerCase() === filter.toLowerCase());

            filteredBookings.forEach(booking => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${booking.bookingId}</td>
                    <td>${booking.guestName || 'N/A'}</td>
                    <td>${booking.roomDetails || 'N/A'}</td>
                    <td>${formatBookingDates(booking.checkInDate, booking.checkOutDate)}</td>
                    <td><span class="badge bg-${getStatusColor(booking.status)}">${booking.status}</span></td>
                    <td>
                        <div class="btn-group">
                            <button class="btn btn-sm btn-light" onclick="updateBookingStatus('${booking.bookingId}', 'CONFIRMED')" ${booking.status === 'CONFIRMED' ? 'disabled' : ''} title="Confirm">
                                <i class="bi bi-check-circle"></i>
                            </button>
                            <button class="btn btn-sm btn-light" onclick="updateBookingStatus('${booking.bookingId}', 'CANCELED')" ${booking.status === 'CANCELED' ? 'disabled' : ''} title="Cancel">
                                <i class="bi bi-x-circle"></i>
                            </button>
                            <button class="btn btn-sm btn-light text-danger" onclick="deleteBooking('${booking.bookingId}')" title="Delete">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                `;
                bookingsTableBody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Error loading bookings:', error);
            document.getElementById('bookingsTableBody').innerHTML = `<tr><td colspan="6" class="text-center py-4 text-danger">Error loading bookings</td></tr>`;
        });
}

// Update the statistics cards on the page
function updateBookingStats(bookings) {
    const stats = bookings.reduce((acc, booking) => {
        acc.total++;
        const status = booking.status.toLowerCase();
        if (acc[status] !== undefined) {
            acc[status]++;
        }
        return acc;
    }, { total: 0, confirmed: 0, pending: 0, canceled: 0 });

    document.getElementById('totalBookings').textContent = stats.total;
    document.getElementById('confirmedBookings').textContent = stats.confirmed;
    document.getElementById('pendingBookings').textContent = stats.pending;
    document.getElementById('canceledBookings').textContent = stats.canceled;
}

// Add a new booking
function addBooking() {
    const form = document.getElementById('addBookingForm');
    const formData = new FormData(form);
    
    const bookingData = {
        bookingId: 'BK' + Date.now(),
        guestId: formData.get('guestId'),
        roomId: formData.get('roomId'),
        checkInDate: formData.get('checkInDate'),
        checkOutDate: formData.get('checkOutDate'),
        totalAmount: parseFloat(formData.get('totalAmount')) || 0, // Make sure to include total amount
        status: 'PENDING',
        guestName: formData.get('guestName'),
        roomDetails: formData.get('roomDetails')
    };

    // First create the booking
    fetch('http://localhost:8080/api/bookings', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || 'Failed to add booking') });
        }
        return response.json();
    })
    .then(createdBooking => {
        // Then create a pending payment for this booking
        const paymentData = {
            bookingId: createdBooking.bookingId,
            guestId: createdBooking.guestId,
            amount: createdBooking.totalAmount,
            paymentMethod: 'PENDING',
            status: 'PENDING',
            paymentDate: new Date().toISOString()
        };

        return fetch('http://localhost:8080/api/payments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(paymentData)
        });
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || 'Booking created but failed to create payment') });
        }
        return response.json();
    })
    .then(() => {
        bootstrap.Modal.getInstance(document.getElementById('addBookingModal')).hide();
        loadBookings();
        form.reset();
        alert('Booking created successfully with PENDING payment');
    })
    .catch(error => {
        console.error('Error in booking/payment process:', error);
        alert(error.message || 'Error processing booking. Please check console for details.');
    });
}

// Update the status of an existing booking
function updateBookingStatus(bookingId, newStatus) {
    fetch(`http://localhost:8080/api/bookings/${bookingId}`)
        .then(response => response.json())
        .then(booking => {
            booking.status = newStatus;
            return fetch(`http://localhost:8080/api/bookings/${bookingId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(booking)
            });
        })
        .then(response => {
            if (response.ok) {
                loadBookings();
            } else {
                alert('Failed to update booking status');
            }
        })
        .catch(error => {
            console.error('Error updating booking:', error);
            alert('Failed to update booking status');
        });
}

// Delete a booking
function deleteBooking(bookingId) {
    if (!confirm('Are you sure you want to delete this booking?')) return;

    fetch(`http://localhost:8080/api/bookings/${bookingId}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                loadBookings();
            } else {
                alert('Failed to delete booking');
            }
        })
        .catch(error => {
            console.error('Error deleting booking:', error);
            alert('Failed to delete booking');
        });
}

// Filter bookings by status
function filterBookings(status) {
    loadBookings(status);
}

// Helper function to format dates
function formatBookingDates(checkIn, checkOut) {
    const options = { month: 'short', day: 'numeric' };
    const checkInDate = new Date(checkIn).toLocaleDateString('en-US', options);
    const checkOutDate = new Date(checkOut).toLocaleDateString('en-US', options);
    return `${checkInDate} - ${checkOutDate}`;
}

// Helper function for status badge colors
function getStatusColor(status) {
    switch (status.toUpperCase()) {
        case 'CONFIRMED': return 'success';
        case 'PENDING': return 'warning';
        case 'CANCELED': return 'danger';
        default: return 'secondary';
    }
}