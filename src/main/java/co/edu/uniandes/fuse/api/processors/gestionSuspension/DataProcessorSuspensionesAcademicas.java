package co.edu.uniandes.fuse.api.processors.gestionSuspension;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import co.edu.uniandes.model.MensajeOut;

import co.edu.uniandes.fuse.api.models.entity.gestionSuspension.ResponseSuspensionesAcademicas;
import co.edu.uniandes.fuse.api.models.entity.gestionSuspension.Suspension;
import co.edu.uniandes.fuse.api.utils.Util;

public class DataProcessorSuspensionesAcademicas implements Processor{
	
	//@BeanInject("props")
	 // private Properties properties;
	  
	  public void process(Exchange exchange, 
								  			@ExchangeProperty("messageusuario") String mensajeusuario, 
								  			@ExchangeProperty("message") String mensaje, 
								  			@ExchangeProperty("estado") boolean estado, 
								  			@ExchangeProperty("message") String message)
	    throws Exception
	  {
	    List<Map<String, Object>> result = (List)exchange.getIn().getBody();
	    List<Suspension> suspensiones = new ArrayList();
	    ResponseSuspensionesAcademicas response = new ResponseSuspensionesAcademicas();
	    MensajeOut mensajeOut = new MensajeOut();
	    if(estado) {
	        if ((result != null) && (!result.isEmpty()))
	        {
	            for (Map map : result)
	            {
	              Suspension suspension = new Suspension();
	              suspension.setsCodigoSuspencion(Util.getColumnString(map.get("ASTD_CODE")));
	              suspension.setsTipoSuspension(this.properties.getProperty("constant.tipo.academico"));
	              suspension.setsPeriodo(Util.getColumnString(map.get("TERM_CODE")));
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
	    exchange.getOut().setHeader("CamelCxfRsResponseClass", ResponseSuspensionesAcademicas.class);
	    exchange.getOut().setBody(response, ResponseSuspensionesAcademicas.class);
	    String responseJson = new ObjectMapper().writeValueAsString(response);
	    exchange.setProperty("responseAudit", responseJson);
	  }

	

}
