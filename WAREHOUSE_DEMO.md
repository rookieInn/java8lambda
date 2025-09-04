## Warehouse Backend Demo

FastAPI + SQLite demo for a simple warehouse/admin system with JWT auth.

### Features
- Auth: `/auth/login` (admin: admin/admin123)
- CRUD: products, suppliers, inventory
- Orders: purchase increases stock, sales decrements stock
- SQLite file at `./data/warehouse.db`

### Quickstart
```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Open docs: http://localhost:8000/docs

### Example
- Login via `/auth/login` (OAuth2PasswordRequestForm): username `admin`, password `admin123`.
- Use token as `Bearer` for protected endpoints.
