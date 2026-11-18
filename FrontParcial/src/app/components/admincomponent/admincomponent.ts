import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Backservice } from '../../services/backservice';
import { Item } from '../../models/itemmodel';

@Component({
  selector: 'app-admincomponent',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admincomponent.html',
  styleUrl: './admincomponent.css',
})
export class Admincomponent implements OnInit {
  items: Item[] = [];


  item: Item = this.nuevoItem();


  editando: boolean = false;

  constructor(private backservice: Backservice) {}

  ngOnInit(): void {
    this.listar();
  }


  nuevoItem(): Item {
    return {
      nombre: '',
      descripcion: '',
      precio: 0,
      cantidad: 0,
      fecha: '',
    };
  }


  listar(): void {
    this.backservice.getAll().subscribe({
      next: (data) => {
        this.items = data || [];
      },

      error: (error) => {
        console.error('Error al listar:', error);
      },
    });
  }


  guardar(): void {
    if (this.editando && this.item.id !== undefined) {
      this.backservice.update(this.item.id, this.item).subscribe({
        next: () => {
          alert('Item actualizado correctamente');
          this.limpiar();
          this.listar();
        },

        error: (error) => {
          console.error(error);
          alert('Error al actualizar');
        },
      });
    } else {
      this.backservice.create(this.item).subscribe({
        next: () => {
          alert('Item creado correctamente');
          this.limpiar();
          this.listar();
        },

        error: (error) => {
          console.error(error);
          alert('Error al crear');
        },
      });
    }
  }


  editar(item: Item): void {
    this.item = { ...item };
    this.editando = true;
  }


  eliminar(id: number): void {
    if (confirm('¿Deseas eliminar este Item?')) {
      this.backservice.delete(id).subscribe({
        next: () => {
          alert('Item eliminado correctamente');
          this.listar();
        },

        error: (error) => {
          console.error(error);
          alert('Error al eliminar');
        },
      });
    }
  }


  limpiar(): void {
    this.item = this.nuevoItem();
    this.editando = false;
  }
}
