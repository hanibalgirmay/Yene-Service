const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

exports.sendNotificationTopic = functions.firestore
  .document("Users/{uid}")
  .onWrite(async (event) => {
    // let docId = event.after.id;
    let title = event.after.get("title");
    let content = event.after.get("content");
    var message = {
      notification: {
        title: title,
        body: content,
      },
      topic: "yene service",
    };

    let res = await admin.messaging().send(message);
    console.log(res);
  });
