// Simple Google Maps initialization
let googleMap = null;
let theatresData = [];

// This function will be called when Google Maps loads
function initMap() {
    console.log('Google Maps API loaded successfully');
}

// Initialize map when theatres are loaded
function setupGoogleMap(theatres) {
    theatresData = theatres;
    
    // Check if Google Maps is available
    if (typeof google === 'undefined') {
        console.error('Google Maps not loaded');
        document.getElementById('map').innerHTML = 
            '<div class="alert alert-warning m-3">Google Maps is loading... Please wait or refresh the page.</div>';
        return;
    }
    
    try {
        const mapElement = document.getElementById('map');
        
        // Create map centered on Mumbai
        googleMap = new google.maps.Map(mapElement, {
            center: { lat: 19.0760, lng: 72.8777 },
            zoom: 12,
            mapTypeControl: true,
            streetViewControl: true,
            fullscreenControl: true
        });
        
        // Add markers for each theatre
        theatres.forEach(theatre => {
            const marker = new google.maps.Marker({
                position: { lat: theatre.latitude, lng: theatre.longitude },
                map: googleMap,
                title: theatre.name,
                animation: google.maps.Animation.DROP
            });
            
            const infoWindow = new google.maps.InfoWindow({
                content: `
                    <div style="padding: 10px; max-width: 200px;">
                        <h6 style="margin: 0 0 8px 0; color: #333;">${theatre.name}</h6>
                        <p style="margin: 0; font-size: 13px; color: #666;">${theatre.address}</p>
                    </div>
                `
            });
            
            marker.addListener('click', () => {
                infoWindow.open(googleMap, marker);
            });
        });
        
        console.log('✅ Map initialized with', theatres.length, 'theatres');
        
    } catch (error) {
        console.error('Error creating map:', error);
        document.getElementById('map').innerHTML = 
            '<div class="alert alert-danger m-3">Error loading map: ' + error.message + '</div>';
    }
}

// Handle "Use Current Location" button
function handleCurrentLocation() {
    if (!navigator.geolocation) {
        alert('Geolocation is not supported by your browser');
        return;
    }
    
    navigator.geolocation.getCurrentPosition(
        (position) => {
            const userLat = position.coords.latitude;
            const userLng = position.coords.longitude;
            
            console.log('User location:', userLat, userLng);
            
            // Center map on user location
            if (googleMap) {
                googleMap.setCenter({ lat: userLat, lng: userLng });
                googleMap.setZoom(14);
                
                // Add marker for user location
                new google.maps.Marker({
                    position: { lat: userLat, lng: userLng },
                    map: googleMap,
                    title: 'Your Location',
                    icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
                });
            }
            
            // Find nearby theatres
            fetch('/smart-booking/theatres', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `latitude=${userLat}&longitude=${userLng}&radius=10`
            })
            .then(response => response.json())
            .then(nearbyTheatres => {
                console.log('Found', nearbyTheatres.length, 'nearby theatres');
                // You can update the theatre list here if needed
            })
            .catch(error => {
                console.error('Error finding nearby theatres:', error);
            });
        },
        (error) => {
            console.error('Geolocation error:', error);
            alert('Unable to get your location: ' + error.message);
        }
    );
}


// Handle location selection from dropdown
function selectLocation() {
    const select = document.getElementById('locationSelect');
    const value = select.value;
    
    if (!value) return;
    
    const [lat, lng] = value.split(',').map(Number);
    
    if (googleMap) {
        googleMap.setCenter({ lat, lng });
        googleMap.setZoom(14);
        
        // Add marker for selected location
        new google.maps.Marker({
            position: { lat, lng },
            map: googleMap,
            title: select.options[select.selectedIndex].text,
            icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
        });
        
        console.log('Location selected:', select.options[select.selectedIndex].text);
    } else {
        console.error('Map not initialized yet');
        alert('Please wait for the map to load');
    }
}

// Search for nearby theatres based on current map center
function searchNearbyTheatres() {
    if (!googleMap) {
        alert('Please wait for the map to load');
        return;
    }
    
    const center = googleMap.getCenter();
    const lat = center.lat();
    const lng = center.lng();
    
    console.log('Searching theatres near:', lat, lng);
    
    // Call backend to find nearby theatres
    fetch('/smart-booking/theatres', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `latitude=${lat}&longitude=${lng}&radius=10`
    })
    .then(response => response.json())
    .then(nearbyTheatres => {
        console.log('Found', nearbyTheatres.length, 'nearby theatres');
        
        if (nearbyTheatres.length === 0) {
            alert('No theatres found within 10km of this location');
        } else {
            alert(`Found ${nearbyTheatres.length} theatre(s) nearby!`);
            // Re-render theatre list with nearby theatres
            if (typeof renderTheatres === 'function') {
                renderTheatres(nearbyTheatres);
            }
        }
    })
    .catch(error => {
        console.error('Error searching theatres:', error);
        alert('Error searching for theatres');
    });
}
