package es.us.dp1.lx_xy_24_25.truco_beasts.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	GenericIdToEntityConverter idToEntityConverter;
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
    	
        registry.addConverter(idToEntityConverter);
    }
    
}