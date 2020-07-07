package davidof.misjuegos;

import java.util.Comparator;

import davidof.misjuegos.repository.entity.Jugador;

public class PuntosComparator implements Comparator<Jugador> {

	@Override
	public int compare(Jugador o1, Jugador o2) {
		// TODO Auto-generated method stub
		return o1.getPuntosJugador().compareTo(o2.getPuntosJugador());
	}

}
