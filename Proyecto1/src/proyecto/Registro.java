package proyecto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import Persistencia.PersistenciaUsuarios;

public class Registro {
	
	//Atributos
	private List<Profesor> profesores;
	private List<Estudiante> estudiantes;
	private List<Usuario> usuarios;
	private List<LearningPath> paths;
    private PersistenciaUsuarios persistencia;
	private Map<String, List<LearningPath>> learningPathsPorEstudiante = new HashMap<>();

    
    //Constructor
    public Registro() {
    	profesores = new ArrayList<>();
		estudiantes = new ArrayList<>();
	usuarios = new ArrayList<>();
        persistencia = new PersistenciaUsuarios();
        paths = new ArrayList<>();
        
	}
	
    public List<Profesor> getProfesores() {
        return profesores;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }
	
    public List<LearningPath> getPaths() {
		return paths;
	}

	//Mteodos
	public void registrarProfesor(Profesor profesor) {
        for (Usuario u : usuarios) {
            if (u instanceof Profesor) {
                Profesor p = (Profesor) u;
                if (p.getCorreo().equals(profesor.getCorreo())) {
                    System.out.println("El profesor ya está registrado.");
                }
            }
        }
        profesores.add(profesor);
        usuarios.add(profesor);
    }
	
	public void registrarEstudiante(Estudiante estudiante) throws Exception {
        for (Usuario u : usuarios) {
            if (u instanceof Estudiante) {
	            if (u.getCorreo().equals(estudiante.getCorreo())) {
	                System.out.println("El estudiante ya está registrado.");
	            }
            }
        }
        estudiantes.add(estudiante);
        usuarios.add(estudiante);
    }
	
    public Profesor loginProfesor(String correo, String contrasena) throws Exception {
        for (Usuario u : usuarios) {
            if (u instanceof Profesor) {
                Profesor p = (Profesor) u;
                if (p.getCorreo().equals(correo) && p.getContrasena().equals(contrasena)) {
                    return p;
                }
            }
        }
        throw new Exception("Login fallido. Usuario o contraseña incorrectos.");
    }


    public Estudiante loginEstudiante(String correo, String contrasena) throws Exception {
        for (Usuario u : usuarios) {
            if (u instanceof Estudiante) {
            	Estudiante e = (Estudiante) u;
	            if (e.getCorreo().equals(correo) && e.getContrasena().equals(contrasena)) {
	                return  e;
	            }
            }
        }
        throw new Exception("Login fallido. Usuario o contraseña incorrectos.");
    }

    public void cargarUsuarios(String archivo) throws Exception {
        usuarios = persistencia.cargarUsuarios(archivo);
    }

    public void salvarUsuarios(String archivo) throws Exception {
        persistencia.salvarUsuarios(archivo, usuarios);
    }
    
    public void agregarPaths(LearningPath lp) {
    	this.paths.add(lp);
    }
public List<LearningPath> getLearningPaths(String nombre) {
        Estudiante estudiante = getEstudiante(nombre);
        if (estudiante == null) {
            return new ArrayList<>(); // Retornar una lista vacía si el estudiante no existe
        }
        return paths.stream()
                .filter(lp -> lp.getEstudiantes().contains(estudiante))
                .collect(Collectors.toList());
    }
    
    public LearningPath getLearningPath(String titulo) {
        for (LearningPath lp : paths) {
            if (lp.getTitulo().equalsIgnoreCase(titulo)) {
                return lp;
            }
        }
        return null;
    }
    public Estudiante getEstudiante(String nombre) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) {
                return estudiante;
            }
        }
        return null;
    }
    
}
