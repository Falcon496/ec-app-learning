import { Injectable } from '@angular/core';
import { createClient, User } from '@supabase/supabase-js';
import { environment } from '../../environments/environment'; 
import { BehaviorSubject, Observable } from 'rxjs';

// Create Supabase client
const supabase = createClient(environment.supabaseUrl, environment.supabaseAnonKey);

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // BehaviorSubject to hold the current username
  private userNameSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  // Observable to expose the current username
  public userName$: Observable<string | null> = this.userNameSubject.asObservable();


  constructor() { 
    // Listen to auth state changes and update the username accordingly
    supabase.auth.onAuthStateChange((event, session) => {
      const currentUser = session?.user ?? null;
      this.updateUserName(currentUser);
    });
  }

  // Update the username based on the user object
  private updateUserName(user: User | null):void {
    const username = user?.user_metadata?.['username'] || null;
    this.userNameSubject.next(username);
  }

  // Sign up a new user and return the result
  public async signUp(username: string, email: string, password: string): Promise<{data: any; error: any}> {
    const {data, error} = await supabase.auth.signUp({
      email,
      password,
      options: {
        data: {username},
        emailRedirectTo: environment.signUpRedirectUrl
      }
    });

    // Return the result of the sign up process
    return {data, error};

  }

  // Sign in an existing user and update the username if successful
  public async signIn(email: string, password: string): Promise<{data: any; error: any}> {
    const{data, error} = await supabase.auth.signInWithPassword({email, password});

    // If no error, update the username
    if(!error) {
      this.updateUserName(data?.user ?? null);
    }

    return {data, error};
  }

  // Sign out the current user and clear the username
  public async signOut(): Promise<{error: any}> {
    const {error} = await supabase.auth.signOut();
    // if no error, clear the username
    if(!error) {
      this.updateUserName(null);
    }
    return {error};
  }

  // Check if a user is authenticated
  public async isAuthenticatedAsync(): Promise<boolean> {
    const { data } = await supabase.auth.getUser();
    // Return true if a user is authenticated, false otherwise
    return !!data?.user;
  }

  // Send a password reset link to the given email
  public async sendResetLink(email: string): Promise<{ data:any; error:any }> {
    const { data, error } = await supabase.auth.resetPasswordForEmail(email, {
      redirectTo: environment.resetPasswordRedirectUrl
    });

    return { data, error };
  }

  // Reset the password for the current user
  public async resetPassword(newPassword: string): Promise<{ data: any; error: any }> {
    const { data, error } = await supabase.auth.updateUser({
      password: newPassword
    });

    return { data, error };
  }
}
