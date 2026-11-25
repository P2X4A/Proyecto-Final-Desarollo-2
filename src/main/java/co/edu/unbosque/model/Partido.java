package co.edu.unbosque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Partido entre dos equipos que se simula minuto a minuto.
 *
 * En cada minuto se decide qué equipo tiene el balón según su posesión. Ese equipo puede
 * generar una ocasión de gol según su poder ofensivo, y la ocasión puede terminar en gol.
 */
public class Partido implements ISimulacion, Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MINUTOS = 90;
	/** Con 900, un ataque de 90 genera una ocasión en el 10 % de los minutos con balón. */
	private static final double DIVISOR_OCASION = 900.0;
	private static final double PROBABILIDAD_GOL = 0.3;

	private EquipoFutbol equipo1;
	private EquipoFutbol equipo2;
	private int golesEquipo1;
	private int golesEquipo2;
	private int accionesGolEquipo1;
	private int accionesGolEquipo2;
	private List<EventoPartido> eventos = new ArrayList<>();
	private EstadoPartido estado = EstadoPartido.PENDIENTE;
	private Random azar;

	public Partido(EquipoFutbol equipo1, EquipoFutbol equipo2) {
		this(equipo1, equipo2, new Random());
	}

	/**
	 * @param azar generador de números aleatorios; con una semilla fija el partido siempre
	 *             da el mismo resultado, lo que sirve para las pruebas
	 */
	public Partido(EquipoFutbol equipo1, EquipoFutbol equipo2, Random azar) {
		if (equipo1 == equipo2) {
			throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo");
		}
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.azar = azar;
	}

	@Override
	public EventoPartido simularMinutoPartido(int minuto) {
		int posesion1 = equipo1.calcularPosesion();
		int posesion2 = equipo2.calcularPosesion();
		boolean atacaEquipo1 = azar.nextInt(posesion1 + posesion2) < posesion1;
		EquipoFutbol atacante = atacaEquipo1 ? equipo1 : equipo2;

		double probabilidadOcasion = atacante.calcularPoderOfensivo() / DIVISOR_OCASION;
		if (azar.nextDouble() >= probabilidadOcasion) {
			return null;
		}

		if (atacaEquipo1) {
			accionesGolEquipo1++;
		} else {
			accionesGolEquipo2++;
		}

		Jugador autor = elegirAutor(atacante);
		boolean esGol = azar.nextDouble() < PROBABILIDAD_GOL;
		String descripcion;

		if (esGol) {
			if (atacaEquipo1) {
				golesEquipo1++;
			} else {
				golesEquipo2++;
			}
			descripcion = "¡Gol de " + atacante.getNombre() + "!";
			if (autor != null) {
				autor.anotarGol();
				descripcion += " Anota " + autor.getNombre() + ".";
			}
		} else {
			descripcion = "Ocasión de " + atacante.getNombre()
					+ (autor != null ? ": " + autor.getNombre() + " remata y no entra." : ", pero no entra.");
		}

		EventoPartido evento = new EventoPartido(minuto, descripcion, esGol);
		eventos.add(evento);
		return evento;
	}

	/**
	 * Escoge quién remata: primero entre los delanteros, si no hay entre los mediocampistas
	 * y si tampoco hay, entre cualquier jugador. Devuelve null si el equipo no tiene plantilla.
	 */
	private Jugador elegirAutor(EquipoFutbol equipo) {
		List<Jugador> candidatos = filtrar(equipo, Posicion.DELANTERO);
		if (candidatos.isEmpty()) {
			candidatos = filtrar(equipo, Posicion.MEDIOCAMPISTA);
		}
		if (candidatos.isEmpty()) {
			candidatos = equipo.getListaJugadores();
		}
		return candidatos.isEmpty() ? null : candidatos.get(azar.nextInt(candidatos.size()));
	}

	private List<Jugador> filtrar(EquipoFutbol equipo, Posicion posicion) {
		List<Jugador> resultado = new ArrayList<>();
		for (Jugador jugador : equipo.getListaJugadores()) {
			if (jugador.getPosicion() == posicion) {
				resultado.add(jugador);
			}
		}
		return resultado;
	}

	@Override
	public int obtenerAccionesGol() {
		return accionesGolEquipo1 + accionesGolEquipo2;
	}

	/**
	 * Juega los 90 minutos y actualiza las estadísticas de los dos equipos.
	 */
	public void simularPartido() {
		if (estado != EstadoPartido.PENDIENTE) {
			throw new IllegalStateException("Este partido ya se jugó");
		}
		estado = EstadoPartido.EN_CURSO;
		for (int minuto = 1; minuto <= MINUTOS; minuto++) {
			simularMinutoPartido(minuto);
		}
		estado = EstadoPartido.FINALIZADO;

		equipo1.registrarResultado(golesEquipo1, golesEquipo2);
		equipo2.registrarResultado(golesEquipo2, golesEquipo1);
		equipo1.registrarPartido(this);
		equipo2.registrarPartido(this);
	}

	/**
	 * @return el equipo ganador o {@code null} si hubo empate
	 */
	public EquipoFutbol saberGanador() {
		if (estado != EstadoPartido.FINALIZADO) {
			throw new IllegalStateException("El partido todavía no se ha jugado");
		}
		if (golesEquipo1 == golesEquipo2) {
			return null;
		}
		return golesEquipo1 > golesEquipo2 ? equipo1 : equipo2;
	}

	public String getMarcador() {
		return equipo1.getNombre() + " " + golesEquipo1 + " - " + golesEquipo2 + " " + equipo2.getNombre();
	}

	public EquipoFutbol getEquipo1() {
		return equipo1;
	}

	public EquipoFutbol getEquipo2() {
		return equipo2;
	}

	public int getGolesEquipo1() {
		return golesEquipo1;
	}

	public int getGolesEquipo2() {
		return golesEquipo2;
	}

	public int getAccionesGolEquipo1() {
		return accionesGolEquipo1;
	}

	public int getAccionesGolEquipo2() {
		return accionesGolEquipo2;
	}

	public List<EventoPartido> getEventos() {
		return Collections.unmodifiableList(eventos);
	}

	public EstadoPartido getEstado() {
		return estado;
	}

	@Override
	public String toString() {
		return estado == EstadoPartido.FINALIZADO ? getMarcador()
				: equipo1.getNombre() + " vs " + equipo2.getNombre();
	}

}
