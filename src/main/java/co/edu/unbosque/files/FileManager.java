package co.edu.unbosque.files;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class FileManager {

	/**
	 * Escribe (o reemplaza) un archivo de texto en UTF-8 y crea las carpetas que falten.
	 *
	 * @return true si se pudo escribir
	 */
	public boolean crearArchivo(String nombreArchivo, String contenidoArchivo) {
		Path ruta = Path.of(nombreArchivo);
		try {
			if (ruta.getParent() != null) {
				Files.createDirectories(ruta.getParent());
			}
			try (BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
				escritor.write(contenidoArchivo);
			}
			return true;
		} catch (IOException exception) {
			System.out.println("El Archivo No Se Pudo Crear: " + exception.getMessage());
			return false;
		}
	}

	/**
	 * @return el contenido del archivo o {@code null} si no existe o no se pudo leer
	 */
	public String leerArchivo(String nombreArchivo) {
		Path ruta = Path.of(nombreArchivo);
		if (!Files.exists(ruta)) {
			return null;
		}
		try {
			return Files.readString(ruta, StandardCharsets.UTF_8);
		} catch (IOException exception) {
			System.out.println("El Archivo No Se Pudo Leer: " + exception.getMessage());
			return null;
		}
	}

	/**
	 * Convierte un nombre en algo seguro para usar como nombre de archivo:
	 * "Copa Bogotá 2026" queda "copa-bogota-2026".
	 */
	public static String nombreSeguro(String nombre) {
		String sinTildes = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		String limpio = sinTildes.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
		return limpio.isEmpty() ? "sin-nombre" : limpio;
	}

}
