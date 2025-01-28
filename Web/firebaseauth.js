import { initializeApp } from "https://www.gstatic.com/firebasejs/10.14.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.14.0/firebase-analytics.js";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/10.14.0/firebase-auth.js";
import { getFirestore, setDoc, doc } from "https://www.gstatic.com/firebasejs/10.14.0/firebase-firestore.js";

// Your web app's Firebase configuration
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
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

// Function to display messages
function showMessage(message, divId) {
  const messageDiv = document.getElementById(divId);
  messageDiv.style.display = "block";
  messageDiv.innerHTML = message;
  messageDiv.style.opacity = 1;
  setTimeout(() => {
    messageDiv.style.opacity = 0;
  }, 5000);
}

// Sign up logic
const signUp = document.getElementById('submitSignUp');
signUp.addEventListener('click', (event) => {
  event.preventDefault();
  
  const email = document.getElementById('rEmail').value;
  const password = document.getElementById('rPassword').value;
  const mobileNumber = document.getElementById('mobileNumber').value;
  const username = document.getElementById('userName').value;

  const auth = getAuth();
  const db = getFirestore();

  createUserWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      const user = userCredential.user;
      
      // Set user data with isAdmin field set to 1
      const userData = { 
        email, 
        mobileNumber, 
        username, 
        isAdmin: 1  // Add isAdmin and set it to 1
      };

      showMessage('Account Created Successfully', 'signUpMessage');
      
      // Store user data in Firestore
      const docRef = doc(db, "users", user.uid);
      setDoc(docRef, userData)
        .then(() => {
          window.location.href = 'index.html';
        })
        .catch((error) => {
          console.error("Error writing document", error);
        });
    })
    .catch((error) => {
      const errorCode = error.code;
      if (errorCode === 'auth/email-already-in-use') {
        showMessage('Email Address Already Exists !!!', 'signUpMessage');
      } else {
        showMessage('Unable to create User', 'signUpMessage');
      }
    });
});

// Sign in logic
const signIn = document.getElementById('submitSignIn');
signIn.addEventListener('click', (event) => {
  event.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const auth = getAuth();

  signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      showMessage('Login is successful', 'signInMessage');
      const user = userCredential.user;
      
      localStorage.setItem('loggedInUserId', user.uid);
      window.location.href = 'homepage.html';
    })
    .catch((error) => {
      const errorCode = error.code;
      if (errorCode === 'auth/invalid-credential') {
        showMessage('Incorrect Email or Password', 'signInMessage');
      } else {
        showMessage('Account does not Exist', 'signInMessage');
      }
    });
});

