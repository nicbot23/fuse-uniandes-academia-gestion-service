package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import com.fasterxml.jackson.annotation.JsonProperty;
import co.edu.uniandes.model.MensajeOut;

public class ResponseMaximoSemestre {

	
	@JsonProperty("Periodo")
	  private String periodo;

	  @JsonProperty("MensajeOut")
	  private MensajeOut mensajeOut;

	  public ResponseMaximoSemestre()
	  {
	    this.periodo = "";
	    this.mensajeOut = new MensajeOut();
	  }

	  public MensajeOut getMensajeOut() {
	    return this.mensajeOut;
	  }

	  public void setMensajeOut(MensajeOut mensajeOut) {
	    this.mensajeOut = mensajeOut;
	  }

	  public String getPeriodo() {
	    return this.periodo;
	  }

	  public void setPeriodo(String periodo) {
	    this.periodo = periodo;
	  }
}
