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

// Initialize Firebase only once
firebase.initializeApp(firebaseConfig);
const db = firebase.database();  // Use Firebase Realtime Database

// Global variables for the pie charts
let oxygenPieChart, co2PieChart, humidityPieChart;

// Function to fetch atmosphere data from Firebase Realtime Database
function fetchAtmosphereData() {
  db.ref('atmosphere').on('value', (snapshot) => {
    const data = snapshot.val();
    if (data) {
      updateCharts(data.oxygenLevel, data.co2Level, data.humidityLevel);
    }
  });
}

// Function to initialize pie charts
function initializeCharts() {
  const ctxOxygen = document.getElementById("oxygenPieChart").getContext("2d");
  const ctxCO2 = document.getElementById("co2PieChart").getContext("2d");
  const ctxHumidity = document.getElementById("humidityPieChart").getContext("2d");

  oxygenPieChart = new Chart(ctxOxygen, {
    type: 'doughnut', // Changed to doughnut
    data: {
      labels: ['Oxygen Level', 'Remaining'],
      datasets: [{
        label: 'Current Oxygen Level', // Added label for clarity
        data: [0, 100], // Initial values
        backgroundColor: ['green', 'gray'],
        borderColor: ['rgba(75, 192, 192, 1)', 'rgba(192, 192, 192, 1)'], // Added border color
        borderWidth: 1 // Added border width
      }]
    },
    options: {
      responsive: true,
      plugins: {
        tooltip: {
          callbacks: {
            label: function (tooltipItem) {
              return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
            }
          }
        }
      }
    }
  });

  co2PieChart = new Chart(ctxCO2, {
    type: 'doughnut',
    data: {
      labels: ['CO2 Level', 'Remaining'],
      datasets: [{
        label: 'Current CO2 Level', // Added label for clarity
        data: [0, 100], // Initial values
        backgroundColor: ['red', 'gray'],
        borderColor: ['rgba(255, 99, 132, 1)', 'rgba(192, 192, 192, 1)'], // Added border color
        borderWidth: 1 // Added border width
      }]
    },
    options: {
      responsive: true,
      plugins: {
        tooltip: {
          callbacks: {
            label: function (tooltipItem) {
              return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
            }
          }
        }
      }
    }
  });

  humidityPieChart = new Chart(ctxHumidity, {
    type: 'doughnut', // Changed to doughnut
    data: {
      labels: ['Humidity Level', 'Remaining'],
      datasets: [{
        label: 'Current Humidity Level', // Added label for clarity
        data: [0, 100], // Initial values
        backgroundColor: ['blue', 'gray'],
        borderColor: ['rgba(54, 162, 235, 1)', 'rgba(192, 192, 192, 1)'], // Added border color
        borderWidth: 1 // Added border width
      }]
    },
    options: {
      responsive: true,
      plugins: {
        tooltip: {
          callbacks: {
            label: function (tooltipItem) {
              return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
            }
          }
        }
      }
    }
  });
}

// Function to update pie charts
function updateCharts(oxygenLevel, co2Level, humidityLevel) {
  oxygenPieChart.data.datasets[0].data = [oxygenLevel, 100 - oxygenLevel];
  oxygenPieChart.update();

  co2PieChart.data.datasets[0].data = [co2Level * 100, 100 - (co2Level * 100)]; // Make sure co2Level is a fraction between 0 and 1
  co2PieChart.update();

  humidityPieChart.data.datasets[0].data = [humidityLevel, 100 - humidityLevel];
  humidityPieChart.update();
}

// Call the function to initialize charts and start fetching data
initializeCharts();
fetchAtmosphereData();

// Section switching logic
function showSection(sectionId) {
  document.querySelectorAll('section').forEach(section => {
    section.style.display = 'none';
  });
  document.getElementById(sectionId).style.display = 'block';

  // Update active class on menu items
  document.querySelectorAll('.menu__item').forEach(item => {
    item.classList.remove('active');
  });
  document.querySelector(`.menu__item[data-section="${sectionId}"]`).classList.add('active');
}

// Event listeners for menu items
document.querySelectorAll('.menu__item').forEach(item => {
  item.addEventListener('click', function() {
    const sectionId = this.getAttribute('data-section');
    showSection(sectionId);
    if (sectionId === 'attendance') {
      loadAttendanceRecords();
    }
  });
});

// Function to load attendance records
function loadAttendanceRecords() {
  const attendanceRef = db.ref('attendance');
  attendanceRef.on('value', (snapshot) => {
    const attendanceData = snapshot.val();
    displayAttendanceRecords(attendanceData);
  });
}

