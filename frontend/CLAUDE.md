# Frontend 작업 규칙

## 기술 스택
- React 18, Vite, JavaScript (TypeScript 아님)
- 스타일: 일반 CSS 파일 / CSS Module (Tailwind 없음)
- 상태관리: useState / useContext (외부 라이브러리 없음)

---

## 개발 명령어
```bash
npm run dev      # 개발 서버 (localhost:5173)
npm run build    # 프로덕션 빌드
npm run preview  # 빌드 결과 미리보기
npm run lint     # ESLint 실행
```

---

## 폴더 구조
```
src/
├── api/          # API 호출 함수 (컴포넌트 안에서 직접 fetch 금지)
├── components/   # 재사용 가능한 UI 컴포넌트
├── pages/        # 페이지 단위 컴포넌트 (상태 관리 담당)
├── hooks/        # 커스텀 훅
└── assets/       # 이미지, 폰트 등 정적 파일
```

---

## 컴포넌트 컨벤션
- 함수형 컴포넌트만 사용 (클래스 컴포넌트 금지)
- 파일명 / 컴포넌트명: PascalCase (`LogInput.jsx`)
- default export 사용
- 상태는 가능하면 Page 컴포넌트에서 관리하고 props로 내려줌
- 비동기 처리는 `async/await` 사용 (`.then()` 체이닝 피하기)

---

## 스타일 컨벤션
- 컴포넌트 공통 스타일: `components/components.css`
- 전역 레이아웃/리셋: `App.css`
- 기본 폰트/body 초기화: `index.css`
- 클래스명: kebab-case (`.result-panel`, `.analyze-btn`)

---

## CSS 레이아웃 주의사항

### Vite 기본 템플릿 초기화 시
Vite 기본 `index.css`에는 아래 스타일이 포함되어 있어 전체 화면 레이아웃을 망친다.

```css
body {
  display: flex;
  place-items: center; /* ← 가로 너비를 좁게 만드는 원인 */
}
```

**전체 화면 레이아웃 작업 시 `index.css`와 `#root`를 반드시 함께 확인하고 초기화한다.**

권장 초기화:
```css
/* index.css */
body { margin: 0; }

#root {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
}
```

---

## 하지 말아야 할 것
- 컴포넌트 안에서 직접 `fetch` / API 호출 — `src/api/`에 분리
- `any` 타입 (JS라도 JSDoc으로 타입 힌트 권장)
- `querySelector` 등 직접 DOM 조작
- props drilling 3단계 이상 — 필요하면 Context 사용
- 커밋 전 `console.log` 잔류

---

## API 연동 패턴
실제 API 연결 전에는 `src/api/` 안에 mock 함수를 만들어두고,
나중에 해당 함수 내부만 교체한다. 컴포넌트 코드는 건드리지 않는다.

```js
// src/api/analyzeApi.js
export async function analyzeLog(log) {
  // TODO: 실제 API로 교체
  await new Promise(res => setTimeout(res, 1000));
  return { summary: "...", errors: [], cause: "...", solution: "..." };
}
```
