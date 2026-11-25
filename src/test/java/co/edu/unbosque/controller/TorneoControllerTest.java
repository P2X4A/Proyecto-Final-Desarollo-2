package co.edu.unbosque.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.unbosque.files.EquipoRepositorioArchivo;
import co.edu.unbosque.files.TorneoRepositorioArchivo;
import co.edu.unbosque.view.Consola;

/**
 * Recorre el menú como lo haría un usuario y revisa lo que se muestra y los archivos creados.
 */
class TorneoControllerTest {

	@TempDir
	Path carpeta;

	private String ejecutar(String entrada) {
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		Consola consola = new Consola(new Scanner(entrada), new PrintStream(salida, true, StandardCharsets.UTF_8));
		new TorneoController(consola, new EquipoRepositorioArchivo(carpeta.resolve("equipos.csv").toString()),
				new TorneoRepositorioArchivo(carpeta.resolve("torneos").toString()),
				carpeta.resolve("reportes").toString()).iniciar();
		return salida.toString(StandardCharsets.UTF_8);
	}

	@Test
	void juegaUnTorneoCompletoDesdeElMenu() {
		String salida = ejecutar(String.join("\n", "3", "4", "Copa de prueba", "6", "8", "9", "1", "9", "0") + "\n");

		assertTrue(salida.contains("Se agregaron 6 equipos de ejemplo."));
		assertTrue(salida.contains("creado con 6 equipos y 15 partidos"));
		assertTrue(salida.contains("Se jugaron 15 partidos"));
		assertTrue(salida.contains("CAMPEÓN:"));
		assertTrue(Files.exists(carpeta.resolve("reportes/copa-de-prueba.txt")));
		assertTrue(Files.exists(carpeta.resolve("torneos/copa-de-prueba.dat")));
		assertTrue(salida.endsWith("¡Hasta pronto!" + System.lineSeparator()));
	}

	@Test
	void registraUnEquipoYVuelveAPreguntarSiLaFormacionEsInvalida() {
		String salida = ejecutar(String.join("\n", "1", "Tigres", "4-4-4", "4-4-2", "100", "70", "40", "1",
				"Luis Pérez", "4", "2", "0") + "\n");

		assertTrue(salida.contains("La formación debe sumar 10 jugadores de campo"));
		assertTrue(salida.contains("Escribe un número entre 50 y 90."));
		assertTrue(salida.contains("Se registró Tigres (4-4-2)"));
		assertTrue(salida.contains("Equipos registrados (1):"));
	}

	@Test
	void avisaSiSeIntentaJugarSinTorneo() {
		String salida = ejecutar("5\n0\n");

		assertTrue(salida.contains("No se pudo: primero crea un torneo"));
	}

	@Test
	void terminaSinErrorSiSeAcabaLaEntrada() {
		String salida = ejecutar("2\n");

		assertTrue(salida.contains("Se terminó la entrada."));
	}

}
