// Global variables
const GOOGLE_API_KEY = "AIzaSyCdAOE-KDvjnttCz_VBSfRWipG0sdcCu7w";
let currentUser = null;
let movies = [];
let selectedSeats = [];
let currentMovieId = null;
let currentTheatreId = null;
let map = null;

// Initialize app on page load
document.addEventListener('DOMContentLoaded', function() {
    initApp();
});

// Initialize application
function initApp() {
    // Check if user is logged in
    const userData = sessionStorage.getItem('user');
    if (userData) {
        currentUser = JSON.parse(userData);
        updateNavbar();
    }
    
    // Set up navigation links
    document.querySelectorAll('.nav-link[data-section]').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const section = this.getAttribute('data-section');
            showSection(section);
        });
    });
    
    // Set up login link
    document.getElementById('loginLink').addEventListener('click', function(e) {
        e.preventDefault();
        if (currentUser) {
            logout();
        } else {
            showLogin();
        }
    });
    
    // Set up login form
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        handleLogin();
    });
    
    // Set up signup form
    document.getElementById('signupForm').addEventListener('submit', function(e) {
        e.preventDefault();
        handleSignup();
    });
    
    // Load movies on initial page load
    loadMovies();
}

// Show specific section
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('section').forEach(section => {
        section.style.display = 'none';
    });
    
    // Show selected section
    const section = document.getElementById(sectionId);
    if (section) {
        section.style.display = 'block';
        
        // Load data based on section
        if (sectionId === 'movies') {
            loadMovies();
        } else if (sectionId === 'theatres') {
            // Wait a bit for Google Maps to load if needed
            setTimeout(() => {
                loadTheatres();
            }, 500);
        }
    }
}

// Load movies from server
function loadMovies() {
    const type = document.getElementById('movieType') ? document.getElementById('movieType').value : 'now_playing';
    const language = document.getElementById('languageFilter') ? document.getElementById('languageFilter').value : '';
    
    let url = `/smart-booking/movies?type=${type}`;
    if (language) {
        url += `&language=${language}`;
    }
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            movies = data;
            renderMovies(movies);
        })
        .catch(error => {
            console.error('Error loading movies:', error);
            alert('Failed to load movies. Please check your internet connection and API key.');
        });
}

// Render movies in grid
function renderMovies(movieList) {
    const movieGrid = document.getElementById('movieGrid');
    movieGrid.innerHTML = '';
    
    movieList.forEach(movie => {
        const col = document.createElement('div');
        col.className = 'col-md-3 col-sm-6';
        
        col.innerHTML = `
            <div class="card movie-card h-100">
                <img src="${movie.posterUrl || 'https://via.placeholder.com/300x450'}" class="card-img-top" alt="${movie.title}">
                <div class="card-body">
                    <h5 class="card-title">${movie.title}</h5>
                    <p class="card-text">
                        <small class="text-muted">${movie.genre} | ${movie.language}</small><br>
                        <strong>Rating:</strong> ${movie.rating}/10
                    </p>
                    <button class="btn btn-sm btn-outline-primary me-2" onclick="showMovieDetails(${movie.id})">Details</button>
                    <button class="btn btn-sm btn-primary" onclick="bookMovie(${movie.id})">Book Now</button>
                </div>
            </div>
        `;
        
        movieGrid.appendChild(col);
    });
}

// Show movie details modal
function showMovieDetails(movieId) {
    const movie = movies.find(m => m.id === movieId);
    if (movie) {
        document.getElementById('movieTitle').textContent = movie.title;
        document.getElementById('moviePoster').src = movie.posterUrl || 'https://via.placeholder.com/300x450';
        document.getElementById('movieGenre').textContent = movie.genre;
        document.getElementById('movieRating').textContent = movie.rating;
        document.getElementById('movieDuration').textContent = movie.duration;
        document.getElementById('movieLanguage').textContent = movie.language;
        document.getElementById('movieDescription').textContent = movie.description;
        
        const modal = new bootstrap.Modal(document.getElementById('movieDetailsModal'));
        modal.show();
    }
}

// Book movie - check authentication first
function bookMovie(movieId) {
    if (!currentUser) {
        showLogin();
        return;
    }
    
    // Store movie ID and show theatre selection
    currentMovieId = movieId;
    showTheatreSelection();
}

