from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.db import get_db
from app.deps import require_admin
from app.models import (
    Product,
    Inventory,
    PurchaseOrder,
    PurchaseOrderItem,
    SalesOrder,
    SalesOrderItem,
)
from app.schemas import (
    PurchaseOrderCreate,
    PurchaseOrderOut,
    SalesOrderCreate,
    SalesOrderOut,
)


router = APIRouter(prefix="/orders", tags=["orders"]) 


@router.post("/purchase", response_model=PurchaseOrderOut, dependencies=[Depends(require_admin)])
def create_purchase_order(payload: PurchaseOrderCreate, db: Session = Depends(get_db)):
    order = PurchaseOrder(supplier_id=payload.supplier_id, status="RECEIVED")
    db.add(order)
    db.flush()

    for item in payload.items:
        db.add(PurchaseOrderItem(order_id=order.id, product_id=item.product_id, quantity=item.quantity, price=item.price))
        inv = db.execute(select(Inventory).where(Inventory.product_id == item.product_id)).scalar_one_or_none()
        if inv:
            inv.quantity += item.quantity
        else:
            db.add(Inventory(product_id=item.product_id, quantity=item.quantity, location="MAIN"))

    db.commit()
    db.refresh(order)
    return order


@router.post("/sales", response_model=SalesOrderOut, dependencies=[Depends(require_admin)])
def create_sales_order(payload: SalesOrderCreate, db: Session = Depends(get_db)):
    order = SalesOrder(customer_name=payload.customer_name or "WALK-IN", status="CONFIRMED")
    db.add(order)
    db.flush()

    for item in payload.items:
        inv = db.execute(select(Inventory).where(Inventory.product_id == item.product_id)).scalar_one_or_none()
        if not inv or inv.quantity < item.quantity:
            raise HTTPException(status_code=400, detail=f"Insufficient stock for product {item.product_id}")
        inv.quantity -= item.quantity
        db.add(SalesOrderItem(order_id=order.id, product_id=item.product_id, quantity=item.quantity, price=item.price))

    db.commit()
    db.refresh(order)
    return order
