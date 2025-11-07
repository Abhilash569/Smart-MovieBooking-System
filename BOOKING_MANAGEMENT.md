# Booking Management Feature

## Overview
The Smart Movie Booking System now includes comprehensive booking management capabilities, allowing users to view, modify, and cancel their bookings.

## Features Added

### 1. View My Bookings
- Users can view all their bookings in a dedicated "My Bookings" section
- Bookings display:
  - Booking ID
  - Movie and Theatre information
  - Selected seats
  - Total price
  - Booking timestamp
  - Current status (CONFIRMED, MODIFIED, or CANCELLED)

### 2. Cancel Bookings
- Users can cancel confirmed or modified bookings
- Cancelled bookings are marked with status "CANCELLED"
- Cancelled seats become available for other users
- Confirmation dialog prevents accidental cancellations

### 3. Modify Bookings
- Users can change their seat selection for existing bookings
- The seat selection modal pre-selects current seats
- System validates new seat availability
- Price is automatically recalculated based on new seat count
- Modified bookings are marked with status "MODIFIED"

## Technical Implementation

### Backend Changes

#### 1. Model Updates (Booking.java)
- Added `status` field to track booking state
- Updated constructors to include status parameter

#### 2. DAO Updates (BookingDAO.java)
- `cancel()` - Marks booking as CANCELLED
- `modifySeats()` - Updates seats and marks as MODIFIED
- `getOccupiedSeats()` - Excludes cancelled bookings
- Updated all queries to handle status field

#### 3. Servlet Updates (BookingServlet.java)
- `doGet()` - Added "myBookings" action to fetch user bookings
- `doPut()` - Handles booking modifications
- `doDelete()` - Handles booking cancellations
- Added authorization checks for all operations

#### 4. Database Schema (DBConnection.java & init.sql)
- Added `status` column to bookings table
- Default value: 'CONFIRMED'
- Index added for performance

### Frontend Changes

#### 1. Navigation (index.jsp)
- Added "My Bookings" link (visible only when logged in)

#### 2. My Bookings Section (index.jsp)
- New section to display user bookings
- Cards show booking details with action buttons

#### 3. JavaScript Functions (app.js)
- `loadMyBookings()` - Fetches and displays user bookings
- `displayBookings()` - Renders booking cards
- `cancelBooking()` - Sends DELETE request to cancel
- `modifyBooking()` - Opens seat selection for modification
- Updated `confirmBooking()` - Handles both new bookings and modifications
- Updated `showSection()` - Loads bookings when section is shown
- Updated `updateNavbar()` - Shows/hides My Bookings link

## Usage

### Viewing Bookings
1. Login to your account
2. Click "My Bookings" in the navigation menu
3. View all your bookings with their current status

### Cancelling a Booking
1. Go to "My Bookings"
2. Find the booking you want to cancel
3. Click "Cancel Booking" button
4. Confirm the cancellation in the dialog
5. Booking status changes to "CANCELLED"

### Modifying a Booking
1. Go to "My Bookings"
2. Find the booking you want to modify
3. Click "Modify Seats" button
4. Select new seats in the seat selection modal
5. Click "Confirm Booking" to save changes
6. Booking status changes to "MODIFIED"

## API Endpoints

### GET /book?action=myBookings
- Returns all bookings for the logged-in user
- Requires authentication

### DELETE /book?bookingId={id}
- Cancels a booking
- Requires authentication and ownership verification

### PUT /book?bookingId={id}&seats={seats}
- Modifies booking seats
- Requires authentication and ownership verification
- Validates seat availability

## Database Schema

```sql
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    theatre_id INT NOT NULL,
    seats VARCHAR(255) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theatre_id) REFERENCES theatres(id)
);
```

## Status Values
- **CONFIRMED** - Initial booking status
- **MODIFIED** - Booking has been modified
- **CANCELLED** - Booking has been cancelled

## Security Features
- All operations require user authentication
- Users can only view/modify/cancel their own bookings
- Seat availability is validated before modifications
- Authorization checks prevent unauthorized access

## Testing

To test the feature:
1. Login with demo account (demo@email.com / password123)
2. Book some movie tickets
3. Navigate to "My Bookings"
4. Try modifying seats
5. Try cancelling a booking
6. Verify status changes are reflected

## Notes
- Cancelled bookings cannot be modified or re-confirmed
- Modified bookings can be cancelled or modified again
- Seat availability is checked in real-time during modifications
- Price is automatically recalculated when modifying seats
