const functions = require("firebase-functions");
const admin = require('firebase-admin');
const { Change } = require("firebase-functions");
// admin.initializeApp(functions.config().firebase);
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
function generateRandomCode(n) {
    let str = ''
    for (let i = 0; i < n; i++) {
      str += Math.floor(Math.random() * 10)
    }
    return str
  }

// const root = admin.database().ref();
// exports.addMessage = functions.https.onRequest(async (req, res) => {

//     const snapshott = await admin.database().ref().push({"original":"ZXz"});
   
//     res.send("assasin")
//    });
// test-5e2be-default-rtdb
// exports.makeUppercase = functions.database.ref('/id')
//     .onWrite((snapshot, context) => {
//       functions.logger.log('Uppercasing', context.params.pushId, original);
//       const uppercase = original.toUpperCase();
    
//       return snapshot.ref.parㅛent.child('uppercase').set(uppercase);
//     });

// exports.addunit = functions.https.onRequest(async (req, res) => {
//     const rand_num = generateRandomCode(4);
//     const snapshott = await admin.database().ref('/id').child(req.query.id).set(rand_num);

//     res.send(req.query.id +"success")
//     });
function addunit(req){
  const rand_num = generateRandomCode(4);
    const snapshott = admin.database().ref('/id').child(req).set(rand_num);
    return req
}

exports.getunit = functions.https.onRequest(async (req, res) => {

    const snapshott = await admin.database().ref('/id').child(req.query.id).get();

    res.send(snapshott);

});

exports.newNodeDetected = functions.database.ref('storage/{storageID}')
      .onWrite((change,context) =>{
        var oldName = change.before.val();
        var newName = change.after.val();
        var storageID = context.params.storageID;
        if (oldName == 0&& newName != 0){
          const snapshott = admin.database().ref('/alarm').child(newName).set(0);
          const snapshott_2 = admin.database().ref('/password').child(newName).set(generateRandomCode(4));
        }
        else{
          admin.database().ref('/alarm').child(oldName).remove();
          admin.database().ref('/password').child(oldName).remove();

        }
        console.log(oldName+newName+storageID);
      });