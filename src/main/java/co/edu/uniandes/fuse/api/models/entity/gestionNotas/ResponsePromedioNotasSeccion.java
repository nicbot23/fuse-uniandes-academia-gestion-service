package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponsePromedioNotasSeccion {
	
//----------------------------------------------------------------------------------------	
//---------------- ATRIBUTES -------------------------------------------------------------
//----------------------------------------------------------------------------------------
	
	@JsonProperty("Nota")
	private Object sNota;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;
	
	
//----------------------------------------------------------------------------------------	
//---------------- METHODS ---------------------------------------------------------------
//----------------------------------------------------------------------------------------
	
	
	public Object getsNota() {
		return this.sNota;
	}

	public void setsNota(Object sNota) {
		this.sNota = sNota;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

}
