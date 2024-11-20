package proyecto1.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.Encuesta;
import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class EncuestaTest {
    private Encuesta encuesta;
    private Profesor profesor;
    private LearningPath learningPath;
    private Estudiante estudiante;
    private ProgresoActividad progreso;

    @BeforeEach
    void setUp() {
        profesor = new Profesor("Juan Perez", "j.prez@uniandes.edu.co", "Juanpe");
        learningPath = new LearningPath("Aprendiendo Java", "Aprendizajes básicos de Java", "Aprender las características de Java", "Bajo", profesor, 120);
        encuesta = new Encuesta(learningPath, "Encuesta inicial", "Evaluar conocimientos", "Bajo", 30, true, profesor);
        estudiante = new Estudiante("Camilo Salazar", "c.salazar@uniandes.edu.co", "camilosa");
        progreso = new ProgresoActividad(encuesta, estudiante);
    }

    @Test
    void testRealizarEncuesta() throws Exception {
        // Simulación de la pregunta que se agrega a la encuesta
        String simulacion = "Cual es tu lenguaje de programacion favorito?";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulacion.getBytes()));
        encuesta.agregarPregunta(scanner);

        // Simular la respuesta del estudiante usando ByteArrayInputStream
        String simulatedInput = "Python\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Realizar la encuesta
        encuesta.realizar(progreso);

        // Verificar que la encuesta se completó correctamente
        assertTrue(progreso.isCompletada());
        assertEquals("Aprobada", progreso.getResultado());

        // Usar reflexión para acceder al mapa 'respuesta' en la clase 'Actividad'
        Field respuestaField = encuesta.getClass().getSuperclass().getDeclaredField("respuesta");
        respuestaField.setAccessible(true);
        Map<Estudiante, String> respuestas = (Map<Estudiante, String>) respuestaField.get(encuesta);

        // Verificar la respuesta del estudiante
        assertEquals("Python", respuestas.get(estudiante));
    }
}