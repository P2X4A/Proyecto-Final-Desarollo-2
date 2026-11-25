package co.edu.unbosque.controller;

import java.util.List;
import java.util.NoSuchElementException;

import co.edu.unbosque.constants.Constant;
import co.edu.unbosque.files.EquipoRepositorioArchivo;
import co.edu.unbosque.files.FileManager;
import co.edu.unbosque.files.IEquipoRepository;
import co.edu.unbosque.files.ITorneoRepository;
import co.edu.unbosque.files.TorneoRepositorioArchivo;
import co.edu.unbosque.interfaces.IView;
import co.edu.unbosque.model.Equipo;
import co.edu.unbosque.model.EquipoFutbol;
import co.edu.unbosque.model.EstadoTorneo;
import co.edu.unbosque.model.EventoPartido;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Partido;
import co.edu.unbosque.model.Posicion;
import co.edu.unbosque.model.Torneo;
import co.edu.unbosque.view.Consola;

/**
 * Conecta la consola con el modelo: lee la opción del menú, llama al modelo y muestra el
 * resultado. Los errores de validación se muestran como mensaje y el menú sigue.
 */
public class TorneoController {
	private final Consola consola;
	private final IEquipoRepository repositorioEquipos;
	private final ITorneoRepository repositorioTorneos;
	private final String carpetaReportes;
	private final FileManager archivos = new FileManager();
	private Torneo torneo;

	public TorneoController() {
		this(new Consola(), new EquipoRepositorioArchivo(Constant.ARCHIVO_EQUIPOS),
				new TorneoRepositorioArchivo(Constant.CARPETA_TORNEOS), Constant.CARPETA_REPORTES);
	}

	public TorneoController(Consola consola, IEquipoRepository repositorioEquipos,
			ITorneoRepository repositorioTorneos, String carpetaReportes) {
		this.consola = consola;
		this.repositorioEquipos = repositorioEquipos;
		this.repositorioTorneos = repositorioTorneos;
		this.carpetaReportes = carpetaReportes;
	}

	public void iniciar() {
		consola.mostrarMensaje("SIMULADOR DE TORNEO DE FÚTBOL");
		try {
			int opcion;
			do {
				consola.mostrarMenuPrincipal();
				opcion = consola.leerEntero("Elige una opción", 0, 9);
				consola.limpiarConsola();
				ejecutarConManejoDeErrores(opcion);
			} while (opcion != IView.OPCION_SALIR);
			consola.mostrarMensaje("¡Hasta pronto!");
		} catch (NoSuchElementException finDeLaEntrada) {
			consola.mostrarMensaje("Se terminó la entrada. ¡Hasta pronto!");
		}
	}

	private void ejecutarConManejoDeErrores(int opcion) {
		try {
			switch (opcion) {
			case 1 -> registrarEquipo();
			case 2 -> listarEquipos();
			case 3 -> agregarEquiposDeEjemplo();
			case 4 -> crearTorneo();
			case 5 -> jugarSiguientePartido();
			case 6 -> jugarTodos();
			case 7 -> mostrarTabla();
			case 8 -> exportarReporte();
			case 9 -> menuArchivos();
			default -> {
				// 0: salir, lo resuelve el ciclo del menú
			}
			}
		} catch (IllegalArgumentException | IllegalStateException error) {
			consola.mostrarMensaje("No se pudo: " + error.getMessage());
		}
	}

