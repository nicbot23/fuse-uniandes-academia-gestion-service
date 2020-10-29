package co.edu.uniandes.fuse.api.models.entity.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseNotasHistorico {

	@JsonProperty("Salida")
	private List<SalidaNotasHistorico> salida;

	@JsonProperty("MensajeOut")
	private MensajeOut mensajeOut;

	public ResponseNotasHistorico() {
		this.salida = new ArrayList();
		this.mensajeOut = new MensajeOut();
	}

	public List<SalidaNotasHistorico> getSalida() {
		return this.salida;
	}

	public void setSalida(List<SalidaNotasHistorico> salida) {
		this.salida = salida;
	}

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setMensajeOut(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}
}
