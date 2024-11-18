package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LearningPathTest {
    private LearningPath learningPath;
    private Profesor profesor;
    private Actividad actividad1;
    private Actividad actividad2;

    @BeforeEach
    void setUp() {
        // Crear un profesor y un LearningPath
        profesor = new Profesor("Juan Pérez", "juan@correo.com", "profesor123");
        learningPath = new LearningPath("Introducción a Programación", "Curso básico de programación",
                "Aprender los fundamentos", "Básico", profesor, 120);
        
        // Crear actividades
        actividad1 = new ActividadConcreta(learningPath, "Variables y Tipos de Datos", 
                                           "Introducción a variables", "Bajo", 60, true, profesor);
        actividad2 = new ActividadConcreta(learningPath, "Condicionales y Bucles", 
                                           "Uso de if-else y loops", "Medio", 90, true, profesor);
    }

    @Test
    void testCrearLearningPath() {
        // Verificar que el LearningPath se creó correctamente
        assertEquals("Introducción a Programación", learningPath.getTitulo());
        assertEquals("Curso básico de programación", learningPath.getDescripcion());
        assertEquals("Aprender los fundamentos", learningPath.getObjetivos());
        assertEquals("Básico", learningPath.getNivelDificultad());
        assertEquals(120, learningPath.getDuracionEstimada());
        assertEquals(profesor, learningPath.getCreador());
        assertNotNull(learningPath.getFechaCreacion());
        assertNotNull(learningPath.getFechaModificacion());
        assertEquals(1, learningPath.getVersion());
    }

    @Test
    void testAñadirActividad() {
        // Añadir actividades al LearningPath y verificar la duración
        learningPath.getActividades().add(actividad1);
        learningPath.añadirTiempoLp(actividad1);
        assertEquals(180, learningPath.getDuracionEstimada(), "La duración estimada no se actualizó correctamente.");

        learningPath.getActividades().add(actividad2);
        learningPath.añadirTiempoLp(actividad2);
        assertEquals(270, learningPath.getDuracionEstimada(), "La duración estimada no se actualizó correctamente tras añadir otra actividad.");
    }

    @Test
    void testReducirActividad() {
        // Añadir actividades y luego eliminar una
        learningPath.getActividades().add(actividad1);
        learningPath.getActividades().add(actividad2);
        learningPath.añadirTiempoLp(actividad1);
        learningPath.añadirTiempoLp(actividad2);

        learningPath.reducirTiempoLp(actividad1);
        learningPath.getActividades().remove(actividad1);
        assertEquals(210, learningPath.getDuracionEstimada(), "La duración estimada no se actualizó correctamente tras eliminar una actividad.");
    }

    @Test
    void testCalcularRatingPromedio() {
        // Añadir actividades y calcular el rating promedio
        learningPath.getActividades().add(actividad1);
        learningPath.getActividades().add(actividad2);

        double ratingPromedio = learningPath.calcularPromedioRating();
        assertTrue(ratingPromedio >= 0 && ratingPromedio <= 5, "El rating promedio no se calculó correctamente.");
    }

    @Test
    void testActualizarFechaModificacionYVersion() {
        // Actualizar la fecha de modificación y la versión del LearningPath
        Date nuevaFecha = new Date();
        learningPath.setFechaModificacion(nuevaFecha);
        assertEquals(nuevaFecha, learningPath.getFechaModificacion());

        learningPath.setVersion(2);
        assertEquals(2, learningPath.getVersion());
    }
}