// Load theatres from server
function loadTheatres() {
    fetch('/smart-booking/theatres')
        .then(response => response.json())
        .then(data => {
            renderTheatres(data);
            // Use the new map function from maps-fix.js
            if (typeof setupGoogleMap === 'function') {
                setupGoogleMap(data);
            } else {
                initGoogleMap(data);
            }
        })
        .catch(error => {
            console.error('Error loading theatres:', error);
        });
}

// Render theatres in list
function renderTheatres(theatres) {
    const theatreList = document.getElementById('theatreList');
    theatreList.innerHTML = '';
    
    theatres.forEach(theatre => {
        const col = document.createElement('div');
        col.className = 'col-md-4';
        
        col.innerHTML = `
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">${theatre.name}</h5>
                    <p class="card-text">${theatre.address}</p>
                    <button class="btn btn-primary btn-sm" onclick="selectTheatre(${theatre.id})">Show Movies</button>
                </div>
            </div>
        `;
        
        theatreList.appendChild(col);
    });
}

// Initialize Google Map
function initGoogleMap(theatres) {
    if (typeof google === 'undefined' || !google.maps) {
        console.log('Google Maps not loaded yet, retrying...');
        // Retry after 1 second
        setTimeout(() => initGoogleMap(theatres), 1000);
        return;
    }
    
    const mapDiv = document.getElementById('map');
    if (!mapDiv) {
        console.error('Map div not found');
        return;
    }
    
    try {
        // Default center (Mumbai)
        const center = { lat: 19.0760, lng: 72.8777 };
        
        // Create map
        map = new google.maps.Map(mapDiv, {
            zoom: 12,
            center: center,
            mapTypeControl: true,
            streetViewControl: true,
            fullscreenControl: true
        });
        
        // Add markers for theatres
        if (theatres && theatres.length > 0) {
            theatres.forEach(theatre => {
                const marker = new google.maps.Marker({
                    position: { lat: theatre.latitude, lng: theatre.longitude },
                    map: map,
                    title: theatre.name,
                    animation: google.maps.Animation.DROP
                });
                
                // Add info window
                const infoWindow = new google.maps.InfoWindow({
                    content: `<div style="padding: 10px;">
                        <h6 style="margin: 0 0 5px 0;">${theatre.name}</h6>
                        <p style="margin: 0; font-size: 14px;">${theatre.address}</p>
                    </div>`
                });
                
                marker.addListener('click', () => {
                    infoWindow.open(map, marker);
                });
            });
            
            console.log('✅ Google Maps initialized with', theatres.length, 'theatres');
        }
    } catch (error) {
        console.error('Error initializing Google Maps:', error);
        mapDiv.innerHTML = '<div class="alert alert-warning m-3">Unable to load Google Maps. Please check your internet connection.</div>';
    }
}

// Get current location
function getCurrentLocation() {
    // Use the new handler from maps-fix.js if available
    if (typeof handleCurrentLocation === 'function') {
        handleCurrentLocation();
    } else {
        // Fallback
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                position => {
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    findNearbyTheatres(lat, lng);
                },
                error => {
                    alert('Unable to get your location');
                }
            );
        } else {
            alert('Geolocation is not supported by your browser');
        }
    }
}

// Find nearby theatres
function findNearbyTheatres(lat, lng) {
    fetch('/smart-booking/theatres', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `latitude=${lat}&longitude=${lng}&radius=10`
    })
    .then(response => response.json())
    .then(data => {
        renderTheatres(data);
    })
    .catch(error => {
        console.error('Error finding nearby theatres:', error);
    });
}

// Select theatre
function selectTheatre(theatreId) {
    currentTheatreId = theatreId;
    showSection('movies');
}

// Show login modal
function showLogin() {
    const signupModal = bootstrap.Modal.getInstance(document.getElementById('signupModal'));
    if (signupModal) signupModal.hide();
    
    const modal = new bootstrap.Modal(document.getElementById('loginModal'));
    modal.show();
}

// Show signup modal
function showSignup() {
    const loginModal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
    if (loginModal) loginModal.hide();
    
    const modal = new bootstrap.Modal(document.getElementById('signupModal'));
    modal.show();
}

