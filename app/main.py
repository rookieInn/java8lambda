from fastapi import FastAPI
from sqlalchemy.orm import Session

from app.core.config import settings
from app.db import Base, engine, SessionLocal
from app.models import User, Product, Supplier, Inventory
from app.security import hash_password
from app.routers import auth as auth_router
from app.routers import core as core_router
from app.routers import orders as orders_router


def seed_data_if_needed():
    if not settings.seed_demo_data:
        return
    db: Session = SessionLocal()
    try:
        if not db.query(User).filter(User.username == "admin").first():
            admin = User(username="admin", password_hash=hash_password("admin123"), is_admin=1)
            db.add(admin)
        if not db.query(Product).first():
            p1 = Product(sku="SKU-001", name="Widget A", description="Basic widget", unit_price=9.99)
            p2 = Product(sku="SKU-002", name="Widget B", description="Advanced widget", unit_price=19.99)
            db.add_all([p1, p2])
        if not db.query(Supplier).first():
            s1 = Supplier(name="Acme Supplies", contact="Jane", phone="123456789", address="123 Road")
            db.add(s1)
        db.commit()
    finally:
        db.close()


app = FastAPI(title=settings.app_name, debug=settings.debug)


@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)
    seed_data_if_needed()


app.include_router(auth_router.router)
app.include_router(core_router.router)
app.include_router(orders_router.router)


@app.get("/")
def root():
    return {"message": "Warehouse Backend Demo running", "login": "/auth/login"}
