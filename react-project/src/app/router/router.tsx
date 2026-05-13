import { Navigate, createBrowserRouter } from 'react-router-dom';
import { AppLayout } from '../layout/AppLayout';
import { ProtectedRoute } from './ProtectedRoute';
import { ForgotPasswordPage } from '../../pages/auth/ForgotPasswordPage';
import { LoginPage } from '../../pages/auth/LoginPage';
import { RegisterPage } from '../../pages/auth/RegisterPage';
import { ResetPasswordPage } from '../../pages/auth/ResetPasswordPage';
import { HomePage } from '../../pages/home/HomePage';
import { OrdersPage } from '../../pages/orders/OrdersPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Navigate to="/login" replace />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/register',
    element: <RegisterPage />,
  },
  {
    path: '/forgot-password',
    element: <ForgotPasswordPage />,
  },
  {
    path: '/reset-password',
    element: <ResetPasswordPage />,
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <AppLayout />,
        children: [
          {
            path: '/home',
            element: <HomePage />,
          },
          {
            path: '/orders',
            element: <OrdersPage />,
          },
        ],
      },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/login" replace />,
  },
]);
