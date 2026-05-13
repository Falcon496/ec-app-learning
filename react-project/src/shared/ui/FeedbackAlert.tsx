import type { ReactNode } from 'react';

type FeedbackAlertProps = {
  children: ReactNode;
  variant: 'success' | 'danger' | 'info' | 'warning';
};

export function FeedbackAlert({ children, variant }: FeedbackAlertProps) {
  return (
    <div className={`alert alert-${variant}`} role="alert">
      {children}
    </div>
  );
}
