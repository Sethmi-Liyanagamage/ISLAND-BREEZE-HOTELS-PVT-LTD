# Hotel Management System

A comprehensive hotel management system built with Spring Boot for managing bookings, rooms, guests, and payments.

## Features

- **Admin Management**: Admin registration, login, and profile management
- **Room Management**: Room availability, types, and pricing
- **Booking Management**: Guest bookings and reservations
- **Payment Processing**: Payment tracking and management
- **Guest Reviews**: Review system for guest feedback
- **Dashboard**: Comprehensive admin dashboard with statistics

## Technology Stack

- **Backend**: Spring Boot 3.2.4
- **Frontend**: HTML, CSS, JavaScript, Bootstrap 5
- **Data Storage**: File-based storage (CSV format)
- **Java Version**: 21

## Project Structure

```
src/main/java/com/hotelmanagementsystem/
├── config/          # Security configuration
├── controller/      # REST API controllers
├── dto/            # Data Transfer Objects
├── model/          # Entity models
├── service/        # Business logic services
└── util/           # Utility classes
```

## How to Run

### Prerequisites
- Java 21 or higher
- The application JAR file in the target directory

### Running the Application

1. **Using the batch file (Windows)**:
   ```bash
   run.bat
   ```

2. **Using Java directly**:
   ```bash
   java -jar target/hotel-management-system-1.0.0.jar
   ```

3. **Access the application**:
   - Open your web browser
   - Navigate to: http://localhost:8080

## API Endpoints

### Admin Endpoints
- `POST /api/admin/register` - Register new admin
- `POST /api/admin/login` - Admin login
- `GET /api/admin/dashboard/stats` - Get dashboard statistics
- `PUT /api/admin/profile/update` - Update admin profile

### Room Management
- `GET /api/rooms` - Get all rooms
- `POST /api/rooms` - Create new room
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room

### Booking Management
- `GET /api/bookings` - Get all bookings
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Cancel booking

### Payment Management
- `GET /api/payments` - Get all payments
- `POST /api/payments` - Process payment
- `PUT /api/payments/{id}` - Update payment

### Review Management
- `GET /api/reviews` - Get all reviews
- `POST /api/reviews` - Submit review
- `PUT /api/reviews/{id}` - Update review

## Data Storage

The application uses file-based storage with CSV format for:
- Admin data (`data/admins.txt`)
- User/Guest data (`data/users.txt`)
- Room data (`data/rooms.txt`)
- Booking data (`data/bookings.txt`)
- Payment data (`data/payments.txt`)
- Review data (`data/reviews.txt`)

## Configuration

Application configuration is in `src/main/resources/application.properties`:
- Server port: 8080
- Application name: hotel-management-system
- Data storage location: ./data

## Development

To modify the application:
1. Update the Java source files in `src/main/java/com/hotelmanagementsystem/`
2. Update HTML/CSS/JS files in `src/main/resources/static/`
3. Recompile and run the application

## Support

For issues or questions, please check the application logs or contact the development team.