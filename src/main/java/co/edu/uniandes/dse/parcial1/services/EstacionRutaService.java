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
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstacionRutaService {
    @Autowired
    EstacionRepository estacionRepository;

    @Autowired
    RutaRepository rutaRepository;

    @Transactional
	public EstacionEntity addEstacionRuta(Long estacionId, Long rutaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de asociarle una ruta a la estacion con id = {0}", estacionId);

		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);

		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.RUTA_NOT_FOUND);

        if (estacionEntity.get().getCapacidad() < 100) {
            List<RutaEntity> rutas = estacionEntity.get().getRutas();
            int rutasCirculares = 0;
            for (RutaEntity ruta : rutas) {
                if (ruta.getTipo() == "circular") {
                    rutasCirculares = rutasCirculares + 1;
                }
            }
            if (rutasCirculares > 2) {
                throw new IllegalOperationException("Una estación con menos de 100 pasajeros no puede tener más de 2 rutas circulares");
            }
        }

		estacionEntity.get().getRutas().add(rutaEntity.get());
		log.info("Termina proceso de asociarle una ruta a la estacion con id = {0}", estacionId);
		return estacionEntity.get();
	}

    @Transactional
    public void removeEstacionRuta(Long estacionId, Long rutaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar una ruta de la estacion con id = {0}", estacionId);

		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);

		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ESTACION_NOT_FOUND);

		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.RUTA_NOT_FOUND);

        if (estacionEntity.get().getCapacidad() < 100) {
            List<RutaEntity> rutas = estacionEntity.get().getRutas();
            int rutasCirculares = 0;
            for (RutaEntity ruta : rutas) {
                if (ruta.getTipo() == "noctura") {
                    rutasCirculares = rutasCirculares + 1;
                }
            }
            if (rutasCirculares == 1) {
                throw new IllegalOperationException("Una estación debe tener como minimo una ruta de tipo nocturna");
            }
        }

		estacionEntity.get().getRutas().remove(rutaEntity.get());

		log.info("Termina proceso de borrar una ruta de la estacion con id = {0}", estacionId);
	}
}
