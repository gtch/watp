package com.charlesgutjahr.watpiyn;

import com.charlesgutjahr.watpiyn.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {


  private static Path tempDirectory;

  public static void main(String[] args) {

    try {
      tempDirectory = Files.createTempDirectory("watpiyn-images");
    } catch (IOException e) {
      System.err.println("Warning: unable to create temporary directory. Logo images will not be displayed.");
      e.printStackTrace();
    }

    ApplicationContext ctx = SpringApplication.run(Application.class, args);

    // List the configuration on startup
    new Config(Config.getDefaultPropertiesFile()).listProperties(System.out);
  }

  public static Path getTempDirectory() {
    return tempDirectory;
  }

}