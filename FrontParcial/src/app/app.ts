import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Navbarcomponent } from './components/navbarcomponent/navbarcomponent';
import { Footercomponent } from './components/footercomponent/footercomponent';
import { Contenidocomponent } from './components/contenidocomponent/contenidocomponent';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbarcomponent, Footercomponent, Contenidocomponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
