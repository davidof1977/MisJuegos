package davidof.misjuegos.repository.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Juego {

	@Id
	private String id;
	private String nombre;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getEnColeccion() {
		return enColeccion;
	}
	public void setEnColeccion(Boolean enColeccion) {
		this.enColeccion = enColeccion;
	}
	public Boolean getEnSeguimiento() {
		return enSeguimiento;
	}
	public void setEnSeguimiento(Boolean enSeguimiento) {
		this.enSeguimiento = enSeguimiento;
	}
	public Boolean getEnListaDeseos() {
		return enListaDeseos;
	}
	public void setEnListaDeseos(Boolean enListaDeseos) {
		this.enListaDeseos = enListaDeseos;
	}
	public List<Partida> getPartidas() {
		return partidas;
	}
	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}
	private Boolean enColeccion;
	private Boolean enSeguimiento;
	private Boolean enListaDeseos;
	private List<Partida> partidas;
}
