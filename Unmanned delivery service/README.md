# Unmanned delivery service
HADA development

1. 구성 
    * Android Studio - java
    * Firebase funtions - nodejs
    * Firebase realtime database
        + storage (호수 : 초기값-0)
        + password (호수 : 비밀번호)
        + alarm (택배함 번호 : 호수)
2. 기능 
    * 아두이노에서 호수를 저장시(택배 보관) - trigger로 이벤트 생성시 랜덤 비밀번호를 생성해 DB에 저장
    * 아두이노에서 잠금 해제 요청시 (승인 요청) - 안드로이드에서 DB변화를 감지하여 변화시 알림 생성 승인시 -> DB 변경
    * 아두이노에서 호수 삭제시 (택배 보관 종료) -  trigger로 이벤트 생성시 DB에서 삭제 
    
3.참고 사이트
   * [firebase 구글 문서](https://firebase.google.com/docs/database/android/start?authuser=0)
   * [Functions 블로그_1](https://ivvve.github.io/2019/08/27/etc/firebase/functions/easy-firebase-cloud-functions-1/)
   * [Functions 블로그_2](https://ivvve.github.io/2019/08/30/etc/firebase/functions/easy-firebase-cloud-functions-2/)
   * [Functions-realtime database Youtube](https://www.youtube.com/watch?v=bpI3Bbhlcas&t=1337s)
