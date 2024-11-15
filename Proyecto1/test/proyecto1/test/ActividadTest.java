package proyecto1.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.Actividad;
import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;

import static org.junit.jupiter.api.Assertions.*;

public class ActividadTest {
	
	private ActividadConcreta actividad;
	private Profesor profesor;
	private LearningPath learningPath;
	private ProgresoActividad progreso;
	private Estudiante estudiante;
	
	@BeforeEach
	
	void setUp() {
		profesor= new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
		learningPath= new LearningPath("Aprendiendo Java", "Aprendizajes basicos de Java", "Aprender las diferentes caracteristicas y reglas de Java", "Bajo", profesor, 120);
		actividad=new ActividadConcreta(learningPath, "Tarea", "Evaluar el entendimiento de los estudiantes", "Bajo", 120, true, profesor);
		estudiante= new Estudiante("Camilo Salazar", "c,salazar@uniandes.edu.co", "Camilosa");
		progreso= new ProgresoActividad(actividad, estudiante);	
				
	}
	@Test
	
	void TestEstablecerFechaLimite() {
		actividad.establecerFechaLimite(null);
	}
	
	
	
	
	

}
