package davidof.misjuegos.repository.entity;

import java.time.LocalDate;

public class Partida implements Comparable<Partida> {
	
	private LocalDate fecha;
	private Boolean ganador;
	private Integer puntos;
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
	

}
