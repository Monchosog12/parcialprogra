import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-contenidocomponent',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './contenidocomponent.html',
})
export class Contenidocomponent {
  titulo = 'Bienvenido a nuestra aplicación';

  descripcion = 'Gestiona tu cuenta de manera sencilla y segura.';
}
