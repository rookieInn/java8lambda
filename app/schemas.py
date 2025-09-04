from datetime import datetime
from typing import Optional, List
from pydantic import BaseModel


class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"


class UserLogin(BaseModel):
    username: str
    password: str


class UserOut(BaseModel):
    id: int
    username: str
    is_admin: int
    created_at: datetime

    class Config:
        from_attributes = True


class SupplierCreate(BaseModel):
    name: str
    contact: Optional[str] = None
    phone: Optional[str] = None
    address: Optional[str] = None


class SupplierOut(BaseModel):
    id: int
    name: str
    contact: Optional[str]
    phone: Optional[str]
    address: Optional[str]
    created_at: datetime

    class Config:
        from_attributes = True


class ProductCreate(BaseModel):
    sku: str
    name: str
    description: Optional[str] = None
    unit_price: float = 0.0


class ProductOut(BaseModel):
    id: int
    sku: str
    name: str
    description: Optional[str]
    unit_price: float
    created_at: datetime

    class Config:
        from_attributes = True


class InventoryCreate(BaseModel):
    product_id: int
    quantity: float
    location: str = "MAIN"


class InventoryOut(BaseModel):
    id: int
    product_id: int
    quantity: float
    location: str
    updated_at: datetime

    class Config:
        from_attributes = True


class PurchaseOrderItemIn(BaseModel):
    product_id: int
    quantity: float
    price: float


class PurchaseOrderCreate(BaseModel):
    supplier_id: int
    items: List[PurchaseOrderItemIn]


class PurchaseOrderOut(BaseModel):
    id: int
    supplier_id: int
    status: str
    created_at: datetime

    class Config:
        from_attributes = True


class SalesOrderItemIn(BaseModel):
    product_id: int
    quantity: float
    price: float


class SalesOrderCreate(BaseModel):
    customer_name: Optional[str] = None
    items: List[SalesOrderItemIn]


class SalesOrderOut(BaseModel):
    id: int
    status: str
    customer_name: Optional[str]
    created_at: datetime

    class Config:
        from_attributes = True
