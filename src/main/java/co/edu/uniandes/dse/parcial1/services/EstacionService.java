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
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstacionService {
    @Autowired
	EstacionRepository estacionRepository;

	@Transactional
	public EstacionEntity createEstacion(EstacionEntity estacionEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de la estación");

        if (estacionEntity.getName() == null || estacionEntity.getName().length() == 0) {
            throw new IllegalOperationException("El nombre de la estación es invalido");
        }
        if (estacionEntity.getDireccion() == null || estacionEntity.getDireccion().length() == 0) {
            throw new IllegalOperationException("La dirección de la estación es invalida");
        }
        if (estacionEntity.getCapacidad() == null || estacionEntity.getCapacidad() <= 0) {
            throw new IllegalOperationException("La capacidad de la estación es invalida");
        }

		log.info("Termina proceso de creación de la estación");
		return estacionRepository.save(estacionEntity);
	}

	@Transactional
	public List<EstacionEntity> getEstaciones() {
		log.info("Inicia proceso de consultar todos las estaciones");
		return estacionRepository.findAll();
	}

	@Transactional
	public EstacionEntity getEstacion(Long estacionId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la estacion con id = {0}", estacionId);

		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);
        }

		log.info("Termina proceso de consultar la estacion con id = {0}", estacionId);
		return estacionEntity.get();
	}

	@Transactional
	public EstacionEntity updateEstacion(Long estacionId, EstacionEntity estacion)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la estacion con id = {0}", estacionId);

		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);

		if (estacion.getName() == null || estacion.getName().length() == 0) {
            throw new IllegalOperationException("El nombre de la estación es invalido");
        }
        if (estacion.getDireccion() == null || estacion.getDireccion().length() == 0) {
            throw new IllegalOperationException("La dirección de la estación es invalida");
        }
        if (estacion.getCapacidad() == null || estacion.getCapacidad() <= 0) {
            throw new IllegalOperationException("La capacidad de la estación es invalida");
        }

		estacion.setId(estacionId);
		log.info("Termina proceso de actualizar la estacion con id = {0}", estacionId);
		return estacionRepository.save(estacion);
	}

	@Transactional
	public void deleteEstacion(Long estacionId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la estacion con id = {0}", estacionId);

		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);

		List<RutaEntity> rutas = estacionEntity.get().getRutas();

		if (!rutas.isEmpty())
			throw new IllegalOperationException("Unable to delete station because it has associated routes");

		estacionRepository.deleteById(estacionId);
		log.info("Termina proceso de borrar la estacion con id = {0}", estacionId);
	}
}
