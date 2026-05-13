import { useState } from 'react';
import { Trash2, X } from 'lucide-react';
import { useAuth } from '../../auth/hooks/useAuth';
import { useCheckoutOrder } from '../../orders/hooks/useOrders';
import type { OrderRequest } from '../../orders/model/types';
import { useCartStore } from '../model/cartStore';
import { FeedbackAlert } from '../../../shared/ui/FeedbackAlert';
import { formatCurrency } from '../../../shared/utils/formatters';

type CartMenuProps = {
  isOpen: boolean;
  onClose: () => void;
};

export function CartMenu({ isOpen, onClose }: CartMenuProps) {
  const [message, setMessage] = useState<string | null>(null);
  const [variant, setVariant] = useState<'success' | 'danger' | 'info'>('info');
  const items = useCartStore((state) => state.items);
  const totalQuantity = useCartStore((state) => state.totalQuantity);
  const totalPrice = useCartStore((state) => state.totalPrice);
  const removeItem = useCartStore((state) => state.removeItem);
  const clearCart = useCartStore((state) => state.clearCart);
  const { userId, userName } = useAuth();
  const checkoutOrder = useCheckoutOrder();

  if (!isOpen) {
    return null;
  }

  const handleCheckout = async () => {
    if (!userId || !userName) {
      setVariant('danger');
      setMessage('Your session is not ready. Please sign in again.');
      return;
    }

    if (items.length === 0) {
      setVariant('info');
      setMessage('Your cart is empty.');
      return;
    }

    const orderRequest: OrderRequest = {
      userId,
      userName,
      totalPrice,
      totalQuantity,
      orderItems: items.map((item) => ({
        productId: item.productId,
        productName: item.productName,
        price: item.price,
      })),
    };

    try {
      const result = await checkoutOrder.mutateAsync({ userId, orderRequest });
      clearCart();
      setVariant('success');
      setMessage(`Order completed: ${result.order.orderNumber}`);
    } catch (error) {
      setVariant('danger');
      setMessage(error instanceof Error ? error.message : 'Order failed.');
    }
  };

  return (
    <div className="dropdown-menu dropdown-menu-end show p-3 cart-menu shadow">
      <div className="d-flex align-items-center justify-content-between mb-3">
        <h2 className="h6 mb-0">Cart Items</h2>
        <button aria-label="Close cart" className="btn btn-sm btn-light" type="button" onClick={onClose}>
          <X aria-hidden="true" size={18} />
        </button>
      </div>

      {message ? <FeedbackAlert variant={variant}>{message}</FeedbackAlert> : null}

      {items.length === 0 ? (
        <p className="text-secondary mb-0">No products in your cart.</p>
      ) : (
        <>
          <div className="table-responsive">
            <table className="table table-sm align-middle">
              <tbody>
                {items.map((item, index) => (
                  <tr key={`${item.productId}-${index}`}>
                    <td>
                      <img className="cart-thumb" src={item.imageUrl} alt="" />
                    </td>
                    <td>{item.productName}</td>
                    <td className="text-end">{formatCurrency(item.price)}</td>
                    <td className="text-end">
                      <button
                        aria-label={`Remove ${item.productName}`}
                        className="btn btn-sm btn-outline-danger"
                        type="button"
                        onClick={() => removeItem(index)}
                      >
                        <Trash2 aria-hidden="true" size={16} />
                      </button>
                    </td>
                  </tr>
                ))}
                <tr>
                  <th colSpan={2}>Quantity</th>
                  <td colSpan={2} className="text-end">
                    {totalQuantity}
                  </td>
                </tr>
                <tr>
                  <th colSpan={2}>Price</th>
                  <td colSpan={2} className="text-end">
                    {formatCurrency(totalPrice)}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <button
            className="btn btn-success w-100"
            type="button"
            disabled={checkoutOrder.isPending}
            onClick={handleCheckout}
          >
            {checkoutOrder.isPending ? 'Ordering...' : 'Place Order'}
          </button>
        </>
      )}
    </div>
  );
}
