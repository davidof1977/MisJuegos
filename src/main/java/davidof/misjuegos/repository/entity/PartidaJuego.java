package davidof.misjuegos.repository.entity;

public class PartidaJuego extends Partida implements Comparable<Partida>{
private String juego;

public String getJuego() {
	return juego;
}

public void setJuego(String juego) {
	this.juego = juego;
}


}
