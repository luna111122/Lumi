# Backend CLAUDE.md

## Stack

- Java 17 / Spring Boot 3.x
- Spring Web, Spring WebFlux (WebClient)
- PostgreSQL + Flyway
- JUnit 5 + Mockito

---

## Package Structure
```
com.logmind
├── domain
│   └── analyze
│       ├── controller/    ← REST 엔드포인트만, 비즈니스 로직 없음
│       │   └── AnalyzeController.java
│       ├── service/       ← 비즈니스 로직, 트랜잭션 경계
│       │   ├── AnalyzeService.java        ← interface
│       │   └── AnalyzeServiceImpl.java    ← 구현체
│       ├── client/        ← 외부 HTTP 호출 (AiClient → FastAPI)
│       │   └── AiClient.java
│       ├── dto/           ← Request / Response 객체
│       │   ├── AnalyzeRequest.java
│       │   └── AnalyzeResponse.java
│       └── exceptions/    ← 이 도메인 전용 예외 및 에러/성공 코드
│           ├── code/      ← BaseErrorCode / BaseSuccessCode 구현 enum
│           │   ├── AnalyzeErrorCode.java
│           │   └── AnalyzeSuccessCode.java
│           └── AnalyzeException.java      ← GeneralException 상속
│
└── global
    ├── apiPayload/
    │   ├── code/
    │   │   ├── BaseErrorCode.java             ← interface
    │   │   ├── BaseSuccessCode.java            ← interface
    │   │   ├── GeneralErrorCode.java           ← enum (공통 에러: 400, 401, 403, 404)
    │   │   └── GeneralSuccessCode.java         ← enum (공통 성공: 200, 201, 202, 204)
    │   ├── exception/
    │   │   ├── handler/
    │   │   │   └── GeneralExceptionHandler.java  ← @RestControllerAdvice
    │   │   └── GeneralException.java
    │   └── ApiResponse.java                   ← 공통 응답 래퍼
    ├── config/
    │   └── WebClientConfig.java               ← WebClient Bean 등록
    ├── entity/
    │   └── BaseEntity.java                    ← createdAt, updatedAt (JPA Auditing)
    ├── security/
    │   ├── jwt/
    │   │   ├── JwtProvider.java               ← 토큰 생성/검증
    │   │   └── JwtFilter.java                 ← OncePerRequestFilter
    │   └── SecurityConfig.java                ← Spring Security 설정
    └── utils/
        └── AuthUtils.java                     ← 현재 로그인 유저 조회 등
```

---

## Conventions

### Naming

#### 기본 규칙
- 클래스 · 메서드 · 변수: camelCase
- 상수: UPPER_SNAKE_CASE
- 패키지: lowercase

#### 클래스 네이밍

| 종류 | 패턴 | 예시 |
|------|------|------|
| 컨트롤러 | `XxxController` | `AnalyzeController` |
| 서비스 | `XxxService` / `XxxServiceImpl` | `AnalyzeService` |
| 리포지토리 | `XxxRepository` | `AnalyzeRepository` |
| 요청 DTO | `XxxRequest` | `AnalyzeRequest` |
| 응답 DTO | `XxxResponse` | `AnalyzeResponse` |
| 예외 | `XxxException` | `AnalyzeException` |
| 유틸 | `XxxUtil`, `XxxHelper` | `DateTimeUtil` |
| 테스트 | `XxxTest` | `AnalyzeControllerTest` |

#### 메서드 네이밍

**동사 + 목적어 (+ 조건/범위)** 형태를 반드시 따른다.

| 좋음 | 나쁨 |
|------|------|
| `createUser` | `userCreate` |
| `updateUserEmail` | `userUpdate` |
| `getUserById` | `getUser` |
| `findActiveUsers` | `userList` |

#### 기능별 패턴

| 유형 | 패턴 | 예시 |
|------|------|------|
| 단건 조회 | `getXxxById`, `findXxxByCondition` | `getUserById(id)` |
| 다건 조회 | `findXxx`, `listXxx`, `searchXxx` | `findUsersByStatus(status)` |
| 상태 확인 | `getCurrentXxx`, `calculateXxx` | `getCurrentUserBalance()` |
| 신규 생성 | `createXxx` | `createUser(request)` |
| JPA save | `saveXxx` | `saveUser(user)` |
| 전체 수정 | `updateXxx` | `updateUser(request)` |
| 부분 수정 | `updateXxxField`, `changeXxxStatus` | `updateUserEmail(email)` |
| 물리 삭제 | `deleteXxx` | `deleteUser(id)` |
| 소프트 삭제 | `softDeleteXxx`, `deactivateXxx` | `softDeleteUser(id)` |
| boolean 반환 | `isXxx`, `hasXxx`, `canXxx`, `existsXxx` | `isUserActive(id)` |

