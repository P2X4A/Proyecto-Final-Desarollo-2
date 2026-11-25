package co.edu.unbosque.view;

import java.io.PrintStream;
import java.util.Scanner;

import co.edu.unbosque.interfaces.IView;

/**
 * Vista por consola: muestra menús y mensajes y lee lo que escribe el usuario.
 * No conoce el modelo; el controlador le pasa el texto ya armado.
 */
public class Consola implements IView {
	private final Scanner consola;
	private final PrintStream salida;

	public Consola() {
		this(new Scanner(System.in), System.out);
	}

	/**
	 * Permite usar otra entrada y salida, por ejemplo en las pruebas.
	 */
	public Consola(Scanner consola, PrintStream salida) {
		this.consola = consola;
		this.salida = salida;
	}

	public void mostrarMensaje(String mensaje) {
		salida.println(mensaje);
	}

	public void mostrarReportePartido(String partido) {
		salida.println("Reporte Del Partido: " + partido);
	}

	/**
	 * Pregunta hasta que el usuario escriba algo.
	 */
	public String leerTexto(String pregunta) {
		while (true) {
			salida.print(pregunta + ": ");
			String texto = obtenerRespuestaUsuario();
			if (!texto.isBlank()) {
				return texto;
			}
			salida.println("Escribe algo para continuar.");
		}
	}

	/**
	 * Pregunta hasta que el usuario escriba un número entero entre {@code minimo} y {@code maximo}.
	 */
	public int leerEntero(String pregunta, int minimo, int maximo) {
		while (true) {
			salida.print(pregunta + ": ");
			String texto = obtenerRespuestaUsuario();
			try {
				int numero = Integer.parseInt(texto);
				if (numero >= minimo && numero <= maximo) {
					return numero;
				}
			} catch (NumberFormatException noEsNumero) {
				// Se vuelve a preguntar abajo.
			}
			salida.println("Escribe un número entre " + minimo + " y " + maximo + ".");
		}
	}

	/**
	 * Lee una línea. Si la entrada se acaba (por ejemplo, al redirigir un archivo), lanza
	 * {@link java.util.NoSuchElementException} y el controlador termina el programa.
	 */
	@Override
	public String obtenerRespuestaUsuario() {
		return consola.nextLine().trim();
	}

	@Override
	public void mostrarMenuPrincipal() {
		salida.println("""

				========== MENÚ PRINCIPAL ==========
				1. Registrar equipo
				2. Ver equipos registrados
				3. Agregar equipos de ejemplo
				4. Crear torneo con los equipos registrados
				5. Jugar el siguiente partido
				6. Jugar todos los partidos pendientes
				7. Ver tabla de posiciones
				8. Exportar reporte del torneo (.txt)
				9. Guardar o cargar torneos
				0. Salir""");
	}

	@Override
	public void limpiarConsola() {
		salida.println();
	}

	@Override
	public void mostrarSubMenu() {
		salida.println("""

				---------- TORNEOS GUARDADOS ----------
				1. Guardar el torneo actual
				2. Cargar un torneo guardado
				3. Ver torneos guardados
				9. Volver""");
	}

}
