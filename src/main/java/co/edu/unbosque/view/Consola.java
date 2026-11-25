package co.edu.unbosque.view;

import java.util.ArrayList;
import java.util.Scanner;

import co.edu.unbosque.interfaces.IView;

public class Consola<E> implements IView{
	private Scanner consola = new Scanner(System.in);

	public String leerOpcion() {
		return consola.nextLine();
	}

	public void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}

	public void mostrarReportePartido(String partido) {
		System.out.println("Reporte Del Partido: " + partido);
	}

	public void mostrarEstadisticasPartido(ArrayList<E> estadisticas) {

		for (E stats : estadisticas) {

			System.out.println("""
					Estadisticas Partido:
					""" + stats.toString());
		}
	}

	@Override
	public String obtenerRespuestaUsuario() {
		String entradaUsuario = consola.nextLine();
		consola.nextLine();
		return entradaUsuario;
	}

	@Override
	public void mostrarMenuPrincipal() {
		System.out.println("""
				1. Opcion 1
				2. Opcion 2
				3. Opcion 3
				4. Opcion 4
				5. Opcion 5
				""");
	}

	@Override
	public void limpiarConsola() {
		consola.close();
		
	}

	@Override
	public void mostrarSubMenu() {
		// TODO Auto-generated method stub
		
	}
	
	

}
