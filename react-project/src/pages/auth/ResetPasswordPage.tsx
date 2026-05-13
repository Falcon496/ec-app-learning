import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { AuthShell } from '../../features/auth/components/AuthShell';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { FeedbackAlert } from '../../shared/ui/FeedbackAlert';

const resetPasswordSchema = z
  .object({
    newPassword: z.string().min(1, 'New password is required.'),
    confirmPassword: z.string().min(1, 'Confirm password is required.'),
  })
  .refine((values) => values.newPassword === values.confirmPassword, {
    message: 'Passwords do not match.',
    path: ['confirmPassword'],
  });

type ResetPasswordFormValues = z.infer<typeof resetPasswordSchema>;

export function ResetPasswordPage() {
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const { resetPassword } = useAuth();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ResetPasswordFormValues>({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues: { newPassword: '', confirmPassword: '' },
  });

  const onSubmit = handleSubmit(async (values) => {
    setErrorMessage(null);
    const { error } = await resetPassword(values.newPassword);

    if (error) {
      setErrorMessage(`Password reset failed: ${error.message}`);
      return;
    }

    navigate('/login', { replace: true });
  });

  return (
    <AuthShell title="Reset Password">
      {errorMessage ? <FeedbackAlert variant="danger">{errorMessage}</FeedbackAlert> : null}
      <form onSubmit={onSubmit} noValidate>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="newPassword">
            New Password
          </label>
          <input id="newPassword" className="form-control form-control-lg" type="password" {...register('newPassword')} />
          {errors.newPassword ? <div className="invalid-feedback d-block">{errors.newPassword.message}</div> : null}
        </div>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="confirmPassword">
            Confirm Password
          </label>
          <input id="confirmPassword" className="form-control form-control-lg" type="password" {...register('confirmPassword')} />
          {errors.confirmPassword ? <div className="invalid-feedback d-block">{errors.confirmPassword.message}</div> : null}
        </div>
        <div className="pt-1 mb-4">
          <button className="btn btn-light text-primary btn-lg" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Resetting...' : 'Reset Password'}
          </button>
        </div>
        <p>
          Remembered?{' '}
          <Link className="text-white" to="/login">
            Login here
          </Link>
        </p>
      </form>
    </AuthShell>
  );
}
