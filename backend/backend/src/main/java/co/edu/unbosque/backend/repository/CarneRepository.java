package co.edu.unbosque.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.backend.model.Carne;

public interface CarneRepository extends JpaRepository<Carne, Long> {

	public List<Carne> findByNombre(String nombre);

	public void deleteByNombre(String nombre);
}
