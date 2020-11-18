import firebase from 'firebase';

  // Your web app's Firebase configuration
  const firebaseConfig = {
    apiKey: "AIzaSyDayliYPF2vRNDs1bVbWOpAVJWjbPBGxXs",
    authDomain: "vue-spas-feebf.firebaseapp.com",
    databaseURL: "https://vue-spas-feebf.firebaseio.com",
    projectId: "vue-spas-feebf",
    storageBucket: "vue-spas-feebf.appspot.com",
    messagingSenderId: "285189180297",
    appId: "1:285189180297:web:175da4fceb058a43976f58"
  };
  // Initialize Firebase
  const firebaseApp = firebase.initializeApp(firebaseConfig);
  export default firebaseApp.firestore();