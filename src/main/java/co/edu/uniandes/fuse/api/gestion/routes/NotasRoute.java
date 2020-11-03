package co.edu.uniandes.fuse.api.gestion.routes;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;

import javax.ws.rs.ClientErrorException;

import org.apache.camel.LoggingLevel;
import co.edu.uniandes.fuse.core.utils.models.ErrorResponse;

import co.edu.uniandes.fuse.api.models.entity.gestionNotas.NotaEstudiante;
import co.edu.uniandes.fuse.api.processors.gestionNotas.ResponseHandler;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseNotasEstudiante;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponseNotasHomologadas;
import co.edu.uniandes.fuse.api.models.entity.gestionNotas.ResponsePromedioAcumulado;
import co.edu.uniandes.fuse.api.processors.gestionNotas.RequestProcessor;

public class NotasRoute extends RestConfiguration {
	
	public void configure() throws Exception {
		
		// EXCEPTIONS       
        onException(Exception.class)
             .handled(true)
             .to("direct-vm:manageException");
        ;
    	
// ------- RESTS -----------------------------------------------------------------------------
    	
        rest("/GestionNotas")
        	//.get("/obtenner-notas-estudiante")
        	//.get("/obtener-notas-historico")
        	.get("/obtener-notas-homologadas") //------
        		.description("Consulta las notas homologadas de un estudiante ")				
        		.consumes("application/json").produces("application/json")
					.param()
						.name("snumerodocumento").description("Numero de documento del estudiante")
					.endParam()
					.param()
						.name("spidm").description("C&oacute;digo del estudiante")
					.endParam()
					.param()
						.name("slogin").description("login del estudiante")
					.endParam()
					.param()
						.name("scodigo").description("Codigo del estudiante")
					.endParam()
					.param()
						.name("snombrecurso").description("Codigo del estudiante")
					.endParam()
					.param()
						.name("scodigocurso").description("Codigo del estudiante")
					.endParam()
					.param()
						.name("snivel").description("Codigo del estudiante")
					.endParam()
				.outType(ResponseNotasHomologadas.class)
				.responseMessage().code("000").message("300 - Redirect<br>400 - Client Error<br>500 - Server Error").responseModel(ErrorResponse.class).endResponseMessage()        	
				.to("direct:obtenerNotasHomologadas")
				//.get("/obtener-notas-maximo-semestre-calificado")
        	//.get("/obtener-notas-minimo-semestre-calificado")
        	.get("/obtener-notas-promedio-acumulado") //----
	        	.description("Consulta promedio acumulado de notas de un estudiante ")				
	    		.consumes("application/json").produces("application/json")
		        	.param()
						.name("snumerodocumento").description("Numero de documento del estudiante")
						.endParam()
					.param()
						.name("spidm").description("C&oacute;digo del estudiante")
					.endParam()
					.param()
						.name("slogin").description("login del estudiante")
					.endParam()
					.param()
						.name("scodigo").description("Codigo del estudiante")
					.endParam()
					.param()
						.name("speriodo").description("Codigo del estudiante")
					.endParam()
					.param()
						.name("snivel").description("Codigo del estudiante")
					.endParam()
				.outType(ResponsePromedioAcumulado.class)
				.responseMessage().code("000").message("300 - Redirect<br>400 - Client Error<br>500 - Server Error").responseModel(ErrorResponse.class).endResponseMessage()
				.to("direct:obtenerPromedioAcumulado")
        	//.get("/obtener-notas-promedio-seccion")
        	//.get("/obtener-notas-promedio-semestre")
        	//.get("/obtener-notas-sicua")
        	
        	;
    	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        
    //-------------- OBTENER NOTAS HOMOLOGADAS ---------------------------------------------------------------------------------	
    	
	    	from("direct:obtenerNotasHomologadas")
	    		.process(new RequestProcessor())
	    		.log("INFO ::::: obtener notas homologadas ")
	    		.to("direct:pidmRoute")//verificar
	    		.choice()
	    			.when(simple("${headers.CamelHttpResponseCode} != '200'"))
		    			.log(LoggingLevel.ERROR,"::::: Error en Consultar SPIDM")
		    			.setProperty("estado").constant(false)
		    			.setProperty("message", simple("{{excepcion.pidm.disconnect}}"))
		    			//.to("direct:route-error-handler")
		    		.otherwise()
		    			.to("velocity:template/gestion-notas/obtener-notas-homologadas.vm") //verificar
		    			.setHeader("CamelSqlQuery").simple("${body}")
		    			.log("${body}")
		    			.to("sql:dumy")
		    			.log("${body}")
		    			.bean(ResponseHandler.class) //verificar
		    			//.to("direct:route-audit-response")
		    	.endChoice()
    		;
	    	
	
	    	
    //-------------- OBTENER PROMEDIO ACUMULADO ---------------------------------------------------------------------------------
	    	
	    	from("direct:obtenerPromedioAcumulado")
	    		//.to("direct:route-audit-request")
	    		.process(new RequestProcessor()) //verificar
	    		.log(":::/obtener-notas-promedio-acumulado")
	    		.to("direct:pidmRoute")
	    		.choice()	
	    			.when().simple("${headers.CamelHttpResponseCode} != '200'")
		    			.log(LoggingLevel.ERROR," :::: Error en Consultar SPIDM")
		    			.setProperty("estado").constant(false)
		    			.setProperty("message",simple("{{excepcion.pidm.disconnect}}"))
		    			//.to("direct:route-error-handler")//verificar
		    		.otherwise()
		    			.to("velocity:template/gestion-notas/obtener-notas-promedio-acumulado.vm")//verificar
		    			.setHeader("CamelSqlQuery").simple("${body}")
		    			.log("${body}")
		    			.to("sql:dumy")//verificar
		    			.log("${body}")
		    			.bean(ResponseHandler.class)//verificar
		    			//.to("direct:route-audit-response")
		    	.end()
	    	;
    		
    	
	//-------------- ROUTE-CONSULTA-PIDM ---------------------------------------------------------------------------------------
 	  
    	     from("direct:pidmRoute")
    	     	.log("ENTRANDO A PIDM")
    	     	.setProperty("ID").simple("${header.spidm}")
    	     	.setProperty("slogin").simple("${header.slogin}")
    	     	.setProperty("snumerodocumento").simple("${header.snumerodocumento}")
    	     	.setProperty("scodigo").simple("${header.scodigo}")
    	     	.log(" SALIDA ID ESTUDIANTE ----> ${property.ID}")
    	     	.choice()
    	     		.when().simple("${property.ID} != null and ${property.ID} != ''")
	    	     		.log("LLEGA EL PIDM")		
	    	     		.setHeader("CamelHttpResponseCode", simple("200"))
	    	     	.otherwise()
	    	     		.log("empty SPIDM...consume UNIANDES ::: WS-GET CONSULTA-PIDM")
	    	     		.removeHeaders("*")
	    	     		.log("consumiendo PIDMMMMM")
	    	     		.setHeader("CamelHttpMethod", constant("GET"))
	    	     		.setHeader("Content-Type", constant("application/json"))
	    	     		.setProperty("endpontpidm").simple("{{endpoint}}?scodigo=${property.scodigo}{{simbol.agregate}}snumerodocumento=${property.snumerodocumento}{{simbol.agregate}}spidm=${property.ID}{{simbol.agregate}}slogin=${property.slogin}")
	    	     		.log("${property.endpontpidm}")
	    	     		.setHeader("CamelHttpUri", simple("${property.endpontpidm}"))
	    	     		.setHeader("Authorization", simple("${property.AutoTokenService}"))
	    	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
	    	     		.log("Mensaje usuario: ${property.messageusuario}}")
	    	     		.log("Authorization : ${header.Authorization}}")
	    	     		.to("http://dummyHttp?throwExceptionOnFailure=false")
	    	     		.log("Response service UNIANDES ::: WS-REST CONSULTA-PIDM : ${body}}")
	    	     		.setProperty("spidm").jsonpath("$.Estudiante.SPidm")
	    	     		.log("este es pidm:  :::   ${property.spidm}")
	    	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
	    	     		.log("este es mensaje USUARIO ${property.messageusuario}")
	    	     		.setProperty("messagetecnico").jsonpath("$.mensajeRta.SMensajeRtaTecnico")
	    	     		.log("este es mensaje tecnico ${property.messagetecnico}")
	    	     	
	    	     .end()
    	     		
    	     ;
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
	}

}
