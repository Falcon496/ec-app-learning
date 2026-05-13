type EmptyStateProps = {
  title: string;
  description?: string;
};

export function EmptyState({ title, description }: EmptyStateProps) {
  return (
    <div className="text-center py-5">
      <h2 className="h5">{title}</h2>
      {description ? <p className="text-secondary mb-0">{description}</p> : null}
    </div>
  );
}
