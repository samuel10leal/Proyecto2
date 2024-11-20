package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.io.*;
import proyecto.*;

public class EstudianteTest {
	
	private Actividad actividad1;
	private Actividad actividad2;
	private Profesor profesor;
	private LearningPath lp;
	private Estudiante estudiante;
	
	@BeforeEach
	void setUp() {
		estudiante= new Estudiante("luis","luis@example.com","pas456");
		profesor = new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
		lp = new LearningPath("Aprendiendo Java", "Aprendizajes básicos de Java", "Aprender las diferentes características y reglas de Java", "Bajo", profesor, 120);
		actividad1 = new ActividadConcreta(lp, "Introducción", "Actividad inicial", "Bajo", 60, true, profesor);
        actividad2 = new ActividadConcreta(lp, "Ejercicios", "Resolver ejercicios", "Medio", 90, true, profesor);
        lp.getActividades().add(actividad1);
        lp.getActividades().add(actividad2);
        estudiante.inscripcion(lp);
	}
	
	@Test
	void testInscripcionLearningPath() {
		
		assertTrue(estudiante.getLearningPathsInscritos().contains(lp), "El estudiante deberia estar inscrito");
		
	}
	
	@Test
	void testVerLearningPaths() {
		estudiante.verLearningPaths();
		
	}
	
	@Test
	
	void testSeleccionarActividad() {
		Actividad actividadSeleccionada= estudiante.seleccionarActividad(new Scanner(System.in), lp);
		assertNotNull(actividadSeleccionada, "Deberia haber una actividad seleccionada");
		assertEquals(actividad1, actividadSeleccionada, "La actividad seleccionada debería ser la 'Introducción'.");
	}
	
	@Test
	void testDarResenaActividad() {
		
		estudiante.realizarActividad(actividad1);
		estudiante.darReseñaActividad(actividad1, "Actividad muy útil.", 4.5f);
	}
	
	@Test
	void testPedirRecomendacionActividad() {
		
		estudiante.pedirRecomendacionActividad(lp);
	}
	
	@Test
	void testPedirProgresoPath() {
		
		estudiante.pedirProgresoPath(lp);
	}
}
