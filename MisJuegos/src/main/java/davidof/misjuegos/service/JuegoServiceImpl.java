package davidof.misjuegos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
