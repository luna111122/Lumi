from pydantic import BaseModel
from typing import List


class AnalyzeRequest(BaseModel):
    log: str


class AnalyzeResponse(BaseModel):
    summary: str
    errors: List[str]
    cause: str
    solution: str
