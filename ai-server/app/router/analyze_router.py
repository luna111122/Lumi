from fastapi import APIRouter

from app.schema.analyze_schema import AnalyzeRequest, AnalyzeResponse
from app.service.analyze_service import analyze_log

router = APIRouter()


@router.post("/analyze", response_model=AnalyzeResponse)
def analyze(request: AnalyzeRequest) -> AnalyzeResponse:
    return analyze_log(request.log)
