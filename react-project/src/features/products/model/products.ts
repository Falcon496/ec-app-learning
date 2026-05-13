import image1 from '../../../assets/images/1.jpg';
import image2 from '../../../assets/images/2.jpg';
import image3 from '../../../assets/images/3.jpg';
import image4 from '../../../assets/images/4.jpg';
import image5 from '../../../assets/images/5.jpg';
import image6 from '../../../assets/images/6.jpg';
import image7 from '../../../assets/images/7.jpg';
import image8 from '../../../assets/images/8.jpg';
import image9 from '../../../assets/images/9.jpg';
import image10 from '../../../assets/images/10.jpg';
import image11 from '../../../assets/images/11.jpg';
import image12 from '../../../assets/images/12.jpg';
import image13 from '../../../assets/images/13.jpg';
import image14 from '../../../assets/images/14.jpg';
import image15 from '../../../assets/images/15.jpg';
import type { Product, ProductCategory, ProductSortOption } from './types';

export const PRODUCT_CATEGORIES: ProductCategory[] = [
  'Fridge',
  'Washer',
  'Kitchen',
  'Vacuum',
  'Climate',
  'TV',
  'Other',
];

export const RAW_PRODUCTS: Product[] = [
  { productId: 1, rating: 4, productName: 'French Door Fridge', category: 'Fridge', price: 2200, isSale: false, releaseDate: '2024-02-01', availableQty: 3, imageUrl: image1 },
  { productId: 2, rating: 5, productName: 'Side-by-Side Fridge', category: 'Fridge', price: 1700, isSale: false, releaseDate: '2023-03-22', availableQty: 0, imageUrl: image2 },
  { productId: 3, rating: 3, productName: 'Top-Freezer Fridge', category: 'Fridge', price: 1100, isSale: true, releaseDate: '2023-01-10', availableQty: 5, imageUrl: image3 },
  { productId: 4, rating: 5, productName: 'Front Load Washer', category: 'Washer', price: 800, isSale: false, releaseDate: '2022-12-30', availableQty: 0, imageUrl: image4 },
  { productId: 5, rating: 3, productName: 'Top Load Washer', category: 'Washer', price: 700, isSale: true, releaseDate: '2023-02-14', availableQty: 2, imageUrl: image5 },
  { productId: 6, rating: 1, productName: 'OTR Microwave', category: 'Kitchen', price: 250, isSale: false, releaseDate: '2023-04-08', availableQty: 8, imageUrl: image6 },
  { productId: 7, rating: 3, productName: 'Stand Mixer', category: 'Kitchen', price: 350, isSale: true, releaseDate: '2023-05-28', availableQty: 3, imageUrl: image7 },
  { productId: 8, rating: 2, productName: 'High-Power Vacuum', category: 'Vacuum', price: 450, isSale: false, releaseDate: '2023-01-17', availableQty: 0, imageUrl: image8 },
  { productId: 9, rating: 5, productName: 'Robot Vacuum', category: 'Vacuum', price: 650, isSale: true, releaseDate: '2023-03-11', availableQty: 1, imageUrl: image9 },
  { productId: 10, rating: 1, productName: 'Air Purifier & Heater', category: 'Climate', price: 400, isSale: false, releaseDate: '2022-11-25', availableQty: 2, imageUrl: image10 },
  { productId: 11, rating: 2, productName: 'Evaporative Cooler', category: 'Climate', price: 300, isSale: false, releaseDate: '2023-02-05', availableQty: 4, imageUrl: image11 },
  { productId: 12, rating: 4, productName: '65 Inch 4K TV', category: 'TV', price: 1100, isSale: false, releaseDate: '2024-04-21', availableQty: 7, imageUrl: image12 },
  { productId: 13, rating: 3, productName: 'OLED TV', category: 'TV', price: 2000, isSale: false, releaseDate: '2023-01-03', availableQty: 2, imageUrl: image13 },
  { productId: 14, rating: 2, productName: 'Mini TV', category: 'TV', price: 250, isSale: true, releaseDate: '2023-01-10', availableQty: 5, imageUrl: image14 },
  { productId: 15, rating: 5, productName: 'Compact TV', category: 'TV', price: 300, isSale: true, releaseDate: '2023-01-10', availableQty: 5, imageUrl: image15 },
];

export function markNewProducts(products: Product[], referenceDate = new Date('2024-04-01')) {
  const threeMonthsAgo = new Date(referenceDate);
  threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);

  return products.map((product) => ({
    ...product,
    isNew: new Date(product.releaseDate) > threeMonthsAgo,
  }));
}

export function filterProducts(products: Product[], selectedCategory: ProductCategory | '') {
  if (!selectedCategory) {
    return products;
  }

  return products.filter((product) => product.category === selectedCategory);
}

export function sortProducts(products: Product[], sortOption: ProductSortOption) {
  const sortedProducts = [...products];

  switch (sortOption) {
    case 'priceAsc':
      return sortedProducts.sort((a, b) => a.price - b.price);
    case 'priceDesc':
      return sortedProducts.sort((a, b) => b.price - a.price);
    case 'ratingDesc':
      return sortedProducts.sort((a, b) => b.rating - a.rating);
    default:
      return sortedProducts.sort((a, b) => a.productId - b.productId);
  }
}

export const PRODUCTS = markNewProducts(RAW_PRODUCTS);
