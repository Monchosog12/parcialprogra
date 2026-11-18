import { Component } from '@angular/core';

@Component({
  selector: 'app-footercomponent',
  standalone: true,
  imports: [],
  templateUrl: './footercomponent.html',
  styleUrl: './footercomponent.css',
})
export class Footercomponent {
  anioActual = new Date().getFullYear();
}
