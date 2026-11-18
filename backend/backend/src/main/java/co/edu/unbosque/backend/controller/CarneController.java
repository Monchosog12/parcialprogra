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

import co.edu.unbosque.backend.dto.CarneDTO;
import co.edu.unbosque.backend.service.CarneService;
import co.edu.unbosque.backend.service.ExternalHTTPRequestHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * CONTROLADOR PLANTILLA (CRUD completo protegido con JWT). Al renombrar la
 * entidad cambiar tambien el @RequestMapping("/item") y la regla "/item/**" en
 * SecurityConfig.
 */
@RestController
@RequestMapping("/carne")
@Transactional
@Tag(name = "Producto (plantilla)", description = "CRUD de ejemplo para el parcial")
@SecurityRequirement(name = "bearerAuth")
public class CarneController {

	@Autowired
	private CarneService itemServ;
	
	

	public CarneController() {
	}

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createNewWithJSON(@RequestBody CarneDTO newItem) {
		if (itemServ.create(newItem) == 0) {
			return new ResponseEntity<>("Item creado exitosamente", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Datos invalidos, el nombre es obligatorio", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getall")
	ResponseEntity<List<CarneDTO>> getAll() {
		List<CarneDTO> items =itemServ.getAll();
		if (items.isEmpty()) {
			return new ResponseEntity<>(items, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(items, HttpStatus.ACCEPTED);
	}

	/** Busca por nombre exacto (los nombres estan cifrados en la BD). */
	@GetMapping("/search")
	ResponseEntity<List<CarneDTO>> search(@RequestParam String nombre) {
		return new ResponseEntity<>(itemServ.searchByNombre(nombre), HttpStatus.ACCEPTED);
	}

	@GetMapping("/count")
	ResponseEntity<Long> countAll() {
		Long count = itemServ.count();
		if (count == 0) {
			return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
	}

	@GetMapping("/exists/{id}")
	ResponseEntity<Boolean> exists(@PathVariable Long id) {
		if (itemServ.exist(id)) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getbyid/{id}")
	ResponseEntity<CarneDTO> getById(@PathVariable Long id) {
		CarneDTO found = itemServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new CarneDTO(), HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updateWithJSON(@RequestParam Long id, @RequestBody CarneDTO newItem) {
		int status = itemServ.updateById(id, newItem);
		return switch (status) {
		case 0 -> new ResponseEntity<>("Item actualizado exitosamente", HttpStatus.ACCEPTED);
		case 2 -> new ResponseEntity<>("Item no encontrado", HttpStatus.NOT_FOUND);
		default -> new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		};
	}

	@DeleteMapping("/deletebyid/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id) {
		if (itemServ.deleteById(id) == 0) {
			return new ResponseEntity<>("Item eliminado exitosamente", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Error al eliminar, item no encontrado", HttpStatus.NOT_FOUND);
	}

}
