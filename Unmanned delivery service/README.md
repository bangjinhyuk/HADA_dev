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
    
