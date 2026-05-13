import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  calculateAndUpdateMemberStatus,
  getMemberStatus,
  getOrderHistory,
  placeOrder,
} from '../api/orderApi';
import type { OrderRequest } from '../model/types';

export const orderKeys = {
  all: ['orders'] as const,
  history: (userId: string, page: number, size: number) => ['orders', 'history', userId, page, size] as const,
  memberStatus: (userId: string) => ['orders', 'memberStatus', userId] as const,
};

export function useOrderHistory(userId: string | null, page: number, size: number) {
  return useQuery({
    queryKey: orderKeys.history(userId ?? 'anonymous', page, size),
    queryFn: () => getOrderHistory(userId!, page, size),
    enabled: Boolean(userId),
  });
}

export function useMemberStatus(userId: string | null) {
  return useQuery({
    queryKey: orderKeys.memberStatus(userId ?? 'anonymous'),
    queryFn: () => getMemberStatus(userId!),
    enabled: Boolean(userId),
    retry: false,
  });
}

export function useCheckoutOrder() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ orderRequest }: { userId: string; orderRequest: OrderRequest }) => {
      const order = await placeOrder(orderRequest);
      const memberStatus = await calculateAndUpdateMemberStatus(orderRequest.userId, order.orderNumber);
      return { order, memberStatus };
    },
    onSuccess: (_data, variables) => {
      void queryClient.invalidateQueries({ queryKey: orderKeys.all });
      void queryClient.invalidateQueries({ queryKey: orderKeys.memberStatus(variables.userId) });
    },
  });
}