	private void registrarEquipo() {
		String nombre = consola.leerTexto("Nombre del equipo");
		EquipoRepositorioArchivo.validarTexto(nombre);
		String formacion = leerFormacion();
		int ataque = consola.leerEntero("Poder ofensivo base (" + Constant.PODER_OFENSIVO_MINIMO + "-"
				+ Constant.PODER_OFENSIVO_MAXIMO + ")", Constant.PODER_OFENSIVO_MINIMO, Constant.PODER_OFENSIVO_MAXIMO);
		int posesion = consola.leerEntero("Posesión base (" + Constant.POSESION_MINIMA + "-"
				+ Constant.POSESION_MAXIMA + ")", Constant.POSESION_MINIMA, Constant.POSESION_MAXIMA);
		EquipoFutbol equipo = new EquipoFutbol(nombre, formacion, ataque, posesion);

		int cantidad = consola.leerEntero("¿Cuántos jugadores quieres registrar? (0-" + Constant.MAXIMO_JUGADORES
				+ ")", 0, Constant.MAXIMO_JUGADORES);
		for (int i = 1; i <= cantidad; i++) {
			String nombreJugador = consola.leerTexto("Nombre del jugador " + i);
			EquipoRepositorioArchivo.validarTexto(nombreJugador);
			int posicion = consola.leerEntero("Posición (1 Arquero, 2 Defensa, 3 Mediocampista, 4 Delantero)", 1, 4);
			equipo.agregarJugador(new Jugador(nombreJugador, Posicion.values()[posicion - 1]));
		}

		boolean existia = repositorioEquipos.cargarEquipo(nombre) != null;
		repositorioEquipos.guardarEquipo(equipo);
		consola.mostrarMensaje((existia ? "Se actualizó " : "Se registró ") + equipo);
	}

	private String leerFormacion() {
		while (true) {
			String formacion = consola.leerTexto("Formación (por ejemplo 4-3-3)");
			try {
				Equipo.validarFormacion(formacion);
				return formacion;
			} catch (IllegalArgumentException formacionInvalida) {
				consola.mostrarMensaje(formacionInvalida.getMessage());
			}
		}
	}

	private void listarEquipos() {
		List<EquipoFutbol> equipos = repositorioEquipos.listaEquipos();
		if (equipos.isEmpty()) {
			consola.mostrarMensaje("No hay equipos registrados. Usa la opción 3 para agregar equipos de ejemplo.");
			return;
		}
		consola.mostrarMensaje("Equipos registrados (" + equipos.size() + "):");
		for (EquipoFutbol equipo : equipos) {
			consola.mostrarMensaje("  - " + equipo);
		}
	}

	private void agregarEquiposDeEjemplo() {
		int agregados = 0;
		for (EquipoFutbol equipo : EquiposDeEjemplo.crear()) {
			if (repositorioEquipos.cargarEquipo(equipo.getNombre()) == null) {
				repositorioEquipos.guardarEquipo(equipo);
				agregados++;
			}
		}
		consola.mostrarMensaje(agregados == 0 ? "Los equipos de ejemplo ya estaban registrados."
				: "Se agregaron " + agregados + " equipos de ejemplo.");
	}

	private void crearTorneo() {
		List<EquipoFutbol> equipos = repositorioEquipos.listaEquipos();
		if (equipos.size() < 2) {
			throw new IllegalStateException("registra al menos 2 equipos (o usa la opción 3)");
		}
		if (torneo != null && torneo.getEstado() == EstadoTorneo.EN_CURSO) {
			consola.mostrarMensaje("El torneo \"" + torneo.getNombre() + "\" queda reemplazado (guárdalo con la opción 9 si lo quieres).");
		}

		Torneo nuevo = new Torneo(consola.leerTexto("Nombre del torneo"));
		for (EquipoFutbol equipo : equipos) {
			nuevo.agregarEquipo(equipo);
		}
		nuevo.iniciar();
		torneo = nuevo;
		consola.mostrarMensaje("Torneo \"" + torneo.getNombre() + "\" creado con " + equipos.size() + " equipos y "
				+ torneo.getPartidos().size() + " partidos.");
	}

	private void jugarSiguientePartido() {
		Partido partido = torneoActual().jugarPartido();
		consola.mostrarMensaje(partido.getEquipo1().getNombre() + " vs " + partido.getEquipo2().getNombre());
		for (EventoPartido evento : partido.getEventos()) {
			consola.mostrarMensaje("  " + evento);
		}
		consola.mostrarReportePartido(partido.getMarcador() + " · ocasiones de gol " + partido.getAccionesGolEquipo1()
				+ " - " + partido.getAccionesGolEquipo2());
		anunciarCampeonSiTermino();
	}