// Handle login
function handleLogin() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    fetch('/smart-booking/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `action=login&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            currentUser = data.user;
            sessionStorage.setItem('user', JSON.stringify(data.user));
            updateNavbar();
            
            const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
            modal.hide();
            
            document.getElementById('loginForm').reset();
            document.getElementById('loginError').style.display = 'none';
        } else {
            document.getElementById('loginError').textContent = data.message;
            document.getElementById('loginError').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error during login:', error);
        document.getElementById('loginError').textContent = 'An error occurred';
        document.getElementById('loginError').style.display = 'block';
    });
}

// Handle signup
function handleSignup() {
    const name = document.getElementById('signupName').value;
    const email = document.getElementById('signupEmail').value;
    const password = document.getElementById('signupPassword').value;
    
    if (password.length < 6) {
        document.getElementById('signupError').textContent = 'Password must be at least 6 characters';
        document.getElementById('signupError').style.display = 'block';
        return;
    }
    
    fetch('/smart-booking/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `action=signup&name=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            currentUser = data.user;
            sessionStorage.setItem('user', JSON.stringify(data.user));
            updateNavbar();
            
            const modal = bootstrap.Modal.getInstance(document.getElementById('signupModal'));
            modal.hide();
            
            document.getElementById('signupForm').reset();
            document.getElementById('signupError').style.display = 'none';
        } else {
            document.getElementById('signupError').textContent = data.message;
            document.getElementById('signupError').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error during signup:', error);
        document.getElementById('signupError').textContent = 'An error occurred';
        document.getElementById('signupError').style.display = 'block';
    });
}

// Update navbar based on login status
function updateNavbar() {
    const loginLink = document.getElementById('loginLink');
    if (currentUser) {
        loginLink.textContent = `Hi, ${currentUser.name}`;
    } else {
        loginLink.textContent = 'Login';
    }
}

// Logout
function logout() {
    currentUser = null;
    sessionStorage.removeItem('user');
    updateNavbar();
    showSection('home');
}

// Show seat selection modal
function showSeatSelection(movieId, theatreId) {
    currentMovieId = movieId;
    currentTheatreId = theatreId;
    selectedSeats = [];
    
    // Fetch occupied seats
    fetch(`/smart-booking/book?movieId=${movieId}&theatreId=${theatreId}`)
        .then(response => response.json())
        .then(occupiedSeats => {
            renderSeatGrid(occupiedSeats);
            updateTotalPrice();
            
            const modal = new bootstrap.Modal(document.getElementById('seatSelectionModal'));
            modal.show();
        })
        .catch(error => {
            console.error('Error loading seats:', error);
            renderSeatGrid([]);
            updateTotalPrice();
            
            const modal = new bootstrap.Modal(document.getElementById('seatSelectionModal'));
            modal.show();
        });
}

// Render seat grid
function renderSeatGrid(occupiedSeats) {
    const seatGrid = document.getElementById('seatGrid');
    seatGrid.innerHTML = '';
    
    const rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
    const cols = 10;
    
    rows.forEach(row => {
        const rowDiv = document.createElement('div');
        rowDiv.className = 'seat-row';
        
        for (let col = 1; col <= cols; col++) {
            const seatId = `${row}${col}`;
            const seatBtn = document.createElement('button');
            seatBtn.className = 'seat-btn';
            seatBtn.textContent = seatId;
            seatBtn.setAttribute('data-seat', seatId);
            
            if (occupiedSeats.includes(seatId)) {
                seatBtn.classList.add('occupied');
                seatBtn.disabled = true;
            } else {
                seatBtn.addEventListener('click', function() {
                    toggleSeat(seatId);
                });
            }
            
            rowDiv.appendChild(seatBtn);
        }
        
        seatGrid.appendChild(rowDiv);
    });
}

// Toggle seat selection
function toggleSeat(seatId) {
    const seatBtn = document.querySelector(`[data-seat="${seatId}"]`);
    
    if (selectedSeats.includes(seatId)) {
        selectedSeats = selectedSeats.filter(s => s !== seatId);
        seatBtn.classList.remove('selected');
    } else {
        selectedSeats.push(seatId);
        seatBtn.classList.add('selected');
    }
    
    updateTotalPrice();
}

