import { Route, Routes, MemoryRouter } from 'react-router-dom';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { AuthContext, type AuthContextValue } from '../../features/auth/context/AuthContext';
import { ProtectedRoute } from './ProtectedRoute';

function renderWithAuth(authValue: AuthContextValue) {
  return render(
    <AuthContext.Provider value={authValue}>
      <MemoryRouter initialEntries={['/home']}>
        <Routes>
          <Route element={<ProtectedRoute />}>
            <Route path="/home" element={<div>Protected Home</div>} />
          </Route>
          <Route path="/login" element={<div>Login Page</div>} />
        </Routes>
      </MemoryRouter>
    </AuthContext.Provider>,
  );
}

const baseAuthValue: AuthContextValue = {
  user: null,
  userId: null,
  userName: null,
  loading: false,
  signIn: vi.fn(),
  signUp: vi.fn(),
  signOut: vi.fn(),
  sendResetLink: vi.fn(),
  resetPassword: vi.fn(),
};

describe('ProtectedRoute', () => {
  it('redirects anonymous users to login', () => {
    renderWithAuth(baseAuthValue);

    expect(screen.getByText('Login Page')).toBeInTheDocument();
  });

  it('renders protected content for authenticated users', () => {
    renderWithAuth({
      ...baseAuthValue,
      user: { id: 'user-1', app_metadata: {}, user_metadata: {}, aud: 'authenticated', created_at: '2024-01-01' },
      userId: 'user-1',
    });

    expect(screen.getByText('Protected Home')).toBeInTheDocument();
  });
});
