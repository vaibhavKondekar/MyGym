(function() {
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

    // Initialize Firebase only if it hasn't been initialized yet
    if (!firebase.apps.length) {
        firebase.initializeApp(firebaseConfig);
    }

    // Reference to the health reports
    const healthReportsRef = firebase.database().ref('health_reports');

    // Fetch all health reports
    healthReportsRef.on('value', (snapshot) => {
        const healthReportsBody = document.getElementById('healthReportsBody');
        healthReportsBody.innerHTML = ''; // Clear the table before displaying new data

        if (!snapshot.exists()) {
            console.error('No health reports found.');
            return; // Exit if no data exists
        }

        snapshot.forEach((childSnapshot) => {
            const report = childSnapshot.val();
            const row = document.createElement('tr');

            // Create table cells for each piece of data
            row.innerHTML = `
                <td>${report.username}</td>
                <td>${report.heartRateAverage} bpm</td>
                <td>${report.bloodOxygenAverage}%</td>
                <td>${report.bloodPressureAverage}</td>
            `;

            // Check for threshold values and set row background color accordingly
            const hasIssue = !isWithinHeartRateRange(report.heartRateAverage) ||
                             !isWithinOxygenLevelRange(report.bloodOxygenAverage) ||
                             !isWithinBloodPressureRange(report.bloodPressureAverage);

            if (hasIssue) {
                row.style.backgroundColor = 'red'; // Highlight in red for issues
            } else {
                row.style.backgroundColor = 'green'; // Highlight in green for normal
            }

            healthReportsBody.appendChild(row);
        });
    }, (error) => {
        console.error('Error fetching health reports:', error); // Log any errors
    });

    // Check if heart rate is within the normal range
    function isWithinHeartRateRange(heartRate) {
        return heartRate >= 60 && heartRate <= 100;
    }

    // Check if blood oxygen level is within the normal range
    function isWithinOxygenLevelRange(oxygenLevel) {
        return oxygenLevel >= 95 && oxygenLevel <= 100;
    }

    // Check if blood pressure is within the normal range
    function isWithinBloodPressureRange(bloodPressure) {
        // Simple logic for placeholder; implement your own logic
        const [systolic, diastolic] = bloodPressure.split('/').map(Number);
        return systolic >= 90 && systolic <= 120 && diastolic >= 60 && diastolic <= 80;
    }
})();
