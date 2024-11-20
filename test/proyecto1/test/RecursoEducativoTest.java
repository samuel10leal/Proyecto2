package proyecto1.test;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;
import proyecto.RecursoEducativo;

public class RecursoEducativoTest {
	private RecursoEducativo recurso;
    private Profesor profesor;
    private LearningPath lp;
    private Estudiante estudiante;

    @Before
    public void setUp() {
   
        profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        lp = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        recurso = new RecursoEducativo(lp, "Test Resource", "Learn Java", "Easy", 30, true, "Video", "http://example.com", profesor);
        estudiante = new Estudiante("Juan Perez", "juan.perez@correo.com", "1234");
    }

    @Test
    public void testGetTipoRecurso() {
        assertEquals("Video", recurso.getTipoRecurso());
    }

    @Test
    public void testGetEnlaceRecurso() {
        assertEquals("http://example.com", recurso.getEnlaceRecurso());
    }

    @Test
    public void testCambios() {
        Scanner scanner = new Scanner("recurso\nNuevo Tipo\n");
        recurso.cambios(scanner);
        assertEquals("Nuevo Tipo", recurso.getTipoRecurso());

        scanner = new Scanner("enlace\nhttp://nuevoenlace.com\n");
        recurso.cambios(scanner);
        assertEquals("http://nuevoenlace.com", recurso.getEnlaceRecurso());
    }

    @Test
    public void testRealizar() {
        ProgresoActividad progreso = new ProgresoActividad(recurso, estudiante);
        recurso.realizar(progreso);
        assertTrue(progreso.isCompletada());
    }

    @Test
    public void testEditarConPermiso() {
        // Simular la entrada del usuario para cambiar el tipo de recurso
        Scanner scanner = new Scanner("recurso\nNuevo Tipo\n");
        // Redirigir la entrada estándar para el método editar
        System.setIn(new java.io.ByteArrayInputStream("recurso\nNuevo Tipo\n".getBytes()));
        recurso.editar(profesor);
        assertEquals("Nuevo Tipo", recurso.getTipoRecurso());

        // Simular la entrada del usuario para cambiar el enlace del recurso
        scanner = new Scanner("enlace\nhttp://nuevoenlace.com\n");
        // Redirigir la entrada estándar para el método editar
        System.setIn(new java.io.ByteArrayInputStream("enlace\nhttp://nuevoenlace.com\n".getBytes()));
        recurso.editar(profesor);
        assertEquals("http://nuevoenlace.com", recurso.getEnlaceRecurso());
    }

    @Test
    public void testEditarSinPermiso() {
        Profesor otroProfesor = new Profesor("Ana", "ana.gomez@correo.com", "5678");
        recurso.editar(otroProfesor);
        assertNotEquals("Nuevo Tipo", recurso.getTipoRecurso());
    }
}