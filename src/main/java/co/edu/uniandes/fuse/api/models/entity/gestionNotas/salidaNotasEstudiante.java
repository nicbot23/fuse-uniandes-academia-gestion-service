package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.uniandes.model.CursoSeccion;

public class salidaNotasEstudiante {

	
	@JsonProperty("CursoSeccion")
	private CursoSeccion cursoSeccion;

	@JsonProperty("Nota")
	private NotaEstudiante nota;

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
}
