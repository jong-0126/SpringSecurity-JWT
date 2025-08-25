
# Spring Boot JWT 인증 시스템

Spring Security + JWT 기반 인증/인가 시스템 구현 예제입니다.  
Access Token, Refresh Token, Redis 블랙리스트, Role 기반 인가 등 실무 수준의 인증 흐름을 포함합니다.

## 주요 기능

- 회원가입 / 로그인
- Access Token / Refresh Token 발급
- JWT 인증 필터 적용
- Role 기반 API 인가
- Redis 기반 Refresh Token 저장
- 토큰 재발급 API (/users/refresh)
- 로그아웃 시 Access Token 블랙리스트 등록

## 사용 기술

- Spring Boot 3.x
- Spring Security
- jjwt (JWT 토큰 생성 및 검증)
- Redis (Refresh Token, 블랙리스트 관리)
- H2 Database (테스트용)
- Lombok

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

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

```

## API 명세

| 메서드 | URL | 설명 |
|--------|-----|------|
| POST | /users/register | 회원가입 |
| POST | /users/login | 로그인 (Access + Refresh Token 발급) |
| GET | /users/me | 사용자 정보 조회 (Access Token 필요) |
| POST | /users/refresh | Refresh Token으로 Access Token 재발급 |
| POST | /users/logout | 로그아웃 (Access Token 블랙리스트 등록) |
| GET | /admin | 관리자 전용 API (ROLE_ADMIN 필요) |

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

- Access Token: 인증용, 수명 짧음 (예: 15~30분)
- Refresh Token: 재발급용, 수명 김 (예: 7일), Redis에 저장
- 로그아웃 시 Access Token은 Redis 블랙리스트에 등록 (TTL 설정)
- `@AuthenticationPrincipal`로 현재 로그인 사용자 객체 접근
- `@PreAuthorize("hasRole('ADMIN')")` 등으로 Role 기반 인가 구현

## 향후 개선 포인트

- 토큰 자동 재발급 기능 (프론트 연동)
- Refresh Token 1회성(Rotation) 처리
- 소셜 로그인(OAuth2) 연동
- 전체 로그아웃, 비활성화 처리 기능 추가
