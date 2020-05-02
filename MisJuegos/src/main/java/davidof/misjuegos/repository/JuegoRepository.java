package davidof.misjuegos.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import davidof.misjuegos.repository.entity.Juego;

@Repository
public interface JuegoRepository extends MongoRepository<Juego, String> {
public Optional<Juego> findByNombre(String nombre);
}

