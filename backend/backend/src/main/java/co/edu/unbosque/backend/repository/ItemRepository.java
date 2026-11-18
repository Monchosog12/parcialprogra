package co.edu.unbosque.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.backend.model.Lacteo;

/**
 * Spring genera las consultas a partir del nombre del metodo. Ejemplos:
 * findByNombre, findByPrecioLessThan, findByFechaBetween, deleteByNombre...
 *
 * OJO: nombre y descripcion se guardan cifrados, asi que hay que pasar el valor
 * ya cifrado (AESUtil.encrypt(nombre)) y solo sirven busquedas exactas.
 */
public interface ItemRepository extends JpaRepository<Lacteo, Long> {

	public List<Lacteo> findByNombre(String nombre);

	public void deleteByNombre(String nombre);
}
