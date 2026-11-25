package co.edu.unbosque.model;

import java.io.Serializable;

/**
 * Algo que pasó en un minuto del partido: una ocasión de gol o un gol.
 */
public class EventoPartido implements Serializable {
	private static final long serialVersionUID = 1L;

	private int minuto;
	private String descripcion;
	private boolean gol;

	public EventoPartido(int minuto, String descripcion, boolean gol) {
		this.minuto = minuto;
		this.descripcion = descripcion;
		this.gol = gol;
	}

	public int getMinuto() {
		return minuto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public boolean isGol() {
		return gol;
	}

	@Override
	public String toString() {
		return String.format("%2d' %s", minuto, descripcion);
	}
}
