package model.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.XmlViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"controller"})
public class SpringMvcJavaConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	private ServletContext application;
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		XmlViewResolver viewResolver = new XmlViewResolver();
		viewResolver.setLocation(new ServletContextResource(application, "/WEB-INF/views.xml"));
		registry.viewResolver(viewResolver);
	}
}
