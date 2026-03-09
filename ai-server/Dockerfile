FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
# command는 docker-compose에서 --reload 옵션을 주어 덮어씁니다.
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]