package davidof.misjuegos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import davidof.misjuegos.PartidasJuegoComparator;
import davidof.misjuegos.PuntosComparator;
import davidof.misjuegos.repository.entity.EstadisticasJuego;
import davidof.misjuegos.repository.entity.Juego;
import davidof.misjuegos.repository.entity.Jugador;
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
			Optional<Juego> juego = JuegoService.obtenerJuego(nombre);
			if (!juego.isPresent()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				return juego;
			}			
		}
		
		@GetMapping("juegos/buscar/{regex}")
		public Optional<List<Juego>> obtenerJuegoQuery(@PathVariable String regex) {
			Optional<List<Juego>> juegos = JuegoService.obtenerJuegoRegex(regex);
			if (juegos.get().isEmpty()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				return juegos;
			}
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
			try {
				Juego juego = JuegoService.obtenerJuego(nombre).orElseThrow(NotFoundException::new);
				List<Partida> partidas = juego.getPartidas();
				if(partidas!=null && partidas.size()> 0) {
					partidas.sort(new PartidasJuegoComparator());
					return partidas;
				}else {
					 throw new ResponseStatusException(
					          HttpStatus.NOT_FOUND, "No hay partidas para este juego.");
				}
			}
			catch(NotFoundException e) {
				 throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe.");
			}

		}
		
		@GetMapping("juegos/{nombre}/ganadas")
		public List<Partida> obtenerPartidasGanadas(@PathVariable String nombre) {
			Optional<Juego> juegos = JuegoService.obtenerJuego(nombre);
			Optional<List<Partida>> partidas;
			try {
				partidas = Optional.ofNullable(juegos.orElseThrow(NotFoundException::new).getPartidas());
				return partidas.orElseGet(ArrayList<Partida>::new).stream()
						.filter(p -> p.getGanador())
						.sorted()
						.collect(Collectors.toList());
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				 throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe.");
			}
		
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
							pj.setJugadores(p.getJugadores());
							pj.setPrimeraPartida(p.isPrimeraPartida());
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
							pj.setJugadores(p.getJugadores());;
							pj.setPrimeraPartida(p.isPrimeraPartida());
							return pj;							
					})).collect(Collectors.toList());
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		
		@GetMapping("juegos/partidas/mes/{mes}")
		public List<PartidaJuego> obtenerTodasPartidasMes(@PathVariable int mes) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p ->  p.getFecha().getMonth().getValue()==mes)
							.map(p -> {
						PartidaJuego pj = new PartidaJuego();
						pj.setFecha(p.getFecha());
						pj.setGanador(p.getGanador());
						pj.setJuego(j.getNombre());
						pj.setPuntos(p.getPuntos());
						pj.setJugadores(p.getJugadores());
						pj.setPrimeraPartida(p.isPrimeraPartida());
						return pj;
					})).collect(Collectors.toList());
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		
		@GetMapping("juegos/partidas/anio/{anio}")
		public List<PartidaJuego> obtenerTodasPartidasAnio(@PathVariable int anio) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p ->  p.getFecha().getYear()==anio)
							.map(p -> {
						PartidaJuego pj = new PartidaJuego();
						pj.setFecha(p.getFecha());
						pj.setGanador(p.getGanador());
						pj.setJuego(j.getNombre());
						pj.setPuntos(p.getPuntos());
						pj.setJugadores(p.getJugadores());
						pj.setPrimeraPartida(p.isPrimeraPartida());
						return pj;
					})).collect(Collectors.toList());
			partidas.sort(new PartidasJuegoComparator());
			return partidas;
		}
		

		@DeleteMapping("juegos/{name}")
		public void eliminar(@PathVariable String name) {
			JuegoService.eliminar(name);	
		}
		@GetMapping("juegos/partidas/jugadores/{regex}")
		public List<String> obtenerJugadores(@PathVariable String regex){
			List<String> jugadores = JuegoService.obtenerTodosJuegos().stream()
				.filter(juego-> juego.getPartidas()!=null)
				.flatMap(j -> j.getPartidas().stream())
				.filter(p -> p.getJugadores()!= null)
				.flatMap(partida -> partida.getJugadores().stream())
				.distinct()
				.map(j -> j.getNombre())
				.collect(Collectors.toList());
			return jugadores.stream().filter(n -> n.matches(regex)).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/{nombre}/estadisticas")
		public List<EstadisticasJuego>  obtenerEstadisticasJuego(@PathVariable String nombre){
			Optional<Juego> juego = JuegoService.obtenerJuego(nombre);
			if (!juego.isPresent()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				List<Jugador> jugadores = juego.get().getPartidas().stream()
				.filter(p -> p.getJugadores()!=null)
				.flatMap(p -> p.getJugadores().stream().map(j -> {
					if (j.getNombre().equalsIgnoreCase(p.getNombreGanador()))
						j.setGanador(true);
					else
						j.setGanador(false);
					return j;

				})).collect(Collectors.toList());
				jugadores.addAll(juego.get().getPartidas().stream()
					.map(p -> {
						Jugador j = new Jugador();
						j.setNombre("Tu");
						j.setPuntosJugador(p.getPuntos());
						if (j.getNombre().equalsIgnoreCase(p.getNombreGanador()))
							j.setGanador(true);
						else
							j.setGanador(false);
						return j;
					}).collect(Collectors.toList()));
				//Agrupar por jugador obteniendo numero de partidas, numero de victorias, puntuancion max y puntuacion media
				final List<EstadisticasJuego> stats = new ArrayList<>();
				
				Map<String,List<Jugador>> mapaJugadores = jugadores.stream().collect(Collectors.groupingBy(Jugador::getNombre));
				mapaJugadores.forEach((nom, jug) ->{
					EstadisticasJuego stat = new EstadisticasJuego();
					stat.setJugador(nom);
					stat.setPartidas(jug.size());
					stat.setPuntuacionMaxima(jug.stream().max(new PuntosComparator()).get().getPuntosJugador());
					stat.setPuntuacionMedia(Math.round(jug.stream().mapToInt(j -> j.getPuntosJugador()).average().getAsDouble()));
					stat.setVictorias(jug.stream().filter(j -> j.isGanador()).count());
					stat.setPctVictorias((double)stat.getVictorias()/stat.getPartidas()*100);
					
					stats.add(stat);
					
				} );
				return stats;
			}			
			

		}
		
		
	
	
}
