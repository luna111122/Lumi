export default function AnalyzeButton({ isLoading, onClick }) {
  return (
    <button className="analyze-btn" onClick={onClick} disabled={isLoading}>
      {isLoading ? "Analyzing..." : "Analyze"}
    </button>
  );
}
