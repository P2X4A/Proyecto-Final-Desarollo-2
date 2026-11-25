package co.edu.unbosque.model;

/**
 * Lo que necesita un partido para simularse minuto a minuto.
 */
public interface ISimulacion {
	/**
	 * Simula un minuto del partido.
	 *
	 * @return el evento ocurrido o {@code null} si en ese minuto no pasó nada importante
	 */
	EventoPartido simularMinutoPartido(int minuto);

	/**
	 * @return cuántas ocasiones de gol hubo en el partido, sumando ambos equipos
	 */
	int obtenerAccionesGol();
}
