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
	public void eliminar(String name, String usuario) {
		juegoRepository.deleteByNombreAndUsuario(name, usuario);
	}

	/*@Override
	public Optional<List<Juego>> obtenerJuegoRegex(String regex) {
		return juegoRepository.findByNombreRegex(regex);
	}*/
	
	@Override
	public Optional<List<Juego>> obtenerJuegoRegex(String regex, String usuario) {
		MongoClient mongoClient = MongoClients.create("mongodb+srv://davidof1977:baralo18@davidof1977.4gzm1.mongodb.net/mis-juegos?retryWrites=true&w=majority");
		MongoTemplate template = new MongoTemplate(mongoClient, "mis-juegos");
		Query q = new Query();
		Criteria criterioNombre = Criteria.where("nombre").regex(regex, "i");
		Criteria criterioUsuario = Criteria.where("usuario").is(usuario);
		q.addCriteria(criterioNombre);
		q.addCriteria(criterioUsuario);
		List<Juego> juegos = template.find(q,Juego.class);
		
		return Optional.of(juegos);
		
	}
	
	@Override 
	public Boolean validarUsuario(String usuario) {
		MongoClient mongoClient = MongoClients.create("mongodb+srv://davidof1977:baralo18@davidof1977.4gzm1.mongodb.net/mis-juegos?retryWrites=true&w=majority");
		MongoTemplate template = new MongoTemplate(mongoClient, "mis-juegos");
		Criteria criterios = Criteria.where("usuario").is(usuario);
		List<Juego> juegos = template.find(new Query().addCriteria(criterios),Juego.class);
		return !juegos.isEmpty();
	}
	
	@Override
	public Optional<List<Juego>> obtenerJuegos(String usuario) {
		return juegoRepository.findByUsuario(usuario);
	}
	
}
