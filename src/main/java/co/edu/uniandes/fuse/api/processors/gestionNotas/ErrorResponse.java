package co.edu.uniandes.fuse.api.processors.gestionNotas;

import co.edu.uniandes.model.MensajeOut;

public class ErrorResponse {
	
	private MensajeOut mensajeOut;

	public MensajeOut getMensajeOut() {
		return this.mensajeOut;
	}

	public void setError(MensajeOut mensajeOut) {
		this.mensajeOut = mensajeOut;
	}

}
