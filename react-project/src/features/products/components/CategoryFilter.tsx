import type { ProductCategory } from '../model/types';

type CategoryFilterProps = {
  categories: ProductCategory[];
  selectedCategory: ProductCategory | '';
  onSelect: (category: ProductCategory) => void;
};

export function CategoryFilter({ categories, selectedCategory, onSelect }: CategoryFilterProps) {
  return (
    <div className="d-flex flex-wrap align-items-center justify-content-center gap-2 mb-4">
      {categories.map((category) => (
        <button
          key={category}
          className={`btn ${selectedCategory === category ? 'btn-primary' : 'btn-outline-primary'}`}
          type="button"
          onClick={() => onSelect(category)}
        >
          {category}
        </button>
      ))}
    </div>
  );
}
