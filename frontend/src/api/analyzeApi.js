export async function analyzeLog(log) {
  await new Promise(res => setTimeout(res, 1000));
  return {
    summary: "Mock summary",
    errors: ["NullPointerException", "PaymentService error"],
    cause: "Mock cause",
    solution: "Mock solution",
  };
}
