package davidof.misjuegos.repository.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

public class Partida {
	@Id
	private String id;
	private LocalDate fecha;
	private Boolean ganador;
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

}
