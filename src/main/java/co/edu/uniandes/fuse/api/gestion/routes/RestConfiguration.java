package co.edu.uniandes.fuse.api.gestion.routes;

import javax.sql.DataSource;

import org.apache.camel.BeanInject;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty.http.SecurityConstraintMapping;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.jndi.JndiTemplate;

public class RestConfiguration extends RouteBuilder{
	
	
	@BeanInject("authConstrain")
	SecurityConstraintMapping authConstrain;
	
	private DataSource dataSourceBanner;
	private DataSource dataSourceRegistro;
	
	private String realm = "";
	private String securityConstraint = "";
	private String realmKey = "";
	private String securityConstraintKey = "";

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		CamelContext context = this.getContext();

		// GET PROPERTIES FILE
		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("ref:props-gestion");		

		// DATASOURCE CONFIGURATION
		try {
			JndiTemplate jndi = new JndiTemplate();
			String jndiName = context.resolvePropertyPlaceholders("{{datasource.api.banner}}");
			dataSourceBanner = jndi.lookup("osgi:service/jdbc/"+jndiName, DataSource.class);
			
			
			JndiTemplate jndiP = new JndiTemplate();
			String jndiNameP = context.resolvePropertyPlaceholders("{{datasource.api.registro}}");
			dataSourceRegistro = jndi.lookup("osgi:service/jdbc/"+jndiNameP, DataSource.class);
			
		} catch (Exception e) {
			//dataSource = TestDataSource.getDataSource();
		}
		context.getComponent("banner", SqlComponent.class).setDataSource(dataSourceBanner);	
		context.getComponent("registro", SqlComponent.class).setDataSource(dataSourceRegistro);	
		
		//BASIC AUTH CONFIGURATION 
		String apiAuth = context.resolvePropertyPlaceholders("{{auth.api.enable}}");
		authConstrain.addInclusion("/*", context.resolvePropertyPlaceholders("{{auth.api.role}}"));
		
		if (Boolean.valueOf(apiAuth) == true) {
			realmKey = "securityConfiguration.realm";
			realm = context.resolvePropertyPlaceholders("{{auth.api.realm}}");
			securityConstraintKey = "securityConfiguration.securityConstraint";
			securityConstraint = "#authConstrain";
		}
		
		
		// REST & SWAGGER CONFIGURATION
				restConfiguration()
				    .component("netty-http")
				    .endpointProperty(realmKey, realm)
				    .endpointProperty(securityConstraintKey, securityConstraint)
				    .endpointProperty("nettySharedHttpServer", "#sharedNettyHttpServer")
				    .bindingMode(RestBindingMode.json)
				    .dataFormatProperty("prettyPrint", "{{rest.api.prettyPrint}}")
				    .contextPath("/{{rest.api.name}}")
				    .apiContextPath("/doc")
				    .apiContextRouteId("doc")
			        .apiProperty("api.title", "{{rest.api.title}}")
			        .apiProperty("api.description", "{{rest.api.description}}")
			        .apiProperty("api.version", "{{rest.api.version}}")
			        .apiProperty("api.contact.name", "{{rest.api.contact.name}}")
			        .apiProperty("api.contact.email", "{{rest.api.contact.email}}")
			        .apiProperty("cors", "{{rest.api.cors}}");
		
	}

}
