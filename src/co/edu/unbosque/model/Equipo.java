package co.edu.unbosque.model;

public abstract class Equipo {
	private String nombre;
	private String formacion;
	private int poderOfensivoBase;
	private int posesionBase;
	private int partidosJugados;
	private int partidosGanados;
	private int partidosPerdidos;
	private int partidosEmpatados;
	
	public Equipo(String nombre, String formacion, int poderOfensivoBase, int posesionBase, int partidosJugados,
			int partidosGanados, int partidosPerdidos, int partidosEmpatados) {
		super();
		this.nombre = nombre;
		this.formacion = formacion;
		this.poderOfensivoBase = poderOfensivoBase;
		this.posesionBase = posesionBase;
		this.partidosJugados = partidosJugados;
		this.partidosGanados = partidosGanados;
		this.partidosPerdidos = partidosPerdidos;
		this.partidosEmpatados = partidosEmpatados;
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
	
	private int calcularPoderOfensivo() {
		
		
		return 0;
	}
	
	private int calcularPosesion() {
		
	}

}
