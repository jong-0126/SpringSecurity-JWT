# Spring Boot JWT 인증 시스템

Spring Security + JWT 기반 인증/인가 시스템 구현 예제입니다.
Access Token, Refresh Token, Redis 블랙리스트, Role 기반 인가 등 실무 수준의 인증 흐름을 포함합니다.

## 주요 기능

* 회원가입 / 로그인
* Access Token / Refresh Token 발급
* JWT 인증 필터 적용
* Role 기반 API 인가
* Redis 기반 Refresh Token 저장
* 토큰 재발급 API (/users/refresh)
* 로그아웃 시 Access Token 블랙리스트 등록

## 사용 기술

* Spring Boot 3.x
* Spring Security
* jjwt (JWT 토큰 생성 및 검증)
* Redis (Refresh Token, 블랙리스트 관리)
* H2 Database (테스트용)
* Lombok

## 프로젝트 구조

```
src
└─ main
   ├─ java
   │   └─ com.example.jwt
   │        ├─ controller
   │        ├─ service
   │        ├─ jwt
   │        ├─ entity
   │        ├─ repository
   │        └─ config
   └─ resources
      └─ application.yml
```

## 실행 방법

1. Redis 실행

```
docker run -p 6379:6379 redis
```

2. 프로젝트 빌드 및 실행

```
./gradlew build
./gradlew bootRun
```

3. H2 Console 접속

```
http://localhost:8080/h2-console
```

## API 명세

| 메서드  | URL             | 설명                               |
| ---- | --------------- | -------------------------------- |
| POST | /users/register | 회원가입                             |
| POST | /users/login    | 로그인 (Access + Refresh Token 발급)  |
| GET  | /users/me       | 사용자 정보 조회 (Access Token 필요)      |
| POST | /users/refresh  | Refresh Token으로 Access Token 재발급 |
| POST | /users/logout   | 로그아웃 (Access Token 블랙리스트 등록)     |
| GET  | /admin          | 관리자 전용 API (ROLE\_ADMIN 필요)      |

## 테스트 예시 (Postman)

### 로그인 요청

```
POST /users/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "1234"
}
```

### 인증 요청

```
GET /users/me
Authorization: Bearer <accessToken>
```

### 토큰 재발급

```
POST /users/refresh
Content-Type: application/json

{
  "refreshToken": "<refreshToken>"
}
```

## 핵심 개념 요약

* Access Token: 인증용, 수명 짧음 (예: 15\~30분)
* Refresh Token: 재발급용, 수명 김 (예: 7일), Redis에 저장
* 로그아웃 시 Access Token은 Redis 블랙리스트에 등록 (TTL 설정)
* `@AuthenticationPrincipal`로 현재 로그인 사용자 객체 접근
* `@PreAuthorize("hasRole('ADMIN')")` 등으로 Role 기반 인가 구현

## 향후 개선 포인트

* 토큰 자동 재발급 기능 (프론트 연동)
* Refresh Token 1회성(Rotation) 처리
* 소셜 로그인(OAuth2) 연동
* 전체 로그아웃, 비활성화 처리 기능 추가

---

## Rate Limiting (요청 제한) 기능

특정 API에 대한 과도한 요청을 방지하기 위해 **Bucket4j + Redis** 기반의 Rate Limiting 필터를 추가했습니다.

### 적용 방식

* 클라이언트 IP 또는 사용자 기준으로 요청 횟수 제한
* 10초당 최대 10회 요청 허용
* 초과 시 HTTP 429 (Too Many Requests) 응답

### 사용 라이브러리

* Bucket4j 7.6.0 (Core + Redis)
* Spring Security Filter에 등록

### 의존성 추가 (Gradle)

```groovy
implementation 'com.github.vladimir-bukhtoyarov:bucket4j-core:7.6.0'
implementation 'com.bucket4j:bucket4j-jcache:7.6.0'
implementation 'com.bucket4j:bucket4j-redis:7.6.0'
```

### Redis 실행 (Docker Compose 예시)

```yaml
redis:
  image: redis:6.2
  ports:
    - "6379:6379"
```

### 동작 흐름

1. 요청이 들어오면 클라이언트 식별자(IP 또는 사용자 ID)를 기준으로 Redis에서 토큰 버킷을 조회
2. 버킷이 비어있지 않으면 요청 통과, 1개 소모
3. 버킷이 비어있으면 429 응답 반환

### 예시 응답

```
HTTP/1.1 429 Too Many Requests
Content-Type: text/plain

Too many requests - try again later
```

### 확장 아이디어

* 사용자 ID 기반 제한 (JWT 인증 이후 필터 실행 시)
* 관리자 계정 제외 설정
* URI별 또는 등급별 요금제 적용
