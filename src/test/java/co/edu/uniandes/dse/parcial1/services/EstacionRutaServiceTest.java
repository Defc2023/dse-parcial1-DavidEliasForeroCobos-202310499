package co.edu.uniandes.dse.parcial1.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ EstacionService.class, RutaService.class })
public class EstacionRutaServiceTest {
    @Autowired
    EstacionRutaService estacionRutaService;

    @Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private EstacionEntity estacionEntity = new EstacionEntity();
    private List<EstacionEntity> estacionList = new ArrayList<>();
	private List<RutaEntity> rutaList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from AuthorEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from BookEntity").executeUpdate();
        estacionList.clear();
        rutaList.clear();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EstacionEntity entity = factory.manufacturePojo(EstacionEntity.class);
			entityManager.persist(entity);
			estacionList.add(entity);
		}
		for (int i = 0; i < 3; i++) {
			RutaEntity entity = factory.manufacturePojo(RutaEntity.class);
			entityManager.persist(entity);
			rutaList.add(entity);
		}
	}

    @Test
	void testRemoveEstacionRuta() throws EntityNotFoundException, IllegalOperationException {
		for (RutaEntity ruta : rutaList) {
			estacionRutaService.removeEstacionRuta(estacionEntity.getId(), ruta.getId());
		}
	}
}
