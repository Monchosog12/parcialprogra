package co.edu.unbosque.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backend.model.User;
import co.edu.unbosque.backend.repository.UserRepository;
import co.edu.unbosque.backend.util.AESUtil;

/**
 * Busca el usuario por su username CIFRADO y devuelve una copia con el username
 * descifrado, para que el token JWT lleve el nombre real ("sub": "admin").
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User found = userRepository.findByUsername(AESUtil.encrypt(username))
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

		// Copia (no se guarda en la BD) con el username en texto plano
		User user = new User(AESUtil.decrypt(found.getUsername()), found.getPassword(), found.getRole());
		user.setId(found.getId());
		user.setEnabled(found.isEnabled());
		user.setAccountNonExpired(found.isAccountNonExpired());
		user.setAccountNonLocked(found.isAccountNonLocked());
		user.setCredentialsNonExpired(found.isCredentialsNonExpired());
		return user;
	}
}
