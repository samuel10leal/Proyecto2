package consolas.proy2;

import java.util.Scanner;

import proyecto.Actividad;
import proyecto.Estudiante;
import proyecto.Profesor;
import proyecto.Registro;
import proyecto.Usuario;
import proyecto.LearningPath;
import proyecto.ProgresoPath;
public class ConsolaProfesor {

    public static void main(String[] args) {
        Registro registro = new Registro();
        Profesor profesorActual = null;
        Scanner scanner = new Scanner(System.in);

        // Cargar usuarios desde el archivo JSON
        try {
            registro.cargarUsuarios("usuarios.json");
            System.out.println("Usuarios cargados exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }

        // Menú principal
        while (true) {
            System.out.println("\n==== CONSOLA DEL PROFESOR ====");
            System.out.println("1. Registrar/Iniciar sesión");
            System.out.println("2. Crear Learning Path");
            System.out.println("3. Editar/Eliminar Learning Path");
            System.out.println("4. Añadir/Editar actividades");
            System.out.println("5. Ver progreso de estudiantes");
            System.out.println("6. Calificar actividades");
            System.out.println("7. Ver notificaciones");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    profesorActual = manejarRegistroInicioSesion(registro, scanner);
                    break;
                case 2:
                    if (validarSesion(profesorActual)) crearLearningPath(profesorActual, registro, scanner);
                    break;
                case 3:
                    if (validarSesion(profesorActual)) editarEliminarLearningPath(profesorActual, scanner);
                    break;
                case 4:
                    if (validarSesion(profesorActual)) añadirEditarActividades(profesorActual, scanner);
                    break;
                case 5:
                    if (validarSesion(profesorActual)) verProgresoEstudiantes(profesorActual);
                    break;
                case 6:
                    if (validarSesion(profesorActual)) calificarActividades(profesorActual, scanner);
                    break;
                case 7:
                    if (validarSesion(profesorActual)) verNotificaciones(profesorActual);
                    break;
                case 8:
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static Profesor manejarRegistroInicioSesion(Registro registro, Scanner scanner) {
        System.out.println("\n=== INICIO DE SESIÓN ===");
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        try {
            Profesor profesor = registro.loginProfesor(correo, contrasena);
            System.out.println("Bienvenido, " + profesor.getNombre() + "!");
            return profesor;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static boolean validarSesion(Profesor profesor) {
        if (profesor == null) {
            System.out.println("Debe iniciar sesión antes de acceder a esta funcionalidad.");
            return false;
        }
        return true;
    }

    // Métodos vacío

    private static void crearLearningPath(Profesor profesor, Registro registro, Scanner scanner) {
        System.out.println("\n=== CREAR LEARNING PATH ===");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Objetivos: ");
        String objetivos = scanner.nextLine();
        System.out.print("Nivel de dificultad: ");
        String nivelDificultad = scanner.nextLine();
        System.out.print("Duración estimada (en días): ");
        int duracionEstimada = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        profesor.crearLearningPath(titulo, descripcion, objetivos, nivelDificultad, duracionEstimada, registro);
    }

    private static void editarEliminarLearningPath(Profesor profesor, Scanner scanner) {
        System.out.println("\n=== EDITAR/ELIMINAR LEARNING PATH ===");
        profesor.verLearningPaths();

        System.out.print("Ingrese el título del Learning Path a editar/eliminar: ");
        String titulo = scanner.nextLine();

        for (proyecto.LearningPath lp : profesor.getLearningPathsCreados()) {
            if (lp.getTitulo().equalsIgnoreCase(titulo)) {
                System.out.println("1. Editar Learning Path");
                System.out.println("2. Eliminar Learning Path");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion == 1) {
                    System.out.print("Nueva descripción: ");
                    String nuevaDescripcion = scanner.nextLine();
                    lp.setDescripcion(nuevaDescripcion);
                    System.out.println("Descripción actualizada.");
                } else if (opcion == 2) {
                    profesor.getLearningPathsCreados().remove(lp);
                    System.out.println("Learning Path eliminado.");
                } else {
                    System.out.println("Opción no válida.");
                }
                return;
            }
        }
        System.out.println("Learning Path no encontrado.");
    }

    private static void añadirEditarActividades(Profesor profesor, Scanner scanner) {
        System.out.println("\n=== AÑADIR/EDITAR ACTIVIDADES ===");
        Actividad nuevaActividad = profesor.crearActividad(scanner);
        if (nuevaActividad != null) {
            System.out.println("Actividad creada exitosamente: " + nuevaActividad.getDescripcion());
        }
    }

    private static void verProgresoEstudiantes(Profesor profesor) {
        System.out.println("\n=== VER PROGRESO DE ESTUDIANTES ===");

        // Iterar sobre los Learning Paths creados por el profesor
        for (LearningPath lp : profesor.getLearningPathsCreados()) {
            System.out.println("Learning Path: " + lp.getTitulo());

            // Iterar sobre los estudiantes inscritos en el Learning Path
            for (ProgresoPath progreso : lp.getProgresos()) {
                Estudiante estudiante = progreso.getEstudiante();

                // Calcular el progreso actualizado
                progreso.calcularProgreso();
                progreso.actualizarTasas();

                // Mostrar el progreso del estudiante
                System.out.println("Estudiante: " + estudiante.getNombre());
                System.out.println("Progreso: " + progreso.getPorcentajePath() + "%");
                System.out.println("Tasa de éxito: " + progreso.getTasaExito() + "%");
                System.out.println("Tasa de fracaso: " + progreso.getTasaFracaso() + "%");
                System.out.println("Completado: " + (progreso.isCompletado() ? "Sí" : "No"));
                if (progreso.getFechaFinPath() != null) {
                    System.out.println("Fecha de finalización: " + progreso.getFechaFinPath());
                }
                System.out.println("-----------");
            }
        }
    }

    

    private static void calificarActividades(Profesor profesor, Scanner scanner) {
        System.out.println("\n=== CALIFICAR ACTIVIDADES ===");
        System.out.print("Ingrese el título del Learning Path: ");
        String titulo = scanner.nextLine();

        for (LearningPath lp : profesor.getLearningPathsCreados()) {
            if (lp.getTitulo().equalsIgnoreCase(titulo)) {
                for (Actividad actividad : lp.getActividades()) {
                    profesor.calificarActividad(actividad, scanner);
                }
                return;
            }
        }
        System.out.println("Learning Path no encontrado.");
    }

    private static void verNotificaciones(Profesor profesor) {
        System.out.println("\n=== VER NOTIFICACIONES ===");
        System.out.println("Función de notificaciones aún no implementada completamente.");
    }
}