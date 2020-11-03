package co.edu.uniandes.fuse.api.gestion.routes;



import org.apache.camel.LoggingLevel;
import org.apache.camel.model.rest.RestParamType;

import co.edu.uniandes.fuse.api.models.entity.gestionSuspension.ResponseSuspensionesAcademicas;
import co.edu.uniandes.fuse.api.models.entity.gestionSuspension.ResponseSuspensionesDisciplinarias;
import co.edu.uniandes.fuse.api.processors.gestionSuspension.AuthorizationProcessor;
import co.edu.uniandes.fuse.api.processors.gestionSuspension.DataProcessorSuspensionesAcademicas;
import co.edu.uniandes.fuse.api.processors.gestionSuspension.DataProcessorSuspensionesDisciplinarias;
import co.edu.uniandes.fuse.core.utils.models.ErrorResponse;


public class SuspensionRoute extends RestConfiguration{
	
	public void configure() throws Exception {

// ------- EXCEPTIONS -----------------------------------------------------------------------------		
		
		onException(Exception.class)
        .handled(true)
        .to("direct-vm:manageException");
		;
// ------- RESTS ----------------------------------------------------------------------------------
    	
        rest("/GestionSuspension")
        	.get("/obtener-suspensiones-academicas")
	        	.description("Consulta las suspensiones academicas de un estudiante")				
				.consumes("application/json").produces("application/json")
					.param()
						.name("snumerodocumento").description("Numero de documento del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("spidm").description("C&oacute;digo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("slogin").description("login del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("scodigo").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
				.outType(ResponseSuspensionesAcademicas.class) 
				.responseMessage().code("000").message("300 - Redirect<br>400 - Client Error<br>500 - Server Error").responseModel(ErrorResponse.class).endResponseMessage()
				.to("direct:obtenerSuspensionesAcademicas")
        	.get("/obtener-suspensiones-disciplinarias")
	        	.description("Consulta las suspensiones disciplinarias de un estudiante")				
				.consumes("application/json").produces("application/json")
					.param()
						.name("snumerodocumento").description("Numero de documento del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("spidm").description("C&oacute;digo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("slogin").description("login del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("scodigo").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("speriodo").description("Periodo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
				.outType(ResponseSuspensionesDisciplinarias.class)//verificar los datos que llegan de los parametros
				.responseMessage().code("000").message("300 - Redirect<br>400 - Client Error<br>500 - Server Error").responseModel(ErrorResponse.class).endResponseMessage()
				.to("direct:obtenerSuspensionesAcademicas")
	      ;
        
        
        
// ------- ROUTES ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        
        
        
     //----- OBTENER SUSPENSIONES ACADEMICAS ----------------------------------------------------------------------   
        
        from("direct:obtenerSuspensionesAcademicas")
			.log("INFO :::::Parametros de la consulta:  ${headers.HTTP_QUERY} ")
			.choice()
				.when(simple("${property.spidm} != null and ${property.spidm} != ''"))
					.setProperty("isPIDM").simple("${property.spidm}")
					.log("INFO :::::: SPIDM is ${property.spidm}")
					.setProperty("estado", constant(true))
				.otherwise()
					.log(":::::::: empty SPIDM...consume UNIANDES ::: WS-REST CONSULTA-PIDM}")
					.removeHeaders("*")
					.setHeader("CamelHttpUri").simple("{{url.consulta.pidm}}snumerodocumento=${headers.snumerodocumento}&amp;slogin=${headers.slogin}&amp;scodigo=${headers.scodigo}")
					.log("${headers.CamelHttpUri}")
					.setHeader("CamelHttpMethod", constant("GET"))
					.setHeader("Content-Type").constant("application/x-www-form-urlencoded")
					.process(new AuthorizationProcessor())
					.setHeader("Authorization").simple("Basic ${property.AuthToken}")
					.to("http4://dummyHttp?throwExceptionOnFailure=false")
					.setProperty("operacionExitosa").jsonpath("$.MensajeOut.BOperacionExitosa")
					.log("Response service UNIANDES ::: WS-REST CONSULTA-PIDM : ${body} code:${headers.CamelHttpResponseCode}")
					.choice()
						.when().simple("${headers.CamelHttpResponseCode} == '200' and ${property.operacionExitosa}")
							.setProperty("isPIDM").jsonpath("$.Estudiante.SPidm")
							.setProperty("estado").constant(true)
							.log("SPIDM response ws jsonpath : ${property.isPIDM}")
						.when().simple("${headers.CamelHttpResponseCode} == '404'")
							.log("Existe un problema con la operacion obtener-pidm")
							.setProperty("estado").constant(false)
							.setProperty("message", simple("{{excepcion.consulta.fallida}}"))
							//.to("direct:route-error-handler")
						.otherwise()
							.log("Existe un problema con la operacion obtener-pidm")
							.setProperty("estado").constant(false)
							.setProperty("message").constant("{{excepcion.registros.vacios}}")
							//.to("direct:route-error-handler")
					.end()
			.end()
			.to("velocity:template/gestion-suspension/query_suspensiones_academicas.vm")
			.log("QUERY::: ${body}")
			.setHeader("CamelSqlQuery").simple("${body}")
			.to("sql:dumy")
			.log( LoggingLevel.DEBUG,"QUERY RESPONSE ${body}")
			.bean(DataProcessorSuspensionesAcademicas.class)
			//.process(new DataProcessorSuspensionesAcademicas())
			//.to("direct:route-audit-response")
		;
        
        
        
     //----- OBTENER SUSPENSIONES ACADEMICAS ----------------------------------------------------------------------   

        
        from("direct:obtenerSuspensionesDisciplinarias")
        	.log("Parametros de la consulta:  ${headers.HTTP_QUERY}")
        	.choice()
        		.when().simple("${property.spidm} != null and ${property.spidm} != ''")
        		.setHeader("SPIDM").simple("${property.spidm}")
        		.log("SPIDM is ${headers.spidm}")
        		.setProperty("estado").constant(true)
        		.log("SPIDM response ws jsonpath : ${headers.SPIDM}")
        		.log("QUERY TO EXECUTE {{sql.query.suspensiones.disciplinarias}}")
        		.to("sql://{{sql.query.suspensiones.disciplinarias}}")
        		.log("QUERY RESPONSE ${body}")
        		.bean(DataProcessorSuspensionesDisciplinarias.class)
        		//.process(new DataProcessorSuspensionesDisciplinarias())
        		.otherwise()
        			.log("empty SPIDM...consume UNIANDES ::: WS-REST CONSULTA-PIDM}")
        			.removeHeaders("*")
        			.setHeader("CamelHttpUri").simple("{{url.consulta.pidm}}snumerodocumento=${property.snumerodocumento}&amp;slogin=${property.slogin}&amp;scodigo=${property.scodigo}")
        		    .log("${headers.CamelHttpUri}")
        		    .setHeader("CamelHttpMethod", constant("GET"))
        		    .setHeader("Content-Type", constant("application/x-www-form-urlencoded"))
        		    .process(new AuthorizationProcessor())
        		    .setHeader("Authorization").simple("Basic ${property.AuthToken}")
        		    .to("http4://dummyHttp?throwExceptionOnFailure=false")
        		    .setProperty("operacionExitosa").jsonpath("$.MensajeOut.BOperacionExitosa")
        		    .log("Response service UNIANDES ::: WS-REST CONSULTA-PIDM : ${body} code:${headers.CamelHttpResponseCode}")
        		    .choice()
        		    	.when().simple("${headers.CamelHttpResponseCode} == '200' and ${property.operacionExitosa}")
        		    	.setHeader("SPIDM").jsonpath("$.Estudiante.SPidm")
        		    	.setProperty("estado").constant(true)
        		    	.log("SPIDM response ws jsonpath : ${headers.SPIDM}")
        		    	.log(LoggingLevel.DEBUG ,"QUERY TO EXECUTE {{sql.query.suspensiones.disciplinarias}}")
        		    	.to("sql://{{sql.query.suspensiones.disciplinarias}}")
        		    	.log("QUERY RESPONSE ${body}")
        		    	.bean(DataProcessorSuspensionesDisciplinarias.class)
        		    	//.process(new DataProcessorSuspensionesDisciplinarias())
        		    	.when().simple("${headers.CamelHttpResponseCode} == '404'")
        		    		.log("Existe un problema con la operacion obtener-pidm")
        		    		.setProperty("estado").constant(false)
        		    		.setProperty("codigoRespuesta").constant("{{msj.code.server.error}}")
        		    		.setProperty("message", simple("{{excepcion.consulta.fallida}}"))
        		    		//.to("direct:route-error-handler")
        		    	.otherwise()
        		    		.log("Existe un problema con la operacion obtener-pidm")
        		    		.setProperty("estado").constant(false)
        		    		.setProperty("codigoRespuesta").constant("{{msj.code.not.found}}")
        		    		.setProperty("message",simple("{{excepcion.registros.vacios}}") )
        		    		.to("direct:route-error-handler")
        		    	
        		    .end()
        		  
        	.end()
        	//.to("direct:route-audit-response")
        	
        ;
        
	}
	

	
	
		

}
