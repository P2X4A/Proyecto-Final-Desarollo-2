package co.edu.unbosque.model;

import java.util.List;
import java.util.Map;

public class EquipoFutbol extends Equipo implements IEstadisticas{
	
	private List<Jugador> listaJugadores;

	public EquipoFutbol(String nombre, String formacion, int poderOfensivoBase, int posesionBase, int partidosJugados,
			int partidosGanados, int partidosPerdidos, int partidosEmpatados) {
		super(nombre, formacion, poderOfensivoBase, posesionBase, partidosJugados, partidosGanados, partidosPerdidos,
				partidosEmpatados);
		
	}


	@Override
	public Map<String, Object> getEstadisticas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Partido> getHistorialPartidas() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
