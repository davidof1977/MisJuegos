package davidof.misjuegos.repository.entity;

import java.time.LocalDate;
import java.util.List;

import davidof.misjuegos.PuntosComparator;

public class Partida implements Comparable<Partida> {
	
	private LocalDate fecha;
	private Boolean ganador;
	private Integer puntos;
	private List<Jugador> jugadores;
	private boolean primeraPartida;
	
	public boolean isPrimeraPartida() {
		return primeraPartida;
	}
	public void setPrimeraPartida(boolean primeraPartida) {
		this.primeraPartida = primeraPartida;
	}
	public List<Jugador> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}
	public Integer getPuntos() {
		return puntos;
	}
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public Boolean getGanador() {
		return ganador;
	}
	public void setGanador(Boolean ganador) {
		this.ganador = ganador;
	}
	@Override
	public int compareTo(Partida o) {
		this.getFecha().compareTo(o.getFecha());
		return 0;
	}
	
	public String getNombreGanador() {
		if(jugadores!=null) {
			Jugador j = jugadores.stream().sorted(new PuntosComparator()).findFirst().get();
			if (j.getPuntosJugador().intValue()> this.puntos)
				return j.getNombre();
			else
				return "Tu";
		}else
			return "Tu";


}
}
