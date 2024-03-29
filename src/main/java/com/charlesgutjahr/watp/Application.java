package com.charlesgutjahr.watp;

import com.charlesgutjahr.watp.config.Config;
import com.charlesgutjahr.watp.config.ConfigLoader;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {


  private static Path tempDirectory;

  public static void main(String[] args) {

    // Create a temporary directory on startup to store images
    try {
      tempDirectory = Files.createTempDirectory("watp-images");
    } catch (IOException e) {
      System.err.println("Warning: unable to create temporary directory. Logo images will not be displayed.");
      e.printStackTrace();
    }

    ApplicationContext ctx = SpringApplication.run(Application.class, args);

    // List the configuration on startup
    Config config = ConfigLoader.loadProperties(ConfigLoader.getDefaultPropertiesFile());
    System.out.println("### watp configuration at startup is: ###");
    System.out.println(new ReflectionToStringBuilder(config, ToStringStyle.MULTI_LINE_STYLE).toString());
    System.out.println();
    System.out.println("### watp is now ready at http://localhost:8080/ ###");
  }

  public static Path getTempDirectory() {
    return tempDirectory;
  }

}