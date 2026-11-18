import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { Loginmodel } from '../models/loginmodel';
import { Authresponsemodel } from '../models/authresponsemodel';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}


  login(datos: Loginmodel): Observable<Authresponsemodel> {
    return this.http.post<Authresponsemodel>(`${this.apiUrl}/login`, datos).pipe(
      tap((respuesta) => {
        localStorage.setItem('token', respuesta.token);
        localStorage.setItem('role', respuesta.roles);
      }),
    );
  }


  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }


  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
