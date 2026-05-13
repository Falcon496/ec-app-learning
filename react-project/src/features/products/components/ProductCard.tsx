import { Star } from 'lucide-react';
import { useCartStore } from '../../cart/model/cartStore';
import type { Product } from '../model/types';
import { formatCurrency } from '../../../shared/utils/formatters';

type ProductCardProps = {
  product: Product;
};

export function ProductCard({ product }: ProductCardProps) {
  const addItem = useCartStore((state) => state.addItem);

  return (
    <article className="card product-card shadow-sm">
      <div className="position-relative">
        {product.isSale ? <span className="badge rounded-pill text-bg-danger position-absolute top-0 start-0 m-2">Sale</span> : null}
        {product.isNew ? <span className="badge rounded-pill text-bg-info position-absolute top-0 end-0 m-2">New</span> : null}
        <img className="card-img-top product-card__image" src={product.imageUrl} alt={product.productName} />
      </div>
      <div className="card-body d-flex flex-column gap-3">
        <div className="d-flex justify-content-between gap-3">
          <h2 className="h6 mb-0">{product.productName}</h2>
          <strong>{formatCurrency(product.price)}</strong>
        </div>
        <div className="d-flex align-items-center justify-content-between gap-3 mt-auto">
          <div className="d-flex text-warning" aria-label={`${product.rating} out of 5 stars`}>
            {[0, 1, 2, 3, 4].map((starIndex) => (
              <Star
                key={starIndex}
                aria-hidden="true"
                size={18}
                fill={product.rating > starIndex ? 'currentColor' : 'none'}
              />
            ))}
          </div>
          {product.availableQty === 0 ? (
            <button className="btn btn-secondary btn-sm" type="button" disabled>
              Out of Stock
            </button>
          ) : (
            <button className="btn btn-success btn-sm" type="button" onClick={() => addItem(product)}>
              Add to Cart
            </button>
          )}
        </div>
      </div>
    </article>
  );
}
