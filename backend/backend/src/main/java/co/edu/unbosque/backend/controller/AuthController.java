package co.edu.unbosque.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.backend.dto.AuthResponseDTO;
import co.edu.unbosque.backend.dto.UserDTO;
import co.edu.unbosque.backend.model.User;
import co.edu.unbosque.backend.security.JwtUtil;
import co.edu.unbosque.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoints publicos de autenticacion.
 *
 * POST /auth/login body {"username","password"} -> 200 {"token","role"} | 401 texto
 * POST /auth/register body {"username","password","role"} -> 201 texto | 409 | 400
 * (role opcional: "USER" o "ADMIN"; si no se envia queda USER)
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Login y registro (publicos)")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Operation(summary = "Iniciar sesion", description = "Devuelve el token JWT y el rol. Usar el token en el header Authorization: Bearer <token>")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			User user = (User) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(user);

			return ResponseEntity.ok(new AuthResponseDTO(jwt, user.getRole().name()));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Nombre de usuario o contraseña inválidos o usuario no encontrado");
		}
	}

	@Operation(summary = "Registrar usuario", description = "Crea un usuario con el rol enviado (USER o ADMIN). Si no se envia rol, queda USER")
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO registerRequest) {
		if (userService.findUsernameAlreadyTaken(registerRequest.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
		}

		// Igual que SpringFirstAppJWT3: se respeta el rol enviado; si no viene, USER
		if (registerRequest.getRole() == null) {
			registerRequest.setRole(User.Role.USER);
		}

		int result = userService.create(registerRequest);
		if (result == 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario");
		}
	}
}
