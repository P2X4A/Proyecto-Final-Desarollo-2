package co.edu.unbosque.view;

import java.util.ArrayList;
import java.util.Scanner;

public class Consola <E> {
	private Scanner consola = new Scanner(System.in);
	
	public int mostrarMenuPrincipal () {
		System.out.println("""
				1. Opcion 1
				2. Opcion 2
				3. Opcion 3
				4. Opcion 5
				5. Opcion 6
				6. Opcion 7
				7. Opcion 8
				""");
		return consola.nextInt();
	}
	
	public int pedirOpcion() {
		System.out.println("Ingresar Opcion A Realizar: ");
		return consola.nextInt();
	}
	
	public void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	public void mostrarEstadisticasPartido(ArrayList<E> estadisticas) {

		for (E stats : estadisticas) {

			System.out.println("""
					Estadisticas Partido:
					""" + stats.toString());
		}
	}
	
}
