import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL_BUSCAR_AMENAZA } from '../config';
import { API_URL_LISTAR_NAVEGACION } from '../config';
import { API_URL_IPINFO } from '../config';
import { API_URL_VT_CONSULTAR_ARCHIVOS_IP } from '../config';
import { API_URL_VT_CONSULTAR_IP } from '../config';

interface LastAnalysisResult {
  engine_name: string;
  result: string;
}

@Component({
  selector: 'app-homeauth',
  templateUrl: './homeauth.component.html',
  styleUrls: ['./homeauth.component.css']
})
export class HomeauthComponent {
  url: string = '';
  response: any = {};
  threatLists: any;
  ipInput: string = '';
  ipInfo: any;

  ipInputVT: string = '';
  ipInfoVT: any;

  ipv4Pattern: string = '^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$';

  constructor(private http: HttpClient) {}
  ngOnInit() {
    this.listadoNavegacion();
  }
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

  listadoNavegacion() {
    this.http.get<any>(API_URL_LISTAR_NAVEGACION).subscribe(
      response => {
        this.threatLists = response.threatLists;
      },
      error => {
        console.error('Error al obtener las listas de amenazas:', error);
      }
    );
  }

  verificarIP() {
    if (!this.ipInput.match(this.ipv4Pattern)) {
      alert('Dirección IP no válida');
      return;
    }
    // 'http://127.0.0.1:5000/ipinfo/verificar_ip'
    this.http.post<any>(API_URL_IPINFO, { ip: this.ipInput })
      .subscribe(
        response => {
          this.ipInfo = response;
        },
        error => {
          console.error('Error al verificar la IP:', error);
        }
      );
  }


  verificarIP_VT() {
    if (!this.ipInput.match(this.ipv4Pattern)) {
      alert('Dirección IP no válida');
      return;
    }
    const body = { ip: this.ipInputVT }; 
    // const url = 'http://127.0.0.1:5000/virus_total/consultar_ip'; 

    this.http.post(API_URL_VT_CONSULTAR_IP, body).subscribe(
      (response) => {
        this.ipInfoVT = response;
      },
      (error) => {
        console.error('Error al consultar la IP:', error);
      }
    );
  }

  convertUnixTimestamp(timestamp: number): Date {
    return new Date(timestamp * 1000);
  }

  getLastAnalysisResult(results: any): string {
    const lastResult: LastAnalysisResult = Object.values(results).pop() as LastAnalysisResult;
    return `${lastResult.engine_name}: ${lastResult.result}`;
  }
}
