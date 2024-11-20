package proyecto1.test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PreguntaMultipleTest {
	
	private PreguntaMultiple pregunta;
	private List<String> opciones;
	private List<String> explicaciones;
	
	@BeforeEach
	void setUp() {
		opciones= Arrays.asList("Opcion 1","Opcion 2", "Opcion 3", "Opcion 4");
		explicaciones = Arrays.asList("Explicación 1", "Explicación 2", "Explicación 3", "Explicación 4");
		pregunta = new PreguntaMultiple( "Cual es la capital de Francia?", opciones, 1, explicaciones);
		
	}
	
	@Test
	void TestCrearPreguntaMultipleValida() {
		
		assertEquals("Cual es la capital de Francia?",pregunta.getTextoPregunta());
		assertEquals(opciones,pregunta.getOpciones());
		assertEquals(explicaciones,pregunta.getExplicaciones());
		
	}
	@Test
    void testCrearPreguntaMultipleConDatosInvalidos() {
        // Verificar que se lanza una excepción si no hay exactamente 4 opciones
        List<String> opcionesInvalidas = Arrays.asList("Opción 1", "Opción 2", "Opción 3");
        List<String> explicacionesInvalidas = Arrays.asList("Explicación 1", "Explicación 2", "Explicación 3");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new PreguntaMultiple("Pregunta inválida", opcionesInvalidas, 1, explicacionesInvalidas);
        });
    }

    @Test
    void testMostrarYResolverRespuestaCorrecta() {
        // Simular una respuesta correcta ingresada por el usuario
        String input = "1\n";
        Scanner scanner = new Scanner(input);
        boolean esCorrecta = pregunta.mostrarYResolver(scanner);
        assertTrue(esCorrecta, "La respuesta debería ser correcta.");
    }

    @Test
    void testMostrarYResolverRespuestaIncorrecta() {
        // Simular una respuesta incorrecta ingresada por el usuario
        String input = "3\n";
        Scanner scanner = new Scanner(input);
        boolean esCorrecta = pregunta.mostrarYResolver(scanner);
        assertFalse(esCorrecta, "La respuesta debería ser incorrecta.");
    }

    @Test
    void testMostrarYResolverEntradaInvalida() {
        // Simular entradas: inválida ("a"), fuera de rango ("5"), y finalmente correcta ("2")
        String input = "a\n5\n2\n";
        Scanner scanner = new Scanner(input);

        List<String> opciones = List.of("Opción 1", "Opción 2", "Opción 3", "Opción 4");
        List<String> explicaciones = List.of("Explicación 1", "Explicación 2", "Explicación 3", "Explicación 4");
        PreguntaMultiple pregunta = new PreguntaMultiple("¿Cuál es la respuesta correcta?", opciones, 2, explicaciones);

        boolean resultado = pregunta.mostrarYResolver(scanner);

        // Verificar que el método reconozca la respuesta correcta tras entradas inválidas
        assertTrue(resultado, "La respuesta correcta debería ser reconocida tras entradas inválidas.");
    }
}
	
	
	


