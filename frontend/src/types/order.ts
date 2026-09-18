export interface OrderItem {
  orderItemId: number;
  productId: number;
  productName: string;
  unitPrice: number;
  quantity: number;
  subtotal: number;
}

export interface Order {
  orderId: number;
  status: string;
  totalAmount: number;
  createdAt: string;
  items: OrderItem[];
}