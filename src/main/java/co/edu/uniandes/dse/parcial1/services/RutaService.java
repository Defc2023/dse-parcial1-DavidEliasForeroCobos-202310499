package co.edu.uniandes.dse.parcial1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.ErrorMessage;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RutaService {
    @Autowired
	RutaRepository rutaRepository;

	@Transactional
	public RutaEntity createRuta(RutaEntity rutaEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de la ruta");

        if (rutaEntity.getNombre() == null || rutaEntity.getNombre().length() == 0) {
            throw new IllegalOperationException("El nombre de la ruta es invalido");
        }
        if (rutaEntity.getColor() == null || rutaEntity.getColor().length() == 0) {
            throw new IllegalOperationException("El color de la ruta es invalido");
        }
        if (rutaEntity.getTipo() == null || rutaEntity.getTipo().length() == 0 || !rutaEntity.getTipo().equalsIgnoreCase("diurna") || !rutaEntity.getTipo().equalsIgnoreCase("nocturna") || !rutaEntity.getTipo().equalsIgnoreCase("circular")) {
            throw new IllegalOperationException("El tipo de ruta es invalida");
        }

		log.info("Termina proceso de creación de la ruta");
		return rutaRepository.save(rutaEntity);
	}

	@Transactional
	public List<RutaEntity> getRutas() {
		log.info("Inicia proceso de consultar todos las rutas");
		return rutaRepository.findAll();
	}

	@Transactional
	public RutaEntity getRuta(Long rutaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la ruta con id = {0}", rutaId);

		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		if (rutaEntity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.RUTA_NOT_FOUND);
        }

		log.info("Termina proceso de consultar la ruta con id = {0}", rutaId);
		return rutaEntity.get();
	}

	@Transactional
	public RutaEntity updateRuta(Long rutaId, RutaEntity ruta)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la ruta con id = {0}", rutaId);

		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.RUTA_NOT_FOUND);

            if (ruta.getNombre() == null || ruta.getNombre().length() == 0) {
                throw new IllegalOperationException("El nombre de la ruta es invalido");
            }
            if (ruta.getColor() == null || ruta.getColor().length() == 0) {
                throw new IllegalOperationException("El color de la ruta es invalido");
            }
            if (ruta.getTipo() == null || ruta.getTipo().length() == 0 || !ruta.getTipo().equalsIgnoreCase("diurna") || !ruta.getTipo().equalsIgnoreCase("nocturna") || !ruta.getTipo().equalsIgnoreCase("circular")) {
                throw new IllegalOperationException("El tipo de ruta es invalida");
            }

		ruta.setId(rutaId);
		log.info("Termina proceso de actualizar la ruta con id = {0}", rutaId);
		return rutaRepository.save(ruta);
	}

	@Transactional
	public void deleteRuta(Long rutaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la ruta con id = {0}", rutaId);

		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);

		List<EstacionEntity> estaciones = rutaEntity.get().getEstaciones();

		if (!estaciones.isEmpty())
			throw new IllegalOperationException("Unable to delete route because it has associated stations");

		rutaRepository.deleteById(rutaId);
		log.info("Termina proceso de borrar la ruta con id = {0}", rutaId);
	}
}
