package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import Persistencia.PersistenciaUsuarios;
import proyecto.Estudiante;
import proyecto.Usuario;

public class ConsolaEstudianteSwing extends JFrame {
    private PersistenciaUsuarios persistencia; // Instancia de PersistenciaUsuarios para cargar y guardar usuarios
    private Estudiante estudianteActual; // Almacena el estudiante que ha iniciado sesión
    private List<Usuario> usuarios; // Lista que contiene todos los usuarios registrados

    // Constructor que inicializa la persistencia y carga la lista de usuarios
    public ConsolaEstudianteSwing(PersistenciaUsuarios persistencia) {
        this.persistencia = persistencia;
        this.usuarios = new ArrayList<>(); // Inicializa la lista de usuarios
        cargarUsuarios(); // Carga la lista de usuarios desde un archivo

        // Configuración de la ventana principal
        setTitle("Consola del Estudiante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Crear un panel con imagen de fondo
        ImagePanel panel = new ImagePanel(new ImageIcon("imagenes/fondoEstudiante.png").getImage());
        panel.setLayout(new GridBagLayout());

        // Crear un subpanel para los botones
        JPanel subPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        subPanel.setOpaque(false); // Hacer el subpanel transparente para ver la imagen de fondo

        // Botones del menú
        JButton btnRegistro = new JButton("Registrar Estudiante");
        JButton btnIniciar = new JButton("Iniciar Sesión");
        JButton btnVerLearning = new JButton("Ver Learning Paths y Actividades");
        JButton btnRealizarActividades = new JButton("Realizar Actividad");
        JButton btnSalir = new JButton("Salir");

        // Añadir los botones al subpanel
        subPanel.add(btnRegistro);
        subPanel.add(btnIniciar);
        subPanel.add(btnVerLearning);
        subPanel.add(btnRealizarActividades);
        subPanel.add(btnSalir);

        // Centrar el subpanel en el panel principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(subPanel, gbc);

        // Acción para el botón de registro
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarEstudiante();
            }
        });

        // Acción para el botón de iniciar sesión
        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginEstudiante();
            }
        });

        // Acción para el botón de ver Learning Paths y actividades
        btnVerLearning.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verLearningPathsYActividades();
            }
        });

        // Acción para el botón de realizar actividades
        btnRealizarActividades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarActividad();
            }
        });

        // Acción para el botón de salir
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Añadir el panel principal a la ventana
        add(panel);

        // Hacer visible la ventana
        setVisible(true);
    }

    // Carga los usuarios desde un archivo usando la persistencia
    private void cargarUsuarios() {
        try {
            usuarios = persistencia.cargarUsuarios("usuarios.json"); // Carga los usuarios desde el archivo JSON
            System.out.println("Usuarios cargados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage()); // En caso de error, muestra un mensaje
        }
    }

    // Registra un nuevo estudiante
    private void registrarEstudiante() {
        String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre:"); // Lee el nombre del estudiante
        String correo = JOptionPane.showInputDialog(this, "Introduce el correo:"); // Lee el correo del estudiante
        String contrasena = JOptionPane.showInputDialog(this, "Introduce la contraseña:"); // Lee la contraseña del estudiante

        Estudiante estudiante = new Estudiante(nombre, correo, contrasena); // Crea un nuevo objeto Estudiante

        // Verifica si ya existe un usuario con el mismo correo
        boolean correoExistente = usuarios.stream().anyMatch(u -> u.getCorreo().equals(correo));
        if (correoExistente) {
            JOptionPane.showMessageDialog(this, "Error: Ya existe un usuario registrado con este correo.");
            return; // Si el correo ya está registrado, no se realiza el registro
        }

        usuarios.add(estudiante); // Añade el nuevo estudiante a la lista local de usuarios

        try {
            persistencia.salvarUsuarios("usuarios.json", usuarios); // Guarda la lista de usuarios actualizada en el archivo JSON
            JOptionPane.showMessageDialog(this, "Estudiante registrado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos del usuario: " + e.getMessage()); // En caso de error al guardar, muestra el mensaje
        }
    }

    // Inicia sesión con las credenciales del estudiante
    private void loginEstudiante() {
        String correo = JOptionPane.showInputDialog(this, "Introduce el correo:"); // Lee el correo ingresado
        String contrasena = JOptionPane.showInputDialog(this, "Introduce la contraseña:"); // Lee la contraseña ingresada

        // Busca un usuario con el correo y contraseña ingresados
        Usuario usuarioEncontrado = usuarios.stream()
                .filter(u -> u.getCorreo().equals(correo) && u.getContrasena().equals(contrasena))
                .findFirst() // Obtiene el primer usuario que cumpla con los criterios
                .orElse(null); // Si no se encuentra, devuelve null

        if (usuarioEncontrado instanceof Estudiante estudiante) { // Si el usuario encontrado es un estudiante
            this.estudianteActual = estudiante; // Establece al estudiante como el estudiante actual
            JOptionPane.showMessageDialog(this, "Bienvenido, " + estudiante.getNombre() + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos. Intente de nuevo."); // Si no se encuentra el usuario, muestra mensaje de error
        }
    }

    // Muestra los Learning Paths y actividades disponibles
    private void verLearningPathsYActividades() {
        if (estudianteActual == null) { // Verifica si el estudiante ha iniciado sesión
            JOptionPane.showMessageDialog(this, "Por favor, inicia sesión primero.");
            return; // Si no ha iniciado sesión, muestra el mensaje y termina el método
        }

        JOptionPane.showMessageDialog(this, "===== Learning Paths y Actividades =====\nFuncionalidad en desarrollo."); // Mensaje temporal mientras se desarrolla la funcionalidad
    }

    // Permite al estudiante realizar una actividad
    private void realizarActividad() {
        if (estudianteActual == null) { // Verifica si el estudiante ha iniciado sesión
            JOptionPane.showMessageDialog(this, "Por favor, inicia sesión primero.");
            return; // Si no ha iniciado sesión, muestra el mensaje y termina el método
        }

        JOptionPane.showMessageDialog(this, "===== Realizar Actividad =====\nFuncionalidad en desarrollo."); // Mensaje temporal mientras se desarrolla la funcionalidad
    }

    // Método principal que inicia la aplicación
    public static void main(String[] args) {
        PersistenciaUsuarios persistencia = new PersistenciaUsuarios(); // Crea una instancia de PersistenciaUsuarios
        new ConsolaEstudianteSwing(persistencia); // Crea una instancia de ConsolaEstudianteSwing y la muestra
    }
}

// Clase para crear un panel con imagen de fondo
class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}