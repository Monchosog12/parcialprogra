package co.edu.unbosque.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.backend.dto.UserDTO;
import co.edu.unbosque.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Requiere JWT. /getall, /count, /exists/*, /getbyid/* -> USER o ADMIN. El
 * resto -> solo ADMIN (ver SecurityConfig). El CORS se configura en
 * SecurityConfig, no hace falta @CrossOrigin.
 */
@RestController
@RequestMapping("/user")
@Transactional
@Tag(name = "Usuarios", description = "Gestion de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

	@Autowired
	private UserService userServ;

	public UserController() {
	}

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createNewWithJSON(@RequestBody UserDTO newUser) {
		if (newUser.getUsername() != null && (newUser.getUsername().contains("<") || newUser.getUsername().contains(">"))) {
			return new ResponseEntity<>("Solicitud con caracteres invalidos", HttpStatus.BAD_REQUEST);
		}
		int status = userServ.create(newUser);
		if (status == 0) {
			return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Error al crear el usuario, el nombre de usuario ya está en uso",
					HttpStatus.NOT_ACCEPTABLE);
		} else {
			return new ResponseEntity<>("Usuario y contraseña son obligatorios", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "/create")
	ResponseEntity<String> createNew(@RequestParam String username, @RequestParam String password) {
		return createNewWithJSON(new UserDTO(username, password));
	}

	@GetMapping("/getall")
	ResponseEntity<List<UserDTO>> getAll() {
		List<UserDTO> users = userServ.getAll();
		if (users.isEmpty()) {
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
	}

	@GetMapping("/count")
	ResponseEntity<Long> countAll() {
		Long count = userServ.count();
		if (count == 0) {
			return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
	}

	@GetMapping("/exists/{id}")
	ResponseEntity<Boolean> exists(@PathVariable Long id) {
		if (userServ.exist(id)) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getbyid/{id}")
	ResponseEntity<UserDTO> getById(@PathVariable Long id) {
		UserDTO found = userServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updateNewWithJSON(@RequestParam Long id, @RequestBody UserDTO newUser) {
		int status = userServ.updateById(id, newUser);
		return switch (status) {
		case 0 -> new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.ACCEPTED);
		case 1 -> new ResponseEntity<>("El nuevo nombre de usuario ya está en uso", HttpStatus.IM_USED);
		case 2 -> new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		default -> new ResponseEntity<>("Error al actualizar", HttpStatus.BAD_REQUEST);
		};
	}

	@PutMapping(path = "/update")
	ResponseEntity<String> updateNew(@RequestParam Long id, @RequestParam String newUsername,
			@RequestParam String newPassword) {
		return updateNewWithJSON(id, new UserDTO(newUsername, newPassword));
	}

	@DeleteMapping("/deletebyid/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id) {
		if (userServ.deleteById(id) == 0) {
			return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/deletebyname")
	ResponseEntity<String> deleteByName(@RequestParam String name) {
		if (userServ.deleteByUsername(name) == 0) {
			return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
	}

}
