package co.edu.uniandes.fuse.api.gestion.routes;



import org.apache.camel.LoggingLevel;
import org.apache.camel.model.rest.RestParamType;

import co.edu.uniandes.fuse.core.utils.models.ErrorResponse;

import co.edu.uniandes.fuse.api.processors.gestionNotas.ResponseHandler;
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
						.name("snombrecurso").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("scodigocurso").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("snivel").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
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
						.name("speriodo").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
					.endParam()
					.param()
						.name("snivel").description("Codigo del estudiante")
						.type(RestParamType.query)
						.required(true)
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
	    		.to("direct:pidmRoute")
	    		.choice()
	    			.when(simple("!${properties.ContinueProcedure}"))
		    			.log(LoggingLevel.ERROR,"::::: Error en Consultar SPIDM")
		    			.setProperty("estado").constant(false)
		    			.setProperty("message", simple("{{excepcion.pidm.disconnect}}"))
		    			.to("direct:route-error-handler")
		    		.otherwise()
		    			.to("velocity:template/gestion-notas/obtener-notas-homologadas.vm")
		    			.setHeader("CamelSqlQuery").simple("${body}")
		    			.log("${body}")
		    			.to("direct:bannerSQL")
		    			.log("${body}")
		    			.bean(ResponseHandler.class)
		    	.endChoice()
    		;
	    	
	
	    	
    //-------------- OBTENER PROMEDIO ACUMULADO ---------------------------------------------------------------------------------
	    	
	    	from("direct:obtenerPromedioAcumulado")
	    		.process(new RequestProcessor())
	    		.log(":::/obtener-notas-promedio-acumulado")
	    		.to("direct:pidmRoute")
	    		.choice()	
	    			.when().simple("!${properties.ContinueProcedure}")
		    			.log(LoggingLevel.ERROR," :::: Error en Consultar SPIDM")
		    			.setProperty("estado").constant(false)
		    			.setProperty("message",simple("{{excepcion.pidm.disconnect}}"))
		    			.to("direct:route-error-handler")
		    		.otherwise()
		    			.to("velocity:template/gestion-notas/obtener-notas-promedio-acumulado.vm")
		    			.setHeader("CamelSqlQuery").simple("${body}")
		    			.log("${body}")
		    			.to("direct:bannerSQL")
		    			.log("${body}")
		    			.bean(ResponseHandler.class)
		    	.end()
	    	;	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
	}

}