#### 조건/상태 명시 기준

필수 조건은 메서드명에 반드시 포함한다.

- 좋음: `findActiveUsers()`, `findUsersByDeletedFalse()`
- 나쁨: `findUsers()`, `getOrder()`

#### 네이밍 체크리스트

새 메서드/클래스 작성 전 반드시 확인:

- 동사 + 목적어 구조인가?
- 반환 타입과 메서드명이 일치하는가? (단건 → `get/find`, 다건 → `list/find`)
- 조건이 명확한가? (모호하면 조건 추가)
- 기존 같은 계층의 네이밍과 통일되었는가?
- boolean 메서드는 `is/has/can` 형태인가?

> 새로운 메서드/클래스 제안 전 해당 계층의 기존 파일 1~2개를 먼저 읽고 패턴을 맞출 것. 모호한 경우 기본 패턴(`createXxx`, `getXxxById`)을 우선 사용.

---

### API Design

1. 모든 API는 `/api` 로 시작
2. RESTful 설계 준수
    - 리소스는 명사, 복수형 사용 (`/api/analyses`)
    - 행위는 HTTP 메서드로 표현 (GET / POST / PUT / DELETE)
    - URL에 동사 금지 (`/api/getAnalyze` ❌)

---

### API Response

모든 응답은 `ApiResponse<T>` 래퍼 사용
```json
{
  "isSuccess": true,
  "code": "COMMON200_1",
  "message": "요청이 성공적으로 처리되었습니다.",
  "result": { ... }
}
```

- 성공: `ApiResponse.onSuccess(code, result)`
- 실패: `ApiResponse.onFailure(code, result)`

---

### Layer 책임

| Layer | 역할 |
|-------|------|
| Controller | 요청 수신, `@Valid` 검사, 응답 반환만 |
| Service | 비즈니스 로직, 트랜잭션 경계 |
| Client | FastAPI 등 외부 HTTP 통신만 |
| Repository | 조회/저장만 — 비즈니스 로직 금지 |

---

### Code Rules

1. 생성자 주입은 `@RequiredArgsConstructor` 사용 — `@Autowired` 금지
2. Controller 응답은 항상 DTO로 반환 — Entity 직접 반환 금지
3. Service는 항상 Interface + Impl 분리
4. Swagger 어노테이션은 Interface에만 작성, Controller는 implements만
    - API 스펙 변경 시 반드시 Swagger Interface도 함께 업데이트
5. AiClient 로직을 Controller / Service에 직접 작성 금지
6. Lombok `@Data` 금지 — `@Getter` + `@Builder` 사용
7. SOLID 원칙 준수
8. 확실하지 않은 부분은 스스로 판단하지 말고 반드시 먼저 질문할 것

---

### Exception & Error Code

1. 예외는 `GeneralException` 상속한 도메인별 커스텀 예외 사용
    - 예: `throw new AnalyzeException(AnalyzeErrorCode.XXX)`
2. 에러코드 enum → `BaseErrorCode` 구현, 도메인별 `XxxErrorCode` 작성
3. 성공코드 enum → `BaseSuccessCode` 구현, 도메인별 `XxxSuccessCode` 작성
4. 공통 에러/성공은 `GeneralErrorCode` / `GeneralSuccessCode` 사용
5. 모든 예외는 `GeneralExceptionHandler` 에서 처리 — Controller try-catch 금지

---

### JPA / DB

1. `@Table(name = "...")` — 테이블명 항상 명시
2. `@Column(name = "...", nullable = ...)` — 컬럼명, nullable 항상 명시
3. `@Enumerated(EnumType.ORDINAL)` 금지 — 반드시 `EnumType.STRING` 사용
4. Entity의 고정 선택값은 enum으로 통일
5. 공통 필드(생성일, 수정일)는 `BaseEntity` 상속
6. 연관관계는 단방향 우선 — 양방향은 꼭 필요할 때만
7. FetchType 기본값
    - `@ManyToOne`, `@OneToOne`: `LAZY` 사용 — `EAGER` 금지
    - `@OneToMany`, `@ManyToMany`: 반드시 `LAZY`, 필요 시 fetch join 사용
