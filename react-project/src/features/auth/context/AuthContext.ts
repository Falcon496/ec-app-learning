import { createContext } from 'react';
import type { AuthError, User } from '@supabase/supabase-js';

type AuthResult<T = unknown> = Promise<{ data: T | null; error: AuthError | null }>;

export type AuthContextValue = {
  user: User | null;
  userId: string | null;
  userName: string | null;
  loading: boolean;
  signIn: (email: string, password: string) => AuthResult;
  signUp: (username: string, email: string, password: string) => AuthResult;
  signOut: () => Promise<{ error: AuthError | null }>;
  sendResetLink: (email: string) => AuthResult;
  resetPassword: (newPassword: string) => AuthResult;
};

export const AuthContext = createContext<AuthContextValue | null>(null);
