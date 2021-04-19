package com.dam.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @author Daniel
 *
 */

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {
	
	private String imagesRoute = "file:D:/Eclipse-Workspace/spring-mvc/src/main/resources/static/images/";
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations(imagesRoute);
	}
}
