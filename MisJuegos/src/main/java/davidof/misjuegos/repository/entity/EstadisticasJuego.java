package davidof.misjuegos.repository.entity;

public class EstadisticasJuego {
	private String jugador;
	private Long victorias;
	private Integer partidas;
	private Double pctVictorias;
	private Long puntuacionMedia;
	private Integer puntuacionMaxima;
	public String getJugador() {
		return jugador;
	}
	public void setJugador(String jugador) {
		this.jugador = jugador;
	}
	public Long getVictorias() {
		return victorias;
	}
	public void setVictorias(Long victorias) {
		this.victorias = victorias;
	}
	public Integer getPartidas() {
		return partidas;
	}
	public void setPartidas(Integer partidas) {
		this.partidas = partidas;
	}
	public Double getPctVictorias() {
		return pctVictorias;
	}
	public void setPctVictorias(Double pctVictorias) {
		this.pctVictorias = pctVictorias;
	}
	public Long getPuntuacionMedia() {
		return puntuacionMedia;
	}
	public void setPuntuacionMedia(Long puntuacionMedia) {
		this.puntuacionMedia = puntuacionMedia;
	}
	public Integer getPuntuacionMaxima() {
		return puntuacionMaxima;
	}
	public void setPuntuacionMaxima(Integer puntuacionMaxima) {
		this.puntuacionMaxima = puntuacionMaxima;
	}
	
}
