// // Initialize Firebase
// const firebaseConfig = {
//     apiKey: "YOUR_API_KEY",
//     authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
//     databaseURL: "https://YOUR_PROJECT_ID.firebaseio.com",  // Correct Realtime Database URL
//     projectId: "YOUR_PROJECT_ID",
//     storageBucket: "YOUR_PROJECT_ID.appspot.com",
//     messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
//     appId: "YOUR_APP_ID"
// };
// firebase.initializeApp(firebaseConfig);
// const db = firebase.database();  // Use Firebase Realtime Database instead of Firestore

// // Global variables for the pie charts
// let oxygenPieChart, co2PieChart, humidityPieChart;

// // Function to fetch atmosphere data from Firebase Realtime Database
// function fetchAtmosphereData() {
//     db.ref('atmosphere').on('value', (snapshot) => {
//         const data = snapshot.val();
//         if (data) {
//             updateCharts(data.oxygenLevel, data.co2Level, data.humidityLevel);
//         }
//     });
// }

// // Function to initialize pie charts
// function initializeCharts() {
//     const ctxOxygen = document.getElementById("oxygenPieChart").getContext("2d");
//     const ctxCO2 = document.getElementById("co2PieChart").getContext("2d");
//     const ctxHumidity = document.getElementById("humidityPieChart").getContext("2d");

//     oxygenPieChart = new Chart(ctxOxygen, {
//         type: 'doughnut', // Changed to doughnut for better visibility
//         data: {
//             labels: ['Oxygen Level', 'Remaining'],
//             datasets: [{
//                 label: 'Current Oxygen Level', // Added label for clarity
//                 data: [0, 100], // Initial values
//                 backgroundColor: ['green', 'gray'],
//             }]
//         },
//         options: {
//             responsive: true,
//             maintainAspectRatio: false, // Allow height adjustment
//             plugins: {
//                 tooltip: {
//                     callbacks: {
//                         label: function (tooltipItem) {
//                             return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
//                         }
//                     }
//                 }
//             },
//             layout: {
//                 padding: {
//                     left: 20,
//                     right: 20,
//                     top: 20,
//                     bottom: 20 // Add padding to the chart layout
//                 }
//             }
//         }
//     });

//     co2PieChart = new Chart(ctxCO2, {
//         type: 'doughnut', // Changed to doughnut for better visibility
//         data: {
//             labels: ['CO2 Level', 'Remaining'],
//             datasets: [{
//                 label: 'Current CO2 Level', // Added label for clarity
//                 data: [0, 100], // Initial values
//                 backgroundColor: ['red', 'gray'],
//             }]
//         },
//         options: {
//             responsive: true,
//             maintainAspectRatio: false, // Allow height adjustment
//             plugins: {
//                 tooltip: {
//                     callbacks: {
//                         label: function (tooltipItem) {
//                             return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
//                         }
//                     }
//                 }
//             },
//             layout: {
//                 padding: {
//                     left: 20,
//                     right: 20,
//                     top: 20,
//                     bottom: 20 // Add padding to the chart layout
//                 }
//             }
//         }
//     });

//     humidityPieChart = new Chart(ctxHumidity, {
//         type: 'doughnut', // Changed to doughnut for better visibility
//         data: {
//             labels: ['Humidity Level', 'Remaining'],
//             datasets: [{
//                 label: 'Current Humidity Level', // Added label for clarity
//                 data: [0, 100], // Initial values
//                 backgroundColor: ['blue', 'gray'],
//             }]
//         },
//         options: {
//             responsive: true,
//             maintainAspectRatio: false, // Allow height adjustment
//             plugins: {
//                 tooltip: {
//                     callbacks: {
//                         label: function (tooltipItem) {
//                             return tooltipItem.label + ': ' + tooltipItem.raw; // Tooltip callback for displaying values
//                         }
//                     }
//                 }
//             },
//             layout: {
//                 padding: {
//                     left: 20,
//                     right: 20,
//                     top: 20,
//                     bottom: 20 // Add padding to the chart layout
//                 }
//             }
//         }
//     });
// }
// document.querySelectorAll('.menu__item').forEach(item => {
//     item.addEventListener('click', (e) => {
//         const section = e.target.getAttribute('data-section');
//         document.querySelectorAll('section').forEach(sec => sec.style.display = 'none'); // Hide all sections
//         document.getElementById(section).style.display = 'block'; // Show the selected section
//     });
// });


// // Function to update pie charts
// function updateCharts(oxygenLevel, co2Level, humidityLevel) {
//     oxygenPieChart.data.datasets[0].data = [oxygenLevel, 100 - oxygenLevel];
//     oxygenPieChart.update();

//     co2PieChart.data.datasets[0].data = [co2Level * 100, 100 - (co2Level * 100)];
//     co2PieChart.update();

//     humidityPieChart.data.datasets[0].data = [humidityLevel, 100 - humidityLevel];
//     humidityPieChart.update();
// }

// // Call the function to initialize charts and start fetching data
// initializeCharts();
// fetchAtmosphereData();
