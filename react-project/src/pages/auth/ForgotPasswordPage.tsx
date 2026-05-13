import { useState } from 'react';
import { Link } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { AuthShell } from '../../features/auth/components/AuthShell';
import { useAuth } from '../../features/auth/hooks/useAuth';
import { FeedbackAlert } from '../../shared/ui/FeedbackAlert';

const forgotPasswordSchema = z.object({
  email: z.string().email('Email must be valid.'),
});

type ForgotPasswordFormValues = z.infer<typeof forgotPasswordSchema>;

export function ForgotPasswordPage() {
  const [feedback, setFeedback] = useState<{ message: string; variant: 'success' | 'danger' } | null>(null);
  const { sendResetLink } = useAuth();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ForgotPasswordFormValues>({
    resolver: zodResolver(forgotPasswordSchema),
    defaultValues: { email: '' },
  });

  const onSubmit = handleSubmit(async (values) => {
    setFeedback(null);
    const { error } = await sendResetLink(values.email);

    if (error) {
      setFeedback({ message: `Error: ${error.message}`, variant: 'danger' });
      return;
    }

    setFeedback({ message: 'Password reset email sent.', variant: 'success' });
  });

  return (
    <AuthShell title="Forgot Password">
      {feedback ? <FeedbackAlert variant={feedback.variant}>{feedback.message}</FeedbackAlert> : null}
      <form onSubmit={onSubmit} noValidate>
        <div className="form-outline mb-4">
          <label className="form-label" htmlFor="email">
            Email address
          </label>
          <input id="email" className="form-control form-control-lg" type="email" {...register('email')} />
          {errors.email ? <div className="invalid-feedback d-block">{errors.email.message}</div> : null}
        </div>
        <div className="pt-1 mb-4">
          <button className="btn btn-light text-primary btn-lg" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Sending...' : 'Send Reset Link'}
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
