package Interfaz;

import javax.swing.*;

import Persistencia.PersistenciaUsuarios;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Cargar la imagen de fondo
        Image fondo = new ImageIcon("imagenes/fondoEstudiante.png").getImage();
        
        // Crear el panel con fondo
        ImagePanel panelPrincipal = new ImagePanel(fondo);
        panelPrincipal.setLayout(new BorderLayout());
        
        // Configuración de la ventana principal
        setTitle("Sistema de Gestión - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        
        // Crear un panel para contener los botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setOpaque(false); // Hacerlo transparente para que se vea el fondo

        // Etiqueta de bienvenida
        JLabel lblBienvenida = new JLabel("Seleccione el tipo de usuario:");
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE); // Color de texto blanco para que resalte sobre el fondo
        panelBotones.add(lblBienvenida);

        // Botón para Estudiantes
        JButton btnEstudiante = new JButton("Soy Estudiante");
        btnEstudiante.setFont(new Font("Arial", Font.BOLD, 14));
        btnEstudiante.setForeground(Color.WHITE);
        btnEstudiante.setBackground(new Color(60, 179, 113)); // Color verde claro
        btnEstudiante.setFocusPainted(false);
        btnEstudiante.addActionListener(e -> {
            PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
            new ConsolaEstudianteSwing(persistencia); // Lanza la interfaz de estudiantes
            dispose(); // Cierra la ventana principal
        });
        panelBotones.add(btnEstudiante);

        // Botón para Profesores
        JButton btnProfesor = new JButton("Soy Profesor");
        btnProfesor.setFont(new Font("Arial", Font.BOLD, 14));
        btnProfesor.setForeground(Color.WHITE);
        btnProfesor.setBackground(new Color(60, 179, 113)); // Color verde claro
        btnProfesor.setFocusPainted(false);
        btnProfesor.addActionListener(e -> {
            
            new ConsolaProfesorSwing(); // Lanza la interfaz de profesores
            dispose(); // Cierra la ventana principal
        });
        panelBotones.add(btnProfesor);

        // Añadir el panel de botones sobre el panel principal
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Añadir el panel principal a la ventana
        add(panelPrincipal);

        // Mostrar la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new); // Iniciar la ventana principal
    }
}

