package Persistencia;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import proyecto.Actividad;
import proyecto.ActividadConcreta;
import proyecto.Estudiante;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.Registro;

public class PersistenciaProgreso {
	
	public void cargarProgresoEstudiantes(String archivo, Registro registro) throws IOException {
	    String contenido = new String(Files.readAllBytes(Paths.get(archivo)));
	    JSONArray jEstudiantes = new JSONArray(contenido);

	    for (int i = 0; i < jEstudiantes.length(); i++) {
	        JSONObject jEstudiante = jEstudiantes.getJSONObject(i);
	        String nombre = jEstudiante.getString("nombre");
	        String correo = jEstudiante.getString("correo");
	        String contrasena = jEstudiante.getString("contrasena");

	        Estudiante estudiante = registro.getEstudiante(nombre);
	        if (estudiante == null) {
	            estudiante = new Estudiante(nombre, correo, contrasena);
	            try {
	                registro.registrarEstudiante(estudiante);
	            } catch (Exception e) {
	                e.printStackTrace();
	                continue; // Continuar con el siguiente estudiante si hay un error
	            }
	        }

	        JSONArray jLearningPaths = jEstudiante.getJSONArray("learningPaths");

	        for (int j = 0; j < jLearningPaths.length(); j++) {
	            JSONObject jLearningPath = jLearningPaths.getJSONObject(j);
	            String titulo = jLearningPath.getString("titulo");
	            int progreso = jLearningPath.getInt("progreso");

	            LearningPath lp = registro.getLearningPath(titulo);
	            if (lp == null) {
	                // Crear un nuevo LearningPath con valores predeterminados
	                lp = new LearningPath(titulo, "Descripción predeterminada", "Objetivos predeterminados", "Nivel de dificultad predeterminado", null, 0);
	                registro.agregarPaths(lp);
	            }
	            lp.añadirEstudiante(estudiante); // Asegurarse de añadir el estudiante al Learning Path
	            lp.setProgreso(estudiante, progreso);

	            JSONArray jActividades = jLearningPath.getJSONArray("actividades");

	            for (int k = 0; k < jActividades.length(); k++) {
	                JSONObject jActividad = jActividades.getJSONObject(k);
	                String tituloActividad = jActividad.getString("titulo");
	                double calificacion = jActividad.optDouble("calificacion", -1); // -1 indica que no hay calificación

	                // Crear instancia de ActividadConcreta
	                Actividad actividad = new ActividadConcreta(lp, "Descripción de la actividad", "Objetivo de la actividad", "Nivel de dificultad", 60, true, null, tituloActividad, calificacion);
	                lp.añadirActividad(actividad);
	            }
	        }
	    }
	}

	public void guardarProgresoEstudiantes(String archivo, Registro registro) throws IOException {
	    JSONArray jEstudiantes = new JSONArray();

	    for (Estudiante estudiante : registro.getEstudiantes()) {
	        JSONObject jEstudiante = new JSONObject();
	        jEstudiante.put("nombre", estudiante.getNombre());

	        JSONArray jLearningPaths = new JSONArray();
	        List<LearningPath> learningPaths = registro.getLearningPaths(estudiante.getNombre());

	        for (LearningPath lp : learningPaths) {
	            JSONObject jLearningPath = new JSONObject();
	            jLearningPath.put("titulo", lp.getTitulo());
	            jLearningPath.put("progreso", lp.getProgreso(estudiante));

	            JSONArray jActividades = new JSONArray();
	            List<Actividad> actividades = lp.getActividades();

	            for (Actividad actividad : actividades) {
	                JSONObject jActividad = new JSONObject();
	                jActividad.put("titulo", actividad.getTitulo());
	                jActividad.put("calificacion", actividad.getCalificacion());
	                jActividades.put(jActividad);
	            }
	            jLearningPath.put("actividades", jActividades);

	            jLearningPaths.put(jLearningPath);
	        }

	        jEstudiante.put("learningPaths", jLearningPaths);
	        jEstudiantes.put(jEstudiante);
	    }

	    try (FileWriter file = new FileWriter(archivo)) {
	        file.write(jEstudiantes.toString(2));
	        System.out.println("Progreso guardado en el archivo: " + archivo);
	    }
	}
}