package co.edu.uniandes.fuse.api.gestion.routes;

import java.sql.SQLException;

import co.edu.uniandes.fuse.core.utils.beans.Cronometro;

public class CrossRoute extends RestConfiguration{
	
	public void configure() throws Exception {
		
		//------------ EXCEPTIONS       ----------------------------------------------------------------------------------------
        onException(SQLException.class)
             .handled(true)
             .to("direct-vm:manageException");
        ;
        
      //-------------- ROUTE-EXECUTE SQL ---------------------------------------------------------------------------------------
        
        
     		from("direct:bannerSQL")
     			.bean(Cronometro.class, "inicio(*,${date:now:HH:mm:ss:SSS})")
     			.to("banner:dumy")
     			.bean(Cronometro.class, "fin(*,${date:now:HH:mm:ss:SSS})")
     			.log("Tiempo de ejecucion SQL: ${property[tiempo]} \n ${header.CamelSqlQuery}")
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
	    	     		.setProperty("endpointpidm").simple("{{endpoint}}?scodigo=${property.scodigo}{{simbol.agregate}}snumerodocumento=${property.snumerodocumento}{{simbol.agregate}}spidm=${property.ID}{{simbol.agregate}}slogin=${property.slogin}")
	    	     		.log("${property.endpontpidm}")
	    	     		.setHeader("CamelHttpUri", simple("${property.endpontpidm}"))
	    	     		.setHeader("Authorization", simple("${property.AutoTokenService}"))
	    	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
	    	     		.log("Mensaje usuario: ${property.messageusuario}")
	    	     		.log("Authorization : ${header.Authorization}")
	    	     		.to("http://dummyHttp?throwExceptionOnFailure=false")
	    	     		.log("Response service UNIANDES ::: WS-REST CONSULTA-PIDM : ${body}")
	    	     		.setProperty("spidm").jsonpath("$.Estudiante.SPidm")
	    	     		.log("este es pidm:  :::   ${property.spidm}")
	    	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
	    	     		.log("este es mensaje USUARIO ${property.messageusuario}")
	    	     		.setProperty("messagetecnico").jsonpath("$.mensajeRta.SMensajeRtaTecnico")
	    	     		.log("este es mensaje tecnico ${property.messagetecnico}")
	    	     	
	    	     .end()
   	     		
   	     ;
   	     
   	     //pidm route refactorizada
   	     
   	  from("direct:pidmRoute")
	     	.log("ENTRANDO A PIDM")
	     	.setProperty("ID").simple("${header.spidm}")
	     	.setProperty("slogin").simple("${header.slogin}")
	     	.setProperty("snumerodocumento").simple("${header.snumerodocumento}")
	     	.setProperty("scodigo").simple("${header.scodigo}")
	     	.log(" SALIDA ID ESTUDIANTE ----> ${property.ID}")
	     	.filter(simple("${property.ID} != null and ${property.ID} != ''"))
	     		.setHeader("ContinueProcedure", constant(true))
	     		.setProperty("AccessProcess", simple("${header.ContinueProcedure}"))
	     		.log("empty SPIDM...consume UNIANDES ::: WS-GET CONSULTA-PIDM ==== (${property.AccessProcess})")
  	     		.removeHeaders("*")
  	     		.log("consumiendo PIDMMMMM")
  	     		.setHeader("CamelHttpMethod", constant("GET"))
  	     		.setHeader("Content-Type", constant("application/json"))
  	     		.setProperty("endpointpidm").simple("{{endpoint}}?scodigo=${property.scodigo}{{simbol.agregate}}snumerodocumento=${property.snumerodocumento}{{simbol.agregate}}spidm=${property.ID}{{simbol.agregate}}slogin=${property.slogin}")
  	     		.log("${property.endpontpidm}")
  	     		.setHeader("CamelHttpUri", simple("${property.endpontpidm}"))
  	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
  	     		.log("Mensaje usuario: ${property.messageusuario}")
  	     		.to("http://dummyHttp?throwExceptionOnFailure=false")
  	     		.log("Response service UNIANDES ::: WS-REST CONSULTA-PIDM : ${body}")
  	     		.setProperty("spidm").jsonpath("$.Estudiante.SPidm")
  	     		.log("See pidm student:  :::   ${property.spidm}")
  	     		.setProperty("messageusuario").jsonpath("$.mensajeRta.SMensajeRtaUsuario")
  	     		.log("This  USER message -- ${property.messageusuario}")
  	     		.setProperty("messagetecnico").jsonpath("$.mensajeRta.SMensajeRtaTecnico")
  	     		.log("This TECHNICAL message -- ${property.messagetecnico}")
  	     	
  	     .end()
	     		
	     ;
	}

}
