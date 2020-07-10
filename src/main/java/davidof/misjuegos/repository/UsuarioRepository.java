package davidof.misjuegos.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import davidof.misjuegos.repository.entity.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	public List<Usuario> findByNombre(String nombre);

}

