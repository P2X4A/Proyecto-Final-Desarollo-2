package co.edu.unbosque.files;

public interface IEquipoRepository {
	void guardarEquipo(EquipoFutbol equipoFutbol);
	EquipoFutbol cargarEquipo(String nombre);
	List<EquipoFutbol> listaEquipos();
}
