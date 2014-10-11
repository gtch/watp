package com.charlesgutjahr.watpiyn;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvc extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (Application.getTempDirectory() != null) {
      registry.addResourceHandler("/images/**").addResourceLocations(Application.getTempDirectory().toUri().toString());
    }
  }

}
