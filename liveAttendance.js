// Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyB3nSmxlDfSTNG1Qv9abpfYZKaO7Qkq8-8",
    authDomain: "gymbuddychat-acee1.firebaseapp.com",
    databaseURL: "https://gymbuddychat-acee1-default-rtdb.firebaseio.com",
    projectId: "gymbuddychat-acee1",
    storageBucket: "gymbuddychat-acee1.appspot.com",
    messagingSenderId: "650900731982",
    appId: "1:650900731982:web:e778dc247a215da33c6b1f",
    measurementId: "G-H1Z02D7GLX"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Initialize Chart.js for live crowd
const ctx = document.getElementById('liveCrowdChart').getContext('2d');
const liveCrowdChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ['In Gym', 'Capacity Left'],
        datasets: [{
            label: 'Current Gym Crowd',
            data: [0, 20], // Initial values
            backgroundColor: ['#4CAF50', '#FFCDD2'],
            borderColor: ['#4CAF50', '#FFCDD2'],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        plugins: {
            tooltip: {
                callbacks: {
                    label: function (tooltipItem) {
                        return tooltipItem.label + ': ' + tooltipItem.raw;
                    }
                }
            }
        }
    }
});

// Firebase Database Reference
const attendanceRef = firebase.database().ref('attendanceCounters/liveattendancecnt');

// Listen for live attendance count changes
attendanceRef.on('value', (snapshot) => {
    const liveCount = snapshot.val();
    document.getElementById('crowdCountText').textContent = `${liveCount} people currently in the gym`;

    // Update Chart.js
    liveCrowdChart.data.datasets[0].data = [liveCount, 20 - liveCount]; // Assuming gym capacity is 20
    liveCrowdChart.update();
});
