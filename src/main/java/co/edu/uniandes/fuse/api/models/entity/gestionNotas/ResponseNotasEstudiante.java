package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.uniandes.model.MensajeOut;
import java.util.ArrayList;

public class ResponseNotasEstudiante {

	@JsonProperty("Salida")
	private List<salidaNotasEstudiante> salida;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;

	public List<salidaNotasEstudiante> getSalida() {
		return this.salida;
	}

	public void setSalida(List<salidaNotasEstudiante> salida) {
		this.salida = salida;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

	public void ResponseNotas()
	  {
	    this.salida = new ArrayList();
	    this.mensajeOut = new MensajeOut();
	  }
}
