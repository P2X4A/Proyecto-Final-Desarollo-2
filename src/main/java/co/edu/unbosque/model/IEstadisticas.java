package co.edu.unbosque.model;

import java.util.List;
import java.util.Map;

public interface IEstadisticas {
	Map<String, Object> getEstadisticas();
	List<Partido> getHistorialPartidas();

}
