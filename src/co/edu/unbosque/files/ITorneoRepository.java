package co.edu.unbosque.files;

import java.util.List;

public interface ITorneoRepository {
	void guardarPartido(Torneo torneo);
	Torneo cargarPartido(String id);
	List<String> listaPartidos();
	

}
