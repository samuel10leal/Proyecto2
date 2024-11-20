package proyecto1.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.*;
import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class QuizTest {

    private Quiz quiz;
    private LearningPath learningPath;
    private Profesor profesor;
    private Estudiante estudiante;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        learningPath = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        quiz = new Quiz(learningPath, "Descripción", "Objetivo", "Intermedio", 2, true, 70.0, profesor);
        estudiante = new Estudiante("Juan Perez", "juan.perez@correo.com", "1234");
    }

    @Test
    public void testInicializacion() {
        assertEquals(70.0, quiz.getNotaAprobacion());
        assertEquals(0.0, quiz.getNotaObtenida());
        assertTrue(quiz.getPreguntas().isEmpty());
    }

    @Test
    public void testAgregarPregunta() {
        Scanner scanner = new Scanner("Pregunta\nOpción 1\nExplicación 1\nOpción 2\nExplicación 2\nOpción 3\nExplicación 3\nOpción 4\nExplicación 4\n1\n");
        quiz.agregarPregunta(scanner);
        assertEquals(1, quiz.getPreguntas().size());
    }

    @Test
    public void testRealizarQuiz() {
        Scanner scanner = new Scanner("Pregunta\nOpción 1\nExplicación 1\nOpción 2\nExplicación 2\nOpción 3\nExplicación 3\nOpción 4\nExplicación 4\n1\n");
        quiz.agregarPregunta(scanner);
        scanner = new Scanner("1\n");
        String resultado = quiz.realizarQuiz(scanner);
        assertEquals("Aprobada", resultado);
        assertEquals(100.0, quiz.getNotaObtenida());
    }

    @Test
    public void testRealizar() {
        ProgresoActividad progreso = new ProgresoActividad(quiz, estudiante);
        quiz.realizar(progreso);
        assertTrue(progreso.isCompletada());
    }

    @Test
    public void testEditar() {
        // Simular la edición sin Scanner
        PreguntaMultiple pregunta = new PreguntaMultiple(
            "Pregunta de prueba",
            List.of("Opción 1", "Opción 2", "Opción 3", "Opción 4"),
            1,
            List.of("Explicación 1", "Explicación 2", "Explicación 3", "Explicación 4")
        );
        quiz.getPreguntas().add(pregunta);
        assertEquals(1, quiz.getPreguntas().size());
    }

    @Test
    public void testEditarSinPermiso() {
        // Crear un profesor diferente
        Profesor otroProfesor = new Profesor("Otro Profesor", "otro@correo.com", "5678");
        
        // Intentar editar con un profesor diferente
        quiz.editar(otroProfesor);
        assertEquals(0, quiz.getPreguntas().size());
    }
}
