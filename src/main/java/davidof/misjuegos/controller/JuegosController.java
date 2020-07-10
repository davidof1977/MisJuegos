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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import davidof.misjuegos.PartidasJuegoComparator;
import davidof.misjuegos.PuntosComparator;
import davidof.misjuegos.repository.entity.ErrorPruebas;
import davidof.misjuegos.repository.entity.EstadisticasJuego;
import davidof.misjuegos.repository.entity.EstadisticasPersonales;
import davidof.misjuegos.repository.entity.Juego;
import davidof.misjuegos.repository.entity.Jugador;
import davidof.misjuegos.repository.entity.Partida;
import davidof.misjuegos.repository.entity.PartidaJuego;
import davidof.misjuegos.service.JuegoService;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
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
		public List<Juego> obtenerTodosJuegos(@RequestHeader("usuario") String usuario) {
			List<Juego> juegos = null;
			try {
				juegos = JuegoService.obtenerJuegos(usuario).orElseThrow(NotFoundException::new);
				return juegos;
			}
			catch(NotFoundException e) {
				 throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe.");
			}
		}

	
		@GetMapping("juegos/{nombre}")
		public Optional<Juego> obtenerJuego(@PathVariable String nombre, @RequestHeader("usuario") String usuario) {
			Optional<Juego> juego = JuegoService.obtenerJuego(nombre);
			if (!juego.isPresent()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				return juego.filter(j -> j.getUsuario().equalsIgnoreCase(usuario));
			}			
		}
		
		@GetMapping("juegos/buscar/{regex}")
		public Optional<List<Juego>> obtenerJuegoQuery(@PathVariable String regex, @RequestHeader("usuario") String usuario) {
			Optional<List<Juego>> juegos = JuegoService.obtenerJuegoRegex(regex, usuario);
			if (juegos.get().isEmpty()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				return juegos;
			}
		}
		
		@GetMapping("juegos/listadeseos")
		public List<Juego> obtenerListaDeseos(@RequestHeader("usuario") String usuario) {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getEnListaDeseos()!=null && juego.getEnListaDeseos()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/coleccion")
		public List<Juego> obtenerColeccion(@RequestHeader("usuario") String usuario) {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getEnColeccion()!=null && juego.getEnColeccion()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/seguimiento")
		public List<Juego> obtenerSeguimiento(@RequestHeader("usuario") String usuario) {
			return JuegoService.obtenerTodosJuegos().stream()
					.filter(juego -> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getEnSeguimiento()!=null && juego.getEnSeguimiento()).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/{nombre}/partidas")
		public List<Partida> obtenerPartidas(@PathVariable String nombre, @RequestHeader("usuario") String usuario) {
			try {
				Juego juego = JuegoService.obtenerJuego(nombre).filter(j -> j.getUsuario().equalsIgnoreCase(usuario)).orElseThrow(NotFoundException::new);
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
		public List<Partida> obtenerPartidasGanadas(@PathVariable String nombre, @RequestHeader("usuario") String usuario) {
			Optional<Juego> juegos = JuegoService.obtenerJuego(nombre).filter(j -> j.getUsuario().equalsIgnoreCase(usuario));
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
		public List<PartidaJuego> obtenerTodasPartidas(@RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
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
		
		/**
		 * Obtiene a cuantos juegos diferentes se ha jugado
		 * @return
		 */
		@GetMapping("juegos/partidas/distintos")
		public Integer obtenerJuegosPartidasDistintos(@RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream().map(p -> {
							PartidaJuego pj = new PartidaJuego();
							pj.setJuego(j.getNombre());
							return pj;							
					})).collect(Collectors.toList());
			 return partidas.stream().collect(Collectors.groupingBy(PartidaJuego::getJuego)).size();
		}
		
		@GetMapping("juegos/partidas/ganadas")
		public List<PartidaJuego> obtenerTodasPartidasGanadas(@RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
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
		public List<PartidaJuego> obtenerTodasPartidasMes(@PathVariable int mes, @RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
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
		
		@GetMapping("juegos/partidas/distintos/mes/{mes}")
		public Integer obtenerPartidasJuegosDistintosMes(@PathVariable int mes, @RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p ->  p.getFecha().getMonth().getValue()==mes)
							.map(p -> {
						PartidaJuego pj = new PartidaJuego();
						pj.setJuego(j.getNombre());
						return pj;
					})).collect(Collectors.toList());
			return partidas.stream().collect(Collectors.groupingBy(PartidaJuego::getJuego)).size();
		}
		
		@GetMapping("juegos/partidas/anio/{anio}")
		public List<PartidaJuego> obtenerTodasPartidasAnio(@PathVariable int anio, @RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
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
		
		@GetMapping("juegos/partidas/distintos/anio/{anio}")
		public Integer obtenerPartidasJuegosDistintosAnio(@PathVariable int anio, @RequestHeader("usuario") String usuario) {
			List<PartidaJuego> partidas = JuegoService.obtenerTodosJuegos().stream()
					.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
					.flatMap(j -> j.getPartidas().stream()
							.filter(p ->  p.getFecha().getYear()==anio)
							.map(p -> {
						PartidaJuego pj = new PartidaJuego();
						pj.setJuego(j.getNombre());
						return pj;
					})).collect(Collectors.toList());
			return partidas.stream().collect(Collectors.groupingBy(PartidaJuego::getJuego)).keySet().size();
		}
		

		@DeleteMapping("juegos/{name}")
		public void eliminar(@PathVariable String name, @RequestHeader("usuario") String usuario) {
			JuegoService.eliminar(name, usuario);	
		}
		@GetMapping("juegos/partidas/jugadores/{regex}")
		public List<String> obtenerJugadores(@PathVariable String regex, @RequestHeader("usuario") String usuario){
			List<String> jugadores = JuegoService.obtenerTodosJuegos().stream()
				.filter(juego-> juego.getUsuario().equalsIgnoreCase(usuario) && juego.getPartidas()!=null)
				.flatMap(j -> j.getPartidas().stream())
				.filter(p -> p.getJugadores()!= null)
				.flatMap(partida -> partida.getJugadores().stream())
				.distinct()
				.map(j -> j.getNombre())
				.collect(Collectors.toList());
			return jugadores.stream().filter(n -> n.matches(regex)).collect(Collectors.toList());
		}
		
		@GetMapping("juegos/{nombre}/estadisticas")
		public List<EstadisticasJuego>  obtenerEstadisticasJuego(@PathVariable String nombre, @RequestHeader("usuario") String usuario){
			Optional<Juego> juego = JuegoService.obtenerJuego(nombre).filter(j -> j.getUsuario().equalsIgnoreCase(usuario));
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
					stat.setPctVictorias(round((double)stat.getVictorias()/stat.getPartidas()*100,2));
					stats.add(stat);
					
				} );
				return stats;
			}
			
		}
		
		public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
		
		@GetMapping("juegos/{nombre}/records")
		public Jugador obtenerRecordsJuego(@PathVariable String nombre, @RequestHeader("usuario") String usuario){
			Optional<Juego> juego = JuegoService.obtenerJuego(nombre).filter(j -> j.getUsuario().equalsIgnoreCase(usuario));
			if (!juego.isPresent()) {
		        throw new ResponseStatusException(
				          HttpStatus.NOT_FOUND, "El juego no existe");
			}else {
				List<Jugador> jugadores = juego.get().getPartidas().stream()
				.filter(p -> p.getJugadores()!=null)
				.flatMap(p -> p.getJugadores().stream()).collect(Collectors.toList());
				jugadores.addAll(juego.get().getPartidas().stream()
					.map(p -> {
						Jugador j = new Jugador();
						j.setNombre("Tu");
						j.setPuntosJugador(p.getPuntos());
						return j;
					}).collect(Collectors.toList()));
				
				return jugadores.stream().sorted((p1,p2) -> p2.getPuntosJugador()-p1.getPuntosJugador()).findFirst().get();
			}
		
		}
	
		@GetMapping("juegos/estadisticas/personales")
		public List<EstadisticasPersonales>  obtenerEstadisticasPersonales(@RequestHeader("usuario") String usuario){
			Optional<List<Juego>> juegos = JuegoService.obtenerJuegos(usuario);
			List<EstadisticasPersonales> estadisticas = juegos.get().stream()
				.filter(j -> j.getPartidas()!=null)	
				.map(j -> {
					EstadisticasPersonales est = new EstadisticasPersonales();
					est.setJuego(j.getNombre());
					est.setPartidas(j.getPartidas().size());
					est.setVictorias(j.getPartidas().stream().filter(p -> p.getGanador()!=null && p.getGanador()).count());
					est.setPctVictorias(round((double)est.getVictorias()/est.getPartidas()*100,2));
					if(j.getPartidas()!=null && j.getPartidas().size()>0) {
						est.setPuntuacionMaxima( j.getPartidas().stream().max((p1,p2) -> p1.getPuntos() - p2.getPuntos()).get().getPuntos());
						est.setPuntuacionMedia(Math.round(j.getPartidas().stream().mapToInt(p -> p.getPuntos()).average().getAsDouble()));
					}else {
						est.setPuntuacionMaxima(0);
						est.setPuntuacionMedia(new Long(0));
					}
					return est;

				}).collect(Collectors.toList());

				return estadisticas;
			}
		
		@GetMapping("juegos/errorpruebas")
		public ErrorPruebas  obtenerError(){
				ErrorPruebas e = new ErrorPruebas();
				e.setCodigo(401);
				String msg = "Error - 1307. <BR> Se ha producido un error. "
						+ "Pongase en contacto con el SAU</BR> ";
				System.out.println(msg);
				e.setDescription(msg);
				return e;
			}
		@GetMapping("usuarios/{usuario}")
		public boolean validarUsuario(String usuario) {
		 return	JuegoService.validarUsuario(usuario);
		}
		
	
	
}
