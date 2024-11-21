package consolas.proy2;

import proyecto.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ConsolaEstudiante {
    private Scanner scanner;
    private Estudiante estudianteActual;
    private Map<String, Estudiante> estudiantesRegistrados;

    public ConsolaEstudiante() {
        this.scanner = new Scanner(System.in);
        this.estudiantesRegistrados = new HashMap<>();
        cargarEstudiantes();
    }

    public void iniciar() {
        while (true) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> registrarEstudiante();
                case 2 -> loginEstudiante();
                case 3 -> verLearningPathsYActividades();
                case 4 -> realizarActividad();
                case 5 -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("Opción inválida. Inténtalo de nuevo.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("===== Menú de Estudiantes =====");
        System.out.println("1. Registrar Estudiante");
        System.out.println("2. Iniciar Sesión");
        System.out.println("3. Ver Learning Paths y Actividades");
        System.out.println("4. Realizar Actividad");
        System.out.println("5. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private void cargarEstudiantes() {
        estudiantesRegistrados.put("luis@example.com", new Estudiante("Luis", "luis@example.com", "password123"));
        estudiantesRegistrados.put("ana@example.com", new Estudiante("Ana", "ana@example.com", "password456"));
    }

    private void registrarEstudiante() {
        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce tu correo: ");
        String correo = scanner.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String contrasena = scanner.nextLine();

        if (estudiantesRegistrados.containsKey(correo)) {
            System.out.println("El correo ya está registrado. Intenta con otro.");
        } else {
            Estudiante nuevoEstudiante = new Estudiante(nombre, correo, contrasena);
            estudiantesRegistrados.put(correo, nuevoEstudiante);
            System.out.println("Estudiante registrado exitosamente.");
        }
    }

    private void loginEstudiante() {
        System.out.print("Introduce tu correo: ");
        String correo = scanner.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String contrasena = scanner.nextLine();

        Estudiante estudiante = estudiantesRegistrados.get(correo);
        if (estudiante != null && estudiante.getContrasena().equals(contrasena)) {
            estudianteActual = estudiante;
            System.out.println("Bienvenido, " + estudiante.getNombre() + "!");
        } else {
            System.out.println("Credenciales incorrectas. Intenta de nuevo.");
        }
    }

    private void verLearningPathsYActividades() {
        if (estudianteActual == null) {
            System.out.println("Por favor, inicia sesión primero.");
            return;
        }

        System.out.println("===== Learning Paths y Actividades =====");

        String archivoLearningPaths = "./datos/learningpaths.json";
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoLearningPaths))) {
            StringBuilder jsonContent = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                jsonContent.append(linea);
            }

            JSONArray learningPathsArray = new JSONArray(jsonContent.toString());

            if (learningPathsArray.isEmpty()) {
                System.out.println("No hay Learning Paths disponibles.");
                return;
            }

            for (int i = 0; i < learningPathsArray.length(); i++) {
                JSONObject lp = learningPathsArray.getJSONObject(i);
                System.out.println("Learning Path: " + lp.getString("titulo"));
                System.out.println("Descripción: " + lp.getString("descripcion"));
                System.out.println("Dificultad: " + lp.getString("nivelDificultad"));
                System.out.println("Duración Estimada: " + lp.getInt("duracionEstimada") + " minutos");

                JSONArray actividades = lp.getJSONArray("actividades");
                System.out.println("Actividades:");
                for (int j = 0; j < actividades.length(); j++) {
                    JSONObject actividad = actividades.getJSONObject(j);
                    System.out.println("  - " + actividad.getString("descripcion") +
                            " (Dificultad: " + actividad.getString("nivelDificultad") + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer los Learning Paths: " + e.getMessage());
        }
    }

    private void realizarActividad() {
        if (estudianteActual == null) {
            System.out.println("Por favor, inicia sesión primero.");
            return;
        }

        System.out.println("===== Realizar Actividad =====");

        String archivoLearningPaths = "./datos/learningpaths.json";
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoLearningPaths))) {
            StringBuilder jsonContent = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                jsonContent.append(linea);
            }

            JSONArray learningPathsArray = new JSONArray(jsonContent.toString());
            if (learningPathsArray.isEmpty()) {
                System.out.println("No hay Learning Paths disponibles.");
                return;
            }

            List<JSONObject> actividadesDisponibles = new ArrayList<>();
            int contador = 1;
            System.out.println("Actividades disponibles:");
            for (int i = 0; i < learningPathsArray.length(); i++) {
                JSONArray actividades = learningPathsArray.getJSONObject(i).getJSONArray("actividades");
                for (int j = 0; j < actividades.length(); j++) {
                    JSONObject actividad = actividades.getJSONObject(j);
                    actividadesDisponibles.add(actividad);
                    System.out.println(contador + ". " +
                            actividad.getString("descripcion") + " (Dificultad: " + actividad.getString("nivelDificultad") + ")");
                    System.out.println("   Objetivo: " + actividad.getString("objetivo"));
                    System.out.println("   Duración Estimada: " + actividad.getInt("duracionEsperada") + " minutos");
                    contador++;
                }
            }

            if (actividadesDisponibles.isEmpty()) {
                System.out.println("No hay actividades disponibles para realizar.");
                return;
            }

            System.out.print("Selecciona el número de la actividad que deseas realizar: ");
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion < 1 || seleccion > actividadesDisponibles.size()) {
                System.out.println("Selección inválida. Inténtalo de nuevo.");
                return;
            }

            JSONObject actividadSeleccionada = actividadesDisponibles.get(seleccion - 1);
            System.out.println("Has seleccionado la actividad: " + actividadSeleccionada.getString("descripcion"));
            System.out.println("Objetivo: " + actividadSeleccionada.getString("objetivo"));
            System.out.println("Nivel de dificultad: " + actividadSeleccionada.getString("nivelDificultad"));
            System.out.println("Duración estimada: " + actividadSeleccionada.getInt("duracionEsperada") + " minutos");

            System.out.print("¿Deseas realizar esta actividad? (si/no): ");
            String confirmacion = scanner.nextLine().toLowerCase();
            if (!confirmacion.equals("si")) {
                System.out.println("Actividad cancelada.");
                return;
            }

            int duracion = actividadSeleccionada.getInt("duracionEsperada");
            System.out.print("La actividad tiene una duración estimada de " + duracion +
                    " minutos. ¿Cuánto tiempo tienes disponible (en minutos)?: ");
            int tiempoDisponible = scanner.nextInt();
            scanner.nextLine();

            if (tiempoDisponible < duracion) {
                System.out.println("No tienes tiempo suficiente para realizar esta actividad.");
                return;
            }

            System.out.println("Realizando actividad...");
            Thread.sleep(2000);
            System.out.println("¡Actividad completada exitosamente!");

        } catch (Exception e) {
            System.out.println("Error al realizar la actividad: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ConsolaEstudiante consola = new ConsolaEstudiante();
        consola.iniciar();
    }
}