8. N+1 문제 발생 지점은 fetch join / `@EntityGraph` / DTO 조회로 해결
9. 페이징은 커스텀 어노테이션 사용

### 트랜잭션

1. `@Transactional`은 Service 레이어에만
2. 읽기 전용 쿼리는 `@Transactional(readOnly = true)` 사용

### 쿼리

1. 단순 조회: Spring Data JPA 메서드 쿼리
2. 복잡한 조회: `@Query` (JPQL) 또는 QueryDSL

### 마이그레이션

1. Entity 변경 시 반드시 Flyway 마이그레이션 스크립트 작성
2. `ddl-auto=validate` 유지 — DDL 직접 수정 금지
3. 파일 네이밍: `V{버전}__{설명}.sql`
4. DB 접속 정보(URL, password 등)는 코드에 직접 노출 금지 — 환경변수 사용

---

### 로깅

- `@Slf4j` 사용 — `System.out.println` 금지
- 로그 레벨 기준

| 레벨 | 사용 기준 |
|------|----------|
| `INFO` | 정상 흐름 (요청 수신, 처리 완료 등) |
| `WARN` | 잠재적 문제 (재시도, 예상된 예외 등) |
| `ERROR` | 처리 불가 오류 (예외 catch, 외부 API 실패 등) |
| `DEBUG` / `TRACE` | 개발 환경에서만 사용 |

- 외부 API 호출(AiClient 등) 실패 시 ERROR 레벨로 로그 남기고 커스텀 예외로 전환 — 원본 예외 메시지 그대로 노출 금지
- 비밀번호 · 토큰 · 개인정보 등 민감정보는 절대 로그에 남기지 말 것

---

### 환경변수 & 설정 파일

- `application.yml` + `application-{profile}.yml` 구조 사용
- 공통 설정은 `application.yml`, 환경별(DB, 외부 API URL 등)은 프로필별 파일로 분리
- DB 비밀번호 · API 키 · JWT 시크릿 등은 반드시 환경변수로 관리 — 코드/설정 파일에 직접 작성 금지

---

### DTO 검증

- Controller에서 `@Valid` + `@RequestBody` 로 요청 DTO 검증
- `@NotNull` / `@NotBlank` / `@Size` / `@Email` / `@Pattern` 등 적절한 검증 어노테이션 사용
- 검증 실패는 `GeneralExceptionHandler`(`@RestControllerAdvice`)에서 처리 — 일관된 에러 응답 포맷 유지

---

### Security

1. 인증/인가는 Spring Security 사용
2. 최소 권한 원칙(least privilege) 준수
3. 민감 정보(비밀번호, 토큰, 키 등)는 코드에 절대 노출 금지
    - 예시 필요 시 `dummy-secret`, `example-token` 등 더미값 사용

---

## Testing

### 슬라이스 테스트 우선 — `@SpringBootTest` 지양

| 레이어 | 어노테이션 | Mock |
|--------|-----------|------|
| Controller | `@WebMvcTest` | `@MockBean` |
| Service | 순수 JUnit 5 | `@ExtendWith(MockitoExtension.class)` |

### 구조

1. given / when / then 구조 필수
2. 테스트 메서드 이름: `메서드명_상황_기대결과`
    - 예: `analyze_로그없음_400반환`
3. 로직 수정 시 관련 테스트도 반드시 함께 수정 — 테스트 없이 로직만 변경 금지
4. 깨진 테스트가 있으면 테스트 vs 코드 중 무엇이 잘못됐는지 먼저 분석 후 수정 이유를 커밋 메시지에 남길 것

### 필수 케이스

1. 성공(happy path) + 실패/예외 케이스 둘 다 작성
2. 경계값 (null, 빈 문자열, 최대/최소값) 테스트
3. AiClient 등 외부 호출은 반드시 Mock으로 대체

### Fixture

- 테스트용 객체는 `test/fixture/` 에 모아서 재사용
- 새 테스트 작성 전 기존 테스트 파일 1~2개 먼저 읽고 패턴 맞출 것

---

```