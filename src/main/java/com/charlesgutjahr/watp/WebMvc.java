package com.charlesgutjahr.watp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;


@Configuration
public class WebMvc extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (Application.getTempDirectory() != null) {
      registry.addResourceHandler("/images/**").addResourceLocations(Application.getTempDirectory().toUri().toString());
    }
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getCacheSettings());
  }


  public WebContentInterceptor getCacheSettings() {
    WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
    webContentInterceptor.setCacheSeconds(0);
    webContentInterceptor.setUseExpiresHeader(true);
    webContentInterceptor.setUseCacheControlHeader(true);
    webContentInterceptor.setUseCacheControlNoStore(true);
    return webContentInterceptor;
  }
}
