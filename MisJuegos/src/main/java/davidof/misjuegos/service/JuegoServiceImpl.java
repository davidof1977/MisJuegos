package davidof.misjuegos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import davidof.misjuegos.repository.JuegoRepository;
import davidof.misjuegos.repository.entity.Juego;

@Service
public class JuegoServiceImpl implements JuegoService {
	
	@Autowired
	private JuegoRepository juegoRepository;

	@Override
	public void guardar(Juego juego) {
		juegoRepository.save(juego);
	}

	@Override
	public List<Juego> obtenerTodosJuegos() {
		return juegoRepository.findAll();
	}

	@Override
	public Optional<Juego> obtenerJuego(String nombre) {
		return juegoRepository.findByNombre(nombre);
	}

	@Override
	public void eliminar(String id) {
		juegoRepository.deleteById(id);	
	}

	/*@Override
	public Optional<List<Juego>> obtenerJuegoRegex(String regex) {
		return juegoRepository.findByNombreRegex(regex);
	}*/
	
	@Override
	public Optional<List<Juego>> obtenerJuegoRegex(String regex) {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoTemplate template = new MongoTemplate(mongoClient, "TestJuegos");
		Criteria criterios = Criteria.where("nombre").regex(regex, "i");      
		List<Juego> juegos = template.find(new Query().addCriteria(criterios),Juego.class);
		return Optional.of(juegos);
		
	}
	
}
