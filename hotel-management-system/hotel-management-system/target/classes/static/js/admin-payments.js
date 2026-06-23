// admin-payments.js

document.addEventListener('DOMContentLoaded', function() {
    loadPayments();
});

// Load Payments from the backend
function loadPayments(filter = 'all') {
    fetch('http://localhost:8080/api/payments')
        .then(response => response.json())
        .then(data => {
            const paymentsTableBody = document.getElementById('paymentsTableBody');
            paymentsTableBody.innerHTML = '';

            if (!data || data.length === 0) {
                paymentsTableBody.innerHTML = `<tr><td colspan="8" class="text-center py-4"><p>No payments found</p></td></tr>`;
                return;
            }

            updatePaymentStats(data);

            const filteredPayments = filter === 'all' 
                ? data 
                : data.filter(p => p.status.toLowerCase() === filter.toLowerCase());

            filteredPayments.forEach(payment => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${payment.paymentId}</td>
                    <td>${payment.bookingId}</td>
                    <td>${payment.guestName || 'N/A'}</td>
                    <td>$${payment.amount.toFixed(2)}</td>
                    <td><span class="badge bg-${getStatusColor(payment.status)}">${payment.status}</span></td>
                    <td>${payment.paymentMethod ? formatPaymentMethod(payment.paymentMethod) : 'N/A'}</td>
                    <td>${formatDateTime(payment.paymentDate)}</td>
                    <td>
                        <div class="btn-group">
                            ${payment.status === 'PAID' ? `
                            <button class="btn btn-sm btn-light" onclick="updatePaymentStatus('${payment.paymentId}', 'REFUNDED')" title="Refund Payment">
                                <i class="bi bi-arrow-counterclockwise"></i>
                            </button>` : ''}
                            <button class="btn btn-sm btn-light text-danger" onclick="deletePayment('${payment.paymentId}')" title="Delete Payment">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                `;
                paymentsTableBody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Error loading payments:', error);
            paymentsTableBody.innerHTML = `<tr><td colspan="8" class="text-center py-4 text-danger">Error loading payments</td></tr>`;
        });
}

// Update payment statistics cards
function updatePaymentStats(payments) {
    const stats = payments.reduce((acc, payment) => {
        acc.total++;
        const status = payment.status.toLowerCase();
        if (acc[status] !== undefined) {
            acc[status]++;
        }
        return acc;
    }, { total: 0, paid: 0, pending: 0, refunded: 0 });

    document.getElementById('totalPayments').textContent = stats.total;
    document.getElementById('paidPayments').textContent = stats.paid;
    document.getElementById('pendingPayments').textContent = stats.pending;
    document.getElementById('refundedPayments').textContent = stats.refunded;
}

// Update the status of a payment (e.g., to REFUNDED)
function updatePaymentStatus(paymentId, newStatus) {
    if (!confirm(`Are you sure you want to mark this payment as ${newStatus}?`)) return;

    fetch(`http://localhost:8080/api/payments/${paymentId}`)
        .then(response => response.json())
        .then(payment => {
            payment.status = newStatus;
            return fetch(`http://localhost:8080/api/payments/${paymentId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payment)
            });
        })
        .then(response => {
            if (response.ok) {
                loadPayments();
            } else {
                alert('Failed to update payment status');
            }
        })
        .catch(error => {
            console.error('Error updating payment:', error);
            alert('Failed to update payment status');
        });
}

// Delete a payment record
function deletePayment(paymentId) {
    if (!confirm('Are you sure you want to delete this payment record?')) return;

    fetch(`http://localhost:8080/api/payments/${paymentId}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                loadPayments();
            } else {
                alert('Failed to delete payment');
            }
        })
        .catch(error => {
            console.error('Error deleting payment:', error);
            alert('Failed to delete payment');
        });
}


// Filter payments table by status
function filterPayments(status) {
    loadPayments(status);
}

// Helper function to format date and time
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    return new Date(dateTimeStr).toLocaleString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}

// Helper function for status badge colors
function getStatusColor(status) {
    switch (status.toUpperCase()) {
        case 'PAID': return 'success';
        case 'PENDING': return 'warning';
        case 'REFUNDED': return 'info';
        case 'CANCELED': return 'danger';
        default: return 'secondary';
    }
}

// Helper function to format payment method names
function formatPaymentMethod(method) {
    return method.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase());
}