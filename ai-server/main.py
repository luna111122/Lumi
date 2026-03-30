from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.router.analyze_router import router

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(router)
