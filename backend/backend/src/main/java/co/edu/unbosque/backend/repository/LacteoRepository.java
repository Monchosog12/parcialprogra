package co.edu.unbosque.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.backend.model.Lacteo;

public interface LacteoRepository extends JpaRepository<Lacteo, Long> {

	public List<Lacteo> findByNombre(String nombre);

	public void deleteByNombre(String nombre);
}
