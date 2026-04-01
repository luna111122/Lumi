import { useState } from "react";
import LogInput from "../components/LogInput";
import AnalyzeButton from "../components/AnalyzeButton";
import ResultPanel from "../components/ResultPanel";
import { analyzeLog } from "../api/analyzeApi";
import "../components/components.css";

export default function AnalyzePage() {
  const [log, setLog] = useState("");
  const [result, setResult] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  async function handleAnalyze() {
    if (!log.trim()) return;
    setIsLoading(true);
    try {
      const data = await analyzeLog(log);
      setResult(data);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <>
      <nav className="navbar">
        <span className="navbar-logo">Lumi</span>
        <span className="navbar-sub">AI Log Analyzer</span>
      </nav>

      <div className="page">
        <p className="page-title">로그 분석</p>
        <p className="page-desc">서버 로그를 붙여넣으면 AI가 에러 원인과 해결책을 분석합니다.</p>

        <div className="workspace">
          <div className="panel">
            <p className="panel-label">Log Input</p>
            <LogInput log={log} setLog={setLog} />
            <div className="btn-row">
              <AnalyzeButton isLoading={isLoading} onClick={handleAnalyze} />
            </div>
          </div>

          <div className="panel">
            <p className="panel-label">Analysis Result</p>
            <ResultPanel result={result} />
          </div>
        </div>
      </div>
    </>
  );
}
