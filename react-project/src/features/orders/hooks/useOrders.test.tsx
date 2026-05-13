import type { PropsWithChildren } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { act, renderHook, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { useCheckoutOrder, useOrderHistory } from './useOrders';
import type { OrderRequest } from '../model/types';

function createWrapper() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false },
    },
  });

  return function Wrapper({ children }: PropsWithChildren) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
  };
}

const orderRequest: OrderRequest = {
  userId: '11111111-1111-1111-1111-111111111111',
  userName: 'tester',
  totalPrice: 100,
  totalQuantity: 1,
  orderItems: [{ productId: 1, productName: 'TV', price: 100 }],
};

beforeEach(() => {
  vi.restoreAllMocks();
});

describe('order hooks', () => {
  it('places an order then updates member status', async () => {
    const fetchMock = vi.fn()
      .mockResolvedValueOnce(new Response(JSON.stringify({ orderNumber: 'ORD-1', status: 'SUCCESS', message: 'ok' }), { status: 201 }))
      .mockResolvedValueOnce(new Response(JSON.stringify({ userId: orderRequest.userId, points: 1, rank: 'Bronze' }), { status: 200 }));
    vi.stubGlobal('fetch', fetchMock);

    const { result } = renderHook(() => useCheckoutOrder(), { wrapper: createWrapper() });

    await act(async () => {
      await result.current.mutateAsync({ userId: orderRequest.userId, orderRequest });
    });

    expect(fetchMock).toHaveBeenCalledWith(
      'http://localhost:8080/api/orders',
      expect.objectContaining({ method: 'POST' }),
    );
    expect(fetchMock).toHaveBeenCalledWith(
      'http://localhost:8080/api/member-status/calculate',
      expect.objectContaining({ method: 'POST' }),
    );
  });

  it('exposes API errors through query state', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response('Backend error', { status: 500 })));

    const { result } = renderHook(() => useOrderHistory(orderRequest.userId, 0, 10), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isError).toBe(true));

    expect(result.current.error).toBeInstanceOf(Error);
  });
});
