import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { RouterOutlet, NavigationEnd, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from './services/product.service';
import { Product } from './models/product.model';
import { Subscription, filter, Observable } from 'rxjs';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy{
  public title = 'angular-project';
  private productService = inject(ProductService);
  public isCartVisible: boolean = false;
  public cartItems: Product[] = [];
  private addToCartSubscription: Subscription;

  private navigationEndSubscription: Subscription | null = null;
  private router: Router = inject(Router);
  public showNavbar: boolean = false;

  public userName$: Observable<string | null>;
  public isMembershipMenuVisible: boolean = false;
  private authService: AuthService = inject(AuthService);

  constructor() {
    this.addToCartSubscription = this.productService.onAddToCart$.subscribe((res:Product) => {
      this.cartItems.unshift(res);
    });
    this.userName$ = this.authService.userName$;
  }

  public ngOnInit(): void {
      this.navigationEndSubscription = this.router.events
        .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
        .subscribe((event: NavigationEnd) => {
          const urlWithoutParams = event.urlAfterRedirects.split('?')[0].split('#')[0]; // Ignore query parameters and fragments
          const noNavbarUrls = ['/login', '/register', '/forgot-password', '/reset-password'];
          this.showNavbar = !noNavbarUrls.some(url => new RegExp(`^${url}`).test(urlWithoutParams));
        });
  }

  public ngOnDestroy(): void {
    if(this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }

    if(this.navigationEndSubscription) {
      this.navigationEndSubscription.unsubscribe();
    }
  }

  public showCart(): void {
    this.isCartVisible = !this.isCartVisible;
  }

  public trackByIndex(index: number, item: Product): number {
    return index;
  }

  public removeProduct(index: number): void {
    this.cartItems.splice(index, 1);
  }

  public getTotalQuantity(): number {
    return this.cartItems.length;
  }

  public getTotalPrice(): number {
    return this.cartItems.reduce((total, item) => total + Number(item.price), 0);
  }

  public showMembershipMenu(): void {
    this.isMembershipMenuVisible = !this.isMembershipMenuVisible;
  }

  public onLogout(): void {
    const confirmed: boolean = confirm('Are you sure you want to logout?');
    if(confirmed) {
      this.authService.signOut().then(() => {
        this.isCartVisible = false;
        this.isMembershipMenuVisible = false;
        this.router.navigateByUrl('/login');
      });
    }
  }
}
