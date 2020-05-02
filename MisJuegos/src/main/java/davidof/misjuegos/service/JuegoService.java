package davidof.misjuegos.service;

import java.util.List;
import java.util.Optional;

import davidof.misjuegos.repository.entity.Juego;



public interface JuegoService {
	
	/**
	 * Guarda el producto
	 * 
	 * @param producto
	 */
	void guardar(Juego juego);
	
	/**
	 * Recupera la lista completa de productos
	 * 
	 * @return
	 */
	List<Juego> obtenerTodosJuegos();
	
	/**
	 * Devuelve el producto correspondiente al id si existe
	 * 
	 * @param id Id del producto
	 * @return
	 */
	Optional<Juego> obtenerJuego(String id);
	
	/**
	 * Elimina el producto correspondiente al id
	 * 
	 * @param id Id del producto
	 */
	void eliminar(String id);
	
}
