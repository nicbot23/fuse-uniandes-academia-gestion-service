package co.edu.uniandes.fuse.api.processors.gestionNotas;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorHandler implements Processor{

	
	@Override
	public void process (Exchange exchange) throws Exception {
		
		String msgFinal = exchange.getProperty("message", String.class);
		
		exchange.setProperty("HttpErrorCode", "http.code.bad.request");
		throw new Exception(msgFinal);
		
	}
}
