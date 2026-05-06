export interface OrderRequest {
  userId: string;
  userName: string;
  totalPrice: number;
  totalQuantity: number;
  orderItems: OrderItem[];
}

export interface OrderItem {
  productId: number;
  productName: string;
  price: number;
}

export interface OrderResponse {
  orderNumber: string;
  status: string;
  message: string;
}
