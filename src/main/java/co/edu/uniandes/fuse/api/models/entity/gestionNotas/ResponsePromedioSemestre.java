package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponsePromedioSemestre {
	
//----------------------------------------------------------------------------------------	
//---------------- ATRIBUTES -------------------------------------------------------------
//----------------------------------------------------------------------------------------

	@JsonProperty("Nota")
	private NotaEstudiante nota;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;
	
//----------------------------------------------------------------------------------------	
//---------------- METHODS ---------------------------------------------------------------
//----------------------------------------------------------------------------------------	
			
	
	public NotaEstudiante getNota() {
		return this.nota;
	}

	public void setNota(NotaEstudiante nota) {
		this.nota = nota;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}
}
