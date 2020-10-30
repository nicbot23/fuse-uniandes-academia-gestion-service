package co.edu.uniandes.fuse.api.processors.gestionSuspension;


import co.edu.uniandes.fuse.api.models.entity.gestionSuspension.ResponseSuspensionesDisciplinarias;
import co.edu.uniandes.fuse.api.utils.Util;
import co.edu.uniandes.model.MensajeOut;
import co.edu.uniandes.model.Suspension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;

public class DataProcessorSuspensionesDisciplinarias {
	
	@BeanInject("props")
    private Properties properties;
	  
	  public void process(Exchange exchange, 
			  			@ExchangeProperty("messageusuario") String mensajeusuario, 
			  			@ExchangeProperty("message") String mensaje, 
			  			@ExchangeProperty("estado") boolean estado)
	    throws Exception
	  {
	    List<Map<String, Object>> result = (List)exchange.getIn().getBody();
	    List<Suspension> suspensiones = new ArrayList();
	    ResponseSuspensionesDisciplinarias response = new ResponseSuspensionesDisciplinarias();
	    MensajeOut mensajeOut = new MensajeOut();
	    if(estado) {
	        if ((result != null) && (!result.isEmpty()))
	        {
	            for (Map map : result)
	            {
	              Suspension suspension = new Suspension();
	              XMLGregorianCalendar fechafinal = Util.getColumnDate(map.get("FECHA_FIN"));
	              Date fin = fechafinal.toGregorianCalendar().getTime();
	              suspension.setdSuspenionDiscFin(fin);
	              XMLGregorianCalendar fechainicio = Util.getColumnDate(map.get("FECHA_INICIO"));
	              Date inicio = fechainicio.toGregorianCalendar().getTime();
	              suspension.setdSuspensionDiscInicio(inicio);
	              suspension.setsDescipcionSuspDisc(Util.getColumnString(map.get("descripcion_suspencion")));
	              suspension.setsRazonSuspDisc(Util.getColumnString(map.get("razon")));
	              suspension.setsTipoSuspension(this.properties.getProperty("constant.tipo.disciplinaria"));
	              suspensiones.add(suspension);
	            }
	            mensajeOut.setsMensajeRtaTecnico(this.properties.getProperty("msj.operacion.exitosa"));
	            mensajeOut.setsMensajeRtaUsuario(this.properties.getProperty("msj.operacion.exitosa"));
	            mensajeOut.setbOperacionExitosa(true);
	            mensajeOut.setsCodigoRespuesta("http 200");
	        }
	        else
	        {
	          mensajeOut.setsMensajeRtaTecnico(this.properties.getProperty("excepcion.registros.vacios"));
	          mensajeOut.setsMensajeRtaUsuario(this.properties.getProperty("excepcion.registros.vacios"));
	          exchange.getOut().setHeader("CamelHttpCode", this.properties.getProperty("http.code.not.found"));
	          mensajeOut.setbOperacionExitosa(false);
	          mensajeOut.setsCodigoRespuesta("http 404");
	        }
	    } else {
	    	
		      mensajeOut.setsCodigoRespuesta("http 500");
		      exchange.getOut().setHeader("CamelHttpCode", this.properties.getProperty("http.code.server.error"));
		      mensajeOut.setsMensajeRtaTecnico(mensaje);
		      mensajeOut.setsMensajeRtaUsuario(mensaje);
		      mensajeOut.setbOperacionExitosa(false);
	    }
	        
	        response.setSuspension(suspensiones);
	        response.setMensajeOut(mensajeOut);
	      
	    exchange.getOut().setHeader("CamelAcceptContentType", "application/json; charset=UTF-8");
	    exchange.getOut().setHeader("Content-Type", "application/json; charset=UTF-8");
	    exchange.getOut().setHeader("CamelCxfRsResponseClass", ResponseSuspensionesDisciplinarias.class);
	    exchange.getOut().setBody(response, ResponseSuspensionesDisciplinarias.class);
	    String responseJson = new ObjectMapper().writeValueAsString(response);
	    exchange.setProperty("responseAudit", responseJson);
	  }

}
