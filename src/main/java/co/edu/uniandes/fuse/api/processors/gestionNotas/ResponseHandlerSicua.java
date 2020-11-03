package co.edu.uniandes.fuse.api.processors.gestionNotas;


import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseNotasSicua;
import co.edu.uniandes.fuse.api.utils.Util;
import co.edu.uniandes.model.MensajeOut;
import co.edu.uniandes.model.NotaParcial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Message;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlerSicua {
	
	
	@BeanInject("props-gestion")
	Properties property;

	public void process(Exchange exchange, @ExchangeProperty("estado") Boolean bOperacionExitosa,
			@ExchangeProperty("message") String message) throws Exception {
		ResponseNotasSicua response = new ResponseNotasSicua();
		MensajeOut mensajeOut = new MensajeOut();

		List listNotasParciales = new ArrayList();
		List listMapDescriptions = new ArrayList();

		String request = (String) exchange.getProperty("bodyNotas");
		String observaciones = (String) exchange.getProperty("bodyDescripciones");
		if ((request != null) && (!"".equals(request))) {
			HashMap result = (HashMap) new ObjectMapper().readValue(request, HashMap.class);
			List<Map<String, Object>> listmap = (List) result.get("results");

			if ((observaciones != null) && (!"".equals(observaciones))) {
				HashMap mapDescriptions = (HashMap) new ObjectMapper().readValue(observaciones, HashMap.class);
				listMapDescriptions = (List) mapDescriptions.get("results");
			}
			if ((listmap != null) && (!listmap.isEmpty())) {
				for (Map notas : listmap) {
					NotaParcial notaParcial = new NotaParcial();
					notaParcial.setsNota(Util.getColumnDoubleToString(notas.get("score")));
					notaParcial.setsTexto(Util.getColumnString(notas.get("text")));
					if ((listMapDescriptions != null) && (!listMapDescriptions.isEmpty())) {
						notaParcial.setsDescripcion(
								getDescriptions(listMapDescriptions, Util.getColumnString(notas.get("columnId"))));
					}
					listNotasParciales.add(notaParcial);
				}
				mensajeOut.setsMensajeRtaTecnico(message);
				mensajeOut.setsMensajeRtaUsuario(message);
			} else {
				mensajeOut.setsMensajeRtaTecnico(this.property.getProperty("excepcion.registros.vacios"));
				mensajeOut.setsMensajeRtaUsuario(this.property.getProperty("excepcion.registros.vacios"));
			}
		}
		mensajeOut.setsCodigoRespuesta("http 200");

		mensajeOut.setbOperacionExitosa(true);
		mensajeOut.setsMensajeRtaTecnico("Operación Exitosa");
		mensajeOut.setsMensajeRtaUsuario("Operación Exitosa");
		response.setNotaParcials(listNotasParciales);
		response.setMensajeOut(mensajeOut);

		String responsejson = new ObjectMapper().writeValueAsString(response);
		exchange.setProperty("responseAudit", responsejson);
		exchange.setProperty("mensajeOut", response.getMensajeOut());
		exchange.getOut().setHeader("CamelAcceptContentType", "application/json; charset=UTF-8");
		exchange.getOut().setHeader("Content-Type", "application/json; charset=UTF-8");
		exchange.getOut().setHeader("CamelCxfRsResponseClass", ResponseNotasSicua.class);
		exchange.getOut().setBody(response, ResponseNotasSicua.class);
	}

	private String getDescriptions(List<Map<String, Object>> listMap, String id) {
		for (Map map : listMap) {
			if (Util.getColumnString(map.get("id")).equals(id)) {
				return Util.getColumnString(map.get("name"));
			}
		}
		return "";
	}

}
