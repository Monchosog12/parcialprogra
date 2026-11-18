package co.edu.unbosque.backend.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO PLANTILLA. Debe tener los mismos atributos (mismos nombres) que la
 * entidad Item para que ModelMapper los copie automaticamente.
 * La fecha viaja en JSON como "2026-11-17" (yyyy-MM-dd).
 */
public class ItemDTO {

	private Long id;
	private String nombre;
	private String descripcion;
	private Double precio;
	private Integer cantidad;
	private LocalDate fecha;

	public ItemDTO() {
	}

	public ItemDTO(String nombre, String descripcion, Double precio, Integer cantidad, LocalDate fecha) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.cantidad = cantidad;
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, descripcion, precio, cantidad, fecha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ItemDTO other = (ItemDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(descripcion, other.descripcion) && Objects.equals(precio, other.precio)
				&& Objects.equals(cantidad, other.cantidad) && Objects.equals(fecha, other.fecha);
	}

	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", cantidad=" + cantidad + ", fecha=" + fecha + "]";
	}

}
