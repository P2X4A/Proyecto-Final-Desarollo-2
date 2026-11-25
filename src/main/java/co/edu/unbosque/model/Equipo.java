package co.edu.unbosque.model;

import java.io.Serializable;

/**
 * Datos comunes de cualquier equipo: formación, fuerza de base y estadísticas del torneo.
 */
public abstract class Equipo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String formacion;
	private int poderOfensivoBase;
	private int posesionBase;
	private int partidosJugados;
	private int partidosGanados;
	private int partidosPerdidos;
	private int partidosEmpatados;
	private int golesFavor;
	private int golesContra;

	public Equipo(String nombre, String formacion, int poderOfensivoBase, int posesionBase, int partidosJugados,
			int partidosGanados, int partidosPerdidos, int partidosEmpatados) {
		super();
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El equipo necesita un nombre");
		}
		validarFormacion(formacion);
		this.nombre = nombre.trim();
		this.formacion = formacion;
		this.poderOfensivoBase = poderOfensivoBase;
		this.posesionBase = posesionBase;
		this.partidosJugados = partidosJugados;
		this.partidosGanados = partidosGanados;
		this.partidosPerdidos = partidosPerdidos;
		this.partidosEmpatados = partidosEmpatados;
	}

	/**
	 * Crea un equipo sin partidos jugados.
	 */
	public Equipo(String nombre, String formacion, int poderOfensivoBase, int posesionBase) {
		this(nombre, formacion, poderOfensivoBase, posesionBase, 0, 0, 0, 0);
	}

	/**
	 * La formación tiene tres líneas (defensas-mediocampistas-delanteros) que suman los 10
	 * jugadores de campo, por ejemplo 4-3-3 o 4-4-2.
	 */
	public static void validarFormacion(String formacion) {
		if (formacion == null || !formacion.matches("[0-9]-[0-9]-[0-9]")) {
			throw new IllegalArgumentException("La formación debe tener el formato D-M-A, por ejemplo 4-3-3");
		}
		int jugadoresDeCampo = 0;
		for (String linea : formacion.split("-")) {
			jugadoresDeCampo += Integer.parseInt(linea);
		}
		if (jugadoresDeCampo != 10) {
			throw new IllegalArgumentException("La formación debe sumar 10 jugadores de campo, por ejemplo 4-3-3");
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFormacion() {
		return formacion;
	}

	public void setFormacion(String formacion) {
		validarFormacion(formacion);
		this.formacion = formacion;
	}

	public int getPoderOfensivoBase() {
		return poderOfensivoBase;
	}

	public void setPoderOfensivoBase(int poderOfensivoBase) {
		this.poderOfensivoBase = poderOfensivoBase;
	}

	public int getPosesionBase() {
		return posesionBase;
	}

	public void setPosesionBase(int posesionBase) {
		this.posesionBase = posesionBase;
	}

	public int getPartidosJugados() {
		return partidosJugados;
	}

	public void setPartidosJugados(int partidosJugados) {
		this.partidosJugados = partidosJugados;
	}

	public int getPartidosGanados() {
		return partidosGanados;
	}

	public void setPartidosGanados(int partidosGanados) {
		this.partidosGanados = partidosGanados;
	}

	public int getPartidosPerdidos() {
		return partidosPerdidos;
	}

	public void setPartidosPerdidos(int partidosPerdidos) {
		this.partidosPerdidos = partidosPerdidos;
	}

	public int getPartidosEmpatados() {
		return partidosEmpatados;
	}

	public void setPartidosEmpatados(int partidosEmpatados) {
		this.partidosEmpatados = partidosEmpatados;
	}

	public int getGolesFavor() {
		return golesFavor;
	}

	public int getGolesContra() {
		return golesContra;
	}

	public int getDiferenciaGoles() {
		return golesFavor - golesContra;
	}

	/**
	 * Tres puntos por victoria y uno por empate.
	 */
	public int getPuntos() {
		return partidosGanados * 3 + partidosEmpatados;
	}

	/**
	 * Cada delantero suma 5 puntos de ataque al poder ofensivo base.
	 */
	public int calcularPoderOfensivo() {
		String[] partes = formacion.split("-");
		int delanteros = Integer.parseInt(partes[2]);
		return poderOfensivoBase + (delanteros * 5);
	}

	/**
	 * Cada mediocampista suma 5 puntos a la posesión base.
	 */
	public int calcularPosesion() {
		String[] partes = formacion.split("-");
		int mediocampistas = Integer.parseInt(partes[1]);
		return posesionBase + (mediocampistas * 5);
	}

	/**
	 * Suma un partido jugado con su marcador a las estadísticas del equipo.
	 */
	public void registrarResultado(int golesFavor, int golesContra) {
		partidosJugados++;
		this.golesFavor += golesFavor;
		this.golesContra += golesContra;

		if (golesFavor > golesContra) {
			partidosGanados++;
		} else if (golesFavor < golesContra) {
			partidosPerdidos++;
		} else {
			partidosEmpatados++;
		}
	}

}
