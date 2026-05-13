export const appEnv = {
  supabaseUrl: import.meta.env.VITE_SUPABASE_URL ?? '',
  supabaseAnonKey: import.meta.env.VITE_SUPABASE_ANON_KEY ?? '',
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
};

export function requireSupabaseEnv() {
  const missingKeys = [
    ['VITE_SUPABASE_URL', appEnv.supabaseUrl],
    ['VITE_SUPABASE_ANON_KEY', appEnv.supabaseAnonKey],
  ]
    .filter(([, value]) => !value)
    .map(([key]) => key);

  if (missingKeys.length > 0) {
    throw new Error(`Missing required env values: ${missingKeys.join(', ')}`);
  }
}
