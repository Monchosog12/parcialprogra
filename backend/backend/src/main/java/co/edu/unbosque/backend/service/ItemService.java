package co.edu.unbosque.backend.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.backend.dto.ItemDTO;
import co.edu.unbosque.backend.model.Item;
import co.edu.unbosque.backend.repository.ItemRepository;
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
public class ItemService implements CRUDOperation<ItemDTO> {

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private ModelMapper modelMapper;

	public ItemService() {
	}

	/**
	 * @return 0 creado, 1 datos invalidos
	 */
	@Override
	public int create(ItemDTO data) {
		if (data.getNombre() == null || data.getNombre().isBlank()) {
			return 1;
		}
		Item entity = modelMapper.map(data, Item.class);
		entity.setId(null); // el id lo genera la BD
		entity.setNombre(AESUtil.encrypt(data.getNombre()));
		entity.setDescripcion(AESUtil.encrypt(data.getDescripcion()));
		itemRepo.save(entity);
		return 0;
	}

	@Override
	public List<ItemDTO> getAll() {
		return itemRepo.findAll().stream().map(this::toDTO).toList();
	}

	/** Busqueda por nombre exacto (con datos cifrados no se puede buscar por partes). */
	public List<ItemDTO> searchByNombre(String nombre) {
		return itemRepo.findByNombre(AESUtil.encrypt(nombre)).stream().map(this::toDTO).toList();
	}

	public ItemDTO getById(Long id) {
		return itemRepo.findById(id).map(this::toDTO).orElse(null);
	}

	@Override
	public int deleteById(Long id) {
		Optional<Item> found = itemRepo.findById(id);
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
	public int updateById(Long id, ItemDTO newData) {
		Optional<Item> found = itemRepo.findById(id);
		if (found.isEmpty()) {
			return 2;
		}
		if (newData.getNombre() == null || newData.getNombre().isBlank()) {
			return 1;
		}
		Item temp = found.get();
		temp.setNombre(AESUtil.encrypt(newData.getNombre()));
		temp.setDescripcion(AESUtil.encrypt(newData.getDescripcion()));
		temp.setPrecio(newData.getPrecio());
		temp.setCantidad(newData.getCantidad());
		temp.setFecha(newData.getFecha());
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
	private ItemDTO toDTO(Item entity) {
		ItemDTO dto = modelMapper.map(entity, ItemDTO.class);
		dto.setNombre(AESUtil.decrypt(entity.getNombre()));
		dto.setDescripcion(AESUtil.decrypt(entity.getDescripcion()));
		return dto;
	}

}
