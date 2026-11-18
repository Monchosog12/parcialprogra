import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Item } from '../models/itemmodel';
import { AuthService } from './authservice';

@Injectable({
  providedIn: 'root',
})
export class Backservice {
  private apiUrl = 'http://localhost:8080/item';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}


  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`,
    });
  }


  getAll(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/getall`, { headers: this.getHeaders() });
  }


  getById(id: number): Observable<Item> {
    return this.http.get<Item>(`${this.apiUrl}/getbyid/${id}`, { headers: this.getHeaders() });
  }


  create(item: Item): Observable<string> {
    return this.http.post(`${this.apiUrl}/createjson`, item, {
      headers: this.getHeaders(),
      responseType: 'text',
    });
  }


  update(id: number, item: Item): Observable<string> {
    return this.http.put(`${this.apiUrl}/updatejson?id=${id}`, item, {
      headers: this.getHeaders(),
      responseType: 'text',
    });
  }


  delete(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/deletebyid/${id}`, {
      headers: this.getHeaders(),
      responseType: 'text',
    });
  }


  search(nombre: string): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.apiUrl}/search`, {
      headers: this.getHeaders(),
      params: { nombre },
    });
  }
}
