package davidof.misjuegos;

import java.util.Comparator;

import davidof.misjuegos.repository.entity.Partida;

public class PartidasJuegoComparator implements Comparator<Partida> {

	@Override
	public int compare(Partida o1, Partida o2) {
		// TODO Auto-generated method stub
		return o1.getFecha().compareTo(o2.getFecha());
	}

}
