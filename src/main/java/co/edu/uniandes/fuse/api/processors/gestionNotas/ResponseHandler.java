package co.edu.uniandes.fuse.api.processors.gestionNotas;


import co.edu.uniandes.fuse.api.models.entity.gestionNotas.NotaEstudiante;
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
		case "/obtener-notas-minimo-semestre-calificado":
			minResponse();
			break;
		case "/obtener-notas-maximo-semestre-calificado":
			maxResponse();
			break;
		case "/obtener-notas-promedio-acumulado":
			promedioAcumulado();
			break;
		case "/obtener-notas-promedio-semestre":
			promedio();
			break;
		case "/obtener-notas-promedio-seccion":
			notas();
			break;
		case "/obtener-notas-estudiante":
			notasEstudiante();
			break;
		case "/obtener-notas-sicua":
			break;
		case "/obtener-notas-historico":
			notasHistorico();
			break;
		case "/obtener-notas-homologadas":
			notasHomologadas();
		}

		String responseJson = new ObjectMapper().writeValueAsString(this.response);
		exchange.setProperty("responseAudit", responseJson);
	}

	public void notasEstudiante() throws JsonGenerationException, JsonMappingException, IOException {
		ResponseNotasEstudiante response = new ResponseNotasEstudiante();
		List listSalida = new ArrayList();
		MensajeOut out = new MensajeOut();
		List<Map<String, Object>> sql = (List) exchange.getIn().getBody();

		if (Util.getColumnBoolean(this.exchange.getProperty("isUser")).booleanValue()) {
			if (!sql.isEmpty()) {
				for (Map<String, Object> map : sql) {
					salidaNotasEstudiante salida = new salidaNotasEstudiante();
					CursoSeccion cs = new CursoSeccion();
					NotaEstudiante nota = new NotaEstudiante();
					cs.setsCRN(Util.getColumnString(map.get("CRN")));
					cs.setsNombreCurso(Util.getColumnString(map.get("MATERIA")));
					cs.setsCodigoCurso(Util.getColumnString(map.get("CODIGO")));
					cs.setsSeccion(Util.getColumnString(map.get("SECCION")));
					cs.setsCreditos(Util.getColumnFloatFromBigDecimal(map.get("CREDS")).toString());

					nota.setsPidm(Util.getColumnIntegerFromBigDecimal(map.get("LLAVE_ESTUDIANTE")).toString());
					nota.setsPeriodo(Util.getColumnString(map.get("SEMESTRE")));
					nota.setsNivel(Util.getColumnString(map.get("NIVEL_HA")));
					nota.setsNota(Util.getColumnString(map.get("NOTA")));

					nota.setdFechaNota(Util.getColumnDate_(map.get("FECHA_NOTA")));

					salida.setCursoSeccion(cs);
					salida.setNota(nota);
					listSalida.add(salida);
				}

				out.setbOperacionExitosa(true);
				out.setsMensajeRtaTecnico(this.properties.getProperty("msj.operacion.exitosa"));
				out.setsMensajeRtaUsuario(this.properties.getProperty("msj.operacion.exitosa"));
				out.setsCodigoRespuesta(this.properties.getProperty("msj.codigo.exitoso"));
			} else {
				out.setbOperacionExitosa(false);
				out.setsCodigoRespuesta(this.properties.getProperty("msj.codigo.notFound"));
				out.setsMensajeRtaTecnico(this.properties.getProperty("excepcion.registros.vacios"));
				out.setsMensajeRtaUsuario(this.properties.getProperty("excepcion.registros.vacios"));
			}

			response.setSalida(listSalida);
		} else {
			out.setbOperacionExitosa(false);
			out.setsCodigoRespuesta(this.properties.getProperty("msj.codigo.notFound"));
			out.setsMensajeRtaTecnico(this.properties.getProperty("excepcion.rol.invalido"));
			out.setsMensajeRtaUsuario(this.properties.getProperty("excepcion.rol.invalido"));
		}

		out.setsIdentificacionProceso((String) this.exchange.getProperty("sIdentificadorProceso"));
		response.setMensajeOut(out);

		String resp = new ObjectMapper().writeValueAsString(response);
		this.exchange.setProperty("stringResponse", resp);
		this.exchange.getOut().setBody(response);
	}

	public void minResponse() {
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();
		ResponseMinimoSemestre res = new ResponseMinimoSemestre();

		if ((resultSet == null) || (resultSet.isEmpty())
				|| (((Map) resultSet.get(0)).get("MIN(SHRTGPA_TERM_CODE)") == null)) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			res.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setBody(res);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = res;

			return;
		}

		for (Map<String, Object> map : resultSet) {
			res.setPeriodo(Util.getColumnString(map.get("MIN(SHRTGPA_TERM_CODE)")));
		}

		this.response = res;
		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");
		res.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(res);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = res;
		this.exchange.getIn().setBody(this.response);
	}

	public void maxResponse() {
		ResponseMaximoSemestre res = new ResponseMaximoSemestre();
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();

		if ((resultSet == null) || (resultSet.isEmpty())
				|| (((Map) resultSet.get(0)).get("MAX(SHRTGPA_TERM_CODE)") == null)) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			res.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			this.exchange.getIn().setBody(res);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = res;
			return;
		}

		for (Map<String, Object> map : resultSet) {
			res.setPeriodo(Util.getColumnString(map.get("MAX(SHRTGPA_TERM_CODE)")));
		}
		this.response = res;
		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");
		res.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(res);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = res;
		this.exchange.getIn().setBody(this.response);
	}

	public void promedioSeccion() {
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();

		List listaInfo = new LinkedList();

		if ((resultSet == null) || (resultSet.isEmpty())) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			ResponsePromedioNotasSeccion res = new ResponsePromedioNotasSeccion();
			res.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			this.exchange.getIn().setBody(res);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = res;
			return;
		}

		for (Map<String, Object> map : resultSet) {
			maxResponse p = new maxResponse();

			listaInfo.add(p);
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

	public void promedio() {
		NotaEstudiante nota = new NotaEstudiante();
		List<Map<String, Object>> resultSet = (List) exchange.getIn().getBody();
		ResponsePromedioSemestre res = new ResponsePromedioSemestre();
		if ((resultSet == null) || (resultSet.isEmpty())) {
			this.mensajeOut.setbOperacionExitosa(false);
			this.mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			this.mensajeOut.setsCodigoRespuesta("http 404");
			this.mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");

			this.exchange.getOut().setHeader("CamelHttpResponseCode", Integer.valueOf(404));
			res.setMensajeOut(this.mensajeOut);
			this.exchange.getIn().setBody(res);
			this.exchange.setProperty("mensajeOut", this.mensajeOut);
			this.response = res;
			this.exchange.getOut().setBody(this.response);
			return;
		}

		if (!resultSet.isEmpty()) {
			Map map = (Map) resultSet.get(0);
			nota.setsNota(Util.getColumnFloatFromBigDecimal(map.get("PROM_SEM_REGULARES")).toString());
			res.setNota(nota);
		}

		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");

		res.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(res);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = res;
		this.exchange.getIn().setBody(res);
	}

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

	public void notas() {
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

		for (Map map : resultSet) {
			NotaEstudiante n = new NotaEstudiante();
			// XMLGregorianCalendar fecha = Util.getColumnDate_(map.get("FECHA_NOTA"));
			// Date fe = fecha.toGregorianCalendar().getTime();
			n.setdFechaNota(Util.getColumnDate_(map.get("FECHA_NOTA")));
			n.setsCRN(Util.getColumnString(map.get("CRN")));
			n.setsCodigoCurso(Util.getColumnString(map.get("CODIGO")));
			n.setsNivel(Util.getColumnString(map.get("NIVEL_HA")));
			n.setsNombreCurso(Util.getColumnString(map.get("NOM_CURSO")));
			n.setsNota(Util.getColumnString(map.get("NOTA")));
			n.setsPeriodo(Util.getColumnString(map.get("SEMESTRE")));
			n.setsPidm(
					new DecimalFormat("#.##").format(Util.getColumnFloatFromBigDecimal(map.get("LLAVE_ESTUDIANTE"))));

			listaInfo.add(n);
		}
		this.response = listaInfo;
		this.mensajeOut.setbOperacionExitosa(true);
		this.mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		this.mensajeOut.setsCodigoRespuesta("http 200");
		this.mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");
		PromedioNotasSeccion res = new PromedioNotasSeccion();
		res.setsNotas(this.response);
		res.setMensajeOut(this.mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(res);
		this.exchange.setProperty("mensajeOut", this.mensajeOut);
		this.response = res;
		this.exchange.getIn().setBody(this.response);
	}

	public void notasHistorico() {
		List<Map<String, Object>> cons = (List) exchange.getIn().getBody();

		MensajeOut mensajeOut = new MensajeOut();
		ResponseNotasHistorico resp = new ResponseNotasHistorico();
		List listaMate = new ArrayList();

		List listaInfo = new LinkedList();
		if ((cons == null) || (cons.isEmpty())) {
			mensajeOut.setbOperacionExitosa(false);
			mensajeOut.setsMensajeRtaTecnico("Error procesando datos");
			mensajeOut.setsCodigoRespuesta("http 404");
			mensajeOut.setsMensajeRtaUsuario("No existe registros asociados a la consulta");
			resp.setMensajeOut(mensajeOut);
			this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200)); // Se cambia éste con
																							// respecto a los otros
			this.exchange.getIn().setBody(resp);
			this.exchange.setProperty("mensajeOut", mensajeOut);
			this.response = resp;
			return;
		}

		for (Map dir : cons) {
			SalidaNotasHistorico salida = new SalidaNotasHistorico();
			CursoSeccion cursoSeccion = new CursoSeccion();
			NotaEstudiante nota = new NotaEstudiante();
			Curso curso = new Curso();

			cursoSeccion.setsCRN(Util.getColumnString(dir.get("CRN")));
			cursoSeccion.setsNombreCurso(Util.getColumnString(dir.get("MATERIA")));
			cursoSeccion.setsCodigoCurso(Util.getColumnString(dir.get("CODIGO")));
			cursoSeccion.setsSeccion(Util.getColumnString(dir.get("SECCION")));
			cursoSeccion.setsCreditos(Util.getColumnFloatFromBigDecimal(dir.get("CREDS")).toString());
			curso.setsNombreLargoCurso(Util.getColumnString(dir.get("NOM_CURSO")));
			nota.setsPidm(Util.getColumnIntegerFromBigDecimal(dir.get("LLAVE_ESTUDIANTE")).toString());
			nota.setsPeriodo(Util.getColumnString(dir.get("SEMESTRE")));
			nota.setsNivel(Util.getColumnString(dir.get("NIVEL_HA")));
			nota.setsNota(Util.getColumnString(dir.get("NOTA")));
			nota.setdFechaNota(Util.getColumnDate_(dir.get("FECHA_NOTA")));

			Profesor profesor = new Profesor();
			Persona persona = new Persona();

			String str = Util.getColumnString(dir.get("NOMBRES_PROF"));
			String[] splited = str.split("\\s+");

			String str2 = Util.getColumnString(dir.get("APELLIDOS_PROF"));
			String[] splited2 = str2.split("\\s+");

			persona.setsPrimerNombre(splited[0]);
			if (splited.length > 1) {
				persona.setsSegundoNombre(splited[1]);
			}

			persona.setsPrimerApellido(splited2[0]);
			if (splited2.length > 1) {
				persona.setsSegundoApellido(splited2[1]);
			}

			profesor.setcPersona(persona);
			profesor.setsLogin(Util.getColumnString(dir.get("USUARIO_PROF")));

			salida.setProfesor(profesor);
			salida.setCursoSeccion(cursoSeccion);
			salida.setCurso(curso);
			salida.setNota(nota);
			listaMate.add(salida);
		}

		mensajeOut.setbOperacionExitosa(true);
		mensajeOut.setsMensajeRtaTecnico("Operacion exitosa");
		mensajeOut.setsCodigoRespuesta("http 200");
		mensajeOut.setsMensajeRtaUsuario("Operacion exitosa");
		resp.setSalida(listaMate);
		resp.setMensajeOut(mensajeOut);
		this.exchange.getIn().setHeader("CamelHttpResponseCode", Integer.valueOf(200));
		this.exchange.getIn().setBody(resp);
		this.exchange.setProperty("mensajeOut", mensajeOut);
		this.response = resp;
		this.exchange.getIn().setBody(this.response);
	}

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

	public class NotaHomologada implements Serializable {

		@JsonProperty("Nota")
		private NotaEstudiante nota;

		@JsonProperty("CursoSeccion")
		private CursoSeccion cursoSeccion;

		public NotaHomologada() {
		}

		public NotaEstudiante getNota() {
			return this.nota;
		}

		public void setNota(NotaEstudiante nota) {
			this.nota = nota;
		}

		public CursoSeccion getCursoSeccion() {
			return this.cursoSeccion;
		}

		public void setCursoSeccion(CursoSeccion cursoSeccion) {
			this.cursoSeccion = cursoSeccion;
		}
	}

	public class maxResponse {

		@JsonProperty("Periodo")
		private String periodo;

		@JsonProperty("MensajeOut")
		private MensajeOut mensajeOut;

		public maxResponse() {
		}

		public MensajeOut getMensajeOut() {
			return this.mensajeOut;
		}

		public void setMensajeOut(MensajeOut mensajeOut) {
			this.mensajeOut = mensajeOut;
		}

		public String getPeriodo() {
			return this.periodo;
		}

		public void setPeriodo(String periodo) {
			this.periodo = periodo;
		}
	}

	public class minResponse {

		@JsonProperty("sPeriodo")
		private String sMinimoSemestreCalificado;

		public minResponse() {
		}

		public String getsMinimoSemestreCalificado() {
			return this.sMinimoSemestreCalificado;
		}

		public void setsMinimoSemestreCalificado(String sMinimoSemestreCalificado) {
			this.sMinimoSemestreCalificado = sMinimoSemestreCalificado;
		}
	}

}