// Function to display attendance records
function displayAttendanceRecords(data) {
    const attendanceRecordsDiv = document.getElementById('attendanceRecords');
    attendanceRecordsDiv.innerHTML = ''; // Clear previous records

    // Create a header for the attendance records
    const dateHeader = document.createElement('h3');
    dateHeader.textContent = `Attendance Records`; // General header for attendance
    attendanceRecordsDiv.appendChild(dateHeader);

    const table = document.createElement('table');
    table.innerHTML = `
        <thead>
            <tr>
                <th>Name</th>
                <th>Check-In Date</th>
                <th>Check-In Time</th>
                <th>Check-Out Date</th>
                <th>Check-Out Time</th>
            </tr>
        </thead>
        <tbody id="attendanceList">
        </tbody>
    `;

    const tbody = table.querySelector('tbody');
    for (const dateKey in data) {
        const dateRecords = data[dateKey];

        for (const personId in dateRecords) {
            const person = dateRecords[personId];
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${person.name || 'N/A'}</td>
                <td>${person.checkInDate || 'N/A'}</td>
                <td>${person.checkInTime || 'N/A'}</td>
                <td>${person.checkOutDate || 'N/A'}</td>
                <td>${person.checkOutTime || 'N/A'}</td>
            `;
            tbody.appendChild(row);
        }
    }

    attendanceRecordsDiv.appendChild(table);
}

// Initialize Chart.js for live crowd
const liveCrowdCtx = document.getElementById('liveCrowdChart').getContext('2d');
const liveCrowdChart = new Chart(liveCrowdCtx, {
  type: 'doughnut',
  data: {
    labels: ['In Gym', 'Capacity Left'],
    datasets: [{
      label: 'Current Gym Crowd',
      data: [0, 20], // Initial values
      backgroundColor: ['rgba(75, 192, 192, 0.7)', 'rgba(192, 192, 192, 0.7)'],
      borderColor: ['rgba(75, 192, 192, 1)', 'rgba(192, 192, 192, 1)'],
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

// Firebase Database Reference for live attendance count
const attendanceRef = db.ref('attendanceCounters/liveattendancecnt');

// Listen for live attendance count changes
attendanceRef.on('value', (snapshot) => {
  const liveCount = snapshot.val();
  document.getElementById('crowdCountText').textContent = `${liveCount} people currently in the gym`;

  // Update Chart.js
  liveCrowdChart.data.datasets[0].data = [liveCount, 20 - liveCount]; // Assuming gym capacity is 20
  liveCrowdChart.update();
});

// Show dashboard by default
document.addEventListener('DOMContentLoaded', () => {
  showSection('dashboard');
});

// Initialize Chart.js for weekly attendance
const weeklyAttendanceCtx = document.getElementById('myChart').getContext('2d');
const myChart = new Chart(weeklyAttendanceCtx, {
  type: 'bar', // Type of chart: bar, line, pie, etc.
  data: {
    labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
    datasets: [{
      label: 'Attendance',
      data: generateRandomAttendanceData(7), // Generate random data for 7 days
      backgroundColor: [
        'rgba(255, 99, 132, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(255, 206, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(153, 102, 255, 0.2)',
        'rgba(255, 159, 64, 0.2)',
        'rgba(255, 205, 86, 0.2)'
      ],
      borderColor: [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)',
        'rgba(255, 205, 86, 1)'
      ],
      borderWidth: 1
    }]
  },
  options: {
    scales: {
      y: {
        beginAtZero: true
      }
    }
  }
});

// Function to generate random attendance data
function generateRandomAttendanceData(numDays) {
  const data = [];
  for (let i = 0; i < numDays; i++) {
    data.push(Math.floor(Math.random() * 20)); // Random number between 0 and 20
  }
  return data;
}
var membersRef = firebase.database().ref('members');

    // Fetch members data in real-time
    membersRef.on('value', function(snapshot) {
        var memberList = document.getElementById('memberList');
        memberList.innerHTML = ''; // Clear existing data

        snapshot.forEach(function(childSnapshot) {
            var member = childSnapshot.val();
            
            // Calculate remaining days
            const endDate = new Date(member.endDate); // Parse end date
            const currentDate = new Date(); // Get current date
            const timeDiff = endDate - currentDate; // Difference in milliseconds
            const remainingDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); // Convert to days

            var row = document.createElement('tr');
            row.innerHTML = `
                <td>${member.fullName || 'N/A'}</td> <!-- Default value if undefined -->
                <td>${member.plan || 'N/A'}</td> <!-- Default value if undefined -->
                <td>${member.joinDate || 'N/A'}</td> <!-- Default value if undefined -->
                <td>${member.endDate || 'N/A'}</td> <!-- Default value if undefined -->
                <td>${remainingDays >= 0 ? remainingDays : 'Expired'}</td> <!-- Calculate remaining days -->
                <td><button class="renew-membership-btn" data-member-id="${childSnapshot.key}">Renew Membership Plan</button></td>
            `;
            memberList.appendChild(row);
        });
    });

// Function to renew membership
function renewMembership(memberId) {
    const startingDate = prompt("Enter the starting date (YYYY-MM-DD):");
    const endingDate = prompt("Enter the ending date (YYYY-MM-DD):");
    const planName = prompt("Enter the plan name:");

    const messageArea = document.getElementById('messageArea'); // Get the message area

    // Clear previous messages
    messageArea.innerHTML = '';

    if (startingDate && endingDate && planName) {
        // Update the member's data in Firebase
        const memberRef = firebase.database().ref('members/' + memberId);
        memberRef.update({
            joinDate: startingDate,
            endDate: endingDate,
            plan: planName
        }).then(() => {
            messageArea.innerHTML = "<p style='color: green;'>Membership renewed successfully!</p>"; // Success message
        }).catch((error) => {
            console.error("Error renewing membership:", error);
            messageArea.innerHTML = "<p style='color: red;'>Failed to renew membership. Please try again.</p>"; // Error message
        });
    } else {
        messageArea.innerHTML = "<p style='color: red;'>All fields are required.</p>"; // Validation message
    }
}

// Event listener for renewing membership
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('renew-membership-btn')) {
        const memberId = event.target.getAttribute('data-member-id');
        renewMembership(memberId);
    }
});
