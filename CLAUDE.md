# Lumi - AI 로그 분석 플랫폼

## 프로젝트 개요
백엔드 서비스 로그를 붙여넣으면 AI가 자동으로 에러를 분석하고 해결 방법을 제안하는 플랫폼.

---

## 기술 스택

| 서비스 | 기술 |
|--------|------|
| Backend | Java 17, Spring Boot |
| Frontend | React, Vite |
| AI Server | Python, FastAPI, Claude API |
| DB | PostgreSQL |
| Infra | Docker, AWS EC2 |

---

## 아키텍처
```
React
  ↓ REST API
Spring Boot (8080)
  ↓ HTTP
FastAPI (8000)
  ↓
Claude API
```

---

## 폴더 구조
```
Lumi/
├── .env
├── .env.example
├── docker-compose.yml
├── backend/               # Spring Boot (Java 17)
│   └── src/
├── frontend/              # React + Vite
│   └── src/
├── ai-server/             # FastAPI (Python)
│   └── main.py
└── infra/
    └── postgres/
```

---

# Git 컨벤션

## 브랜치 네이밍
```
<type>/#<issue>-<description>
```

**예시**
```
feat/#12-kakao-login
fix/#23-jwt-refresh
chore/#7-ci-setup
```

## 브랜치 전략
- 기본 브랜치: `dev`
- `main` 직접 머지 금지 — 반드시 `dev`를 거쳐야 한다
- `dev`는 `main` 머지 전 완충 브랜치로 사용한다

## 브랜치 / 커밋 타입

| type       | 설명                          |
|------------|-------------------------------|
| `feat`     | 기능 추가                     |
| `fix`      | 버그 수정                     |
| `docs`     | 문서만 수정                   |
| `style`    | 포맷/공백 등 (로직 변화 없음) |
| `refactor` | 리팩토링                      |
| `test`     | 테스트 추가/수정              |
| `chore`    | 빌드/설정/의존성/CI           |
| `perf`     | 성능 개선                     |
| `revert`   | 되돌리기                      |
| `hotfix`   | 긴급 수정                     |

## 커밋 메시지
```
<type>: 커밋 내용 설명
```

**예시**
```
feat: 결제 로직 작성
fix: JWT 토큰 만료 버그 수정
```

- 커밋 메시지는 동사형(`~하다`, `~함`)이 아닌 명사형으로 끝낸다.


# Claude 작업 규칙

## 공통 규칙 (커밋 / PR / 이슈 생성 전 필수)
1. 작업 전 반드시 사용자 승인을 받는다.
2. 초안(커밋 메시지 / PR 내용 / 이슈 내용)을 먼저 작성하고, 사용자 확인 후 실행한다.
3. 실행 후 반영 여부를 확인하고 사용자에게 보고한다.

## 커밋 생성
- 커밋 메시지는 [타입]: [요약] 형식으로 작성한다.
- 초안 확인 없이 커밋을 실행하지 않는다.

## PR 생성
- PR 제목, 본문(변경 사항 요약, 관련 이슈) 초안을 작성한 뒤 확인을 받는다.
- Draft PR 여부도 사용자에게 확인한다.

## 이슈 생성
- 이슈 제목, 본문(재현 방법 / 목적 / 기대 동작) 초안을 작성한 뒤 확인을 받는다.
- 라벨, 담당자 지정이 필요한 경우 함께 확인한다.