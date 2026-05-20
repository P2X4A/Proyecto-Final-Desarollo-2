package co.edu.unbosque.model;

import java.util.List;

public class Partido implements ISimulacion{

	private EquipoFutbol equipo1;
	private EquipoFutbol equipo2;
	private int golesEquipo1;
	private int golesEquipo2;
	private int accionesGolEquipo1;
	private int accionesGolEquipo2;
	private List <EventoPartido> eventos;
	@Override
	public EventoPartido simularMinutoPartido(int minuto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtenerAccionesGol() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void simularPartido() {
		
	}
	
	public EquipoFutbol saberGanador() {
		
		return equipo1;
	};

}
