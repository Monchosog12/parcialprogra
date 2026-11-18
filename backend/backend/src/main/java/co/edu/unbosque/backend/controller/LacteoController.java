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

import co.edu.unbosque.backend.dto.FoodDTO;
import co.edu.unbosque.backend.dto.LacteoDTO;
import co.edu.unbosque.backend.service.ExternalHTTPRequestHandler;
import co.edu.unbosque.backend.service.LacteoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * CRUD de Lacteo protegido con JWT + busqueda externa en FoodData Central.
 */
@RestController
@RequestMapping("/lacteo")
@Transactional
@Tag(name = "Lacteo", description = "CRUD de lacteos y busqueda en FoodData Central")
@SecurityRequirement(name = "bearerAuth")
public class LacteoController {

	@Autowired
	private LacteoService lacteoServ;

	public LacteoController() {
	}

	

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createNewWithJSON(@RequestBody LacteoDTO newItem) {
		if (lacteoServ.create(newItem) == 0) {
			return new ResponseEntity<>("Lacteo creado exitosamente", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Datos invalidos, el nombre es obligatorio", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getall")
	ResponseEntity<List<LacteoDTO>> getAll() {
		List<LacteoDTO> items = lacteoServ.getAll();
		if (items.isEmpty()) {
			return new ResponseEntity<>(items, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(items, HttpStatus.ACCEPTED);
	}

	/** Busca por nombre exacto (los nombres estan cifrados en la BD). */
	@GetMapping("/search")
	ResponseEntity<List<LacteoDTO>> search(@RequestParam String nombre) {
		return new ResponseEntity<>(lacteoServ.searchByNombre(nombre), HttpStatus.ACCEPTED);
	}

	@GetMapping("/count")
	ResponseEntity<Long> countAll() {
		Long count = lacteoServ.count();
		if (count == 0) {
			return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
	}

	@GetMapping("/exists/{id}")
	ResponseEntity<Boolean> exists(@PathVariable Long id) {
		if (lacteoServ.exist(id)) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getbyid/{id}")
	ResponseEntity<LacteoDTO> getById(@PathVariable Long id) {
		LacteoDTO found = lacteoServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new LacteoDTO(), HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updateWithJSON(@RequestParam Long id, @RequestBody LacteoDTO newItem) {
		int status = lacteoServ.updateById(id, newItem);
		return switch (status) {
		case 0 -> new ResponseEntity<>("Lacteo actualizado exitosamente", HttpStatus.ACCEPTED);
		case 2 -> new ResponseEntity<>("Lacteo no encontrado", HttpStatus.NOT_FOUND);
		default -> new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		};
	}

	@DeleteMapping("/deletebyid/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id) {
		if (lacteoServ.deleteById(id) == 0) {
			return new ResponseEntity<>("Lacteo eliminado exitosamente", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Error al eliminar, lacteo no encontrado", HttpStatus.NOT_FOUND);
	}

}
