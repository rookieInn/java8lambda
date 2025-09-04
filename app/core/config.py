from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "Warehouse Backend Demo"
    debug: bool = True
    secret_key: str = "super-secret-demo-key"
    access_token_expire_minutes: int = 60 * 24
    sqlite_db_path: str = "./data/warehouse.db"
    seed_demo_data: bool = True

    class Config:
        env_file = ".env"


settings = Settings()
