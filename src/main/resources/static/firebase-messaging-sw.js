importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');

// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {

        apiKey: "AIzaSyB7AIDmo5v_7LtOwIySVqHTrei97wDekLA",
        authDomain: "inhub-50c3d.firebaseapp.com",
        projectId: "inhub-50c3d",
        storageBucket: "inhub-50c3d.appspot.com",
        messagingSenderId: "348918158865",
        appId: "1:348918158865:web:3528db683cc719688043b0",
        measurementId: "G-25PSHDGP5Y"
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();