// Calculate and update total price
function updateTotalPrice() {
    const pricePerSeat = 150;
    const total = selectedSeats.length * pricePerSeat;
    document.getElementById('totalPrice').textContent = total;
}

// Confirm booking
function confirmBooking() {
    if (selectedSeats.length === 0) {
        alert('Please select at least one seat');
        return;
    }
    
    const seats = selectedSeats.join(',');
    
    fetch('/smart-booking/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `movieId=${currentMovieId}&theatreId=${currentTheatreId}&seats=${seats}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(`Booking confirmed! Total: ₹${data.totalPrice}`);
            
            const modal = bootstrap.Modal.getInstance(document.getElementById('seatSelectionModal'));
            modal.hide();
            
            selectedSeats = [];
        } else {
            alert('Booking failed: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error during booking:', error);
        alert('An error occurred while booking');
    });
}


// Change movie type (popular, now_playing, etc.)
function changeMovieType() {
    const type = document.getElementById('movieType').value;
    const language = document.getElementById('languageFilter').value;
    
    fetch(`/smart-booking/movies?type=${type}&language=${language}`)
        .then(response => response.json())
        .then(data => {
            movies = data;
            filterMovies();
        })
        .catch(error => {
            console.error('Error loading movies:', error);
        });
}

// Filter movies by language and genre
function filterMovies() {
    const language = document.getElementById('languageFilter').value;
    const genre = document.getElementById('genreFilter').value;
    const searchTerm = document.getElementById('movieSearch').value.toLowerCase();
    
    let filtered = movies;
    
    // Filter by language
    if (language) {
        filtered = filtered.filter(movie => 
            movie.language && movie.language.toLowerCase() === language
        );
    }
    
    // Filter by genre
    if (genre) {
        filtered = filtered.filter(movie => 
            movie.genre && movie.genre.toLowerCase().includes(getGenreNameFromId(genre).toLowerCase())
        );
    }
    
    // Filter by search term
    if (searchTerm) {
        filtered = filtered.filter(movie => 
            movie.title.toLowerCase().includes(searchTerm)
        );
    }
    
    renderMovies(filtered);
}

// Search movies as user types
function searchMovies() {
    filterMovies();
}

// Helper function to get genre name from ID
function getGenreNameFromId(genreId) {
    const genres = {
        '28': 'Action',
        '35': 'Comedy',
        '18': 'Drama',
        '27': 'Horror',
        '10749': 'Romance',
        '878': 'Sci-Fi',
        '53': 'Thriller',
        '16': 'Animation'
    };
    return genres[genreId] || '';
}


// Load user bookings
function loadMyBookings() {
    if (!currentUser) {
        alert('Please login to view your bookings');
        showLogin();
        return;
    }
    
    fetch('book?action=myBookings')
        .then(response => response.json())
        .then(bookings => {
            displayBookings(bookings);
        })
        .catch(error => {
            console.error('Error loading bookings:', error);
            alert('Failed to load bookings');
        });
}

// Display bookings
function displayBookings(bookings) {
    const bookingsList = document.getElementById('bookingsList');
    
    if (bookings.length === 0) {
        bookingsList.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info">
                    <h5>No bookings found</h5>
                    <p>You haven't made any bookings yet. Browse movies and book your tickets!</p>
                    <button class="btn btn-primary" onclick="showSection('movies')">Browse Movies</button>
                </div>
            </div>
        `;
        return;
    }
    
    bookingsList.innerHTML = bookings.map(booking => `
        <div class="col-md-6 mb-4">
            <div class="card ${booking.status === 'CANCELLED' ? 'border-danger' : ''}">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <h5 class="card-title">Booking #${booking.id}</h5>
                        <span class="badge ${getStatusBadgeClass(booking.status)}">${booking.status}</span>
                    </div>
                    <p class="card-text">
                        <strong>Movie ID:</strong> ${booking.movieId}<br>
                        <strong>Theatre ID:</strong> ${booking.theatreId}<br>
                        <strong>Seats:</strong> ${booking.seats}<br>
                        <strong>Total Price:</strong> ₹${booking.totalPrice}<br>
                        <strong>Booked At:</strong> ${new Date(booking.bookedAt).toLocaleString()}
                    </p>
                    ${booking.status === 'CONFIRMED' || booking.status === 'MODIFIED' ? `
                        <div class="btn-group w-100" role="group">
                            <button class="btn btn-warning" onclick="modifyBooking(${booking.id}, ${booking.movieId}, ${booking.theatreId}, '${booking.seats}')">
                                Modify Seats
                            </button>
                            <button class="btn btn-danger" onclick="cancelBooking(${booking.id})">
                                Cancel Booking
                            </button>
                        </div>
                    ` : ''}
                </div>
            </div>
        </div>
    `).join('');
}

// Get status badge class
function getStatusBadgeClass(status) {
    switch(status) {
        case 'CONFIRMED': return 'bg-success';
        case 'MODIFIED': return 'bg-info';
        case 'CANCELLED': return 'bg-danger';
        default: return 'bg-secondary';
    }
}

// Cancel booking
function cancelBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    fetch(`book?bookingId=${bookingId}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            loadMyBookings(); // Reload bookings
        } else {
            alert(data.message || 'Failed to cancel booking');
        }
    })
    .catch(error => {
        console.error('Error cancelling booking:', error);
        alert('An error occurred while cancelling the booking');
    });
}

