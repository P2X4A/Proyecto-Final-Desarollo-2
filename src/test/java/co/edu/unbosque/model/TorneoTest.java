package co.edu.unbosque.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TorneoTest {

	private Torneo torneoCon(int cantidadEquipos) {
		Torneo torneo = new Torneo("Copa de prueba", new Random(2026));
		for (int i = 1; i <= cantidadEquipos; i++) {
			torneo.agregarEquipo(new EquipoFutbol("Equipo " + i, "4-4-2", 60 + i, 40));
		}
		return torneo;
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 4, 5, 6 })
	void cadaParDeEquiposSeEnfrentaUnaSolaVez(int cantidadEquipos) {
		Torneo torneo = torneoCon(cantidadEquipos);
		torneo.iniciar();

		assertEquals(cantidadEquipos * (cantidadEquipos - 1) / 2, torneo.getPartidos().size());
		Set<String> cruces = new HashSet<>();
		for (Partido partido : torneo.getPartidos()) {
			List<String> par = List.of(partido.getEquipo1().getNombre(), partido.getEquipo2().getNombre());
			String cruce = par.stream().sorted().toList().toString();
			assertTrue(cruces.add(cruce), "El cruce " + cruce + " está repetido");
		}
	}

	@Test
	void noAceptaEquiposRepetidosNiDespuesDeEmpezar() {
		Torneo torneo = torneoCon(2);

		assertThrows(IllegalArgumentException.class,
				() -> torneo.agregarEquipo(new EquipoFutbol("equipo 1", "4-3-3", 70, 40)));
		torneo.iniciar();
		assertThrows(IllegalStateException.class,
				() -> torneo.agregarEquipo(new EquipoFutbol("Equipo 9", "4-3-3", 70, 40)));
	}

	@Test
	void necesitaAlMenosDosEquiposYNoSeJuegaSinIniciar() {
		Torneo torneo = torneoCon(1);

		assertThrows(IllegalStateException.class, torneo::jugarPartido);
		assertThrows(IllegalStateException.class, torneo::iniciar);
	}

	@Test
	void alJugarTodoTerminaYElCampeonEsElPrimeroDeLaTabla() {
		Torneo torneo = torneoCon(6);
		assertThrows(IllegalStateException.class, torneo::obtenerCampeon);
		torneo.iniciar();

		List<Partido> jugados = torneo.jugarTodos();

		assertEquals(15, jugados.size());
		assertEquals(EstadoTorneo.FINALIZADO, torneo.getEstado());
		assertTrue(torneo.getPartidosPendientes().isEmpty());
		assertEquals(torneo.tablaPosiciones().get(0).getNombre(), torneo.obtenerCampeon());
		assertThrows(IllegalStateException.class, torneo::jugarPartido);
	}

	@Test
	void laTablaQuedaOrdenadaPorPuntosYDiferenciaDeGol() {
		Torneo torneo = torneoCon(5);
		torneo.iniciar();
		torneo.jugarTodos();

		List<EquipoFutbol> tabla = torneo.tablaPosiciones();
		for (int i = 1; i < tabla.size(); i++) {
			EquipoFutbol anterior = tabla.get(i - 1);
			EquipoFutbol actual = tabla.get(i);
			assertTrue(anterior.getPuntos() > actual.getPuntos()
					|| (anterior.getPuntos() == actual.getPuntos()
							&& anterior.getDiferenciaGoles() >= actual.getDiferenciaGoles()));
		}
		for (EquipoFutbol equipo : tabla) {
			assertEquals(4, equipo.getPartidosJugados());
		}
	}

	@Test
	void elReporteIncluyeTablaResultadosYCampeon() {
		Torneo torneo = torneoCon(3);
		torneo.iniciar();
		torneo.jugarTodos();

		String reporte = torneo.generarReporte();

		assertTrue(reporte.contains("TABLA DE POSICIONES"));
		assertTrue(reporte.contains(torneo.getPartidos().get(0).getMarcador()));
		assertTrue(reporte.contains("CAMPEÓN: " + torneo.obtenerCampeon()));
	}

}
