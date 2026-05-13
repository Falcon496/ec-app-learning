export interface OrderItem {
  productId: number;
  productName: string;
  price: number;
}

export interface OrderRequest {
  userId: string;
  userName: string;
  totalPrice: number;
  totalQuantity: number;
  orderItems: OrderItem[];
}

export interface OrderResponse {
  orderNumber: string;
  status: string;
  message: string;
}

export interface MemberStatusResponse {
  userId: string;
  points: number;
  rank: string;
}

export interface OrderDto {
  orderDate: string;
  orderNumber: string;
  totalPrice: number;
  totalQuantity: number;
  orderItems: OrderItem[];
}

export interface PageableDto {
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

export interface OrderHistoryResponse {
  content: OrderDto[];
  pageableDto: PageableDto;
}
