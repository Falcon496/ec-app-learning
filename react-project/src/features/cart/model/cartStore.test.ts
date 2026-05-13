import { describe, expect, it, beforeEach } from 'vitest';
import { useCartStore } from './cartStore';
import type { Product } from '../../products/model/types';

const product: Product = {
  productId: 1,
  rating: 4,
  productName: 'French Door Fridge',
  category: 'Fridge',
  price: 2200,
  isSale: false,
  releaseDate: '2024-02-01',
  availableQty: 3,
  imageUrl: '/image.jpg',
};

beforeEach(() => {
  useCartStore.getState().clearCart();
});

describe('cartStore', () => {
  it('adds products and recalculates totals', () => {
    useCartStore.getState().addItem(product);
    useCartStore.getState().addItem({ ...product, productId: 2, price: 800 });

    expect(useCartStore.getState().totalQuantity).toBe(2);
    expect(useCartStore.getState().totalPrice).toBe(3000);
  });

  it('removes products by index', () => {
    useCartStore.getState().addItem(product);
    useCartStore.getState().addItem({ ...product, productId: 2, price: 800 });

    useCartStore.getState().removeItem(0);

    expect(useCartStore.getState().items).toHaveLength(1);
    expect(useCartStore.getState().items[0].productId).toBe(1);
    expect(useCartStore.getState().totalPrice).toBe(2200);
  });
});
