package co.edu.unbosque.backend.dto;

/**
 * Nutriente de un alimento de FoodData Central (ej. Protein, 20.5, G).
 */
public class NutrienteDTO {

	private String nombre;
	private Double valor;
	private String unidad;

	public NutrienteDTO() {
	}

	public NutrienteDTO(String nombre, Double valor, String unidad) {
		this.nombre = nombre;
		this.valor = valor;
		this.unidad = unidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	@Override
	public String toString() {
		return "NutrienteDTO [nombre=" + nombre + ", valor=" + valor + ", unidad=" + unidad + "]";
	}

}
