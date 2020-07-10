package davidof.misjuegos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import davidof.misjuegos.repository.UsuarioRepository;
import davidof.misjuegos.repository.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;

	@Override
	public void guardar(Usuario usuario) {
		repository.save(usuario);
	}

	@Override
	public List<Usuario> obtenerUsuario(String nombre) {
		return repository.findByNombre(nombre);
	}
}
