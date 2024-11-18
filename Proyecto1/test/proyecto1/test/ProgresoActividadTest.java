package proyecto1.test;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.*;
import java.util.Date;

public class ProgresoActividadTest {

    private ProgresoActividad progreso;
    private ActividadConcreta actividad;
    private Estudiante estudiante;
    private Profesor profesor;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        estudiante = new Estudiante("Juan Perez", "juan.perez@correo.com", "1234");
        actividad = new ActividadConcreta(new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10), "Descripción", "Objetivo", "Intermedio", 2, true, profesor);
        progreso = new ProgresoActividad(actividad, estudiante);
    }

    @Test
    public void testInicializacion() {
        assertEquals(actividad, progreso.getActividad());
        assertFalse(progreso.isCompletada());
        assertEquals("Por completar", progreso.getResultado());
        assertEquals(0, progreso.getTiempoDedicado());
        assertNull(progreso.getFechaInicio());
        assertNull(progreso.getFechaFin());
        assertEquals(estudiante, progreso.getEstudiante());
    }

    @Test
    public void testSetFechaInicio() {
        Date fechaInicio = new Date();
        progreso.setFechaInicio(fechaInicio);
        assertEquals(fechaInicio, progreso.getFechaInicio());
    }

    @Test
    public void testCalcularTiempoDedicado() {
        Date inicio = new Date();
        Date fin = new Date(inicio.getTime() + 3600000); // 1 hora después
        long tiempoDedicado = progreso.calcularTiempoDedicado(inicio, fin);
        assertEquals(1, tiempoDedicado);
    }

    @Test
    public void testCompletarActividad() {
        progreso.completarActividad("Aprobado");
        assertEquals("Aprobado", progreso.getResultado());
    }

    @Test
    public void testMarcarRealizada() {
        Date fechaInicio = new Date();
        Date fechaFin = new Date(fechaInicio.getTime() + 7200000); // 2 horas después
        progreso.setFechaInicio(fechaInicio);
        progreso.marcarRealizada("Aprobado", fechaFin);
        assertTrue(progreso.isCompletada());
        assertEquals("Aprobado", progreso.getResultado());
        assertEquals(fechaFin, progreso.getFechaFin());
        assertEquals(2, progreso.getTiempoDedicado());
    }
}
