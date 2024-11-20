package proyecto1.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.Actividad;
import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;
import proyecto.Reseña;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ActividadTest {

    private ActividadConcreta actividad;
    private Profesor profesor;
    private LearningPath learningPath;
    private ProgresoActividad progreso;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        profesor = new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
        learningPath = new LearningPath("Aprendiendo Java", "Aprendizajes básicos de Java", "Aprender las diferentes características y reglas de Java", "Bajo", profesor, 120);
        actividad = new ActividadConcreta(learningPath, "Tarea", "Evaluar el entendimiento de los estudiantes", "Bajo", 120, true, profesor);
        estudiante = new Estudiante("Camilo Salazar", "c.salazar@uniandes.edu.co", "Camilosa");
        progreso = new ProgresoActividad(actividad, estudiante);
    }

    @Test
    void testEstablecerFechaLimite() {
        actividad.establecerFechaLimite(null);
        long expectedTime = System.currentTimeMillis() + (3 * 60 * 60 * 1000); // 3 horas adelante
        long actualTime = actividad.getFechaLimite().getTime();
        
        assertTrue(Math.abs(expectedTime - actualTime) < 2000, "La fecha límite no es correcta");
    }

    @Test
    void testAgregarActividadSeguimiento() {
        ActividadConcreta actividadSeguimiento = new ActividadConcreta(learningPath, "Seguimiento", "Objetivo Seguimiento", "Básico", 30, false, profesor);
        actividad.agregarActividadSeguimiento(actividadSeguimiento);
        assertEquals(1, actividad.getActividadSeguimiento().size());
        assertEquals("Seguimiento", actividad.getActividadSeguimiento().get(0).getDescripcion());
    }

    @Test
    void testAgregarPrerrequisito() {
        ActividadConcreta prerrequisito = new ActividadConcreta(learningPath, "Prerrequisito", "Objetivo Prerrequisito", "Avanzado", 90, true, profesor);
        actividad.agregarPrerrequisito(prerrequisito);
        assertEquals(1, actividad.getPrerrequisitos().size());
        assertEquals("Prerrequisito", actividad.getPrerrequisitos().get(0).getDescripcion());
    }

    @Test
    void testRecomendarActividad() {
        progreso.setFechaInicio(new Date()); // Establecer fecha de inicio
        progreso.marcarRealizada("Aprobada", new Date()); // Establecer fecha de finalización
        assertDoesNotThrow(() -> actividad.recomendarActividad(progreso, actividad));
    }

    @Test
    void testClonarActividad() {
        Profesor nuevoProfesor = new Profesor("Ana García", "ana.garcia@uniandes.edu.co", "Anita");
        Actividad clonada = actividad.clonarActividad(nuevoProfesor);
        assertNotNull(clonada);
        assertEquals("Ana García", clonada.getCreador().getNombre());
        assertEquals("Tarea", clonada.getDescripcion());
    }

    @Test
    void testCalcularPromedioRating() {
        Reseña reseña1 = new Reseña("Excelente", 5);
        Reseña reseña2 = new Reseña("Bueno", 3);
        actividad.agregarReseña(reseña1);
        actividad.agregarReseña(reseña2);
        assertEquals(4.0, actividad.calcularPromedioRating());
    }
}
	
	
	


