package proyecto1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.Registro;

public class RegistroTest {

    private Registro registro;
    private Profesor profesor;
    private Estudiante estudiante;
    private LearningPath learningPath;

    @BeforeEach
    public void setUp() {
        registro = new Registro();
        profesor = new Profesor("Profe","profesor@correo.com", "1234");
        estudiante = new Estudiante("estudiente","estudiante@correo.com", "1234");
        learningPath = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
    }

    @Test
    public void testRegistrarProfesor() {
        registro.registrarProfesor(profesor);
        assertTrue(registro.getProfesores().contains(profesor));
    }

    @Test
    public void testRegistrarEstudiante() throws Exception {
        registro.registrarEstudiante(estudiante);
        assertTrue(registro.getEstudiantes().contains(estudiante));
    }

    @Test
    public void testLoginProfesor() throws Exception {
        registro.registrarProfesor(profesor);
        Profesor loginProfesor = registro.loginProfesor("profesor@correo.com", "1234");
        assertEquals(profesor, loginProfesor);
    }

    @Test
    public void testLoginEstudiante() throws Exception {
        registro.registrarEstudiante(estudiante);
        Estudiante loginEstudiante = registro.loginEstudiante("estudiante@correo.com", "1234");
        assertEquals(estudiante, loginEstudiante);
    }

    @Test
    public void testAgregarPaths() {
        registro.agregarPaths(learningPath);
        assertTrue(registro.getPaths().contains(learningPath));
    }
}