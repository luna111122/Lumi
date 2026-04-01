export default function LogInput({ log, setLog }) {
  return (
    <textarea
      className="log-input"
      value={log}
      onChange={(e) => setLog(e.target.value)}
      placeholder="서버 로그를 붙여넣으세요..."
      spellCheck={false}
    />
  );
}
