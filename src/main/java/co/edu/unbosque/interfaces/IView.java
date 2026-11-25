package co.edu.unbosque.interfaces;

public interface IView {
	public static final int OPCION_SALIR = 0;
	public static final int OPCION_VOLVER = 9;
	
	String obtenerRespuestaUsuario();
	void mostrarMenuPrincipal();
	void limpiarConsola();
	void mostrarSubMenu();
	

}
