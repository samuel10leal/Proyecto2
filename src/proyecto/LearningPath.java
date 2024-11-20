package proyecto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LearningPath {
	
	//Atributos
	private String titulo;
    private String descripcion;
    private String objetivos;
    private String nivelDificultad;
    private int duracionEstimada;
    private double rating;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private int version;
    private List<Actividad> actividades;
    private Profesor creador; // El profesor que creó el Learning Path
	private ArrayList progresos;
    
 // Constructor
    public LearningPath(String titulo, String descripcion, String objetivos, String nivelDificultad, Profesor creador, int duracionEstimada) {
        super();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.objetivos = objetivos;
        this.nivelDificultad = nivelDificultad;
        this.creador = creador;
        this.actividades = new ArrayList<>();
        this.progresos = new ArrayList<>(); // Inicializamos la lista de progresos
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
        this.duracionEstimada = duracionEstimada;
        this.version = 1;
        this.rating = calcularPromedioRating();
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public List<ProgresoPath> getProgresos() {
        return progresos;
    }

    public Profesor getCreador() {
        return creador;
    }

    public void setFechaModificacion(Date date) {
        this.fechaModificacion = date;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public double calcularPromedioRating() {
        if (actividades.isEmpty()) {
            return 0;
        }
        float total = 0;
        int count = 0;
        for (Actividad actividad : actividades) {
            float rating = actividad.calcularPromedioRating();
            if (rating > 0) {
                total += rating;
                count++;
            }
        }
        return count > 0 ? total / count : 0;
    }

    // Métodos adicionales
    public void inscribirEstudiante(Estudiante estudiante) {
        ProgresoPath nuevoProgreso = new ProgresoPath(this, new Date(), estudiante);
        progresos.add(nuevoProgreso);
    }

    public void mostrarEstructura() {
        System.out.println("Estructura del Learning Path: " + this.titulo);
        for (Actividad actividad : actividades) {
            System.out.println("Actividad: " + actividad.getDescripcion());
            System.out.println(" - Nivel de dificultad: " + actividad.getNivelDificultad());
            System.out.println(" - Duración: " + actividad.getDuracionEsperada() + " minutos");
            System.out.println(" - Objetivo: " + actividad.getObjetivo());
        }
    }

	public void añadirTiempoLp(Actividad actividad) {
		// TODO Auto-generated method stub
		
	}

	public void reducirTiempoLp(Actividad actividad) {
		// TODO Auto-generated method stub
		
	}
}

