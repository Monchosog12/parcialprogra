package co.edu.unbosque.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.backend.model.User;

public interface LacteoRepository extends JpaRepository<User, Long> {

}
