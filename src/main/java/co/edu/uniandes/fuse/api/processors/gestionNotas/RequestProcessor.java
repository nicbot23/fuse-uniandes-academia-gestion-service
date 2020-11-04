package co.edu.uniandes.fuse.api.processors.gestionNotas;

import java.util.ArrayList;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;

public class RequestProcessor implements Processor{
	
	Exchange exchange = null;
	String operation = "";
	ArrayList<Object> docs = new ArrayList<Object>();

	public void process(Exchange exchange) throws Exception {
		this.exchange = exchange;
		this.operation = ((String) exchange.getIn().getHeader("CamelHttpPath"));
		validateHeaders();
		if (this.operation.equals("/obtener-notas-promedio-seccion")) {
			validarPeriodo();
			validarCRN();
			validarNivelOpcional();
		} else if (this.operation.equals("/obtener-notas-estudiante")) {
			validarPeriodo();
			setDocument();
		} else if (this.operation.equals("/obtener-notas-sicua")) {
			validarSicua();
		} else if ((this.operation.equals("/obtener-notas-promedio-acumulado"))
				|| (this.operation.equals("/obtener-notas-promedio-semestre"))) {
			setDocument();
			validarPeriodo();
			validarNivel();
		} else if (this.operation.equals("/obtener-notas-historico")) {
			setDocument();
			validarCurso();
		} else if ((this.operation.equals("/obtener-notas-minimo-semestre-calificado"))
				|| (this.operation.equals("/obtener-notas-maximo-semestre-calificado"))) {
			setDocument();
			validarNivel();
		} else {
			setDocument();
			validarCurso();
			validarNivel();
		}
	}

