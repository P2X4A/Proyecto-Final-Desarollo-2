package co.edu.unbosque.controller;

import java.util.List;

import co.edu.unbosque.model.EquipoFutbol;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Posicion;

/**
 * Seis equipos inventados para probar el simulador sin registrar todo a mano.
 */
final class EquiposDeEjemplo {

	private EquiposDeEjemplo() {
	}

	static List<EquipoFutbol> crear() {
		return List.of(
				equipo("Leones FC", "4-3-3", 75, 40, "Andrés Rincón", Posicion.DELANTERO, "Felipe Mora", Posicion.MEDIOCAMPISTA),
				equipo("Halcones del Norte", "4-4-2", 70, 45, "Camilo Ortiz", Posicion.DELANTERO, "Julián Peña", Posicion.DELANTERO),
				equipo("Real Andino", "3-5-2", 68, 42, "Sebastián Lara", Posicion.DELANTERO, "Mateo Quintero", Posicion.MEDIOCAMPISTA),
				equipo("Deportivo Sabana", "5-3-2", 62, 40, "Nicolás Vargas", Posicion.DELANTERO, "David Rojas", Posicion.DEFENSA),
				equipo("Unión Caribe", "4-2-4", 66, 38, "Kevin Mosquera", Posicion.DELANTERO, "Jhon Palacios", Posicion.DELANTERO),
				equipo("Atlético Bosque", "4-5-1", 64, 44, "Daniel Cárdenas", Posicion.DELANTERO, "Santiago Gil", Posicion.MEDIOCAMPISTA));
	}

	private static EquipoFutbol equipo(String nombre, String formacion, int ataque, int posesion,
			String jugador1, Posicion posicion1, String jugador2, Posicion posicion2) {
		EquipoFutbol equipo = new EquipoFutbol(nombre, formacion, ataque, posesion);
		equipo.agregarJugador(new Jugador(jugador1, posicion1));
		equipo.agregarJugador(new Jugador(jugador2, posicion2));
		return equipo;
	}

}
