package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseNotasHomologadas {

	
	@JsonProperty("Nota")
	  private Object sNotas;

	  @JsonProperty("MensajeOut")
	  private MensajeOut mensajeOut;

	  public Object getsNotas()
	  {
	    return this.sNotas;
	  }

	  public void setsNotas(Object sNotas) {
	    this.sNotas = sNotas;
	  }

	  public MensajeOut getMensajeOut() {
	    return this.mensajeOut;
	  }

	  public void setMensajeOut(MensajeOut mensajeOut) {
	    this.mensajeOut = mensajeOut;
	  }
}
