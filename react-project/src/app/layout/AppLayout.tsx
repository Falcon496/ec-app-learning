import { useState } from 'react';
import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom';
import { ShoppingCart, UserRound } from 'lucide-react';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { CartMenu } from '../../features/cart/components/CartMenu';
import { useCartStore } from '../../features/cart/model/cartStore';

export function AppLayout() {
  const [isCartVisible, setIsCartVisible] = useState(false);
  const [isUserMenuVisible, setIsUserMenuVisible] = useState(false);
  const totalQuantity = useCartStore((state) => state.totalQuantity);
  const { signOut, userName } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    const confirmed = window.confirm('Are you sure you want to logout?');
    if (!confirmed) {
      return;
    }

    await signOut();
    setIsCartVisible(false);
    setIsUserMenuVisible(false);
    navigate('/login', { replace: true });
  };

  return (
    <>
      <nav className="navbar navbar-expand-sm navbar-dark bg-primary sticky-top">
        <div className="container-fluid">
          <div className="d-flex align-items-center gap-3">
            <Link className="navbar-brand fw-semibold" to="/home">
              Electronics Store
            </Link>
            <div className="navbar-nav flex-row gap-2">
              <NavLink className="nav-link text-white" to="/home">
                Products
              </NavLink>
              <NavLink className="nav-link text-white" to="/orders">
                Orders
              </NavLink>
            </div>
          </div>

          <div className="d-flex align-items-center gap-3">
            <div className="position-relative">
              <button
                aria-label="Cart"
                className="btn btn-primary position-relative"
                type="button"
                onClick={() => setIsCartVisible((value) => !value)}
              >
                <ShoppingCart aria-hidden="true" size={22} />
                {totalQuantity > 0 ? (
                  <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-primary">
                    {totalQuantity}
                  </span>
                ) : null}
              </button>
              <CartMenu isOpen={isCartVisible} onClose={() => setIsCartVisible(false)} />
            </div>

            <div className="position-relative">
              <button
                className="btn btn-light text-primary d-flex align-items-center gap-2"
                type="button"
                onClick={() => setIsUserMenuVisible((value) => !value)}
              >
                <UserRound aria-hidden="true" size={18} />
                <span>{userName ?? 'Member'}</span>
              </button>
              <ul className={`dropdown-menu dropdown-menu-end ${isUserMenuVisible ? 'show' : ''}`}>
                <li>
                  <button className="dropdown-item" type="button" onClick={handleLogout}>
                    Logout
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </nav>
      <Outlet />
    </>
  );
}
