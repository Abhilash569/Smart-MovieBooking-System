<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Movie Booking System</title>
    
    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Smart Movie Booking</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-section="home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-section="movies">Movies</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" data-section="theatres">Theatres</a>
                    </li>
                    <li class="nav-item" id="myBookingsNav" style="display: none;">
                        <a class="nav-link" href="#" data-section="myBookings">My Bookings</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#" id="loginLink">Login</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Home Section -->
    <section id="home" class="hero-section">
        <div class="container text-center">
            <h1 class="display-3 mb-4">Welcome to Smart Movie Booking</h1>
            <p class="lead mb-5">Book your favorite movies at nearby theatres with ease</p>
            <button class="btn btn-primary btn-lg me-3" onclick="showSection('movies')">Browse Movies</button>
            <button class="btn btn-outline-light btn-lg" onclick="showLogin()">Get Started</button>
        </div>
    </section>
    
    <!-- Movies Section -->
    <section id="movies" class="container my-5" style="display: none;">
        <h2 class="mb-4">Browse Movies</h2>
        
        <!-- Search and Filter Section -->
        <div class="card mb-4">
            <div class="card-body">
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">Search Movies</label>
                        <input type="text" id="movieSearch" class="form-control" placeholder="Search by title..." onkeyup="searchMovies()">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Language</label>
                        <select id="languageFilter" class="form-select" onchange="filterMovies()">
                            <option value="">All Languages</option>
                            <option value="en">English</option>
                            <option value="hi">Hindi</option>
                            <option value="ta">Tamil</option>
                            <option value="te">Telugu</option>
                            <option value="ml">Malayalam</option>
                            <option value="kn">Kannada</option>
                            <option value="mr">Marathi</option>
                            <option value="bn">Bengali</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Genre</label>
                        <select id="genreFilter" class="form-select" onchange="filterMovies()">
                            <option value="">All Genres</option>
                            <option value="28">Action</option>
                            <option value="35">Comedy</option>
                            <option value="18">Drama</option>
                            <option value="27">Horror</option>
                            <option value="10749">Romance</option>
                            <option value="878">Sci-Fi</option>
                            <option value="53">Thriller</option>
                            <option value="16">Animation</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Show</label>
                        <select id="movieType" class="form-select" onchange="changeMovieType()">
                            <option value="now_playing">Now Playing</option>
                            <option value="popular">Popular</option>
                            <option value="upcoming">Upcoming</option>
                            <option value="top_rated">Top Rated</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="movieGrid" class="row g-4">
            <!-- Movie cards will be dynamically inserted here -->
        </div>
    </section>
    
    <!-- Theatres Section -->
    <section id="theatres" class="container my-5" style="display: none;">
        <h2 class="mb-4">Find Theatres</h2>
        <div class="row mb-4">
            <div class="col-md-6">
                <label for="locationSelect" class="form-label">Select Location</label>
                <select id="locationSelect" class="form-select" onchange="selectLocation()">
                    <option value="">Choose a location...</option>
                    
                    <optgroup label="Mumbai">
                        <option value="19.0760,72.8777">Mumbai - All Areas</option>
                        <option value="19.0896,72.8869">Mumbai - Phoenix Marketcity</option>
                        <option value="19.1758,72.9476">Mumbai - Inorbit Mall</option>
                        <option value="19.1368,72.8261">Mumbai - Andheri West</option>
                        <option value="19.0176,72.8561">Mumbai - Wadala</option>
                        <option value="19.2183,72.9781">Mumbai - Thane West</option>
                    </optgroup>
                    
                    <optgroup label="Delhi NCR">
                        <option value="28.7041,77.1025">Delhi - Connaught Place</option>
                        <option value="28.5355,77.3910">Delhi - Noida</option>
                        <option value="28.4595,77.0266">Gurgaon - Cyber City</option>
                        <option value="28.6139,77.2090">Delhi - Saket</option>
                        <option value="28.6692,77.4538">Delhi - Ghaziabad</option>
                    </optgroup>
                    
                    <optgroup label="Bangalore">
                        <option value="12.9716,77.5946">Bangalore - City Center</option>
                        <option value="12.9352,77.6245">Bangalore - Koramangala</option>
                        <option value="12.9698,77.7500">Bangalore - Whitefield</option>
                        <option value="13.0358,77.5970">Bangalore - Indiranagar</option>
                        <option value="12.9899,77.7273">Bangalore - Electronic City</option>
                    </optgroup>
                    
                    <optgroup label="Hyderabad">
                        <option value="17.3850,78.4867">Hyderabad - City Center</option>
                        <option value="17.4400,78.3489">Hyderabad - HITEC City</option>
                        <option value="17.4239,78.4738">Hyderabad - Banjara Hills</option>
                        <option value="17.4065,78.4772">Hyderabad - Jubilee Hills</option>
                    </optgroup>
                    
                    <optgroup label="Chennai">
                        <option value="13.0827,80.2707">Chennai - City Center</option>
                        <option value="13.0569,80.2425">Chennai - T Nagar</option>
                        <option value="12.9822,80.2210">Chennai - Velachery</option>
                        <option value="13.0475,80.2400">Chennai - Adyar</option>
                    </optgroup>
                    
                    <optgroup label="Pune">
                        <option value="18.5204,73.8567">Pune - City Center</option>
                        <option value="18.5362,73.9264">Pune - Koregaon Park</option>
                        <option value="18.5679,73.9143">Pune - Viman Nagar</option>
                        <option value="18.5089,73.8055">Pune - Kothrud</option>
                    </optgroup>
                    
                    <optgroup label="Kolkata">
                        <option value="22.5726,88.3639">Kolkata - City Center</option>
                        <option value="22.5355,88.3634">Kolkata - Park Street</option>
                        <option value="22.5726,88.4309">Kolkata - Salt Lake</option>
                        <option value="22.5448,88.3426">Kolkata - Ballygunge</option>
                    </optgroup>
                    
                    <optgroup label="Ahmedabad">
                        <option value="23.0225,72.5714">Ahmedabad - City Center</option>
                        <option value="23.0395,72.5066">Ahmedabad - Satellite</option>
                        <option value="23.0732,72.5310">Ahmedabad - Vastrapur</option>
                    </optgroup>
                    
                    <optgroup label="Other Cities">
                        <option value="26.9124,75.7873">Jaipur - City Center</option>
                        <option value="21.1458,79.0882">Nagpur - City Center</option>
                        <option value="23.2599,77.4126">Bhopal - City Center</option>
                        <option value="17.6868,75.9064">Solapur - City Center</option>
                        <option value="30.7333,76.7794">Chandigarh - City Center</option>
                    </optgroup>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">&nbsp;</label>
                <button class="btn btn-primary w-100" onclick="getCurrentLocation()">
                    <i class="bi bi-geo-alt-fill"></i> Use My Location
                </button>
            </div>
            <div class="col-md-3">
                <label class="form-label">&nbsp;</label>
                <button class="btn btn-success w-100" onclick="searchNearbyTheatres()">
                    <i class="bi bi-search"></i> Search Theatres
                </button>
            </div>
        </div>
        <div id="map" style="height: 400px; background: #e9ecef; border-radius: 8px; margin-bottom: 20px;">
            <!-- Google Maps will be loaded here -->
        </div>
        <div id="theatreList" class="row g-4">
            <!-- Theatre cards will be dynamically inserted here -->
        </div>
    </section>
    
    <!-- Login Modal -->
    <div class="modal fade" id="loginModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Login</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="loginForm">
                        <div class="mb-3">
                            <label for="loginEmail" class="form-label">Email</label>
                            <input type="email" class="form-control" id="loginEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="loginPassword" class="form-label">Password</label>
                            <input type="password" class="form-control" id="loginPassword" required>
                        </div>
                        <div id="loginError" class="alert alert-danger" style="display: none;"></div>
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>
                    <p class="mt-3 text-center">
                        Don't have an account? <a href="#" onclick="showSignup()">Sign up</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Signup Modal -->
    <div class="modal fade" id="signupModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Sign Up</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="signupForm">
                        <div class="mb-3">
                            <label for="signupName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="signupName" required>
                        </div>
                        <div class="mb-3">
                            <label for="signupEmail" class="form-label">Email</label>
                            <input type="email" class="form-control" id="signupEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="signupPassword" class="form-label">Password</label>
                            <input type="password" class="form-control" id="signupPassword" required minlength="6">
                        </div>
                        <div id="signupError" class="alert alert-danger" style="display: none;"></div>
                        <button type="submit" class="btn btn-primary w-100">Sign Up</button>
                    </form>
                    <p class="mt-3 text-center">
                        Already have an account? <a href="#" onclick="showLogin()">Login</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Movie Details Modal -->
    <div class="modal fade" id="movieDetailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="movieTitle"></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4">
                            <img id="moviePoster" src="" alt="Movie Poster" class="img-fluid rounded">
                        </div>
                        <div class="col-md-8">
                            <p><strong>Genre:</strong> <span id="movieGenre"></span></p>
                            <p><strong>Rating:</strong> <span id="movieRating"></span>/10</p>
                            <p><strong>Duration:</strong> <span id="movieDuration"></span> mins</p>
                            <p><strong>Language:</strong> <span id="movieLanguage"></span></p>
                            <p><strong>Description:</strong></p>
                            <p id="movieDescription"></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- My Bookings Section -->
    <section id="myBookings" class="container my-5" style="display: none;">
        <h2 class="mb-4">My Bookings</h2>
        <div id="bookingsList" class="row">
            <!-- Bookings will be loaded here -->
        </div>
    </section>

    <!-- Seat Selection Modal -->
    <div class="modal fade" id="seatSelectionModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Select Seats</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-3">
                        <div class="screen">SCREEN</div>
                    </div>
                    <div id="seatGrid" class="seat-grid mb-4">
                        <!-- Seat grid will be dynamically generated -->
                    </div>
                    <div class="seat-legend mb-3">
                        <span><span class="seat-box available"></span> Available</span>
                        <span><span class="seat-box selected"></span> Selected</span>
                        <span><span class="seat-box occupied"></span> Occupied</span>
                    </div>
                    <div class="text-center">
                        <h5>Total Price: ₹<span id="totalPrice">0</span></h5>
                        <button class="btn btn-success btn-lg mt-3" onclick="confirmBooking()">Confirm Booking</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap 5.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Google Maps API with callback -->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCdAOE-KDvjnttCz_VBSfRWipG0sdcCu7w&callback=initMap" async defer></script>
    
    <!-- Maps Fix Script -->
    <script src="js/maps-fix.js"></script>
    
    <!-- Custom JavaScript -->
    <script src="js/app.js"></script>
    
</body>
</html>
