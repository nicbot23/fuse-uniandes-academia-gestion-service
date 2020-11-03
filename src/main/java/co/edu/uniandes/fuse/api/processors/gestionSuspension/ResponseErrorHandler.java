package co.edu.uniandes.fuse.api.processors.gestionSuspension;

import co.edu.uniandes.model.MensajeOut;
import java.util.Properties;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class ResponseErrorHandler {
	
	@BeanInject("props-gestion")
	private Properties properties;

	public void process(Exchange exchange, @ExchangeProperty("estado") Boolean estado,
			@ExchangeProperty("message") String message, @ExchangeProperty("codigoRespuesta") String code)
			throws Exception {
		ResponseClass responseClass = new ResponseClass();
		MensajeOut mensajeOut = new MensajeOut();
		mensajeOut.setbOperacionExitosa(false);
		mensajeOut.setsMensajeRtaTecnico(message);
		mensajeOut.setsMensajeRtaUsuario(message);
		if (exchange.getProperty("operation").toString().equals("/obtener-suspensiones-disciplinarias")) {
			mensajeOut.setsCodigoRespuesta(code);
		} else {
			mensajeOut.setsCodigoRespuesta("http " + this.properties.getProperty("http.code.bad.request"));
		}

		responseClass.setMensajeRta(mensajeOut);
		exchange.setProperty("mensajeOut", mensajeOut);
		String responsejson = new ObjectMapper().writeValueAsString(responseClass);
		exchange.setProperty("responseAudit", responsejson);
		exchange.getOut().setHeader("CamelAcceptContentType", "application/json; charset=UTF-8");
		exchange.getOut().setHeader("Content-Type", "application/json; charset=UTF-8");
		exchange.getOut().setHeader("CamelCxfRsResponseClass", ResponseClass.class);
		exchange.getOut().setBody(responseClass);
	}

	private class ResponseClass {
		@JsonProperty("MensajeOut")
		private MensajeOut mensajeRta;

		private ResponseClass() {
		}

		public MensajeOut getMensajeRta() {
			return this.mensajeRta;
		}

		public void setMensajeRta(MensajeOut mensajeRta) {
			this.mensajeRta = mensajeRta;
		}
	}

}
