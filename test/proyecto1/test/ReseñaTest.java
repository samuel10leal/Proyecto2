package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.Actividad;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.Reseña;

public class ReseñaTest {

    private Reseña reseña;
    private Actividad actividad;
    private LearningPath learningPath;
    private Profesor profesor;

    @BeforeEach
    public void setUp() {
    	profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        learningPath = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        reseña = new Reseña("Buena actividad", 8.5f);
        actividad = new ActividadConcreta(learningPath, "Tarea 1", "Objetivo de la tarea", "Intermedio", 60, true, profesor);
    }

    @Test
    public void testGetTexto() {
        assertEquals("Buena actividad", reseña.getTexto());
    }

    @Test
    public void testGetRating() {
        assertEquals(8.5f, reseña.getRating());
    }

    @Test
    public void testHacerReseña() {
        reseña.hacerReseña(actividad);
        assertTrue(actividad.getReseñas().contains(reseña));
    }

    @Test
    public void testHacerReseñaRatingInvalido() {
        Reseña reseñaInvalida = new Reseña("Mala actividad", 11.0f);
        reseñaInvalida.hacerReseña(actividad);
        assertFalse(actividad.getReseñas().contains(reseñaInvalida));
    }
}
