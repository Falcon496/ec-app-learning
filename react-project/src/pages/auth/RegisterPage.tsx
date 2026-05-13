import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { AuthShell } from '../../features/auth/components/AuthShell';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { FeedbackAlert } from '../../shared/ui/FeedbackAlert';

const registerSchema = z.object({
  username: z.string().min(3, 'Username must be at least 3 characters.').max(20, 'Username must be 20 characters or less.'),
  email: z.string().email('Email must be valid.'),
  password: z.string().min(1, 'Password is required.'),
});

type RegisterFormValues = z.infer<typeof registerSchema>;

export function RegisterPage() {
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const { signUp } = useAuth();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: { username: '', email: '', password: '' },
  });

  const onSubmit = handleSubmit(async (values) => {
    setErrorMessage(null);
    const { error } = await signUp(values.username, values.email, values.password);

    if (error) {
      setErrorMessage(`Registration failed: ${error.message}`);
      return;
    }

    navigate('/login', { replace: true });
  });

  return (
    <AuthShell title="Register">
      {errorMessage ? <FeedbackAlert variant="danger">{errorMessage}</FeedbackAlert> : null}
      <form onSubmit={onSubmit} noValidate>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="username">
            Username
          </label>
          <input id="username" className="form-control form-control-lg" type="text" {...register('username')} />
          {errors.username ? <div className="invalid-feedback d-block">{errors.username.message}</div> : null}
        </div>
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
            {isSubmitting ? 'Registering...' : 'Register'}
          </button>
        </div>
        <p>
          Already have an account?{' '}
          <Link className="text-white" to="/login">
            Login here
          </Link>
        </p>
      </form>
    </AuthShell>
  );
}
