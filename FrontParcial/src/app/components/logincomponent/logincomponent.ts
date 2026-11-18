import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/authservice';

@Component({
  selector: 'app-logincomponent',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './logincomponent.html',
  styleUrl: './logincomponent.css',
})
export class Logincomponent {
  username = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}



  iniciarSesion(): void {

    const datos = {
      username: this.username,
      password: this.password
    };

    this.authService.login(datos).subscribe({

      next: (respuesta) => {

        console.log('Login exitoso');
        console.log('Token:', respuesta.token);
        console.log('Rol:', respuesta.roles);

        if (respuesta.roles === 'ADMIN') {

          this.router.navigate(['/admin']);

        } else {

          this.router.navigate(['/contenido']);

        }

      },

      error: (error) => {

        console.error('Error al iniciar sesión:', error);
        alert('Usuario o contraseña incorrectos');

      }

    });

  }
}
