import json
import os

import anthropic
from dotenv import load_dotenv
from fastapi import HTTPException

from app.prompt.analyze_prompt import SYSTEM_PROMPT, USER_PROMPT_TEMPLATE
from app.schema.analyze_schema import AnalyzeResponse

load_dotenv()

client = anthropic.Anthropic(api_key=os.getenv("ANTHROPIC_API_KEY"))


def analyze_log(log: str) -> AnalyzeResponse:
    try:
        message = client.messages.create(
            model="claude-sonnet-4-5",
            max_tokens=1024,
            system=SYSTEM_PROMPT,
            messages=[
                {"role": "user", "content": USER_PROMPT_TEMPLATE.format(log=log)}
            ],
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Claude API 호출 실패: {str(e)}")

    raw = message.content[0].text

    try:
        data = json.loads(raw)
    except json.JSONDecodeError:
        raise HTTPException(status_code=500, detail="응답 JSON 파싱 실패")

    return AnalyzeResponse(**data)
