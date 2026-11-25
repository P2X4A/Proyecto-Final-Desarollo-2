package co.edu.unbosque.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Equipo de fútbol con su plantilla y el historial de partidos del torneo.
 */
public class EquipoFutbol extends Equipo implements IEstadisticas {
	private static final long serialVersionUID = 1L;

	private List<Jugador> listaJugadores = new ArrayList<>();
	private List<Partido> historialPartidos = new ArrayList<>();

	public EquipoFutbol(String nombre, String formacion, int poderOfensivoBase, int posesionBase, int partidosJugados,
			int partidosGanados, int partidosPerdidos, int partidosEmpatados) {
		super(nombre, formacion, poderOfensivoBase, posesionBase, partidosJugados, partidosGanados, partidosPerdidos,
				partidosEmpatados);
	}

	public EquipoFutbol(String nombre, String formacion, int poderOfensivoBase, int posesionBase) {
		super(nombre, formacion, poderOfensivoBase, posesionBase);
	}

	public void agregarJugador(Jugador jugador) {
		listaJugadores.add(jugador);
	}

	public List<Jugador> getListaJugadores() {
		return Collections.unmodifiableList(listaJugadores);
	}

	/**
	 * Guarda un partido ya jugado en el historial del equipo.
	 */
	public void registrarPartido(Partido partido) {
		historialPartidos.add(partido);
	}

	/**
	 * Estadísticas en el orden de una tabla de posiciones:
	 * PJ, PG, PE, PP, GF, GC, DG y Pts.
	 */
	@Override
	public Map<String, Object> getEstadisticas() {
		Map<String, Object> estadisticas = new LinkedHashMap<>();
		estadisticas.put("PJ", getPartidosJugados());
		estadisticas.put("PG", getPartidosGanados());
		estadisticas.put("PE", getPartidosEmpatados());
		estadisticas.put("PP", getPartidosPerdidos());
		estadisticas.put("GF", getGolesFavor());
		estadisticas.put("GC", getGolesContra());
		estadisticas.put("DG", getDiferenciaGoles());
		estadisticas.put("Pts", getPuntos());
		return estadisticas;
	}

	@Override
	public List<Partido> getHistorialPartidas() {
		return Collections.unmodifiableList(historialPartidos);
	}

	@Override
	public String toString() {
		return String.format("%s (%s) · ataque %d · posesión %d · %d jugadores", getNombre(), getFormacion(),
				calcularPoderOfensivo(), calcularPosesion(), listaJugadores.size());
	}

}
