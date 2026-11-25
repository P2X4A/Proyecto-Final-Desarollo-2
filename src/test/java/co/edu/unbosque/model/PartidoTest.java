package co.edu.unbosque.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartidoTest {
	private EquipoFutbol local;
	private EquipoFutbol visitante;

	@BeforeEach
	void crearEquipos() {
		local = new EquipoFutbol("Leones FC", "4-3-3", 75, 40);
		local.agregarJugador(new Jugador("Andrés Rincón", Posicion.DELANTERO));
		visitante = new EquipoFutbol("Real Andino", "3-5-2", 68, 42);
	}

	@Test
	void conLaMismaSemillaDaElMismoResultado() {
		Partido primero = new Partido(local, visitante, new Random(42));
		primero.simularPartido();

		EquipoFutbol otroLocal = new EquipoFutbol("Leones FC", "4-3-3", 75, 40);
		otroLocal.agregarJugador(new Jugador("Andrés Rincón", Posicion.DELANTERO));
		Partido segundo = new Partido(otroLocal, new EquipoFutbol("Real Andino", "3-5-2", 68, 42), new Random(42));
		segundo.simularPartido();

		assertEquals(primero.getMarcador(), segundo.getMarcador());
		assertEquals(primero.obtenerAccionesGol(), segundo.obtenerAccionesGol());
	}

	@Test
	void losGolesCoincidenConLosEventosYConLasEstadisticas() {
		Partido partido = new Partido(local, visitante, new Random(7));
		partido.simularPartido();

		long golesEnEventos = partido.getEventos().stream().filter(EventoPartido::isGol).count();
		assertEquals(partido.getGolesEquipo1() + partido.getGolesEquipo2(), golesEnEventos);
		assertEquals(partido.getEventos().size(), partido.obtenerAccionesGol());
		assertTrue(partido.obtenerAccionesGol() >= golesEnEventos);

		assertEquals(EstadoPartido.FINALIZADO, partido.getEstado());
		assertEquals(1, local.getPartidosJugados());
		assertEquals(1, visitante.getPartidosJugados());
		assertEquals(partido.getGolesEquipo1(), local.getGolesFavor());
		assertEquals(partido.getGolesEquipo1(), visitante.getGolesContra());
		assertSame(partido, local.getHistorialPartidas().get(0));
	}

	@Test
	void losGolesDelLocalSeLosAnotaSuDelantero() {
		Partido partido = new Partido(local, visitante, new Random(3));
		partido.simularPartido();

		assertEquals(partido.getGolesEquipo1(), local.getListaJugadores().get(0).getGoles());
	}

	@Test
	void elGanadorEsQuienHizoMasGolesYEmpateDevuelveNull() {
		Partido partido = new Partido(local, visitante, new Random(11));
		partido.simularPartido();

		EquipoFutbol ganador = partido.saberGanador();
		if (partido.getGolesEquipo1() == partido.getGolesEquipo2()) {
			assertNull(ganador);
		} else {
			assertSame(partido.getGolesEquipo1() > partido.getGolesEquipo2() ? local : visitante, ganador);
		}
	}

	@Test
	void noSePuedeJugarDosVecesNiPedirGanadorAntesDeJugar() {
		Partido partido = new Partido(local, visitante, new Random(1));

		assertThrows(IllegalStateException.class, partido::saberGanador);
		partido.simularPartido();
		assertThrows(IllegalStateException.class, partido::simularPartido);
	}

	@Test
	void unEquipoNoJuegaContraSiMismo() {
		assertThrows(IllegalArgumentException.class, () -> new Partido(local, local));
	}

}
