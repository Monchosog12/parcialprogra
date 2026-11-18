import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Backservice } from '../../services/backservice';
import { Item } from '../../models/itemmodel';

@Component({
  selector: 'app-admincomponent',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admincomponent.html',
  styleUrl: './admincomponent.css',
})
export class Admincomponent implements OnInit {
  items: Item[] = [];

  item: Item = this.nuevoItem();

  editando = false;

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
        alert('No se pudieron cargar los productos');
      },
    });
  }

  guardar(): void {
    if (this.editando && this.item.id !== undefined) {
      this.backservice.update(this.item.id, this.item).subscribe({
        next: () => {
          alert('Producto actualizado correctamente');
          this.limpiar();
          this.listar();
        },
        error: (error) => {
          console.error(error);
          alert('Error al actualizar el producto');
        },
      });
    } else {
      this.backservice.create(this.item).subscribe({
        next: () => {
          alert('Producto registrado correctamente');
          this.limpiar();
          this.listar();
        },
        error: (error) => {
          console.error(error);
          alert('Error al registrar el producto');
        },
      });
    }
  }

  editar(registro: Item): void {
    this.item = { ...registro };
    this.editando = true;
  }

  eliminar(id: number): void {
    if (confirm('¿Estás seguro de eliminar este producto?')) {
      this.backservice.delete(id).subscribe({
        next: () => {
          alert('Producto eliminado correctamente');

          if (this.item.id === id) {
            this.limpiar();
          }

          this.listar();
        },
        error: (error) => {
          console.error(error);
          alert('Error al eliminar el producto');
        },
      });
    }
  }

  limpiar(): void {
    this.item = this.nuevoItem();
    this.editando = false;
  }
}
