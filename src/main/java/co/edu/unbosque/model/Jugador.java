package co.edu.unbosque.model;

import java.io.Serializable;

/**
 * Jugador de un equipo. Se usa para saber quién anota los goles.
 */
public class Jugador implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;
	private Posicion posicion;
	private int goles;

	public Jugador(String nombre, Posicion posicion) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El jugador necesita un nombre");
		}
		this.nombre = nombre.trim();
		this.posicion = posicion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Posicion getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public int getGoles() {
		return goles;
	}

	public void anotarGol() {
		goles++;
	}

	@Override
	public String toString() {
		return nombre + " (" + posicion.name().toLowerCase() + ")";
	}
}
