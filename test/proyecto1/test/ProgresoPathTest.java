package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.*;
import java.util.Date;

public class ProgresoPathTest {

    private ProgresoPath progresoPath;
    private LearningPath learningPath;
    private Estudiante estudiante;
    private Profesor profesor;

    @BeforeEach
    public void setUp() {
    	profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        estudiante = new Estudiante("Juan Perez", "juan.perez@correo.com", "1234");
        learningPath = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        progresoPath = new ProgresoPath(learningPath, new Date(), estudiante);
    }

    @Test
    public void testInicializacion() {
        assertEquals(learningPath, progresoPath.getLp());
        assertEquals(0, progresoPath.getPorcentajePath());
        assertEquals(0, progresoPath.getTasaExito());
        assertFalse(progresoPath.isCompletado());
        assertEquals(estudiante, progresoPath.getEstudiante());
        assertTrue(progresoPath.getActividadesRealizadas().isEmpty());
    }

    @Test
    public void testAgregarActividadRealizada() {
        ActividadConcreta actividad = new ActividadConcreta(learningPath, "Descripción", "Objetivo", "Intermedio", 2, true, profesor);
        progresoPath.agregarActividadRealizada(actividad);
        assertTrue(progresoPath.getActividadesRealizadas().contains(actividad));
    }

    @Test
    public void testMarcarCompletado() {
        ActividadConcreta actividad = new ActividadConcreta(learningPath, "Descripción", "Objetivo", "Intermedio", 2, true, profesor);
        learningPath.getActividades().add(actividad);
        progresoPath.agregarActividadRealizada(actividad);
        progresoPath.marcarCompletado();
        assertTrue(progresoPath.isCompletado());
        assertNotNull(progresoPath.getFechaFinPath());
    }

    @Test
    public void testCalcularProgreso() {
        ActividadConcreta actividad = new ActividadConcreta(learningPath, "Descripción", "Objetivo", "Intermedio", 2, true, profesor);
        learningPath.getActividades().add(actividad);
        progresoPath.agregarActividadRealizada(actividad);
        estudiante.getProgresosAct().put(actividad, new ProgresoActividad(actividad, estudiante));
        estudiante.getProgresosAct().get(actividad).completarActividad("Aprobada");
        progresoPath.calcularProgreso();
        assertEquals(100, progresoPath.getPorcentajePath());
    }

    @Test
    public void testActualizarTasas() {
        ActividadConcreta actividad1 = new ActividadConcreta(learningPath, "Descripción1", "Objetivo1", "Intermedio", 2, true, profesor);
        ActividadConcreta actividad2 = new ActividadConcreta(learningPath, "Descripción2", "Objetivo2", "Intermedio", 2, true, profesor);
        learningPath.getActividades().add(actividad1);
        learningPath.getActividades().add(actividad2);
        progresoPath.agregarActividadRealizada(actividad1);
        progresoPath.agregarActividadRealizada(actividad2);
        estudiante.getProgresosAct().put(actividad1, new ProgresoActividad(actividad1, estudiante));
        estudiante.getProgresosAct().put(actividad2, new ProgresoActividad(actividad2, estudiante));
        estudiante.getProgresosAct().get(actividad1).completarActividad("Aprobada");
        estudiante.getProgresosAct().get(actividad2).completarActividad("Reprobada");
        progresoPath.actualizarTasas();
        assertEquals(50, progresoPath.getTasaExito());
        assertEquals(50, progresoPath.getTasaFracaso());
    }
}