package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.Curso;
import co.edu.uniandes.model.CursoSeccion;
import co.edu.uniandes.model.Profesor;


import org.codehaus.jackson.annotate.JsonProperty;

public class SalidaNotasHistorico {
	
	@JsonProperty("CursoSeccion")
	private CursoSeccion cursoSeccion;

	@JsonProperty("Curso")
	private Curso curso;

	@JsonProperty("Nota")
	private NotaEstudiante nota;

	@JsonProperty("Profesor")
	private Profesor profesor;

	
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public CursoSeccion getCursoSeccion() {
		return this.cursoSeccion;
	}

	public void setCursoSeccion(CursoSeccion cursoSeccion) {
		this.cursoSeccion = cursoSeccion;
	}

	public NotaEstudiante getNota() {
		return this.nota;
	}

	public void setNota(NotaEstudiante nota) {
		this.nota = nota;
	}

	public Curso getCurso() {
		return this.curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	
	
	
	
	
	
	

}
