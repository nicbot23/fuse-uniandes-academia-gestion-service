package co.edu.uniandes.fuse.api.processors.gestionSuspension;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spi.UnitOfWork;

public class RequestAuditProcessor implements Processor{
	
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> requestMap = new HashMap();
		long currentTimeMillis = System.currentTimeMillis();
		String user = null;
		if (exchange.getIn().getHeader("CamelAuthentication") != null) {
			Subject sub = (Subject) exchange.getIn().getHeader("CamelAuthentication");
			user = sub.getPrincipals().toString();
		}
		requestMap.put("nombreoperacion", exchange.getProperty("operation"));
		requestMap.put("susuario", exchange.getProperty("SUsuario"));
		requestMap.put("EXCHANGE_ID", exchange.getExchangeId());
		requestMap.put("CORRELATION_ID", exchange.getProperty("CamelCorrelationId", String.class));
		requestMap.put("ROUTE_NAME", exchange.getFromRouteId());
		requestMap.put("CONTEXTO", exchange.getUnitOfWork().getRouteContext().getCamelContext().getName());
		requestMap.put("REQUEST_BODY", getPathUrl(exchange) + exchange.getProperty("urlendpoint"));
		requestMap.put("USER", user);
		requestMap.put("START_TIMESTAMP", Long.valueOf(currentTimeMillis));
		requestMap.put("IPEJECUCION", "10.0.0.0");
		requestMap.put("SERVICE_TYPE", "REST");
		requestMap.put("VERSIONSERVICIO", exchange.getProperty("versionservicio"));
		exchange.setProperty("REQUEST_AUDIT", requestMap);
		requestMap.put("SISTEMASINVOCADOS", null);
		exchange.setProperty("AuditStartTimeStamp", Long.valueOf(currentTimeMillis));
	}

	private String getRemoteIp(Exchange exchange) {
		org.apache.cxf.message.Message cxfMessage = (org.apache.cxf.message.Message) exchange.getIn()
				.getHeader("CamelCxfMessage", org.apache.cxf.message.Message.class);

		ServletRequest request = (ServletRequest) cxfMessage.get("HTTP.REQUEST");
		return request.getRemoteAddr();
	}

	private String getPathUrl(Exchange exchange) {
		try {
			org.apache.cxf.message.Message cxfMessage = (org.apache.cxf.message.Message) exchange.getIn()
					.getHeader("CamelCxfMessage", org.apache.cxf.message.Message.class);

			return cxfMessage.get("org.apache.cxf.request.url").toString();
		} catch (Exception e) {
		}
		return "";
	}

}
