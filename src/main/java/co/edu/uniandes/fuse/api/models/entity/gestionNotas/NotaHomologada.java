package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

import co.edu.uniandes.model.CursoSeccion;

public class NotaHomologada implements Serializable{
	
	
//----------------------------------------------------------------------------------------	
//---------------- ATRIBUTES -------------------------------------------------------------
//----------------------------------------------------------------------------------------
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("Nota")
	private NotaEstudiante nota;

	@JsonProperty("CursoSeccion")
	private CursoSeccion cursoSeccion;
	
//----------------------------------------------------------------------------------------	
//---------------- METHODS ---------------------------------------------------------------
//----------------------------------------------------------------------------------------	
	
	public NotaHomologada() {
	}

	public NotaEstudiante getNota() {
		return this.nota;
	}

	public void setNota(NotaEstudiante nota) {
		this.nota = nota;
	}

	public CursoSeccion getCursoSeccion() {
		return this.cursoSeccion;
	}

	public void setCursoSeccion(CursoSeccion cursoSeccion) {
		this.cursoSeccion = cursoSeccion;
	}
	
	

}
