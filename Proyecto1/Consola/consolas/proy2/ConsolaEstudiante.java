package consolas.proy2;

import proyecto.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConsolaEstudiante {
    private Scanner scanner;
    private Estudiante estudianteActual;
    private Map<String, Estudiante> estudiantesRegistrados;
    private Map<String, Map<String, Integer>> actividadesPorDia;

    public ConsolaEstudiante() {
        this.scanner = new Scanner(System.in);
        this.estudiantesRegistrados = new HashMap<>();
        this.actividadesPorDia = new HashMap<>();
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
                case 5 -> mostrarGraficoActividades();
                case 6 -> {
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
        System.out.println("5. Ver Gráfico de Actividades");
        System.out.println("6. Salir");
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

            System.out.println("\n===== Learning Paths y Actividades =====");
            for (int i = 0; i < learningPathsArray.length(); i++) {
                JSONObject lp = learningPathsArray.getJSONObject(i);
                System.out.println("\nLearning Path: " + lp.getString("titulo"));
                System.out.println("Descripción: " + lp.getString("descripcion"));
                System.out.println("Dificultad: " + lp.getString("nivelDificultad"));
                System.out.println("Duración Estimada: " + lp.getInt("duracionEstimada") + " minutos");

                JSONArray actividades = lp.getJSONArray("actividades");
                System.out.println("Actividades:");
                for (int j = 0; j < actividades.length(); j++) {
                    JSONObject actividad = actividades.getJSONObject(j);
                    System.out.println("  - " + actividad.getString("descripcion") +
                            " (Dificultad: " + actividad.getString("nivelDificultad") + ")");
                    System.out.println("    Objetivo: " + actividad.getString("objetivo"));
                    System.out.println("    Duración Estimada: " + actividad.getInt("duracionEsperada") + " minutos");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer los Learning Paths: " + e.getMessage());
        }
    }

    private void registrarActividad() {
        if (estudianteActual == null) {
            System.out.println("No se puede registrar la actividad. Ningún estudiante ha iniciado sesión.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaHoy = sdf.format(new Date());

        // Obtener o inicializar el registro de actividades del día actual
        actividadesPorDia.putIfAbsent(fechaHoy, new HashMap<>());
        Map<String, Integer> actividadesEstudiante = actividadesPorDia.get(fechaHoy);

        // Incrementar el conteo de actividades para el estudiante actual
        String correoEstudiante = estudianteActual.getCorreo();
        actividadesEstudiante.put(correoEstudiante,
                actividadesEstudiante.getOrDefault(correoEstudiante, 0) + 1);

        System.out.println("Actividad registrada correctamente para " + correoEstudiante +
                " en la fecha " + fechaHoy + ".");
    }

    // Método para realizar una actividad
    private void realizarActividad() {
        if (estudianteActual == null) {
            System.out.println("Por favor, inicia sesión primero.");
            return;
        }

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
            System.out.println("\nActividades disponibles:");
            int contador = 1;
            for (int i = 0; i < learningPathsArray.length(); i++) {
                JSONArray actividades = learningPathsArray.getJSONObject(i).getJSONArray("actividades");
                for (int j = 0; j < actividades.length(); j++) {
                    JSONObject actividad = actividades.getJSONObject(j);
                    actividadesDisponibles.add(actividad);
                    System.out.println(contador + ". " + actividad.getString("descripcion") +
                            " (" + actividad.getString("nivelDificultad") + ")");
                    contador++;
                }
            }

            if (actividadesDisponibles.isEmpty()) {
                System.out.println("No hay actividades disponibles para realizar.");
                return;
            }

            System.out.print("\nSelecciona el número de la actividad que deseas realizar: ");
            int seleccion;
            try {
                seleccion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine(); // Limpiar entrada
                return;
            }

            if (seleccion < 1 || seleccion > actividadesDisponibles.size()) {
                System.out.println("Selección inválida. Inténtalo de nuevo.");
                return;
            }

            JSONObject actividadSeleccionada = actividadesDisponibles.get(seleccion - 1);
            System.out.println("\nRealizando actividad: " + actividadSeleccionada.getString("descripcion"));

            try {
                Thread.sleep(2000); // Simulación
                System.out.println("¡Actividad completada exitosamente!");
                registrarActividad(); // Registrar la actividad realizada
            } catch (InterruptedException e) {
                System.out.println("Error durante la simulación de la actividad.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de Learning Paths: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("Error al procesar el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }



    private void mostrarGraficoActividades() {
        System.out.println("\n===== Gráfico de Actividades =====");
        actividadesPorDia.forEach((fecha, actividades) -> {
            System.out.print(fecha + ": ");
            actividades.forEach((correo, cantidad) -> {
                System.out.print(correo + " (" + cantidad + ") ");
            });
            System.out.println();
        });
    }

    public static void main(String[] args) {
        ConsolaEstudiante consola = new ConsolaEstudiante();
        consola.iniciar();
    }
}
