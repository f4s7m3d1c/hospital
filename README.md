# 주변 병원 탐색 API
[App](https://github.com/f4s7m3d1c/app)에서 사용되는 백엔드

## 환경
- JDK 17
- mariadb 10.5
- Spring Boot 3.1.2

## 사용법
### 1. API KEY 발급
생활안전정보에서 제공하는 OpenAPI 키를 발급 받아야 합니다

아래 링크를 누른 후 서비스 명이 `병의원(병/의원)` 인 것을 찾아서 키를 발급 받으면 됩니다.

[발급 받으러 가기](https://safemap.go.kr/opna/data/dataList.do)

### 2. 명령어를 통해 실행
```shell
 java -jar hospital.jar --DB_HOST=YOUR_DB_HOST --DB_PORT=YOUR_DB_PORT --DB_NAME=YOUR_DB_NAME --DB_USER=YOUR_DB_USERNAME --DB_PASSWORD=YOUR_DB_PASSWORD --HOSPITAL_API_KEY=YOUR_KEY
```

### 3. 요청
```http request
GET http://localhost:8080/api/hospital?lat=${위도}&lon=${경도}
```