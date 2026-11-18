package co.edu.unbosque.backend.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backend.dto.CarneDTO;
import co.edu.unbosque.backend.model.Carne;
import co.edu.unbosque.backend.model.Lacteo;
import co.edu.unbosque.backend.repository.CarneRepository;
import co.edu.unbosque.backend.util.AESUtil;

/**
 * SERVICIO PLANTILLA. Al renombrar la entidad, reemplazar Item por el nombre
 * nuevo (Ctrl+F) y ajustar las validaciones de create/update.
 *
 * Los campos de texto (nombre, descripcion) se guardan CIFRADOS con
 * AESUtil.encrypt() y se descifran con AESUtil.decrypt() al leer. Los numeros y
 * fechas no se cifran porque la columna no es de texto.
 */
@Service
public class CarneService implements CRUDOperation<CarneDTO> {

	@Autowired
	private CarneRepository itemRepo;

	@Autowired
	private ModelMapper modelMapper;

	public CarneService() {
	}

	/**
	 * @return 0 creado, 1 datos invalidos
	 */
	@Override
	public int create(CarneDTO data) {
		if (data.getNombre() == null || data.getNombre().isBlank()) {
			return 1;
		}
		Carne entity = modelMapper.map(data, Carne.class);
		entity.setId(null); // el id lo genera la BD
		entity.setNombre(AESUtil.encrypt(data.getNombre()));
		itemRepo.save(entity);
		return 0;
	}

	@Override
	public List<CarneDTO> getAll() {
		return itemRepo.findAll().stream().map(this::toDTO).toList();
	}

	/** Busqueda por nombre exacto (con datos cifrados no se puede buscar por partes). */
	public List<CarneDTO> searchByNombre(String nombre) {
		return itemRepo.findByNombre(AESUtil.encrypt(nombre)).stream().map(this::toDTO).toList();
	}

	public CarneDTO getById(Long id) {
		return itemRepo.findById(id).map(this::toDTO).orElse(null);
	}

	@Override
	public int deleteById(Long id) {
		Optional<Carne> found = itemRepo.findById(id);
		if (found.isPresent()) {
			itemRepo.delete(found.get());
			return 0;
		}
		return 1;
	}

	/**
	 * @return 0 actualizado, 1 datos invalidos, 2 no encontrado
	 */
	@Override
	public int updateById(Long id, CarneDTO newData) {
		Optional<Carne> found = itemRepo.findById(id);
		if (found.isEmpty()) {
			return 2;
		}
		if (newData.getNombre() == null || newData.getNombre().isBlank()) {
			return 1;
		}
		Carne temp = found.get();
		temp.setNombre(AESUtil.encrypt(newData.getNombre()));
		temp.setPrecio(newData.getPrecio());
		temp.setCantidad(newData.getCantidad());
		itemRepo.save(temp);
		return 0;
	}

	@Override
	public long count() {
		return itemRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return itemRepo.existsById(id);
	}

	/** Entidad (cifrada) -> DTO (descifrado). */
	private CarneDTO toDTO(Lacteo entity) {
		CarneDTO dto = modelMapper.map(entity, CarneDTO.class);
		dto.setNombre(AESUtil.decrypt(entity.getNombre()));
		return dto;
	}

}
