import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL_BUSCAR_AMENAZA } from '../config';

@Component({
  selector: 'app-homeauth',
  templateUrl: './homeauth.component.html',
  styleUrls: ['./homeauth.component.css']
})
export class HomeauthComponent {
  url: string = '';
  response: any = {};

  constructor(private http: HttpClient) {}

  escanearAmenazas() {
    const body = { url: this.url };

    this.http.post(API_URL_BUSCAR_AMENAZA, body)
      .subscribe(
        (data: any) => {
          this.response = data;
          console.log('Respuesta del servidor:', data);
        },
        error => {
          console.error('Error en la petición:', error);
        }
      );
  }
}
