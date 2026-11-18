import { Component, OnInit } from '@angular/core';
import { Backservice } from '../../services/backservice';
import { Item } from '../../models/itemmodel';

@Component({
  selector: 'app-contenidocomponent',
  standalone: true,
  templateUrl: './contenidocomponent.html',
  styleUrl: './contenidocomponent.css',
})
export class Contenidocomponent implements OnInit {
  items: Item[] = [];

  constructor(private backservice: Backservice) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.backservice.getAll().subscribe({
      next: (data) => {
        this.items = data || [];
        console.log(data);
      },

      error: (error) => {
        console.log(error);
      },
    });
  }
}