// Modify booking
function modifyBooking(bookingId, movieId, theatreId, currentSeats) {
    currentMovieId = movieId;
    currentTheatreId = theatreId;
    
    // Load occupied seats
    fetch(`book?movieId=${movieId}&theatreId=${theatreId}`)
        .then(response => response.json())
        .then(occupiedSeats => {
            // Remove current booking's seats from occupied list
            const currentSeatsArray = currentSeats.split(',').map(s => s.trim());
            const filteredOccupied = occupiedSeats.filter(seat => !currentSeatsArray.includes(seat));
            
            // Generate seat grid
            generateSeatGrid(filteredOccupied);
            
            // Pre-select current seats
            selectedSeats = [...currentSeatsArray];
            currentSeatsArray.forEach(seat => {
                const seatElement = document.querySelector(`[data-seat="${seat}"]`);
                if (seatElement) {
                    seatElement.classList.add('selected');
                }
            });
            
            updateTotalPrice();
            
            // Store booking ID for modification
            window.currentBookingId = bookingId;
            window.isModifying = true;
            
            // Show modal
            const modal = new bootstrap.Modal(document.getElementById('seatSelectionModal'));
            modal.show();
        })
        .catch(error => {
            console.error('Error loading seats:', error);
            alert('Failed to load seat information');
        });
}

// Update confirmBooking to handle modifications
const originalConfirmBooking = window.confirmBooking;
window.confirmBooking = function() {
    if (window.isModifying && window.currentBookingId) {
        // Modify existing booking
        if (selectedSeats.length === 0) {
            alert('Please select at least one seat');
            return;
        }
        
        const seats = selectedSeats.join(',');
        
        fetch(`book?bookingId=${window.currentBookingId}&seats=${encodeURIComponent(seats)}`, {
            method: 'PUT'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                bootstrap.Modal.getInstance(document.getElementById('seatSelectionModal')).hide();
                window.isModifying = false;
                window.currentBookingId = null;
                loadMyBookings();
            } else {
                alert(data.message || 'Failed to modify booking');
            }
        })
        .catch(error => {
            console.error('Error modifying booking:', error);
            alert('An error occurred while modifying the booking');
        });
    } else {
        // Call original booking function
        if (originalConfirmBooking) {
            originalConfirmBooking();
        }
    }
};

// Update showSection to load bookings when My Bookings is shown
const originalShowSection = window.showSection;
window.showSection = function(sectionName) {
    if (originalShowSection) {
        originalShowSection(sectionName);
    }
    
    if (sectionName === 'myBookings') {
        loadMyBookings();
    }
};

// Update updateNavbar to show My Bookings link when logged in
const originalUpdateNavbar = window.updateNavbar;
window.updateNavbar = function() {
    if (originalUpdateNavbar) {
        originalUpdateNavbar();
    }
    
    const myBookingsNav = document.getElementById('myBookingsNav');
    if (currentUser) {
        myBookingsNav.style.display = 'block';
    } else {
        myBookingsNav.style.display = 'none';
    }
};


// Payment and Ticket Generation Functions

