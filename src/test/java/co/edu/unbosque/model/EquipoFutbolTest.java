package co.edu.unbosque.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EquipoFutbolTest {

	@Test
	void calculaAtaqueYPosesionSegunLaFormacion() {
		EquipoFutbol equipo = new EquipoFutbol("Leones FC", "4-3-3", 70, 40);

		assertEquals(70 + 3 * 5, equipo.calcularPoderOfensivo());
		assertEquals(40 + 3 * 5, equipo.calcularPosesion());
	}

	@ParameterizedTest
	@ValueSource(strings = { "4-4-3", "433", "4-3", "a-b-c", "" })
	void rechazaFormacionesInvalidas(String formacion) {
		assertThrows(IllegalArgumentException.class, () -> new EquipoFutbol("Leones FC", formacion, 70, 40));
	}

	@Test
	void sumaPuntosYGolesConCadaResultado() {
		EquipoFutbol equipo = new EquipoFutbol("Leones FC", "4-4-2", 70, 40);

		equipo.registrarResultado(3, 1); // gana
		equipo.registrarResultado(2, 2); // empata
		equipo.registrarResultado(0, 1); // pierde

		Map<String, Object> estadisticas = equipo.getEstadisticas();
		assertEquals(List.of("PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts"), List.copyOf(estadisticas.keySet()));
		assertEquals(3, estadisticas.get("PJ"));
		assertEquals(1, estadisticas.get("PG"));
		assertEquals(1, estadisticas.get("PE"));
		assertEquals(1, estadisticas.get("PP"));
		assertEquals(5, estadisticas.get("GF"));
		assertEquals(4, estadisticas.get("GC"));
		assertEquals(1, estadisticas.get("DG"));
		assertEquals(4, estadisticas.get("Pts"));
	}

}
