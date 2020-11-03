package co.edu.uniandes.fuse.api.processors.gestionSuspension;

import java.util.Properties;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Base64;

public class AuthorizationProcessor implements Processor{
	
	@BeanInject("props-gestion")
	private Properties properties;

	public void process(Exchange exchange) throws Exception {
		String user = this.properties.getProperty("jaas.login.module.connection.username");
		String pwd = this.properties.getProperty("jaas.login.module.connection.password");
		String str = user + ":" + pwd;
		String token = new String(Base64.encodeBase64(str.getBytes("UTF-8")));
		exchange.setProperty("AuthToken", token);
	}

}