// Proceed to payment
function proceedToPayment() {
    if (selectedSeats.length === 0) {
        alert('Please select at least one seat');
        return;
    }
    
    const totalPrice = selectedSeats.length * 150;
    
    // Update payment modal
    document.getElementById('paymentSeats').textContent = selectedSeats.join(', ');
    document.getElementById('paymentAmount').textContent = totalPrice;
    document.getElementById('payButtonAmount').textContent = totalPrice;
    
    // Hide seat selection modal and show payment modal
    bootstrap.Modal.getInstance(document.getElementById('seatSelectionModal')).hide();
    const paymentModal = new bootstrap.Modal(document.getElementById('paymentModal'));
    paymentModal.show();
}

// Process payment
function processPayment() {
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    
    // Show loading
    const payButton = event.target;
    const originalText = payButton.innerHTML;
    payButton.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Processing...';
    payButton.disabled = true;
    
    // Simulate payment processing
    setTimeout(() => {
        // Create booking after payment
        confirmBookingWithPayment(paymentMethod);
    }, 1500);
}

// Confirm booking with payment
function confirmBookingWithPayment(paymentMethod) {
    if (!currentUser) {
        alert('Please login to book tickets');
        showLogin();
        return;
    }
    
    if (selectedSeats.length === 0) {
        alert('Please select at least one seat');
        return;
    }
    
    const seats = selectedSeats.join(',');
    
    fetch('/smart-booking/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `movieId=${currentMovieId}&theatreId=${currentTheatreId}&seats=${encodeURIComponent(seats)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Hide payment modal
            bootstrap.Modal.getInstance(document.getElementById('paymentModal')).hide();
            
            // Generate and show ticket
            generateTicket({
                bookingId: Date.now(), // Use timestamp as booking ID for demo
                seats: selectedSeats,
                totalPrice: data.totalPrice,
                paymentMethod: paymentMethod,
                movieId: currentMovieId,
                theatreId: currentTheatreId
            });
            
            // Reset selection
            selectedSeats = [];
        } else {
            alert(data.message || 'Booking failed. Please try again.');
            // Re-enable pay button
            const payButton = document.querySelector('#paymentModal .btn-success');
            payButton.innerHTML = '<i class="bi bi-lock"></i> Pay ₹' + document.getElementById('payButtonAmount').textContent;
            payButton.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error creating booking:', error);
        alert('An error occurred while processing your booking');
        // Re-enable pay button
        const payButton = document.querySelector('#paymentModal .btn-success');
        payButton.innerHTML = '<i class="bi bi-lock"></i> Pay ₹' + document.getElementById('payButtonAmount').textContent;
        payButton.disabled = false;
    });
}

// Generate ticket with QR code
function generateTicket(bookingData) {
    const ticketDetails = document.getElementById('ticketDetails');
    const currentDate = new Date().toLocaleString();
    
    ticketDetails.innerHTML = `
        <div class="border rounded p-3 mb-3">
            <p class="mb-2"><strong>Booking ID:</strong> #${bookingData.bookingId}</p>
            <p class="mb-2"><strong>Movie ID:</strong> ${bookingData.movieId}</p>
            <p class="mb-2"><strong>Theatre ID:</strong> ${bookingData.theatreId}</p>
            <p class="mb-2"><strong>Seats:</strong> ${bookingData.seats.join(', ')}</p>
            <p class="mb-2"><strong>Total Amount:</strong> ₹${bookingData.totalPrice}</p>
            <p class="mb-2"><strong>Payment Method:</strong> ${bookingData.paymentMethod}</p>
            <p class="mb-2"><strong>Booking Time:</strong> ${currentDate}</p>
            <p class="mb-0 text-success"><strong>Status:</strong> CONFIRMED</p>
        </div>
    `;
    
    // Generate QR code
    const qrContainer = document.getElementById('qrCode');
    qrContainer.innerHTML = ''; // Clear previous QR code
    
    const qrData = JSON.stringify({
        bookingId: bookingData.bookingId,
        movieId: bookingData.movieId,
        theatreId: bookingData.theatreId,
        seats: bookingData.seats.join(','),
        amount: bookingData.totalPrice,
        date: currentDate
    });
    
    new QRCode(qrContainer, {
        text: qrData,
        width: 200,
        height: 200,
        colorDark: "#000000",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.H
    });
    
    // Show ticket modal
    const ticketModal = new bootstrap.Modal(document.getElementById('ticketModal'));
    ticketModal.show();
}

