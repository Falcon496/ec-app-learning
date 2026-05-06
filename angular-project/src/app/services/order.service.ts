import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderRequest, OrderResponse} from "../models/order.model";
import { environment} from "../../environments/environment";
import {MemberStatusResponse} from "../models/member-status";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  placeOrder(orderRequest: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.apiUrl}/orders`, orderRequest);
  }

  calculateAndUpdateMemberStatus(userId: string, orderNumber: string): Observable<MemberStatusResponse> {
    return this.http.post<MemberStatusResponse>(`${this.apiUrl}/member-status/calculate`, { userId, orderNumber });
  }

}
