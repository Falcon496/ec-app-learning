import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { AuthShell } from '../../features/auth/components/AuthShell';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { FeedbackAlert } from '../../shared/ui/FeedbackAlert';

const loginSchema = z.object({
  email: z.string().email('Email must be valid.'),
  password: z.string().min(1, 'Password is required.'),
});

type LoginFormValues = z.infer<typeof loginSchema>;

export function LoginPage() {
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const { signIn } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname ?? '/home';
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const onSubmit = handleSubmit(async (values) => {
    setErrorMessage(null);
    const { error } = await signIn(values.email, values.password);

    if (error) {
      setErrorMessage(`Login failed: ${error.message}`);
      return;
    }

    navigate(from, { replace: true });
  });

  return (
    <AuthShell title="Log in">
      {errorMessage ? <FeedbackAlert variant="danger">{errorMessage}</FeedbackAlert> : null}
      <form onSubmit={onSubmit} noValidate>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="email">
            Email address
          </label>
          <input id="email" className="form-control form-control-lg" type="email" {...register('email')} />
          {errors.email ? <div className="invalid-feedback d-block">{errors.email.message}</div> : null}
        </div>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="password">
            Password
          </label>
          <input id="password" className="form-control form-control-lg" type="password" {...register('password')} />
          {errors.password ? <div className="invalid-feedback d-block">{errors.password.message}</div> : null}
        </div>
        <div className="pt-1 mb-4">
          <button className="btn btn-light text-primary btn-lg" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Logging in...' : 'Login'}
          </button>
        </div>
        <p>
          Forgot your password?{' '}
          <Link className="text-white" to="/forgot-password">
            Reset it here
          </Link>
        </p>
        <p>
          Don't have an account?{' '}
          <Link className="text-white" to="/register">
            Register here
          </Link>
        </p>
      </form>
    </AuthShell>
  );
}
