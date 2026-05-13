import { describe, expect, it } from 'vitest';
import { filterProducts, markNewProducts, sortProducts } from './products';
import type { Product } from './types';

const products: Product[] = [
  { productId: 2, rating: 5, productName: 'B', category: 'TV', price: 300, isSale: false, releaseDate: '2024-03-01', availableQty: 1, imageUrl: '/b.jpg' },
  { productId: 1, rating: 2, productName: 'A', category: 'Fridge', price: 100, isSale: false, releaseDate: '2023-12-31', availableQty: 1, imageUrl: '/a.jpg' },
  { productId: 3, rating: 4, productName: 'C', category: 'TV', price: 200, isSale: false, releaseDate: '2024-01-02', availableQty: 1, imageUrl: '/c.jpg' },
];

describe('product utilities', () => {
  it('marks products released within the latest three months as new', () => {
    const markedProducts = markNewProducts(products, new Date('2024-04-01'));

    expect(markedProducts.find((product) => product.productId === 2)?.isNew).toBe(true);
    expect(markedProducts.find((product) => product.productId === 1)?.isNew).toBe(false);
  });

  it('filters by category', () => {
    const filteredProducts = filterProducts(products, 'TV');

    expect(filteredProducts.map((product) => product.productId)).toEqual([2, 3]);
  });

  it('sorts without mutating the original list', () => {
    const sortedProducts = sortProducts(products, 'priceAsc');

    expect(sortedProducts.map((product) => product.productId)).toEqual([1, 3, 2]);
    expect(products.map((product) => product.productId)).toEqual([2, 1, 3]);
  });
});
