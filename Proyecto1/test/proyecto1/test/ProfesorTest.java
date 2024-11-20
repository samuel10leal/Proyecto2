package proyecto1.test;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import proyecto.Actividad;

import proyecto.Estudiante;
import proyecto.Examen;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.RecursoEducativo;
import proyecto.Tarea;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Scanner;

public class ProfesorTest {
    
    private Profesor profesor;
    private LearningPath lp;
    private ActividadConcreta actividad;
    private Estudiante estudiante;
    private RecursoEducativo recurso;

    @BeforeEach
    void setUp() {
        profesor = new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
        estudiante = new Estudiante("Luis", "luis@example.com", "pas456");
        lp = new LearningPath("Aprendiendo Java", "Aprendizajes básicos de Java", "Aprender las diferentes características y reglas de Java", "Bajo", profesor, 120);
        actividad = new ActividadConcreta(lp, "Introducción", "Actividad inicial", "Bajo", 60, true, profesor);
        recurso = new RecursoEducativo(lp, "Recurso 1", "Descripcion del recurso", "Bajo", 60, true, "Documento", "http://link.com", profesor);
        lp.getActividades().add(actividad);
        lp.getActividades().add(recurso);
        estudiante.inscripcion(lp);
    }
    
    
    @Test
    void testCrearLearningPath() {
        assertNotNull(lp, "El learning path debería ser creado correctamente");
        
    }

    @Test
    void testInscribirEstudianteEnLearningPath() {
        assertTrue(estudiante.getLearningPathsInscritos().contains(lp), "El estudiante debe estar inscrito en el Learning Path.");
    }

    @Test
    void testCrearRecursoEducativo() {
        assertNotNull(recurso, "El recurso educativo no debe ser nulo.");
        assertEquals("Recurso 1", recurso.getDescripcion(), "La descripción del recurso debe ser correcta.");
    }
    
    @Test
    void testEditarRecurso() {
        String tipoRecursoAnterior = recurso.getTipoRecurso();
        recurso.editar(profesor);
        assertNotEquals(tipoRecursoAnterior, recurso.getTipoRecurso(), "El tipo de recurso debe haber cambiado.");
    }

    @Test
    void testNoEditarRecursoPorOtroProfesor() {
        Profesor otroProfesor = new Profesor("Carlos Gomez", "c.gomez@uniandes.edu.co", "CarlosG");
        String tipoRecursoAnterior = recurso.getTipoRecurso();
        recurso.editar(otroProfesor);
        assertEquals(tipoRecursoAnterior, recurso.getTipoRecurso(), "El tipo de recurso no debe cambiar si el editor no es el creador.");
    }
    
    @Test
    void testVerificarActividadesDeLearningPath() {
        List<Actividad> actividades = lp.getActividades();
        assertEquals(2, actividades.size(), "El Learning Path debe tener dos actividades.");
    }
    
    @Test
    void testGetTipoUsuario() {
        assertEquals("Profesor", profesor.getTipoUsuario(), "El tipo de usuario debería ser Profesor");
    }

    

    @Test
    void testDarReseñaActividad() {
        Actividad actividad = new Examen(lp, "Examen Final", "Evaluación de todo el curso", "Alto", 120, true, profesor);
        profesor.darReseñaActividad(actividad, "Muy buen examen", 5.0f);
        
    }

    

    @Test
    void testAñadirActividadALearningPath() {
        Actividad actividad = new Tarea(lp, "Tarea 1", "Descripción de tarea", "Bajo", 60, true, profesor);
        profesor.añadirActividadALearningPath(actividad);
        assertTrue(lp.getActividades().contains(actividad), "La actividad debería ser añadida al learning path");
    }
}
