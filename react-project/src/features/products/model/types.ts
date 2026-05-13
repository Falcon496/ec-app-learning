export interface Product {
  productId: number;
  rating: number;
  productName: string;
  category: ProductCategory;
  price: number;
  isSale: boolean;
  releaseDate: string;
  availableQty: number;
  imageUrl: string;
  isNew?: boolean;
}

export type ProductCategory = 'Fridge' | 'Washer' | 'Kitchen' | 'Vacuum' | 'Climate' | 'TV' | 'Other';

export type ProductSortOption = 'default' | 'priceAsc' | 'priceDesc' | 'ratingDesc';
