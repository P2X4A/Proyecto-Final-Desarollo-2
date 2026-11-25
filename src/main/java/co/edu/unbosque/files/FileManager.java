package co.edu.unbosque.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	public  boolean crearArchivo (String nombreArchivo, String contenidoArchivo) {
		try {
			File archivo = new File(nombreArchivo);
			FileWriter archivoEscritor = new FileWriter(archivo);
			BufferedWriter archivoEscritorEnBloques = new BufferedWriter(archivoEscritor);
		
			archivoEscritorEnBloques.write(contenidoArchivo);
			archivoEscritorEnBloques.flush();
			archivoEscritorEnBloques.close();
			archivoEscritor.close();
			return true;
		} catch (IOException exception) {
			System.out.println("El Archivo No Se Pudo Crear" + exception.getMessage());
			return false;
		}
	}

}
