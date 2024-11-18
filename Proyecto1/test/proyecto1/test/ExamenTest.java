package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import proyecto.*;

public class ExamenTest {
	
	private Profesor profesor;
    private Estudiante estudiante;
    private LearningPath lp;
    private Examen examen;
    
    @BeforeEach
    void setUp() {
       
        estudiante = new Estudiante("Luis", "luis@example.com", "pas456");
        profesor = new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
        lp = new LearningPath("Aprendiendo Java", "Aprendizajes básicos de Java", "Aprender las diferentes características y reglas de Java", "Bajo", profesor, 120);
        examen = new Examen(lp, "Examen de Java", "Evaluar el conocimiento básico de Java", "Bajo", 60, true, profesor);
    }
    
    @Test
    void testAgregarPregunta() {
    	Scanner scanner = new Scanner(System.in);
        examen.agregarPregunta(scanner);
        
        assertFalse(examen.getPreguntasAbiertas().isEmpty(),"Deberia haber una pregunta");
        
        
    }
    @Test
    void testRealizarExamen() {
        // Crear un progreso para el examen
        ProgresoActividad progreso = new ProgresoActividad(examen, estudiante);
        
        // Asignar fecha de inicio
        progreso.setFechaInicio(new Date());  // Asegúrate de que la fecha de inicio se establezca

        // Realizar el examen
        examen.realizarExamen(progreso);  // Este método debería marcar el progreso de alguna manera

        // Establecer una fecha de fin para simular que el examen se completó
        progreso.setFechaFin(new Date());  // Asignar una fecha de fin para marcar la actividad como completada

        // Marcar como realizada
        progreso.marcarRealizada("Enviada", new Date());

        // Verificar que el estado del progreso es 'Enviada' después de marcarlo como realizado
        assertTrue(progreso.isCompletada(), "El progreso debe estar completado.");
        assertEquals("Enviada", progreso.getResultado(), "El resultado debería ser 'Enviada'.");
    }
    
    @Test
    void testEditarExamen() {
        // Crear un Estudiante, un Profesor y un Examen
        Profesor nocreador = new Profesor("Luis", "luis@example.com", "contraseña");
        Profesor creador = new Profesor("Juan Pérez", "juanperez@uni.edu", "profesor123");
        examen = new Examen(lp, "Examen Final", "Examen de Java", "Alto", 120, true, profesor);
        
        // Intentar editar el examen con un Estudiante (esto no debería permitir la edición)
        int numeroDePreguntasAntes = examen.getPreguntasAbiertas().size();
        
        // El Estudiante no debería poder editar el examen
        examen.editar(nocreador);  // El estudiante no tiene permisos
        
        // Verificar que el examen no ha sido editado (el número de preguntas no ha cambiado)
        assertEquals(numeroDePreguntasAntes, examen.getPreguntasAbiertas().size(), 
                     "El examen no debería ser editado por el estudiante.");
        
        // Ahora, hacer que el Profesor edite el examen (debería permitir la edición)
        int preguntasAntesDeEdicion = examen.getPreguntasAbiertas().size();
        
        // El Profesor tiene permisos para editar
        examen.editar(creador);  // El profesor debería poder editar
        
        // Verificar que el número de preguntas ha cambiado (el profesor sí puede editar)
        assertTrue(examen.getPreguntasAbiertas().size() > preguntasAntesDeEdicion, 
                   "El profesor debería poder editar el examen.");
    }


    @Test
    void testConstructorExamen() {
        // Verificar que el examen se haya creado con los atributos correctos
        assertNotNull(examen, "El examen debería haberse creado correctamente.");
        assertEquals("Examen de Java", examen.getDescripcion(), "La descripción del examen no es la correcta.");
        assertEquals("Evaluar el conocimiento básico de Java", examen.getObjetivo(), "El objetivo del examen no es el correcto.");
        assertEquals("Bajo", examen.getNivelDificultad(), "El nivel de dificultad no es el correcto.");
    }
}



