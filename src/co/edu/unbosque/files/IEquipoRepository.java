package co.edu.unbosque.files;

import java.util.List;

import co.edu.unbosque.model.EquipoFutbol;

public interface IEquipoRepository {
	void guardarEquipo(EquipoFutbol equipoFutbol);
	EquipoFutbol cargarEquipo(String nombre);
	List<EquipoFutbol> listaEquipos();
}