	private void jugarTodos() {
		List<Partido> jugados = torneoActual().jugarTodos();
		consola.mostrarMensaje("Se jugaron " + jugados.size() + " partidos:");
		for (Partido partido : jugados) {
			consola.mostrarMensaje("  " + partido.getMarcador());
		}
		consola.limpiarConsola();
		consola.mostrarMensaje(torneo.tablaComoTexto());
		anunciarCampeonSiTermino();
	}

	private void mostrarTabla() {
		Torneo actual = torneoActual();
		consola.mostrarMensaje(actual.getNombre() + " · " + actual.getPartidosJugados().size() + " de "
				+ actual.getPartidos().size() + " partidos jugados");
		consola.mostrarMensaje(actual.tablaComoTexto());
	}

	private void exportarReporte() {
		Torneo actual = torneoActual();
		String ruta = carpetaReportes + "/" + FileManager.nombreSeguro(actual.getNombre()) + ".txt";
		if (!archivos.crearArchivo(ruta, actual.generarReporte())) {
			throw new IllegalStateException("no se pudo escribir " + ruta);
		}
		consola.mostrarMensaje("Reporte guardado en " + ruta);
	}

	private void menuArchivos() {
		int opcion;
		do {
			consola.mostrarSubMenu();
			opcion = consola.leerEntero("Elige una opción", 1, IView.OPCION_VOLVER);
			consola.limpiarConsola();
			try {
				switch (opcion) {
				case 1 -> guardarTorneo();
				case 2 -> cargarTorneo();
				case 3 -> listarTorneosGuardados();
				case IView.OPCION_VOLVER -> {
					// vuelve al menú principal
				}
				default -> consola.mostrarMensaje("Opción no válida.");
				}
			} catch (IllegalArgumentException | IllegalStateException error) {
				consola.mostrarMensaje("No se pudo: " + error.getMessage());
			}
		} while (opcion != IView.OPCION_VOLVER);
	}

	private void guardarTorneo() {
		repositorioTorneos.guardarTorneo(torneoActual());
		consola.mostrarMensaje("Se guardó \"" + torneo.getNombre() + "\".");
	}

	private void cargarTorneo() {
		if (repositorioTorneos.listaTorneos().isEmpty()) {
			consola.mostrarMensaje("Todavía no hay torneos guardados.");
			return;
		}
		listarTorneosGuardados();
		String nombre = consola.leerTexto("Nombre del torneo a cargar");
		Torneo cargado = repositorioTorneos.cargarTorneo(nombre);
		if (cargado == null) {
			consola.mostrarMensaje("No hay un torneo guardado con ese nombre.");
			return;
		}
		torneo = cargado;
		consola.mostrarMensaje("Se cargó \"" + torneo.getNombre() + "\": " + torneo.getPartidosJugados().size()
				+ " de " + torneo.getPartidos().size() + " partidos jugados.");
	}

	private void listarTorneosGuardados() {
		List<String> nombres = repositorioTorneos.listaTorneos();
		if (nombres.isEmpty()) {
			consola.mostrarMensaje("Todavía no hay torneos guardados.");
			return;
		}
		consola.mostrarMensaje("Torneos guardados:");
		for (String nombre : nombres) {
			consola.mostrarMensaje("  - " + nombre);
		}
	}

	private void anunciarCampeonSiTermino() {
		if (torneo.getEstado() == EstadoTorneo.FINALIZADO) {
			consola.mostrarMensaje("Terminó el torneo. CAMPEÓN: " + torneo.obtenerCampeon());
		}
	}

	private Torneo torneoActual() {
		if (torneo == null) {
			throw new IllegalStateException("primero crea un torneo (opción 4) o carga uno (opción 9)");
		}
		return torneo;
	}

}
