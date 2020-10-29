package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import co.edu.uniandes.model.NotaParcial;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseNotasSicua {
	
	@JsonProperty("NotaParcial")
	private List<NotaParcial> notaParcials;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;

	public List<NotaParcial> getNotaParcials() {
		return this.notaParcials;
	}

	public void setNotaParcials(List<NotaParcial> notaParcials) {
		this.notaParcials = notaParcials;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

}
