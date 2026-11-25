package co.edu.unbosque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Torneo todos contra todos a una vuelta. Gana quien termina primero en la tabla.
 */
public class Torneo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Orden de la tabla: puntos, diferencia de gol, goles a favor y, al final, nombre. */
	private static final Comparator<EquipoFutbol> ORDEN_TABLA = Comparator
			.comparingInt(EquipoFutbol::getPuntos).reversed()
			.thenComparing(Comparator.comparingInt(EquipoFutbol::getDiferenciaGoles).reversed())
			.thenComparing(Comparator.comparingInt(EquipoFutbol::getGolesFavor).reversed())
			.thenComparing(EquipoFutbol::getNombre, String.CASE_INSENSITIVE_ORDER);

	private String nombre;
	private List<EquipoFutbol> equipos = new ArrayList<>();
	private List<Partido> partidos = new ArrayList<>();
	private EstadoTorneo estado = EstadoTorneo.CREADO;
	private Random azar;

	public Torneo(String nombre) {
		this(nombre, new Random());
	}

	public Torneo(String nombre, Random azar) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El torneo necesita un nombre");
		}
		this.nombre = nombre.trim();
		this.azar = azar;
	}

	public void agregarEquipo(EquipoFutbol equipo) {
		if (estado != EstadoTorneo.CREADO) {
			throw new IllegalStateException("No se pueden agregar equipos a un torneo que ya empezó");
		}
		for (EquipoFutbol existente : equipos) {
			if (existente.getNombre().equalsIgnoreCase(equipo.getNombre())) {
				throw new IllegalArgumentException("Ya hay un equipo llamado " + equipo.getNombre());
			}
		}
		equipos.add(equipo);
	}

	/**
	 * Arma el calendario con el método del círculo: en cada fecha todos los equipos juegan una
	 * vez (o uno descansa si son impares) y al final cada par de equipos se enfrentó una sola vez.
	 */
	public void iniciar() {
		if (estado != EstadoTorneo.CREADO) {
			throw new IllegalStateException("El torneo ya empezó");
		}
		if (equipos.size() < 2) {
			throw new IllegalStateException("Se necesitan al menos 2 equipos para empezar");
		}

		List<EquipoFutbol> rueda = new ArrayList<>(equipos);
		if (rueda.size() % 2 != 0) {
			rueda.add(null); // quien queda frente a null descansa esa fecha
		}
		int cantidad = rueda.size();
		for (int fecha = 0; fecha < cantidad - 1; fecha++) {
			for (int i = 0; i < cantidad / 2; i++) {
				EquipoFutbol local = rueda.get(i);
				EquipoFutbol visitante = rueda.get(cantidad - 1 - i);
				if (local != null && visitante != null) {
					// Se alterna la localía para que el primero no sea siempre local.
					partidos.add(fecha % 2 == 0 ? new Partido(local, visitante, azar)
							: new Partido(visitante, local, azar));
				}
			}
			// El primero queda fijo y los demás rotan una posición.
			rueda.add(1, rueda.remove(cantidad - 1));
		}
		estado = EstadoTorneo.EN_CURSO;
	}

	/**
	 * Juega el siguiente partido pendiente del calendario.
	 */
	public Partido jugarPartido() {
		if (estado == EstadoTorneo.CREADO) {
			throw new IllegalStateException("Primero hay que iniciar el torneo");
		}
		for (Partido partido : partidos) {
			if (partido.getEstado() == EstadoPartido.PENDIENTE) {
				partido.simularPartido();
				if (getPartidosPendientes().isEmpty()) {
					estado = EstadoTorneo.FINALIZADO;
				}
				return partido;
			}
		}
		throw new IllegalStateException("Ya se jugaron todos los partidos");
	}

	/**
	 * Juega todos los partidos que faltan.
	 */
	public List<Partido> jugarTodos() {
		if (estado == EstadoTorneo.CREADO) {
			throw new IllegalStateException("Primero hay que iniciar el torneo");
		}
		List<Partido> jugados = new ArrayList<>();
		while (estado == EstadoTorneo.EN_CURSO) {
			jugados.add(jugarPartido());
		}
		return jugados;
	}

	public List<EquipoFutbol> tablaPosiciones() {
		List<EquipoFutbol> tabla = new ArrayList<>(equipos);
		tabla.sort(ORDEN_TABLA);
		return tabla;
	}

	public String obtenerCampeon() {
		if (estado != EstadoTorneo.FINALIZADO) {
			throw new IllegalStateException("El torneo todavía no ha terminado");
		}
		return tablaPosiciones().get(0).getNombre();
	}

	public String tablaComoTexto() {
		StringBuilder texto = new StringBuilder();
		texto.append(String.format("%-4s%-22s%4s%4s%4s%4s%4s%4s%5s%5s%n",
				"Pos", "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts"));
		int posicion = 1;
		for (EquipoFutbol equipo : tablaPosiciones()) {
			texto.append(String.format("%-4d%-22s%4d%4d%4d%4d%4d%4d%5d%5d%n", posicion++,
					recortar(equipo.getNombre(), 21), equipo.getPartidosJugados(), equipo.getPartidosGanados(),
					equipo.getPartidosEmpatados(), equipo.getPartidosPerdidos(), equipo.getGolesFavor(),
					equipo.getGolesContra(), equipo.getDiferenciaGoles(), equipo.getPuntos()));
		}
		return texto.toString();
	}

	/**
	 * Reporte completo: tabla, resultados, goleadores y campeón (si ya terminó).
	 */
	public String generarReporte() {
		String salto = System.lineSeparator();
		StringBuilder reporte = new StringBuilder();
		reporte.append("Torneo: ").append(nombre).append(salto);
		reporte.append("Estado: ").append(estado.name().toLowerCase().replace('_', ' ')).append(" · ")
				.append(getPartidosJugados().size()).append(" de ").append(partidos.size())
				.append(" partidos jugados").append(salto).append(salto);

		reporte.append("TABLA DE POSICIONES").append(salto).append(tablaComoTexto()).append(salto);

		reporte.append("RESULTADOS").append(salto);
		for (Partido partido : getPartidosJugados()) {
			reporte.append("  ").append(partido.getMarcador()).append(salto);
		}

		List<Jugador> goleadores = goleadores();
		if (!goleadores.isEmpty()) {
			reporte.append(salto).append("GOLEADORES").append(salto);
			for (Jugador jugador : goleadores) {
				reporte.append(String.format("  %-24s%3d%n", jugador.getNombre(), jugador.getGoles()));
			}
		}

		if (estado == EstadoTorneo.FINALIZADO) {
			reporte.append(salto).append("CAMPEÓN: ").append(obtenerCampeon()).append(salto);
		}
		return reporte.toString();
	}

	/**
	 * Jugadores con al menos un gol, de más a menos goles.
	 */
	public List<Jugador> goleadores() {
		List<Jugador> goleadores = new ArrayList<>();
		for (EquipoFutbol equipo : equipos) {
			for (Jugador jugador : equipo.getListaJugadores()) {
				if (jugador.getGoles() > 0) {
					goleadores.add(jugador);
				}
			}
		}
		goleadores.sort(Comparator.comparingInt(Jugador::getGoles).reversed().thenComparing(Jugador::getNombre));
		return goleadores;
	}

	private static String recortar(String texto, int maximo) {
		return texto.length() <= maximo ? texto : texto.substring(0, maximo - 1) + "…";
	}

	public List<Partido> getPartidosPendientes() {
		return partidos.stream().filter(p -> p.getEstado() == EstadoPartido.PENDIENTE).toList();
	}

	public List<Partido> getPartidosJugados() {
		return partidos.stream().filter(p -> p.getEstado() == EstadoPartido.FINALIZADO).toList();
	}

	public String getNombre() {
		return nombre;
	}

	public List<EquipoFutbol> getEquipos() {
		return Collections.unmodifiableList(equipos);
	}

	public List<Partido> getPartidos() {
		return Collections.unmodifiableList(partidos);
	}

	public EstadoTorneo getEstado() {
		return estado;
	}

}
