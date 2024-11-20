package proyecto1.test;

import proyecto.Actividad;
import proyecto.Reseña;
import proyecto.Usuario;

//Por lo que usuario es abstracta
public class UsuarioConcreto extends Usuario {

    public UsuarioConcreto(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena);
    }

    @Override
    public void verLearningPaths() {
        // Implementación de ejemplo
        System.out.println("Viendo learning paths");
    }

    @Override
    public String getTipoUsuario() {
        return "Concreto";
    }

    @Override
    public void darReseñaActividad(Actividad actividad, String texto, float rating) {
        // Implementación de ejemplo
        Reseña reseña = new Reseña(texto, rating);
        actividad.agregarReseña(reseña);
    }
}