package co.edu.unbosque.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.unbosque.model.EquipoFutbol;
import co.edu.unbosque.model.EstadoTorneo;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Posicion;
import co.edu.unbosque.model.Torneo;

class RepositoriosTest {

	@TempDir
	Path carpeta;

	@Test
	void guardaYCargaEquiposConSuPlantilla() {
		EquipoRepositorioArchivo repositorio = new EquipoRepositorioArchivo(carpeta.resolve("equipos.csv").toString());
		EquipoFutbol leones = new EquipoFutbol("Leones FC", "4-3-3", 75, 40);
		leones.agregarJugador(new Jugador("Andrés Rincón", Posicion.DELANTERO));

		repositorio.guardarEquipo(leones);
		repositorio.guardarEquipo(new EquipoFutbol("Real Andino", "3-5-2", 68, 42));

		List<EquipoFutbol> equipos = repositorio.listaEquipos();
		assertEquals(2, equipos.size());
		EquipoFutbol cargado = repositorio.cargarEquipo("leones fc");
		assertEquals("4-3-3", cargado.getFormacion());
		assertEquals(75, cargado.getPoderOfensivoBase());
		assertEquals("Andrés Rincón", cargado.getListaJugadores().get(0).getNombre());
		assertEquals(Posicion.DELANTERO, cargado.getListaJugadores().get(0).getPosicion());
		assertNull(repositorio.cargarEquipo("No existe"));
	}

	@Test
	void guardarUnEquipoConElMismoNombreLoReemplaza() {
		EquipoRepositorioArchivo repositorio = new EquipoRepositorioArchivo(carpeta.resolve("equipos.csv").toString());
		repositorio.guardarEquipo(new EquipoFutbol("Leones FC", "4-3-3", 75, 40));
		repositorio.guardarEquipo(new EquipoFutbol("Leones FC", "4-4-2", 80, 45));

		assertEquals(1, repositorio.listaEquipos().size());
		assertEquals("4-4-2", repositorio.cargarEquipo("Leones FC").getFormacion());
	}

	@Test
	void rechazaNombresConLosSeparadoresDelArchivo() {
		EquipoRepositorioArchivo repositorio = new EquipoRepositorioArchivo(carpeta.resolve("equipos.csv").toString());

		assertThrows(IllegalArgumentException.class,
				() -> repositorio.guardarEquipo(new EquipoFutbol("Leones;FC", "4-3-3", 75, 40)));
	}

	@Test
	void guardaYCargaUnTorneoAMitadDeCamino() {
		Torneo torneo = new Torneo("Copa Bogotá 2026", new Random(5));
		torneo.agregarEquipo(new EquipoFutbol("Leones FC", "4-3-3", 75, 40));
		torneo.agregarEquipo(new EquipoFutbol("Real Andino", "3-5-2", 68, 42));
		torneo.agregarEquipo(new EquipoFutbol("Unión Caribe", "4-2-4", 66, 38));
		torneo.iniciar();
		torneo.jugarPartido();

		TorneoRepositorioArchivo repositorio = new TorneoRepositorioArchivo(carpeta.toString());
		repositorio.guardarTorneo(torneo);

		assertEquals(List.of("copa-bogota-2026"), repositorio.listaTorneos());
		Torneo cargado = repositorio.cargarTorneo("Copa Bogotá 2026");
		assertEquals(torneo.tablaComoTexto(), cargado.tablaComoTexto());
		assertEquals(1, cargado.getPartidosJugados().size());

		cargado.jugarTodos();
		assertEquals(EstadoTorneo.FINALIZADO, cargado.getEstado());
		assertNull(repositorio.cargarTorneo("Otro torneo"));
	}

	@Test
	void nombreSeguroQuitaTildesYSimbolos() {
		assertEquals("copa-bogota-2026", FileManager.nombreSeguro("  Copa Bogotá 2026!  "));
		assertEquals("sin-nombre", FileManager.nombreSeguro("¿?"));
	}

}
