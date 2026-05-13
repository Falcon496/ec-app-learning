import type { ReactNode } from 'react';

type AuthShellProps = {
  title: string;
  children: ReactNode;
};

export function AuthShell({ title, children }: AuthShellProps) {
  return (
    <section className="auth-page d-flex align-items-center">
      <div className="container py-5">
        <div className="row justify-content-center justify-content-lg-start">
          <div className="col-sm-10 col-md-7 col-lg-5 text-white">
            <div className="mb-5">
              <div className="display-6 fw-bold">Electronics Store</div>
            </div>
            <div className="auth-form">
              <h1 className="h3 fw-normal mb-4">{title}</h1>
              {children}
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
