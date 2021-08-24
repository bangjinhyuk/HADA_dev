const functions = require("firebase-functions");
const admin = require('firebase-admin');
const { Change } = require("firebase-functions");
// admin.initializeApp(functions.config().firebase);
admin.initializeApp();


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions



exports.humiDetected = functions.database.ref('{school}/{sensor}/humi')
      .onWrite((change,context) =>{
        var oldData = change.before.val();
        var newData = change.after.val();
        var schoolID = context.params.school;
        var sensorID = context.params.sensor;
        // 1. 현재 시간(Locale)
        const curr = new Date();

        // 2. UTC 시간 계산
        const utc = 
            curr.getTime() + 
            (curr.getTimezoneOffset() * 60 * 1000);

        // 3. UTC to KST (UTC + 9시간)
        const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
        const kr_curr = new Date(utc + (KR_TIME_DIFF));

        var date = kr_curr.toLocaleDateString().replace(/\//gi,"-");
        var time = kr_curr.toLocaleTimeString();
        console.log(curr);        
        console.log(utc);
        console.log(kr_curr);
        console.log(date);
        console.log(time);
        const snapshott = admin.database().ref(schoolID+'/'+sensorID+'/humiset/'+date).child(time).set(newData);
        // console.log(oldData+newData+schoolID+sensorID+kr_curr);
      });

exports.tempDetected = functions.database.ref('{school}/{sensor}/temp')
      .onWrite((change,context) =>{
        var oldData = change.before.val();
        var newData = change.after.val();
        var schoolID = context.params.school;
        var sensorID = context.params.sensor;
        // 1. 현재 시간(Locale)
        const curr = new Date();

        // 2. UTC 시간 계산
        const utc = 
            curr.getTime() + 
            (curr.getTimezoneOffset() * 60 * 1000);


        // 3. UTC to KST (UTC + 9시간)
        const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
        const kr_curr = new Date(utc + (KR_TIME_DIFF));

        var date = kr_curr.toLocaleDateString().replace(/\//gi,"-");
        var time = kr_curr.toLocaleTimeString();
        console.log(curr);        
        console.log(utc);
        console.log(kr_curr);
        console.log(date);
        console.log(time);
        const snapshott = admin.database().ref(schoolID+'/'+sensorID+'/tempset/'+date).child(time).set(newData);
        // console.log(oldData+newData+schoolID+sensorID+kr_curr);
      });