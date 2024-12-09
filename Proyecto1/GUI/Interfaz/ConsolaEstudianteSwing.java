package Interfaz;


import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import Persistencia.PersistenciaUsuarios;
import proyecto.Estudiante;
import proyecto.Usuario;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
public class ConsolaEstudianteSwing extends JFrame {
    private PersistenciaUsuarios persistencia; // Instancia de PersistenciaUsuarios para cargar y guardar usuarios
    private Estudiante estudianteActual; // Almacena el estudiante que ha iniciado sesión
    private List<Usuario> usuarios; // Lista que contiene todos los usuarios registrados
    private Map<String, Map<String, Integer>> actividadesPorDia = new HashMap<>();
 // Declaración de Scanner para leer desde la consola
    private Scanner scanner = new Scanner(System.in);

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
        JButton btnRegistro = crearBotonConEstilo("Registrar Estudiante", "imagenes/iconoRegistro.png");
        JButton btnIniciar = crearBotonConEstilo("Iniciar Sesión", "imagenes/iconoIniciar.png");
        JButton btnVerLearning = crearBotonConEstilo("Ver Learning Paths y Actividades", "imagenes/iconoLearning.png");
        JButton btnRealizarActividades = crearBotonConEstilo("Realizar Actividad", "imagenes/iconoActividad.png");
        JButton btnSalir = crearBotonConEstilo("Salir", "imagenes/iconoSalir.png");
        JButton btnVerGrafico = crearBotonConEstilo("Ver Actividades Anuales", "imagenes/iconoGrafico.png");
        subPanel.add(btnVerGrafico);

