package co.edu.uniandes.fuse.api.processors.gestionNotas;


import co.edu.uniandes.fuse.api.models.entity.gestionNotas.NotaEstudiante;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.NotaHomologada;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.PromedioNotasSeccion;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseMaximoSemestre;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseMinimoSemestre;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseNotasEstudiante;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseNotasHistorico;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponsePromedioAcumulado;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponsePromedioNotasSeccion;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponsePromedioSemestre;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.SalidaNotasHistorico;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.salidaNotasEstudiante;
import co.edu.uniandes.fuse.api.utils.Util;
import co.edu.uniandes.model.Curso;
import co.edu.uniandes.model.CursoSeccion;
import co.edu.uniandes.model.MensajeOut;
import co.edu.uniandes.model.Persona;
import co.edu.uniandes.model.Profesor;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandler {
	
	@BeanInject("props-gestion")
	private Properties properties;
	private Exchange exchange;
	private Object response;
	private MensajeOut mensajeOut;

	public void process(Exchange exchange, @ExchangeProperty("operation") String operation) throws Exception {
		this.exchange = exchange;
		this.mensajeOut = new MensajeOut();

		switch (operation) {
		case "/obtener-notas-promedio-acumulado":
			promedioAcumulado();
			break;
		case "/obtener-notas-homologadas":
		      notasHomologadas();
		}

		String responseJson = new ObjectMapper().writeValueAsString(this.response);
		exchange.setProperty("responseAudit", responseJson);
	}

	
	/**
	 * Promedio Acumulado
	 */
	public void promedioAcumulado() {
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();
		ResponsePromedioAcumulado resp = new ResponsePromedioAcumulado();

		if ((resultSet == null) || (resultSet.isEmpty())) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			this.exchange.getOut().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			resp.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setBody(resp);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = resp;
			this.exchange.getOut().setBody(this.response);
			return;
		}

		if (!resultSet.isEmpty()) {
			Map sql = (Map) resultSet.get(0);
			resp.setPromedioAcumuladoEstudiante(
					Util.getColumnFloatFromBigDecimal(sql.get("PROM_SEM_REGULARES")).toString());
		}

		this.response = resp;
		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");

		resp.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(resp);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = resp;
		this.exchange.getIn().setBody(resp);
	}

	/**
	 * Notas homologadas
	 */
	public void notasHomologadas() {
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();

		List listaInfo = new LinkedList();

		if ((resultSet == null) || (resultSet.isEmpty())) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			ResponseNotasEstudiante res = new ResponseNotasEstudiante();
			res.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			this.exchange.getIn().setBody(res);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = res;
			return;
		}

		for (Map<String, Object> map : resultSet) {
			NotaHomologada h = new NotaHomologada();
			NotaEstudiante n = new NotaEstudiante();
			CursoSeccion cs = new CursoSeccion();
			cs.setsCreditos(Util.getColumnFloatFromBigDecimal(map.get("CREDITOS")).toString());
			n.setsCodigoCurso(Util.getColumnString(map.get("COD_CURSO")));
			n.setsNivel(Util.getColumnString(map.get("NIVEL_CURSO")));
			n.setsNombreCurso(Util.getColumnString(map.get("NOMBRE_CURSO")));
			n.setsNota(Util.getColumnString(map.get("NOTA")));
			n.setsPeriodo(Util.getColumnString(map.get("PERIODO")));
			n.setsPidm(new DecimalFormat("#.##").format(Util.getColumnFloatFromBigDecimal(map.get("PIDM"))));
			h.setNota(n);
			h.setCursoSeccion(cs);
			listaInfo.add(h);
		}
		this.response = listaInfo;
		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");
		ResponsePromedioNotasSeccion res = new ResponsePromedioNotasSeccion();
		res.setsNota(this.response);
		res.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(res);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = res;
		this.exchange.getIn().setBody(this.response);
	}

	

	

}
