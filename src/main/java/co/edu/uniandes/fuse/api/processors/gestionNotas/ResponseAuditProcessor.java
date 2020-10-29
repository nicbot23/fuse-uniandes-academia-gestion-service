package co.edu.uniandes.fuse.api.processors.gestionNotas;

import co.edu.uniandes.model.MensajeOut;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseAuditProcessor implements Processor{
	
	
	 private static final Logger LOG = LoggerFactory.getLogger(ResponseAuditProcessor.class);
	
	public void process(Exchange exchange) throws Exception {
		Map requestMap = new HashMap();
		String body = null;
		if (exchange.getIn() != null) {
			String response = MessageHelper.extractBodyAsString(exchange.getIn());
			if (response != null) {
				body = response.replaceAll("(\\r|\\n)", "");
			}
		}
		if (exchange.getProperty("AuditStartTimeStamp") == null) {
			LOG.warn("Not exist property AuditStartTimeStamp");
		}
		if ((exchange.getProperty("mensajeOut") instanceof MensajeOut)) {
			MensajeOut response = (MensajeOut) exchange.getProperty("mensajeOut");
			if (response != null) {
				requestMap.put("codigorespuesta", response.getsCodigoRespuesta());
				requestMap.put("identificadorproceso", response.getsIdentificacionProceso());
				requestMap.put("mensajerespuestatecnico", response.getsMensajeRtaTecnico());
				requestMap.put("mensajerespuestausuario", response.getsMensajeRtaUsuario());
				requestMap.put("operacionexitosa", Boolean.valueOf(response.isbOperacionExitosa()));
				requestMap.put("canal", "canal");
			}
		}

		body = (String) exchange.getProperty("responseAudit");
		requestMap.put("EXCHANGE_ID", exchange.getExchangeId());
		requestMap.put("ROUTE_NAME", exchange.getFromRouteId());
		requestMap.put("CONTEXTO", exchange.getUnitOfWork().getRouteContext().getCamelContext().getName());
		requestMap.put("RESPONSE_BODY", body);
		requestMap.put("START_TIMESTAMP", exchange.getProperty("AuditStartTimeStamp"));
		requestMap.put("END_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));

		exchange.setProperty("REQUEST_AUDIT", requestMap);
	}

}