        btnVerGrafico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarGraficoActividades();
            }
        });

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
        if (estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Por favor, inicia sesión primero.");
            return;
        }

        String archivoLearningPaths = "datos/learningpaths.json";
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoLearningPaths))) {
            StringBuilder jsonContent = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                jsonContent.append(linea);
            }

            JSONArray learningPathsArray = new JSONArray(jsonContent.toString());

            if (learningPathsArray.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay Learning Paths disponibles.");
                return;
            }

            // Crear un array de títulos de Learning Paths para mostrar en el JList
            String[] learningPathsTitles = new String[learningPathsArray.length()];
            for (int i = 0; i < learningPathsArray.length(); i++) {
                learningPathsTitles[i] = learningPathsArray.getJSONObject(i).getString("titulo");
            }

            // Crear un JList para mostrar los títulos de los Learning Paths
            JList<String> listLearningPaths = new JList<>(learningPathsTitles);
            listLearningPaths.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(listLearningPaths);
            scrollPane.setPreferredSize(new Dimension(300, 150));

            int option = JOptionPane.showConfirmDialog(this, scrollPane, "Selecciona un Learning Path", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                // Obtener el Learning Path seleccionado
                String selectedTitle = listLearningPaths.getSelectedValue();
                if (selectedTitle != null) {
                    // Buscar el Learning Path seleccionado y mostrar sus detalles
                    for (int i = 0; i < learningPathsArray.length(); i++) {
                        JSONObject lp = learningPathsArray.getJSONObject(i);
                        if (lp.getString("titulo").equals(selectedTitle)) {
                            StringBuilder info = new StringBuilder();
                            info.append("Learning Path: ").append(lp.getString("titulo")).append("\n")
                                .append("Descripción: ").append(lp.getString("descripcion")).append("\n")
                                .append("Objetivos: ").append(lp.getString("objetivos")).append("\n")
                                .append("Nivel de Dificultad: ").append(lp.getString("nivelDificultad")).append("\n")
                                .append("Duración Estimada: ").append(lp.getInt("duracionEstimada")).append(" días\n")
                                .append("Actividades:\n");

                            // Mostrar las actividades de ese Learning Path
                            JSONArray actividades = lp.getJSONArray("actividades");
                            for (int j = 0; j < actividades.length(); j++) {
                                JSONObject actividad = actividades.getJSONObject(j);
                                info.append("  - ").append(actividad.getString("descripcion"))
                                    .append(" (Tipo: ").append(actividad.getString("tipo"))
                                    .append(", Dificultad: ").append(actividad.getString("nivelDificultad"))
                                    .append(")\n");
                                info.append("    Objetivo: ").append(actividad.getString("objetivo"))
                                    .append(", Duración Esperada: ").append(actividad.getInt("duracionEsperada")).append(" minutos\n");
                            }

                            JOptionPane.showMessageDialog(this, info.toString(), "Detalles del Learning Path", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los Learning Paths: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    
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

            // Cargar actividades en la lista
            for (int i = 0; i < learningPathsArray.length(); i++) {
                JSONArray actividades = learningPathsArray.getJSONObject(i).getJSONArray("actividades");
                for (int j = 0; j < actividades.length(); j++) {
                    JSONObject actividad = actividades.getJSONObject(j);
                    actividadesDisponibles.add(actividad);
                }
            }

            if (actividadesDisponibles.isEmpty()) {
                System.out.println("No hay actividades disponibles para realizar.");
                return;
            }

            // Mostrar actividades con botones
            mostrarActividadesConDialogo(actividadesDisponibles);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de Learning Paths: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("Error al procesar el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }


    private void registrarActividad() {
        if (estudianteActual == null) {
            System.out.println("No hay estudiante logueado.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaHoy = sdf.format(new Date());

        actividadesPorDia.putIfAbsent(fechaHoy, new HashMap<>());

        Map<String, Integer> actividadesHoy = actividadesPorDia.get(fechaHoy);

        actividadesHoy.put(estudianteActual.getCorreo(),
                actividadesHoy.getOrDefault(estudianteActual.getCorreo(), 0) + 1);

        System.out.println("Actividad registrada para el día: " + fechaHoy);
    }

    private void mostrarGraficoActividades() {
        if (estudianteActual == null) {
            JOptionPane.showMessageDialog(null, "Por favor, inicia sesión primero.");
            return;
        }

        JFrame frame = new JFrame("Actividades Anuales");
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panelGrafico = new JPanel(new GridLayout(7, 53, 2, 2));
        panelGrafico.setBackground(Color.WHITE);

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String dia : diasSemana) {
            JLabel labelDia = new JLabel(dia, SwingConstants.CENTER);
            labelDia.setOpaque(true);
            labelDia.setBackground(Color.LIGHT_GRAY);
            labelDia.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelGrafico.add(labelDia);
        }

        if (actividadesPorDia == null) {
            actividadesPorDia = new HashMap<>();
        }

        LocalDate inicioAno = LocalDate.of(2024, 1, 1);
        int diaInicialSemana = inicioAno.getDayOfWeek().getValue();

        for (int i = 1; i < diaInicialSemana; i++) {
            panelGrafico.add(new JLabel(""));
        }

        LocalDate fechaActual = inicioAno;
        int totalDiasAno = inicioAno.isLeapYear() ? 366 : 365;

        for (int dia = 1; dia <= totalDiasAno; dia++) {
            JLabel celda = new JLabel("", SwingConstants.CENTER);
            celda.setOpaque(true);
            celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            String fecha = fechaActual.toString();
            int actividadesRealizadas = actividadesPorDia.getOrDefault(fecha, new HashMap<>())
                                                         .values()
                                                         .stream()
                                                         .mapToInt(Integer::intValue)
                                                         .sum();

            if (actividadesRealizadas > 0) {
                int intensidad = Math.min(255, 255 - (actividadesRealizadas * 20));
                celda.setBackground(new Color(0, intensidad, 0));
            } else {
                celda.setBackground(Color.WHITE);
            }

            celda.setToolTipText("Fecha: " + fecha + " - Actividades: " + actividadesRealizadas);
            panelGrafico.add(celda);

            fechaActual = fechaActual.plusDays(1);
        }

        JPanel panelLeyenda = new JPanel(new FlowLayout());
        panelLeyenda.setBorder(BorderFactory.createTitledBorder("Leyenda"));

        JLabel sinActividad = new JLabel("Sin actividad");
        sinActividad.setOpaque(true);
        sinActividad.setBackground(Color.WHITE);
        sinActividad.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sinActividad.setPreferredSize(new Dimension(100, 20));

        JLabel actividadBaja = new JLabel("1-5 actividades");
        actividadBaja.setOpaque(true);
        actividadBaja.setBackground(new Color(0, 200, 0));
        actividadBaja.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        actividadBaja.setPreferredSize(new Dimension(150, 20));

        JLabel actividadAlta = new JLabel("6+ actividades");
        actividadAlta.setOpaque(true);
        actividadAlta.setBackground(new Color(0, 128, 0));
        actividadAlta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        actividadAlta.setPreferredSize(new Dimension(150, 20));

        panelLeyenda.add(sinActividad);
        panelLeyenda.add(actividadBaja);
        panelLeyenda.add(actividadAlta);

        mainPanel.add(panelGrafico, BorderLayout.CENTER);
        mainPanel.add(panelLeyenda, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    private void mostrarActividadesConDialogo(List<JSONObject> actividadesDisponibles) {
        // Crear un diálogo modal para seleccionar la actividad
        JDialog dialogo = new JDialog((Frame) null, "Seleccionar Actividad", true);
        dialogo.setSize(400, 300);
        dialogo.setLayout(new BorderLayout());
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel para mostrar las actividades
        JPanel panelActividades = new JPanel(new GridLayout(0, 1, 5, 5));
        panelActividades.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (JSONObject actividad : actividadesDisponibles) {
            String descripcion = actividad.getString("descripcion");
            String nivelDificultad = actividad.getString("nivelDificultad");

            JButton botonActividad = new JButton("<html><b>" + descripcion + "</b><br>Nivel: " + nivelDificultad + "</html>");
            botonActividad.addActionListener(e -> {
                realizarActividadSeleccionada(actividad);
                dialogo.dispose(); // Cerrar el diálogo
            });

            panelActividades.add(botonActividad);
        }

        // Scroll para manejar listas largas
        JScrollPane scrollPane = new JScrollPane(panelActividades);
        dialogo.add(scrollPane, BorderLayout.CENTER);

        // Botón para cancelar
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dialogo.dispose());
        JPanel panelBotonCancelar = new JPanel();
        panelBotonCancelar.add(botonCancelar);
        dialogo.add(panelBotonCancelar, BorderLayout.SOUTH);

        dialogo.setLocationRelativeTo(null); // Centrar la ventana
        dialogo.setVisible(true);
    }

    private void realizarActividadSeleccionada(JSONObject actividadSeleccionada) {
        // Mostrar un mensaje de progreso
        JOptionPane.showMessageDialog(null, 
            "Realizando actividad: " + actividadSeleccionada.getString("descripcion") + "\nPor favor espera...", 
            "Realizando Actividad", 
            JOptionPane.INFORMATION_MESSAGE);

        // Simulación de actividad (espera breve)
        try {
            Thread.sleep(2000); // Simulación de tiempo necesario para realizar la actividad
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(null, 
            "¡Actividad completada exitosamente!", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);

        // Registrar la actividad
        registrarActividad();
    }


    







    // Método principal que inicia la aplicación
    public static void main(String[] args) {
        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
        new ConsolaEstudianteSwing(persistencia);
    }

    private JButton crearBotonConEstilo(String texto, String rutaIcono) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(60, 179, 113)); // Color verde claro
        boton.setForeground(Color.WHITE);
        boton.setBorder(new EmptyBorder(10, 10, 10, 10));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Añadir icono si se proporciona
        if (rutaIcono != null && !rutaIcono.isEmpty()) {
            boton.setIcon(new ImageIcon(rutaIcono));
            boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        }

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(46, 139, 87)); // Cambia a un verde más oscuro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(60, 179, 113)); // Vuelve al verde claro
            }
        });

        return boton;
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

