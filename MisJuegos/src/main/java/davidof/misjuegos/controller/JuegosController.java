package davidof.misjuegos.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import davidof.misjuegos.PartidasJuegoComparator;
import davidof.misjuegos.repository.entity.Juego;
import davidof.misjuegos.repository.entity.Partida;
import davidof.misjuegos.repository.entity.PartidaJuego;
import davidof.misjuegos.service.JuegoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
@RestController
public class JuegosController {
	
		
		@Autowired
		private JuegoService JuegoService;

		
		@PostMapping(path = "/juegos", consumes = "application/json")
		public void guardar(@RequestBody Juego juego) {
			JuegoService.guardar(juego);
		}
		
		
		@GetMapping("/juegos")
		public List<Juego> obtenerTodosJuegos() {
			return JuegoService.obtenerTodosJuegos();
		}

	
		@GetMapping("juegos/{nombre}")
		public Optional<Juego> obtenerJuego(@PathVariable String nombre) {
			return JuegoService.obtenerJuego(nombre);
		}
		
		@GetMapping("juegos/buscar/{regex}")
		public Optional<List<Juego>> obtenerJuegoQuery(@PathVariable String regex) {
			return JuegoService.obtenerJuegoRegex(regex);
		}
		
		@GetMapping("juegos/listadeseos")
		public List<Juego> obtenerListaDeseos() {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getEnListaDeseos()!=null && juego.getEnListaDeseos()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/coleccion")
		public List<Juego> obtenerColeccion() {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getEnColeccion()!=null && juego.getEnColeccion()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/seguimiento")
		public List<Juego> obtenerSeguimiento() {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getEnSeguimiento()!=null && juego.getEnSeguimiento()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/{nombre}/partidas")
		public List<Partida> obtenerPartidas(@PathVariable String nombre) {
			List<Partida> partidas = JuegoService.obtenerJuego(nombre).map(j -> j.getPartidas()).orElseGet(ArrayList<Partida>::new);
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		
		@GetMapping("juegos/{nombre}/ganadas")
		public List<Partida> obtenerPartidasGanadas(@PathVariable String nombre) {
			return JuegoService.obtenerJuego(nombre)
					.map(j -> j.getPartidas().stream()
							.filter(p -> p.getGanador())
							.sorted()
							.collect(Collectors.toList()))
					.orElseGet(ArrayList<Partida>::new);
		}
		
		@GetMapping("juegos/partidas")
		public List<PartidaJuego> obtenerTodasPartidas() {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream().map(p -> {
							PartidaJuego pj = new PartidaJuego();
							pj.setFecha(p.getFecha());
							pj.setGanador(p.getGanador());
							pj.setJuego(j.getNombre());
							pj.setPuntos(p.getPuntos());
							return pj;							
					})).sorted().collect(Collectors.toList());
			 partidas.sort(new PartidasJuegoComparator());
			 return partidas;
		}
		
		@GetMapping("juegos/partidas/ganadas")
		public List<PartidaJuego> obtenerTodasPartidasGanadas() {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p -> p.getGanador()!=null && p.getGanador())
							.map(p -> {
							PartidaJuego pj = new PartidaJuego();
							pj.setFecha(p.getFecha());
							pj.setGanador(p.getGanador());
							pj.setJuego(j.getNombre());
							pj.setPuntos(p.getPuntos());
							return pj;							
					})).collect(Collectors.toList());
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		
		@GetMapping("juegos/partidas/{mes}")
		public List<PartidaJuego> obtenerTodasPartidas(@PathVariable int mes) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p -> p.getFecha().getMonth().getValue()==mes)
							.map(p -> {
						PartidaJuego pj = new PartidaJuego();
						pj.setFecha(p.getFecha());
						pj.setGanador(p.getGanador());
						pj.setJuego(j.getNombre());
						pj.setPuntos(p.getPuntos());
						return pj;
					})).collect(Collectors.toList());
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		

		@DeleteMapping("juegos/{id}")
		public void eliminar(@PathVariable String id) {
			JuegoService.eliminar(id);	
		}
		

		
	
}
