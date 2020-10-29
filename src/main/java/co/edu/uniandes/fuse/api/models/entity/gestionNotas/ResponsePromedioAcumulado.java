package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponsePromedioAcumulado {

//----------------------------------------------------------------------------------------	
//---------------- ATRIBUTES -------------------------------------------------------------
//----------------------------------------------------------------------------------------		
	
	@JsonProperty("PromedioAcumuladoEstudiante")
	private String promedioAcumuladoEstudiante;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;
	
//----------------------------------------------------------------------------------------	
//---------------- CONSTRUCTOR -----------------------------------------------------------
//----------------------------------------------------------------------------------------	
	
	public ResponsePromedioAcumulado() {
		this.promedioAcumuladoEstudiante = "";
		this.mensajeOut = new MensajeOut();
	}
	
//----------------------------------------------------------------------------------------	
//---------------- METHODS ---------------------------------------------------------------
//----------------------------------------------------------------------------------------	
	

	public String getPromedioAcumuladoEstudiante() {
		return this.promedioAcumuladoEstudiante;
	}

	public void setPromedioAcumuladoEstudiante(String promedioAcumuladoEstudiante) {
		this.promedioAcumuladoEstudiante = promedioAcumuladoEstudiante;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

}
