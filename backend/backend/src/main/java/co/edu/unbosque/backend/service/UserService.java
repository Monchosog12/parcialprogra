package co.edu.unbosque.backend.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backend.dto.UserDTO;
import co.edu.unbosque.backend.model.User;
import co.edu.unbosque.backend.repository.UserRepository;
import co.edu.unbosque.backend.util.AESUtil;

/**
 * El username se guarda CIFRADO con AESUtil.encrypt() y se descifra con
 * AESUtil.decrypt() al leer. La contraseña se guarda con hash BCrypt.
 */
@Service
public class UserService implements CRUDOperation<UserDTO> {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserService() {
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return userRepo.existsById(id);
	}

	/**
	 * @return 0 creado, 1 username ya en uso, 2 datos invalidos
	 */
	@Override
	public int create(UserDTO data) {
		if (isBlank(data.getUsername()) || isBlank(data.getPassword())) {
			return 2;
		}
		if (findUsernameAlreadyTaken(data.getUsername())) {
			return 1;
		}
		User entity = new User(AESUtil.encrypt(data.getUsername().trim()), passwordEncoder.encode(data.getPassword()));
		if (data.getRole() != null) {
			entity.setRole(data.getRole());
		}
		userRepo.save(entity);
		return 0;
	}

	@Override
	public List<UserDTO> getAll() {
		return userRepo.findAll().stream().map(this::toDTO).toList();
	}

	@Override
	public int deleteById(Long id) {
		Optional<User> found = userRepo.findById(id);
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		}
		return 1;
	}

	public int deleteByUsername(String username) {
		if (isBlank(username)) {
			return 1;
		}
		Optional<User> found = userRepo.findByUsername(AESUtil.encrypt(username.trim()));
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		}
		return 1;
	}

	/**
	 * Si la contraseña llega vacia se conserva la actual. Si el rol llega nulo se
	 * conserva el actual.
	 *
	 * @return 0 actualizado, 1 nuevo username ya en uso por otro, 2 no encontrado,
	 *         3 datos invalidos
	 */
	@Override
	public int updateById(Long id, UserDTO newData) {
		Optional<User> found = userRepo.findById(id);
		if (found.isEmpty()) {
			return 2;
		}
		if (isBlank(newData.getUsername())) {
			return 3;
		}
		String encryptedUsername = AESUtil.encrypt(newData.getUsername().trim());
		Optional<User> sameName = userRepo.findByUsername(encryptedUsername);
		if (sameName.isPresent() && !sameName.get().getId().equals(id)) {
			return 1;
		}
		User temp = found.get();
		temp.setUsername(encryptedUsername);
		if (!isBlank(newData.getPassword())) {
			temp.setPassword(passwordEncoder.encode(newData.getPassword()));
		}
		if (newData.getRole() != null) {
			temp.setRole(newData.getRole());
		}
		userRepo.save(temp);
		return 0;
	}

	public UserDTO getById(Long id) {
		return userRepo.findById(id).map(this::toDTO).orElse(null);
	}

	public boolean findUsernameAlreadyTaken(String username) {
		return !isBlank(username) && userRepo.existsByUsername(AESUtil.encrypt(username.trim()));
	}

	/** Entidad (cifrada) -> DTO (descifrado). */
	private UserDTO toDTO(User entity) {
		UserDTO dto = modelMapper.map(entity, UserDTO.class);
		dto.setUsername(AESUtil.decrypt(entity.getUsername()));
		return dto;
	}

	private boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

}
