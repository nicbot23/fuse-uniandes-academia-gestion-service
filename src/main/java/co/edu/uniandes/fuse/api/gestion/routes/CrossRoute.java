package co.edu.uniandes.fuse.api.gestion.routes;

import java.sql.SQLException;

import co.edu.uniandes.fuse.core.utils.beans.Cronometro;

public class CrossRoute extends RestConfiguration{
	
	public void configure() throws Exception {
		
		// EXCEPTIONS       
        onException(SQLException.class)
             .handled(true)
             .to("direct-vm:manageException");
        ;
        
     // ROUTE EXECUTE SQL
     		from("direct:bannerSQL")
     			.bean(Cronometro.class, "inicio(*,${date:now:HH:mm:ss:SSS})")
     			.to("banner:dumy")
     			.bean(Cronometro.class, "fin(*,${date:now:HH:mm:ss:SSS})")
     			.log("Tiempo de ejecucion SQL: ${property[tiempo]} \n ${header.CamelSqlQuery}")
     		;
     		
     // ROUTE EXECUTE SQL
    		/*from("direct:NifeSQL")
    			.bean(Cronometro.class, "inicio(*,${date:now:HH:mm:ss:SSS})")
    			.to("nife:dumy")
    			.bean(Cronometro.class, "fin(*,${date:now:HH:mm:ss:SSS})")
    			;	*/
    		
    		from("direct:RegistroSQL")
    			.bean(Cronometro.class, "inicio(*,${date:now:HH:mm:ss:SSS})")
    			.to("registro:dumy")
    			.bean(Cronometro.class, "fin(*,${date:now:HH:mm:ss:SSS})")
    			;
	}

}
