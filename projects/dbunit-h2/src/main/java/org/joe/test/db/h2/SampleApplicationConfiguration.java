package org.joe.test.db.h2;

import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mock.web.MockServletContext;

public class SampleApplicationConfiguration implements EmbeddedServletContainerFactory {

	@Override
	public EmbeddedServletContainer getEmbeddedServletContainer(
			ServletContextInitializer... initializers) {
		for(ServletContextInitializer i : initializers)
		{
		//	new JndiDataSourceAutoConfiguration().d
		}
		return null;
	}

	
	
	
}
