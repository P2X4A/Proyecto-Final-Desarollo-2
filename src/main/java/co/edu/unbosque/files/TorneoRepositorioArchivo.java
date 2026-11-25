package co.edu.unbosque.files;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import co.edu.unbosque.model.Torneo;

/**
 * Guarda cada torneo completo (equipos, partidos, eventos y estado) en un archivo binario
 * con serialización de Java, para poder retomarlo después.
 */
public class TorneoRepositorioArchivo implements ITorneoRepository {
	private static final String EXTENSION = ".dat";

	private final Path carpeta;

	public TorneoRepositorioArchivo(String carpeta) {
		this.carpeta = Path.of(carpeta);
	}

	@Override
	public void guardarTorneo(Torneo torneo) {
		try {
			Files.createDirectories(carpeta);
			try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(rutaDe(torneo.getNombre())))) {
				salida.writeObject(torneo);
			}
		} catch (IOException exception) {
			throw new IllegalStateException("No se pudo guardar el torneo: " + exception.getMessage(), exception);
		}
	}

	/**
	 * @return el torneo guardado o {@code null} si no hay uno con ese nombre
	 */
	@Override
	public Torneo cargarTorneo(String nombre) {
		Path ruta = rutaDe(nombre);
		if (!Files.exists(ruta)) {
			return null;
		}
		try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ruta))) {
			return (Torneo) entrada.readObject();
		} catch (IOException | ClassNotFoundException exception) {
			throw new IllegalStateException("No se pudo leer el torneo: " + exception.getMessage(), exception);
		}
	}

	/**
	 * @return los nombres de archivo (sin extensión) de los torneos guardados
	 */
	@Override
	public List<String> listaTorneos() {
		List<String> nombres = new ArrayList<>();
		if (!Files.isDirectory(carpeta)) {
			return nombres;
		}
		try (Stream<Path> archivos = Files.list(carpeta)) {
			archivos.map(ruta -> ruta.getFileName().toString())
					.filter(nombre -> nombre.endsWith(EXTENSION))
					.map(nombre -> nombre.substring(0, nombre.length() - EXTENSION.length()))
					.sorted()
					.forEach(nombres::add);
		} catch (IOException exception) {
			throw new IllegalStateException("No se pudo leer la carpeta de torneos: " + exception.getMessage(), exception);
		}
		return nombres;
	}

	private Path rutaDe(String nombreTorneo) {
		return carpeta.resolve(FileManager.nombreSeguro(nombreTorneo) + EXTENSION);
	}

}