// Download ticket
function downloadTicket() {
    // Create a simple text version of the ticket
    const ticketDetails = document.getElementById('ticketDetails').innerText;
    const ticketText = `SMART MOVIE BOOKING SYSTEM\n\nE-TICKET\n\n${ticketDetails}\n\nThank you for booking with us!`;
    
    const blob = new Blob([ticketText], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ticket-${Date.now()}.txt`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
    
    alert('Ticket downloaded! You can also take a screenshot of the QR code.');
}

// View my bookings from ticket modal
function viewMyBookings() {
    bootstrap.Modal.getInstance(document.getElementById('ticketModal')).hide();
    showSection('myBookings');
}


// Theatre Selection Functions

// Show theatre selection modal
function showTheatreSelection() {
    // Load all theatres initially
    loadTheatresForSelection();
    
    const modal = new bootstrap.Modal(document.getElementById('theatreSelectionModal'));
    modal.show();
}

// Load theatres for selection
function loadTheatresForSelection() {
    fetch('/smart-booking/theatres')
        .then(response => response.json())
        .then(theatres => {
            displayTheatresForSelection(theatres);
        })
        .catch(error => {
            console.error('Error loading theatres:', error);
            alert('Failed to load theatres');
        });
}

// Get nearby theatres based on user location
function getNearbyTheatres() {
    const statusEl = document.getElementById('locationStatus');
    statusEl.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Getting your location...';
    
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;
                
                statusEl.innerHTML = '<span class="text-success">✓ Location found</span>';
                
                // Fetch nearby theatres
                fetch('/smart-booking/theatres', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `latitude=${lat}&longitude=${lng}&radius=10`
                })
                .then(response => response.json())
                .then(theatres => {
                    if (theatres.length === 0) {
                        statusEl.innerHTML = '<span class="text-warning">No theatres found nearby. Showing all theatres.</span>';
                        loadTheatresForSelection();
                    } else {
                        displayTheatresForSelection(theatres, true);
                    }
                })
                .catch(error => {
                    console.error('Error fetching nearby theatres:', error);
                    statusEl.innerHTML = '<span class="text-danger">Error finding nearby theatres</span>';
                    loadTheatresForSelection();
                });
            },
            (error) => {
                console.error('Geolocation error:', error);
                statusEl.innerHTML = '<span class="text-danger">Location access denied. Showing all theatres.</span>';
                loadTheatresForSelection();
            }
        );
    } else {
        statusEl.innerHTML = '<span class="text-danger">Geolocation not supported</span>';
        loadTheatresForSelection();
    }
}

// Display theatres for selection
function displayTheatresForSelection(theatres, isNearby = false) {
    const theatreList = document.getElementById('theatreList');
    
    if (theatres.length === 0) {
        theatreList.innerHTML = '<div class="col-12"><p class="text-center text-muted">No theatres available</p></div>';
        return;
    }
    
    const title = isNearby ? '<div class="col-12"><h6 class="text-success mb-3">Nearby Theatres</h6></div>' : '';
    
    theatreList.innerHTML = title + theatres.map(theatre => `
        <div class="col-md-6 mb-3">
            <div class="card theatre-card h-100" onclick="selectTheatreForBooking(${theatre.id})">
                <div class="card-body">
                    <h6 class="card-title">${theatre.name}</h6>
                    <p class="card-text small text-muted mb-2">
                        <i class="bi bi-geo-alt"></i> ${theatre.address}
                    </p>
                    ${isNearby ? '<span class="badge bg-success">Nearby</span>' : ''}
                    <button class="btn btn-sm btn-primary mt-2 w-100">
                        Select Theatre
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Select theatre and proceed to seat selection
function selectTheatreForBooking(theatreId) {
    currentTheatreId = theatreId;
    
    // Hide theatre selection modal
    bootstrap.Modal.getInstance(document.getElementById('theatreSelectionModal')).hide();
    
    // Show seat selection modal
    showSeatSelection(currentMovieId, theatreId);
}
