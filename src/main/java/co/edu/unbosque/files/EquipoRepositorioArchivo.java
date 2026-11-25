package co.edu.unbosque.files;

import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.model.EquipoFutbol;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Posicion;

/**
 * Guarda los equipos registrados en un archivo de texto, un equipo por línea:
 *
 * <pre>
 * nombre;formación;poder ofensivo base;posesión base;jugador:POSICION|jugador:POSICION
 * Leones FC;4-3-3;75;40;Andrés Rincón:DELANTERO|Felipe Mora:MEDIOCAMPISTA
 * </pre>
 *
 * Solo se guardan los datos del equipo, no las estadísticas de un torneo.
 */
public class EquipoRepositorioArchivo implements IEquipoRepository {
	private static final String SEPARADOR_CAMPOS = ";";
	private static final String SEPARADOR_JUGADORES = "|";
	private static final String SEPARADOR_POSICION = ":";

	private final String rutaArchivo;
	private final FileManager archivos = new FileManager();

	public EquipoRepositorioArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	/**
	 * Agrega el equipo o reemplaza al que tenga el mismo nombre.
	 */
	@Override
	public void guardarEquipo(EquipoFutbol equipoFutbol) {
		validarTexto(equipoFutbol.getNombre());
		for (Jugador jugador : equipoFutbol.getListaJugadores()) {
			validarTexto(jugador.getNombre());
		}

		List<EquipoFutbol> equipos = listaEquipos();
		equipos.removeIf(e -> e.getNombre().equalsIgnoreCase(equipoFutbol.getNombre()));
		equipos.add(equipoFutbol);

		StringBuilder contenido = new StringBuilder();
		for (EquipoFutbol equipo : equipos) {
			contenido.append(aLinea(equipo)).append(System.lineSeparator());
		}
		if (!archivos.crearArchivo(rutaArchivo, contenido.toString())) {
			throw new IllegalStateException("No se pudo guardar el archivo de equipos " + rutaArchivo);
		}
	}

	@Override
	public EquipoFutbol cargarEquipo(String nombre) {
		for (EquipoFutbol equipo : listaEquipos()) {
			if (equipo.getNombre().equalsIgnoreCase(nombre.trim())) {
				return equipo;
			}
		}
		return null;
	}

	@Override
	public List<EquipoFutbol> listaEquipos() {
		List<EquipoFutbol> equipos = new ArrayList<>();
		String contenido = archivos.leerArchivo(rutaArchivo);
		if (contenido == null) {
			return equipos;
		}
		for (String linea : contenido.split("\\R")) {
			if (!linea.isBlank()) {
				equipos.add(deLinea(linea));
			}
		}
		return equipos;
	}

	private String aLinea(EquipoFutbol equipo) {
		List<String> jugadores = new ArrayList<>();
		for (Jugador jugador : equipo.getListaJugadores()) {
			jugadores.add(jugador.getNombre() + SEPARADOR_POSICION + jugador.getPosicion().name());
		}
		return String.join(SEPARADOR_CAMPOS, equipo.getNombre(), equipo.getFormacion(),
				String.valueOf(equipo.getPoderOfensivoBase()), String.valueOf(equipo.getPosesionBase()),
				String.join(SEPARADOR_JUGADORES, jugadores));
	}

	private EquipoFutbol deLinea(String linea) {
		String[] campos = linea.split(SEPARADOR_CAMPOS, -1);
		if (campos.length < 4) {
			throw new IllegalStateException("Línea de equipo mal formada en " + rutaArchivo + ": " + linea);
		}
		EquipoFutbol equipo = new EquipoFutbol(campos[0], campos[1], Integer.parseInt(campos[2].trim()),
				Integer.parseInt(campos[3].trim()));
		if (campos.length > 4 && !campos[4].isBlank()) {
			for (String datosJugador : campos[4].split("\\" + SEPARADOR_JUGADORES)) {
				String[] partes = datosJugador.split(SEPARADOR_POSICION);
				equipo.agregarJugador(new Jugador(partes[0], Posicion.valueOf(partes[1])));
			}
		}
		return equipo;
	}

	/**
	 * Los nombres no pueden llevar los caracteres que separan los campos del archivo.
	 */
	public static void validarTexto(String texto) {
		if (texto.contains(SEPARADOR_CAMPOS) || texto.contains(SEPARADOR_JUGADORES)
				|| texto.contains(SEPARADOR_POSICION)) {
			throw new IllegalArgumentException("Los nombres no pueden contener ; | ni :");
		}
	}

}
