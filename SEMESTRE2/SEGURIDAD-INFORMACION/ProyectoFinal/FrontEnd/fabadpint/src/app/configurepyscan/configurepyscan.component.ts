import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-configurepyscan',
  templateUrl: './configurepyscan.component.html',
  styleUrl: './configurepyscan.component.css'
})
export class ConfigurepyscanComponent {
  constructor(private http: HttpClient, private router: Router) {}

  goToSignUpPage() {
    this.router.navigateByUrl('/signup');
  }

}
