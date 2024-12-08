package proyecto;

public class ActividadConcreta extends Actividad {

    public ActividadConcreta(LearningPath lp, String descripcion, String objetivo, String nivelDificultad,
                             int duracionEsperada, boolean obligatorio, Profesor creador, String titulo, Double calificacion) {
        super(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, creador);
        this.titulo = titulo;
        this.calificacion = calificacion;
    }

    @Override
    public void realizar(ProgresoActividad progreso) {
        System.out.println("Realizando la actividad: " + this.titulo);
    }

    @Override
    public void editar(Profesor editor) {
        System.out.println("Editando la actividad: " + this.titulo);
    }
}