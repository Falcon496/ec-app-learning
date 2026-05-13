import { create } from 'zustand';
import type { Product } from '../../products/model/types';

type CartState = {
  items: Product[];
  totalQuantity: number;
  totalPrice: number;
  addItem: (product: Product) => void;
  removeItem: (index: number) => void;
  clearCart: () => void;
};

function calculateTotals(items: Product[]) {
  return {
    totalQuantity: items.length,
    totalPrice: items.reduce((total, item) => total + Number(item.price), 0),
  };
}

export const useCartStore = create<CartState>((set) => ({
  items: [],
  totalQuantity: 0,
  totalPrice: 0,
  addItem: (product) =>
    set((state) => {
      const items = [product, ...state.items];
      return { items, ...calculateTotals(items) };
    }),
  removeItem: (index) =>
    set((state) => {
      const items = state.items.filter((_, itemIndex) => itemIndex !== index);
      return { items, ...calculateTotals(items) };
    }),
  clearCart: () => set({ items: [], totalQuantity: 0, totalPrice: 0 }),
}));
