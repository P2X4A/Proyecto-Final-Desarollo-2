package co.edu.unbosque.files;

import java.util.List;

import co.edu.unbosque.model.Torneo;

public interface ITorneoRepository {
	void guardarTorneo(Torneo torneo);
	Torneo cargarTorneo(String nombre);
	List<String> listaTorneos();


}
