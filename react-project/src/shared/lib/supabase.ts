import { createClient } from '@supabase/supabase-js';
import { appEnv, requireSupabaseEnv } from '../config/env';

requireSupabaseEnv();

export const supabase = createClient(appEnv.supabaseUrl, appEnv.supabaseAnonKey);
