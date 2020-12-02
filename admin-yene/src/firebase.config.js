import * as firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";
import "firebase/messaging";

const firebaseConfig = {
  apiKey: "AIzaSyChBXr04r2TPSBMiWLj5c0M4tJqJYOrWHI",
  authDomain: "yene-service.firebaseapp.com",
  databaseURL: "https://yene-service.firebaseio.com",
  projectId: "yene-service",
  storageBucket: "yene-service.appspot.com",
  messagingSenderId: "885935246711",
  appId: "1:885935246711:web:065367c7006ab2b1ca6572",
};

firebase.initializeApp(firebaseConfig);

//util
const db = firebase.firestore();
const auth = firebase.auth();
const msg = firebase.messaging();

//Database Collections
const usersCollection = db.collection("Users");
const providerCollection = db.collection("Service_Providers");
const reportsCollection = db.collection("Reports");
const serviceListCollection = db.collection("Service_List");
const reviewsCollection = db.collection("Reviews");

export {
  msg,
  db,
  auth,
  usersCollection,
  providerCollection,
  reportsCollection,
  reviewsCollection,
  serviceListCollection,
};
