// admin-rooms.js

document.addEventListener('DOMContentLoaded', function() {
    loadRooms();
});

// Load all rooms from the backend
function loadRooms() {
    fetch('http://localhost:8080/api/rooms')
        .then(response => response.json())
        .then(data => {
            const roomGrid = document.getElementById('roomGrid'); // Assumes you have a container with this ID
            const emptyState = document.getElementById('roomEmptyState'); // Assumes an empty state element
            
            if (!data || data.length === 0) {
                roomGrid.innerHTML = '';
                if (emptyState) emptyState.style.display = 'block';
                return;
            }

            if (emptyState) emptyState.style.display = 'none';
            roomGrid.innerHTML = '';

            data.forEach(room => {
                const col = document.createElement('div');
                col.className = 'col-md-6 col-lg-4';
                col.innerHTML = `
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <h5 class="card-title">${room.roomType} - #${room.roomNumber}</h5>
                                <span class="badge bg-${getStatusColor(room.status)}">${room.status}</span>
                            </div>
                            <p class="card-text">Price: <strong>$${room.pricePerNight.toFixed(2)}</strong> / night</p>
                            <p class="text-muted small">${room.amenities.join(', ')}</p>
                            <div class="btn-group mt-2">
                                <button class="btn btn-sm btn-light" onclick="editRoom('${room.roomId}')">Edit</button>
                                <button class="btn btn-sm btn-light text-danger" onclick="deleteRoom('${room.roomId}')">Delete</button>
                            </div>
                        </div>
                    </div>
                `;
                roomGrid.appendChild(col);
            });
        })
        .catch(error => {
            console.error('Error loading rooms:', error);
            if (document.getElementById('roomEmptyState')) {
                document.getElementById('roomEmptyState').innerHTML = '<p class="text-danger">Error loading rooms.</p>';
                document.getElementById('roomEmptyState').style.display = 'block';
            }
        });
}

// Add a new room to the hotel
function addRoom() {
    const form = document.getElementById('addRoomForm'); // Assumes a form with this ID in a modal
    const formData = new FormData(form);
    const roomData = {
        roomNumber: formData.get('roomNumber'),
        roomType: formData.get('roomType'),
        pricePerNight: parseFloat(formData.get('pricePerNight')),
        amenities: formData.get('amenities').split(',').map(s => s.trim()),
        status: 'AVAILABLE'
    };

    fetch('http://localhost:8080/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(roomData)
    })
    .then(response => response.ok ? response.json() : Promise.reject('Failed to add room'))
    .then(() => {
        bootstrap.Modal.getInstance(document.getElementById('addRoomModal')).hide();
        loadRooms();
        form.reset();
    })
    .catch(error => {
        console.error('Error adding room:', error);
        alert('Failed to add room');
    });
}

// Logic for editing a room (you would populate a modal here)
function editRoom(roomId) {
    console.log('Editing room:', roomId);
    // 1. Fetch room data by ID
    // 2. Populate the 'addRoomModal' with the fetched data
    // 3. Change the modal's save button to call an 'updateRoom(roomId)' function
    // 4. Show the modal
}

// Delete a room
function deleteRoom(roomId) {
    if (!confirm('Are you sure you want to delete this room?')) return;

    fetch(`http://localhost:8080/api/rooms/${roomId}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                loadRooms();
            } else {
                alert('Failed to delete room. It may be part of an active booking.');
            }
        })
        .catch(error => {
            console.error('Error deleting room:', error);
            alert('Failed to delete room.');
        });
}

// Helper function for status colors
function getStatusColor(status) {
    switch (status.toUpperCase()) {
        case 'AVAILABLE': return 'success';
        case 'BOOKED': return 'danger';
        case 'MAINTENANCE': return 'warning';
        default: return 'secondary';
    }
}