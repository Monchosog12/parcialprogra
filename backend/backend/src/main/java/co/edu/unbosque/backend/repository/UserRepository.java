package co.edu.unbosque.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);

	public boolean existsByUsername(String username);

	public void deleteByUsername(String username);
}
