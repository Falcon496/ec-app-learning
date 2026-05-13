import { useState } from 'react';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { useMemberStatus, useOrderHistory } from '../../features/orders/hooks/useOrders';
import { EmptyState } from '../../shared/ui/EmptyState';
import { FeedbackAlert } from '../../shared/ui/FeedbackAlert';
import { LoadingState } from '../../shared/ui/LoadingState';
import { formatCurrency, formatDateTime } from '../../shared/utils/formatters';

const PAGE_SIZE = 10;

export function OrdersPage() {
  const [page, setPage] = useState(0);
  const { userId } = useAuth();
  const orderHistory = useOrderHistory(userId, page, PAGE_SIZE);
  const memberStatus = useMemberStatus(userId);
  const pageable = orderHistory.data?.pageableDto;
  const canGoBack = page > 0;
  const canGoNext = pageable ? page + 1 < pageable.totalPages : false;

  return (
    <main className="page-shell">
      <section className="container py-5">
        <div className="d-flex flex-column flex-md-row justify-content-between gap-3 mb-4">
          <div>
            <h1 className="h3 mb-1">Orders</h1>
            <p className="text-secondary mb-0">Review your purchase history and membership rank.</p>
          </div>
          <div className="text-md-end">
            {memberStatus.isLoading ? (
              <span className="badge text-bg-light">Loading rank...</span>
            ) : memberStatus.data ? (
              <div>
                <div className="badge text-bg-primary fs-6">{memberStatus.data.rank}</div>
                <div className="small text-secondary mt-1">{memberStatus.data.points} points</div>
              </div>
            ) : (
              <span className="badge text-bg-secondary">No rank yet</span>
            )}
          </div>
        </div>

        {orderHistory.isLoading ? <LoadingState label="Loading orders..." /> : null}

        {orderHistory.isError ? (
          <FeedbackAlert variant="danger">
            {orderHistory.error instanceof Error ? orderHistory.error.message : 'Failed to load orders.'}
          </FeedbackAlert>
        ) : null}

        {orderHistory.data && orderHistory.data.content.length === 0 ? (
          <EmptyState title="No orders yet" description="Items you order from the cart will appear here." />
        ) : null}

        {orderHistory.data && orderHistory.data.content.length > 0 ? (
          <div className="d-flex flex-column gap-3">
            {orderHistory.data.content.map((order) => (
              <article className="card shadow-sm" key={order.orderNumber}>
                <div className="card-body">
                  <div className="d-flex flex-column flex-lg-row justify-content-between gap-3 mb-3">
                    <div>
                      <h2 className="h6 mb-1">{order.orderNumber}</h2>
                      <p className="text-secondary mb-0">{formatDateTime(order.orderDate)}</p>
                    </div>
                    <div className="text-lg-end">
                      <strong>{formatCurrency(order.totalPrice)}</strong>
                      <div className="small text-secondary">{order.totalQuantity} items</div>
                    </div>
                  </div>
                  <ul className="order-items">
                    {order.orderItems.map((item) => (
                      <li key={`${order.orderNumber}-${item.productId}-${item.productName}`}>
                        {item.productName} - {formatCurrency(item.price)}
                      </li>
                    ))}
                  </ul>
                </div>
              </article>
            ))}
          </div>
        ) : null}

        {pageable ? (
          <div className="d-flex justify-content-between align-items-center mt-4">
            <button
              className="btn btn-outline-primary"
              type="button"
              disabled={!canGoBack || orderHistory.isFetching}
              onClick={() => setPage((currentPage) => Math.max(currentPage - 1, 0))}
            >
              Previous
            </button>
            <span className="text-secondary">
              Page {pageable.pageNumber + 1} of {Math.max(pageable.totalPages, 1)}
            </span>
            <button
              className="btn btn-outline-primary"
              type="button"
              disabled={!canGoNext || orderHistory.isFetching}
              onClick={() => setPage((currentPage) => currentPage + 1)}
            >
              Next
            </button>
          </div>
        ) : null}
      </section>
    </main>
  );
}