	public void validateHeaders() {
		String queryString = (String) this.exchange.getIn().getHeader("CamelHttpQuery", String.class);
		MultivaluedMap<?, ?> queryMap = JAXRSUtils.getStructuredParams(queryString, "&", false, false);
		if (queryMap.size() == 0) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("No se ha diligenciado los campos minimos requeridos");
		}
			
	}

	public void setDocument() {
		String sdocument = (String) this.exchange.getIn().getHeader("snumerodocumento");
		String slogin = (String) this.exchange.getIn().getHeader("slogin");
		String spidm = (String) this.exchange.getIn().getHeader("spidm");
		String scodigo = (String) this.exchange.getIn().getHeader("scodigo");
		if ((sdocument != null) && (sdocument.length() > 0)) {
			this.exchange.setProperty("snumerodocumento", sdocument);
		}
		if ((slogin != null) && (slogin.length() > 0)) {
			this.exchange.setProperty("slogin", slogin);
		}
		if ((spidm != null) && (spidm.length() > 0)) {
			try {
				Integer.parseInt(spidm);
			} catch (Exception e) {
				throw new IllegalArgumentException("Campo spidm invalido");
			}
			this.exchange.setProperty("spidm", spidm);
		}
		if ((scodigo != null) && (scodigo.length() > 0)) {
			this.exchange.setProperty("scodigo", scodigo);
		}

		if ((sdocument == null) && (slogin == null) && (spidm == null) && (scodigo == null)) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("No se ha diligenciado los campos minimos requeridos");
		}
		if ((sdocument != null) && (slogin != null) && (spidm != null) && (scodigo != null)) {
			if ((sdocument.equals("")) && (slogin.equals("")) && (spidm.equals("")) && (scodigo.equals("")))
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("No se ha diligenciado los campos minimos requeridos");
		}

		if ((spidm != null) && (!spidm.trim().equals(""))) {
			try {
				Long.parseLong(spidm);
			} catch (NumberFormatException e) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo spidm inválido.");
			}
		}

		if ((sdocument != null) && (!sdocument.trim().equals(""))) {
			try {
				Long.parseLong(sdocument);
			} catch (NumberFormatException e) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo snumerodocumento inválido.");
			}
		}

		if ((scodigo != null) && (!scodigo.trim().equals(""))) {
			try {
				Long.parseLong(scodigo);
			} catch (NumberFormatException e) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo scodigo inválido.");
			}
		}
		if ((slogin != null) && (!slogin.trim().equals(""))) {
			try {
				Long.parseLong(slogin);
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo slogin inválido.");
			} catch (NumberFormatException e) {
			}
		}

	}

	public void validarPeriodo() {
		String periodo = (String) this.exchange.getIn().getHeader("speriodo");

		if (periodo != null) {
			if (periodo.equals("")) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("campo speriodo invalido");
			}
				
			if (periodo.length() != 6) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("campo speriodo invalido");
			}
		} else if (periodo == null) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo speriodo invalido");
		}
		try {
			Integer.parseInt(periodo);
		} catch (NumberFormatException e) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo speriodo invalido");
		}
		this.exchange.setProperty("speriodo", periodo);
	}

	public void validarCurso() {
		String nombre = (String) this.exchange.getIn().getHeader("snombrecurso");
		String codigo = (String) this.exchange.getIn().getHeader("scodigocurso");
		String crn = (String) this.exchange.getIn().getHeader("scrn");

		if ((crn == null || crn.trim().equals("")) && (codigo == null || codigo.trim().equals(""))
				&& (nombre == null || nombre.trim().equals(""))) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("Parámetros insuficientes para realizar la consulta");
		}

		if ((crn != null) && (!crn.trim().equals(""))) {
			try {
				Long.parseLong(crn);
			} catch (NumberFormatException e) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo scrn inválido");
			}
		}

		if ((codigo != null) && (!codigo.trim().equals(""))) {
			try {
				Long.parseLong(codigo);
			} catch (NumberFormatException e) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo scodigocurso inválido");
			}
		}

		if ((nombre != null) && (!nombre.trim().equals(""))) {
			try {
				Long.parseLong(nombre);
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("Campo snombrecurso inválido");
			} catch (NumberFormatException e) {
			}
		}

		this.exchange.setProperty("snombrecurso", nombre);
		this.exchange.setProperty("scodigocurso", codigo);
		this.exchange.setProperty("scrn", crn);

	}

	public void validarNivel() {
		String nivel = (String) this.exchange.getIn().getHeader("snivel");
		if (nivel == null) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo snivel inavlido");
		}
		if (nivel.equals("")) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo snivel inavlido");
		}
		this.exchange.setProperty("snivel", nivel);
	}

	public void validarCRN() {
		String crn = (String) this.exchange.getIn().getHeader("scrn");
		if (crn == null) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo scrn invalido");
		}
			
		if (crn.equals("")) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo scrn invalido");
		}
			
			
		try {
			Integer.parseInt(crn);
		} catch (NumberFormatException e) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo scrn invalido");
		}

		this.exchange.setProperty("scrn", crn);
	}

	public void validarNivelOpcional() {
		String nivel = (String) this.exchange.getIn().getHeader("snivel");
		if (nivel != null) {
			if (nivel.equals("")) {
				exchange.setProperty("HttpErrorCode", "http.code.bad.request");
				throw new IllegalArgumentException("campo snivel invalido");
			}

			this.exchange.setProperty("snivel", nivel);
		}
	}

	public void validarSicua() {
		String slogin = (String) this.exchange.getIn().getHeader("slogin");
		String sCodigoCurso = (String) this.exchange.getIn().getHeader("scodigocurso");
		if (((slogin == null) || (slogin.equals(""))) && ((sCodigoCurso == null) || (sCodigoCurso.equals("")))) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("No se ha diligenciado los campos minimos requeridos");
		}
		if ((slogin == null) || (slogin.trim().equals(""))) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo slogin invalido");
		}
		if ((sCodigoCurso == null) || (sCodigoCurso.trim().equals(""))) {
			exchange.setProperty("HttpErrorCode", "http.code.bad.request");
			throw new IllegalArgumentException("campo sCodigoCurso invalido");
		}
		this.exchange.setProperty("login_estudiante", slogin);
		this.exchange.setProperty("codigo_curso", sCodigoCurso);
	}

}
