package proyecto1.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;
import proyecto.Tarea;

public class TareaTest {
    private Tarea tarea;
    private Profesor profesor;
    private LearningPath lp;
    private Estudiante estudiante;

    @Before
    public void setUp() {
        profesor = new Profesor("Profesor", "profesor@correo.com", "1234");
        lp = new LearningPath("Java Basics", "Descripción", "Objetivos", "Intermedio", profesor, 10);
        tarea = new Tarea(lp, "Test Tarea", "Learn Java", "Easy", 30, true, profesor);
        estudiante = new Estudiante("Juan Perez", "juan.perez@correo.com", "1234");
    }

    @Test
    public void testGetMedioEntrega() {
        assertEquals("", tarea.getMedioEntrega());
    }

    @Test
    public void testEnviarTarea() {
        Scanner scanner = new Scanner("Email\n");
        ProgresoActividad progreso = new ProgresoActividad(tarea, estudiante);
        tarea.enviarTarea(scanner, progreso);
        assertEquals("Email", tarea.getMedioEntrega());
    }

    @Test
    public void testCambios() {
        Scanner scanner = new Scanner("descripcion\nNueva Descripcion\n");
        tarea.cambios(scanner);
        assertEquals("Nueva Descripcion", tarea.getDescripcion());

        scanner = new Scanner("objetivo\nNuevo Objetivo\n");
        tarea.cambios(scanner);
        assertEquals("Nuevo Objetivo", tarea.getObjetivo());
    }

    @Test
    public void testRealizar() {
        // Simular la entrada del usuario para enviar la tarea
        System.setIn(new java.io.ByteArrayInputStream("Email\n".getBytes()));
        ProgresoActividad progreso = new ProgresoActividad(tarea, estudiante);
        tarea.realizar(progreso);
        assertTrue(progreso.isCompletada());
    }

    @Test
    public void testEditarConPermiso() {
        // Simular la entrada del usuario para cambiar la descripción
        System.setIn(new java.io.ByteArrayInputStream("descripcion\nNueva Descripcion\n".getBytes()));
        tarea.editar(profesor);
        assertEquals("Nueva Descripcion", tarea.getDescripcion());

        // Simular la entrada del usuario para cambiar el objetivo
        System.setIn(new java.io.ByteArrayInputStream("objetivo\nNuevo Objetivo\n".getBytes()));
        tarea.editar(profesor);
        assertEquals("Nuevo Objetivo", tarea.getObjetivo());
    }

    @Test
    public void testEditarSinPermiso() {
        Profesor otroProfesor = new Profesor("Ana", "ana.gomez@correo.com", "5678");
        tarea.editar(otroProfesor);
        assertNotEquals("Nueva Descripcion", tarea.getDescripcion());
    }
}