package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.Actividad;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.Usuario;

public class UsuarioTest {

    private Usuario usuario;
    private Actividad actividad;
    private LearningPath learningPath;
    private Profesor profesor;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        learningPath = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        usuario = new UsuarioConcreto("Juan Perez", "juan.perez@correo.com", "1234");
        actividad = new ActividadConcreta(learningPath, "Tarea 1", "Objetivo de la tarea", "Intermedio", 60, true, profesor);
    }

    @Test
    public void testGetNombre() {
        assertEquals("Juan Perez", usuario.getNombre());
    }

    @Test
    public void testGetCorreo() {
        assertEquals("juan.perez@correo.com", usuario.getCorreo());
    }

    @Test
    public void testGetContrasena() {
        assertEquals("1234", usuario.getContrasena());
    }

    @Test
    public void testVerLearningPaths() {
        usuario.verLearningPaths();
        // No hay una aserción específica aquí, solo verificamos que el método se puede llamar sin errores
    }

    @Test
    public void testGetTipoUsuario() {
        assertEquals("Concreto", usuario.getTipoUsuario());
    }

    @Test
    public void testDarReseñaActividad() {
        usuario.darReseñaActividad(actividad, "Buena actividad", 8.5f);
        assertFalse(actividad.getReseñas().isEmpty());
        assertEquals("Buena actividad", actividad.getReseñas().get(0).getTexto());
        assertEquals(8.5f, actividad.getReseñas().get(0).getRating());
    }
}