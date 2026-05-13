import { apiRequest } from '../../../shared/api/httpClient';
import type { MemberStatusResponse, OrderHistoryResponse, OrderRequest, OrderResponse } from '../model/types';

export function placeOrder(orderRequest: OrderRequest) {
  return apiRequest<OrderResponse>('/orders', {
    method: 'POST',
    body: orderRequest,
  });
}

export function calculateAndUpdateMemberStatus(userId: string, orderNumber: string) {
  return apiRequest<MemberStatusResponse>('/member-status/calculate', {
    method: 'POST',
    body: { userId, orderNumber },
  });
}

export function getOrderHistory(userId: string, page: number, size: number) {
  const params = new URLSearchParams({
    userId,
    page: String(page),
    size: String(size),
  });

  return apiRequest<OrderHistoryResponse>(`/orders?${params.toString()}`);
}

export function getMemberStatus(userId: string) {
  return apiRequest<MemberStatusResponse>(`/member-status/${userId}`);
}
