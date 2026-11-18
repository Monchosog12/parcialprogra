package co.edu.unbosque.backend.dto;

import java.util.List;

/**
 * Alimento simplificado traido de FoodData Central (USDA).
 */
public class FoodDTO {

	private Long fdcId;
	private String descripcion;
	private String categoria;
	private String marca;
	private List<NutrienteDTO> nutrientes;

	public FoodDTO() {
	}

	public FoodDTO(Long fdcId, String descripcion, String categoria, String marca, List<NutrienteDTO> nutrientes) {
		this.fdcId = fdcId;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.marca = marca;
		this.nutrientes = nutrientes;
	}

	public Long getFdcId() {
		return fdcId;
	}

	public void setFdcId(Long fdcId) {
		this.fdcId = fdcId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public List<NutrienteDTO> getNutrientes() {
		return nutrientes;
	}

	public void setNutrientes(List<NutrienteDTO> nutrientes) {
		this.nutrientes = nutrientes;
	}

	@Override
	public String toString() {
		return "FoodDTO [fdcId=" + fdcId + ", descripcion=" + descripcion + ", categoria=" + categoria + ", marca="
				+ marca + ", nutrientes=" + nutrientes + "]";
	}

}
