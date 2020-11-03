package co.edu.uniandes.fuse.api.processors.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class ResponseErrorHandler {
	
	public void process(Exchange exchange, @ExchangeProperty("estado") Boolean estado,
										   @ExchangeProperty("message") String message) throws Exception {
		
		ErrorResponse nueva = new ErrorResponse();
		
		MensajeOut m = new MensajeOut();
		
		m.setbOperacionExitosa(false);
		
		if (message.equals("No se puede establecer la conexion con el servicio remoto")) {
			m.setsCodigoRespuesta("http 500");
		} else {
			m.setsCodigoRespuesta("http 400");
		}
		
		m.setsMensajeRtaTecnico(message);
		m.setsMensajeRtaUsuario(message);
		nueva.setError(m);
		exchange.setProperty("mensajeOut", m);

		exchange.getOut().setHeader("CamelAcceptContentType", "application/json; charset=UTF-8");
		exchange.getOut().setHeader("Content-Type", "application/json; charset=UTF-8");

		exchange.getOut().setHeader("CamelHttpResponseCode", Integer.valueOf(400));
		exchange.getOut().setBody(nueva, ErrorResponse.class);
		String responseJson = new ObjectMapper().writeValueAsString(nueva);
		exchange.setProperty("responseAudit", responseJson);
	}

}
