package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import proyecto.Profesor;
import proyecto.Registro;

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
        btnActividades.addActionListener(e -> añadirEditarActividades());
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

        // Mostrar lista de Learning Paths
        String[] opciones = profesorActual.getLearningPathsCreados()
                .stream()
                .map(lp -> lp.getTitulo())
                .toArray(String[]::new);

        String titulo = (String) JOptionPane.showInputDialog(null, "Seleccione un Learning Path:", "Editar/Eliminar",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (titulo != null) {
            // Opciones de edición o eliminación
            String[] acciones = {"Editar", "Eliminar"};
            int accion = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Acción",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, acciones, acciones[0]);

            if (accion == 0) { // Editar
                String nuevaDescripcion = JOptionPane.showInputDialog("Nueva descripción:");
                if (nuevaDescripcion != null) {
                    profesorActual.getLearningPathsCreados()
                            .stream()
                            .filter(lp -> lp.getTitulo().equalsIgnoreCase(titulo))
                            .findFirst()
                            .ifPresent(lp -> lp.setDescripcion(nuevaDescripcion));
                    JOptionPane.showMessageDialog(null, "Descripción actualizada.");
                }
            } else if (accion == 1) { // Eliminar
                profesorActual.getLearningPathsCreados()
                        .removeIf(lp -> lp.getTitulo().equalsIgnoreCase(titulo));
                JOptionPane.showMessageDialog(null, "Learning Path eliminado.");
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

    private void añadirEditarActividades() {
        JOptionPane.showMessageDialog(null, "Funcionalidad aún no implementada.");
    }

    private void verProgresoEstudiantes() {
        JOptionPane.showMessageDialog(null, "Funcionalidad aún no implementada.");
    }

    private void calificarActividades() {
        JOptionPane.showMessageDialog(null, "Funcionalidad aún no implementada.");
    }

    private void verNotificaciones() {
        JOptionPane.showMessageDialog(null, "Funcionalidad aún no implementada.");
    }

    public static void main(String[] args) {
        new ConsolaProfesorSwing();
    }
}