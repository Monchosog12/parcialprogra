import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Navbarcomponent } from './navbarcomponent/navbarcomponent';
import { Footercomponent } from './footercomponent/footercomponent';
import { Contenidocomponent } from './contenidocomponent/contenidocomponent';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbarcomponent, Footercomponent, Contenidocomponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
