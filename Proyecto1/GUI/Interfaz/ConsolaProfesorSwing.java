package Interfaz;

import Persistencia.PersistenciaProgreso;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import proyecto.Actividad;
import proyecto.Encuesta;
import proyecto.Estudiante;
import proyecto.Examen;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.Quiz;
import proyecto.RecursoEducativo;
import proyecto.Registro;
import proyecto.Tarea;
import proyecto1.test.ActividadConcreta;
import java.util.List;
import java.util.ArrayList;

public class ConsolaProfesorSwing {
    private Registro registro;
    private Profesor profesorActual;

    public ConsolaProfesorSwing() {
        registro = new Registro();
        profesorActual = null;

        // Cargar usuarios desde archivo JSON
        try {
            registro.cargarUsuarios("usuarios.json");
            JOptionPane.showMessageDialog(null, "Usuarios cargados exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Crear ventana principal
        JFrame frame = new JFrame("Consola del Profesor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(8, 1));

        // Botones del menú
        JButton btnRegistro = new JButton("Registrar/Iniciar sesión");
        JButton btnCrearLP = new JButton("Crear Learning Path");
        JButton btnEditarLP = new JButton("Editar/Eliminar Learning Path");
        JButton btnActividades = new JButton("Añadir/Editar actividades");
        JButton btnProgreso = new JButton("Ver progreso de estudiantes");
        JButton btnCalificar = new JButton("Calificar actividades");
        JButton btnNotificaciones = new JButton("Ver notificaciones");
        JButton btnSalir = new JButton("Salir");

        // Agregar botones al frame
        frame.add(btnRegistro);
        frame.add(btnCrearLP);
        frame.add(btnEditarLP);
        frame.add(btnActividades);
        frame.add(btnProgreso);
        frame.add(btnCalificar);
        frame.add(btnNotificaciones);
        frame.add(btnSalir);

        // Configurar acciones de los botones
        btnRegistro.addActionListener(e -> manejarRegistroInicioSesion());
        btnCrearLP.addActionListener(e -> crearLearningPath());
        btnEditarLP.addActionListener(e -> editarEliminarLearningPath());
        btnActividades.addActionListener(e -> añadirActividad());
        btnProgreso.addActionListener(e -> verProgresoEstudiantes());
        btnCalificar.addActionListener(e -> calificarActividades());
        btnNotificaciones.addActionListener(e -> verNotificaciones());
        btnSalir.addActionListener(e -> System.exit(0));

        // Mostrar ventana
        frame.setVisible(true);
    }

    private void manejarRegistroInicioSesion() {
        JTextField correoField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
            "Correo:", correoField,
            "Contraseña:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Inicio de Sesión", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                profesorActual = registro.loginProfesor(correoField.getText(), new String(passwordField.getPassword()));
                JOptionPane.showMessageDialog(null, "Bienvenido, " + profesorActual.getNombre() + "!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void crearLearningPath() {
        if (!validarSesion()) return;

        JTextField tituloField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField objetivosField = new JTextField();
        JTextField nivelDificultadField = new JTextField();
        JTextField duracionField = new JTextField();

        Object[] fields = {
            "Título:", tituloField,
            "Descripción:", descripcionField,
            "Objetivos:", objetivosField,
            "Nivel de dificultad:", nivelDificultadField,
            "Duración estimada (en días):", duracionField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Crear Learning Path", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                profesorActual.crearLearningPath(
                    tituloField.getText(),
                    descripcionField.getText(),
                    objetivosField.getText(),
                    nivelDificultadField.getText(),
                    Integer.parseInt(duracionField.getText()),
                    registro
                );
                JOptionPane.showMessageDialog(null, "Learning Path creado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al crear Learning Path: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarEliminarLearningPath() {
        if (!validarSesion()) return;

        // Obtener la lista de Learning Paths creados por el profesor
        if (profesorActual.getLearningPathsCreados().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se ha creado ningún Learning Path.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Mostrar lista de Learning Paths
        String[] opciones = profesorActual.getLearningPathsCreados()
                .stream()
                .map(LearningPath::getTitulo)
                .toArray(String[]::new);

        String titulo = (String) JOptionPane.showInputDialog(
                null, 
                "Seleccione un Learning Path:", 
                "Editar/Eliminar",
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                opciones.length > 0 ? opciones[0] : null);

        if (titulo != null) {
            // Opciones de edición o eliminación
            String[] acciones = {"Editar", "Eliminar"};
            int accion = JOptionPane.showOptionDialog(
                    null, 
                    "¿Qué desea hacer?", 
                    "Acción",
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    acciones, 
                    acciones[0]);

            if (accion == 0) { // Editar
                String nuevaDescripcion = JOptionPane.showInputDialog("Nueva descripción:");
                if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
                    profesorActual.getLearningPathsCreados()
                            .stream()
                            .filter(lp -> lp.getTitulo().equalsIgnoreCase(titulo))
                            .findFirst()
                            .ifPresent(lp -> lp.setDescripcion(nuevaDescripcion));
                    JOptionPane.showMessageDialog(null, "Descripción actualizada.");
                } else {
                    JOptionPane.showMessageDialog(null, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (accion == 1) { // Eliminar
                int confirmacion = JOptionPane.showConfirmDialog(
                        null, 
                        "¿Está seguro de que desea eliminar este Learning Path?", 
                        "Confirmación", 
                        JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    profesorActual.getLearningPathsCreados()
                            .removeIf(lp -> lp.getTitulo().equalsIgnoreCase(titulo));
                    JOptionPane.showMessageDialog(null, "Learning Path eliminado.");
                }
            }
        }
    }


    private boolean validarSesion() {
        if (profesorActual == null) {
            JOptionPane.showMessageDialog(null, "Debe iniciar sesión antes de acceder a esta funcionalidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void añadirActividad() {
        if (!validarSesion()) {
            return;
        }

        // Solicitar Learning Path
        String tituloLP = JOptionPane.showInputDialog("Ingrese el título del Learning Path:");
        if (tituloLP == null || tituloLP.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El título del Learning Path no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Solicitar detalles de la actividad
        String tituloActividad = JOptionPane.showInputDialog("Ingrese el título de la nueva actividad:");
        if (tituloActividad == null || tituloActividad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El título de la actividad no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción de la nueva actividad:");
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción de la actividad no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String objetivo = JOptionPane.showInputDialog("Ingrese el objetivo de la nueva actividad:");
        if (objetivo == null || objetivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El objetivo de la actividad no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nivelDificultad = JOptionPane.showInputDialog("Ingrese el nivel de dificultad de la nueva actividad:");
        if (nivelDificultad == null || nivelDificultad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nivel de dificultad de la actividad no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String duracionEsperadaStr = JOptionPane.showInputDialog("Ingrese la duración esperada de la nueva actividad (en horas):");
        if (duracionEsperadaStr == null || duracionEsperadaStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La duración esperada de la actividad no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int duracionEsperada;
        try {
            duracionEsperada = Integer.parseInt(duracionEsperadaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La duración esperada debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Preguntar si la actividad es obligatoria
        int obligatorioOption = JOptionPane.showConfirmDialog(null, "¿Es esta actividad obligatoria?", "Obligatoriedad", JOptionPane.YES_NO_OPTION);
        boolean obligatorio = (obligatorioOption == JOptionPane.YES_OPTION);

        Profesor creador = profesorActual; // Asumiendo que el creador es el profesor actual

        // Mostrar un cuadro de selección para elegir el tipo de actividad
        Object[] opcionesActividad = {"Recurso Educativo", "Encuesta", "Tarea", "Quiz", "Examen"};
        String tipoActividad = (String) JOptionPane.showInputDialog(
                null, 
                "Seleccione el tipo de actividad", 
                "Tipo de Actividad", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                opcionesActividad, 
                opcionesActividad[0]
        );

        if (tipoActividad == null) {
            return; // El usuario canceló
        }

        // Crear la actividad según el tipo seleccionado
        profesorActual.getLearningPathsCreados()
                .stream()
                .filter(lp -> lp.getTitulo().equalsIgnoreCase(tituloLP))
                .findFirst()
                .ifPresentOrElse(lp -> {
                    Actividad nuevaActividad = null;

                    switch (tipoActividad) {
                        case "Quiz":
                            // Crear un Quiz y agregarlo al Learning Path
                            double notaAprobacion = solicitarNotaAprobacion(); // Método que solicita la nota de aprobación
                            nuevaActividad = new Quiz(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, notaAprobacion, creador);

                            // Solicitar las preguntas para el Quiz
                            int cantidadPreguntasQuiz = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas preguntas deseas agregar al Quiz?"));
                            for (int i = 0; i < cantidadPreguntasQuiz; i++) {
                                // Solicitar pregunta y opciones
                                ((Quiz) nuevaActividad).agregarPregunta(new Scanner(System.in)); // Agrega las preguntas usando Scanner
                            }
                            break;
                        case "Examen":
                            // Crear un Examen y agregarlo al Learning Path
                            nuevaActividad = new Examen(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, creador);

                            // Solicitar las preguntas para el Examen
                            int cantidadPreguntasExamen = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas preguntas deseas agregar al Examen?"));
                            for (int i = 0; i < cantidadPreguntasExamen; i++) {
                                // Solicitar pregunta abierta para el examen
                                ((Examen) nuevaActividad).agregarPregunta(new Scanner(System.in)); // Agrega las preguntas usando Scanner
                            }
                            break;
                        case "Encuesta":
                            // Crear una Encuesta y agregarla al Learning Path
                            nuevaActividad = new Encuesta(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, creador);

                            // Solicitar las preguntas para la Encuesta
                            int cantidadPreguntasEncuesta = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas preguntas deseas agregar a la Encuesta?"));
                            for (int i = 0; i < cantidadPreguntasEncuesta; i++) {
                                // Solicitar pregunta abierta para la encuesta
                                ((Encuesta) nuevaActividad).agregarPregunta(new Scanner(System.in)); // Agrega las preguntas usando Scanner
                            }
                            break;
                        case "Recurso Educativo":
                            // Solicitar tipo de recurso y enlace
                            String tipoRecurso = JOptionPane.showInputDialog("Ingrese el tipo de recurso educativo (por ejemplo, video, artículo, etc.):");
                            String enlaceRecurso = JOptionPane.showInputDialog("Ingrese el enlace del recurso educativo:");

                            nuevaActividad = new RecursoEducativo(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, tipoRecurso, enlaceRecurso, creador);
                            break;
                        case "Tarea":
                            nuevaActividad = new Tarea(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, creador);
                            break;
                    }

                    if (nuevaActividad != null) {
                        lp.getActividades().add(nuevaActividad);
                        JOptionPane.showMessageDialog(null, "Actividad añadida exitosamente.");
                    }
                }, () -> {
                    JOptionPane.showMessageDialog(null, "Learning Path no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                });
    }

    // Método auxiliar para solicitar la nota de aprobación del Quiz
    private double solicitarNotaAprobacion() {
        String input = JOptionPane.showInputDialog("Ingrese la nota de aprobación (0-10) para el Quiz:");
        try {
            double nota = Double.parseDouble(input);
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(null, "La nota de aprobación debe estar entre 0 y 10.", "Error", JOptionPane.ERROR_MESSAGE);
                return solicitarNotaAprobacion();
            }
            return nota;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada no válida. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return solicitarNotaAprobacion();
        }
    }

    private void verProgresoEstudiantes() {
         if (!validarSesion()) return;

        PersistenciaProgreso persistenciaProgreso = new PersistenciaProgreso();
        try {
            persistenciaProgreso.cargarProgresoEstudiantes("progresoEstudiantes.json", registro);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar el progreso de los estudiantes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de estudiantes y su progreso
        StringBuilder progreso = new StringBuilder();
        List<Estudiante> estudiantes = registro.getEstudiantes();
        progreso.append("Número de estudiantes: ").append(estudiantes.size()).append("\n");

        for (Estudiante estudiante : estudiantes) {
            progreso.append("Estudiante: ").append(estudiante.getNombre()).append("\n");
            List<LearningPath> learningPaths = registro.getLearningPaths(estudiante.getNombre());
            if (learningPaths.isEmpty()) {
                progreso.append("  No hay Learning Paths asociados a este estudiante.\n");
            } else {
                for (LearningPath lp : learningPaths) {
                    progreso.append("  Learning Path: ").append(lp.getTitulo()).append("\n");
                    int progresoEstudiante = lp.getProgreso(estudiante);
                    progreso.append("    Progreso: ").append(progresoEstudiante).append("%\n");
                }
            }
        }

        // Mostrar el progreso en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, progreso.toString(), "Progreso de Estudiantes", JOptionPane.INFORMATION_MESSAGE);
    }

   private void calificarActividades() {
        if (!validarSesion()) return;

        // Solicitar el nombre del estudiante
        String nombreEstudiante = JOptionPane.showInputDialog("Ingrese el nombre del estudiante:");
        if (nombreEstudiante == null || nombreEstudiante.trim().isEmpty()) {
            return; // Terminar si el nombre está vacío o se cancela
        }

        // Obtener la lista de Learning Paths del estudiante
        List<LearningPath> learningPaths = registro.getLearningPaths(nombreEstudiante);
        if (learningPaths.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron Learning Paths para el estudiante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Seleccionar el Learning Path
        String[] learningPathTitles = learningPaths.stream()
                                                   .map(LearningPath::getTitulo)
                                                   .toArray(String[]::new);
        String tituloLearningPath = (String) JOptionPane.showInputDialog(null, "Seleccione un Learning Path:", "Calificar Actividad", JOptionPane.QUESTION_MESSAGE, null, learningPathTitles, learningPathTitles[0]);
        if (tituloLearningPath == null) return;

        // Obtener el Learning Path seleccionado
        LearningPath learningPath = learningPaths.stream()
                                                 .filter(lp -> lp.getTitulo().equals(tituloLearningPath))
                                                 .findFirst()
                                                 .orElse(null);
        if (learningPath == null) {
            JOptionPane.showMessageDialog(null, "Learning Path no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de actividades del Learning Path
        List<Actividad> actividadesList = learningPath.getActividades();
        String[] actividades = actividadesList.stream()
                                              .map(Actividad::getTitulo)
                                              .toArray(String[]::new);

        // Seleccionar la actividad a calificar
        String tituloActividad = (String) JOptionPane.showInputDialog(null, "Seleccione una actividad:", "Calificar Actividad", JOptionPane.QUESTION_MESSAGE, null, actividades, actividades[0]);
        if (tituloActividad == null) return;

        // Solicitar la calificación
        String calificacionStr = JOptionPane.showInputDialog("Ingrese la calificación:");
        if (calificacionStr == null || calificacionStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La calificación no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double calificacion = Double.parseDouble(calificacionStr);
            learningPath.calificarActividad(nombreEstudiante, tituloActividad, calificacion);
            JOptionPane.showMessageDialog(null, "Actividad calificada exitosamente.");

            // Crear un objeto JSON con la calificación manualmente
            CalificacionJson calificacionJson = new CalificacionJson(nombreEstudiante, tituloLearningPath, tituloActividad, calificacion);
            String json = calificacionJson.toJson();
            System.out.println("Calificación en JSON: " + json);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La calificación debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
   }
    
    private void verNotificaciones() {
          if (!validarSesion()) return;

        if (profesorActual.getNotificaciones().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tienes notificaciones.", "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder notificaciones = new StringBuilder();
        for (String notificacion : profesorActual.getNotificaciones()) {
            notificaciones.append(notificacion).append("\n");
        }

        JOptionPane.showMessageDialog(null, notificaciones.toString(), "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new ConsolaProfesorSwing();
    }
}
