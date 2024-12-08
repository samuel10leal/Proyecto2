package Interfaz;

public class CalificacionJson {
    private String nombreEstudiante;
    private String tituloLearningPath;
    private String tituloActividad;
    private double calificacion;

    // Constructor
    public CalificacionJson(String nombreEstudiante, String tituloLearningPath, String tituloActividad, double calificacion) {
        this.nombreEstudiante = nombreEstudiante;
        this.tituloLearningPath = tituloLearningPath;
        this.tituloActividad = tituloActividad;
        this.calificacion = calificacion;
    }

    // Getters y Setters (omitidos para brevedad)

    // Método para convertir el objeto a JSON
    public String toJson() {
        return "{" +
                "\"nombreEstudiante\":\"" + nombreEstudiante + "\"," +
                "\"tituloLearningPath\":\"" + tituloLearningPath + "\"," +
                "\"tituloActividad\":\"" + tituloActividad + "\"," +
                "\"calificacion\":" + calificacion +
                "}";
    }

    @Override
    public String toString() {
        return "CalificacionJson{" +
                "nombreEstudiante='" + nombreEstudiante + '\'' +
                ", tituloLearningPath='" + tituloLearningPath + '\'' +
                ", tituloActividad='" + tituloActividad + '\'' +
                ", calificacion=" + calificacion +
                '}';
    }
}