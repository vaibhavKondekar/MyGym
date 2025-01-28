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
const app = firebase.initializeApp(firebaseConfig);
const database = firebase.database();

// Function to handle member addition
function handleAddMember(event) {
    event.preventDefault(); // Prevent the default form submission
    console.log('handleAddMember function executed'); // Debug log

    // Get form values
    const fullName = document.getElementById('fullName').value;
    const email = document.getElementById('email').value;
    const gender = document.getElementById('gender').value;
    const age = document.getElementById('age').value;
    const weight = document.getElementById('weight').value;
    const height = document.getElementById('height').value;
    const healthIssues = document.getElementById('healthIssues').value || 'None'; // Fallback for empty health issues
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const packageSelected = document.getElementById('package').value;

    // Debug: Log form data to ensure it's being captured
    console.log('Form Data:', {
        fullName,
        email,
        gender,
        age,
        weight,
        height,
        healthIssues,
        startDate,
        endDate,
        packageSelected
    });

    // Create a new member object based on your Firebase structure
    const newMember = {
        fullName: fullName,
        email: email,
        gender: gender,
        age: age,
        height: height,
        weight: weight,
        healthIssues: healthIssues,
        joinDate: startDate,
        endDate: endDate,
        plan: packageSelected,
        memberId: database.ref().push().key,  // Generate a unique ID for the member
        remainingDays: 16  // Hardcoded remainingDays for now, adjust as needed
    };

    // Debug: Log the new member object
    console.log('New Member Object:', newMember);

    // Push the new member to the database using the unique memberId
    database.ref('members/' + newMember.memberId).set(newMember)
        .then(() => {
            alert('Member added successfully!');
            console.log('Member added successfully!'); // Log success to console
            document.getElementById('addMemberForm').reset(); // Reset the form
        })
        .catch((error) => {
            console.error('Error adding member: ', error);
            alert('Error adding member: ' + error.message);
        });
}

// Handle form submission
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('addMemberForm');
    form.addEventListener('submit', handleAddMember);
});
