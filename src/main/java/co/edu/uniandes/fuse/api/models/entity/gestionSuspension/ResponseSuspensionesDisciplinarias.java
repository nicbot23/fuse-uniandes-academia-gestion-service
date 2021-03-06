package co.edu.uniandes.fuse.api.models.entity.gestionSuspension;

import co.edu.uniandes.model.MensajeOut;
import co.edu.uniandes.model.Suspension;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseSuspensionesDisciplinarias {
	
	@JsonProperty("Suspension")
	private List<Suspension> suspension;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;

	public ResponseSuspensionesDisciplinarias()
	  {
	    this.suspension = new ArrayList();
	    this.mensajeOut = new MensajeOut();
	  }

	public List<Suspension> getSuspension() {
		return this.suspension;
	}

	public void setSuspension(List<Suspension> suspension) {
		this.suspension = suspension;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

}
