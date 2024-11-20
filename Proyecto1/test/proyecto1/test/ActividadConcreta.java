package proyecto1.test;

import proyecto.Actividad;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;

public class ActividadConcreta extends Actividad {

    public ActividadConcreta(LearningPath lp, String descripcion, String objetivo, String nivelDificultad,
                             int duracionEsperada, boolean obligatorio, Profesor creador) {
        super(lp, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, creador);
    }

    @Override
    public void realizar(ProgresoActividad progreso) {
        
    }

    @Override
    public void editar(Profesor editor) {
        
    }
}
//creacion