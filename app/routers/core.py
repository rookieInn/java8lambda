from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.db import get_db
from app.deps import require_admin
from app.models import Product, Supplier, Inventory
from app.schemas import (
    ProductCreate,
    ProductOut,
    SupplierCreate,
    SupplierOut,
    InventoryCreate,
    InventoryOut,
)


router = APIRouter(prefix="/api", tags=["core"])


# Products
@router.post("/products", response_model=ProductOut, dependencies=[Depends(require_admin)])
def create_product(payload: ProductCreate, db: Session = Depends(get_db)):
    if db.query(Product).filter(Product.sku == payload.sku).first():
        raise HTTPException(status_code=400, detail="SKU already exists")
    obj = Product(**payload.model_dump())
    db.add(obj)
    db.commit()
    db.refresh(obj)
    return obj


@router.get("/products", response_model=list[ProductOut])
def list_products(db: Session = Depends(get_db)):
    return db.query(Product).order_by(Product.id.desc()).all()


@router.delete("/products/{product_id}", dependencies=[Depends(require_admin)])
def delete_product(product_id: int, db: Session = Depends(get_db)):
    obj = db.get(Product, product_id)
    if not obj:
        raise HTTPException(status_code=404, detail="Product not found")
    db.delete(obj)
    db.commit()
    return {"deleted": True}


# Suppliers
@router.post("/suppliers", response_model=SupplierOut, dependencies=[Depends(require_admin)])
def create_supplier(payload: SupplierCreate, db: Session = Depends(get_db)):
    obj = Supplier(**payload.model_dump())
    db.add(obj)
    db.commit()
    db.refresh(obj)
    return obj


@router.get("/suppliers", response_model=list[SupplierOut])
def list_suppliers(db: Session = Depends(get_db)):
    return db.query(Supplier).order_by(Supplier.id.desc()).all()


@router.delete("/suppliers/{supplier_id}", dependencies=[Depends(require_admin)])
def delete_supplier(supplier_id: int, db: Session = Depends(get_db)):
    obj = db.get(Supplier, supplier_id)
    if not obj:
        raise HTTPException(status_code=404, detail="Supplier not found")
    db.delete(obj)
    db.commit()
    return {"deleted": True}


# Inventory
@router.post("/inventory", response_model=InventoryOut, dependencies=[Depends(require_admin)])
def create_inventory(payload: InventoryCreate, db: Session = Depends(get_db)):
    obj = Inventory(**payload.model_dump())
    db.add(obj)
    db.commit()
    db.refresh(obj)
    return obj


@router.get("/inventory", response_model=list[InventoryOut])
def list_inventory(db: Session = Depends(get_db)):
    return db.query(Inventory).order_by(Inventory.id.desc()).all()
