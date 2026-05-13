import { useMemo, useState } from 'react';
import { CategoryFilter } from '../../features/products/components/CategoryFilter';
import { ProductCard } from '../../features/products/components/ProductCard';
import { PRODUCT_CATEGORIES, PRODUCTS, filterProducts, sortProducts } from '../../features/products/model/products';
import type { ProductCategory, ProductSortOption } from '../../features/products/model/types';
import { EmptyState } from '../../shared/ui/EmptyState';

export function HomePage() {
  const [selectedCategory, setSelectedCategory] = useState<ProductCategory | ''>('');
  const [sortOption, setSortOption] = useState<ProductSortOption>('default');
  const visibleProducts = useMemo(() => {
    return sortProducts(filterProducts(PRODUCTS, selectedCategory), sortOption);
  }, [selectedCategory, sortOption]);

  const handleSelectCategory = (category: ProductCategory) => {
    setSelectedCategory((currentCategory) => (currentCategory === category ? '' : category));
  };

  return (
    <main className="page-shell">
      <section className="container py-5">
        <CategoryFilter
          categories={PRODUCT_CATEGORIES}
          selectedCategory={selectedCategory}
          onSelect={handleSelectCategory}
        />

        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
          <p className="mb-0 text-secondary">{visibleProducts.length} items found</p>
          <select
            aria-label="Sort products"
            className="form-select w-auto"
            value={sortOption}
            onChange={(event) => setSortOption(event.target.value as ProductSortOption)}
          >
            <option value="default">Sort by: Product Id</option>
            <option value="priceAsc">Sort by: Price: Low to High</option>
            <option value="priceDesc">Sort by: Price: High to Low</option>
            <option value="ratingDesc">Sort by: Avg. Customer Review</option>
          </select>
        </div>

        {visibleProducts.length === 0 ? (
          <EmptyState title="No products found in this category" />
        ) : (
          <div className="row g-4">
            {visibleProducts.map((product) => (
              <div className="col-md-6 col-xl-3" key={product.productId}>
                <ProductCard product={product} />
              </div>
            ))}
          </div>
        )}
      </section>
    </main>
  );
}
