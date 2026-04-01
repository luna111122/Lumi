export default function ResultPanel({ result }) {
  if (!result) {
    return (
      <div className="result-empty">
        <span className="result-empty-icon">🔍</span>
        <span>분석 결과가 여기에 표시됩니다.</span>
      </div>
    );
  }

  return (
    <div className="result-panel">
      <div className="result-section">
        <h3>Summary</h3>
        <p>{result.summary}</p>
      </div>

      <div className="result-section errors">
        <h3>Errors</h3>
        <ul>
          {result.errors.map((err, i) => (
            <li key={i}>{err}</li>
          ))}
        </ul>
      </div>

      <div className="result-section cause">
        <h3>Cause</h3>
        <p>{result.cause}</p>
      </div>

      <div className="result-section solution">
        <h3>Solution</h3>
        <p>{result.solution}</p>
      </div>
    </div>
  );
}
