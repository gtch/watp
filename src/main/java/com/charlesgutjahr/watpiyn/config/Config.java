package com.charlesgutjahr.watpiyn.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;


public class Config {


  private final Properties properties;
  private String filePath;

  private String IMAGE_FILENAME = "image.filename";

  private String HEADER_ENABLED = "header.enabled";
  private String HEADER_TEXT = "header.text";
  private String STARTAGAIN_ENABLED = "startagain.enabled";
  private String STARTAGAIN_TEXT = "startagain.text";

  private String INTRO_HEADING = "intro.heading";
  private String INTRO_TEXT = "intro.text";

  private String PRIVACY_HEADING = "privacy.heading";
  private String PRIVACY_FILENAME = "privacy.filename";
  private String PRIVACY_TEXT = "privacy.text";


  public Config() {
    this.properties = new Properties();
  }
  public Config(File file) {
    this.properties = new Properties();
    loadProperties(file);
  }


  public static File getDefaultPropertiesFile() {
    URL jarUrl = Config.class.getProtectionDomain().getCodeSource().getLocation();
    File jarFile = new File(jarUrl.getPath(), "watpiyn.properties");
    return jarFile;
  }


  public boolean isValid() {
    return true;
  }


  /**
   * Write the list of configured properties to the given PrintStream. Useful for debugging.
   * @param out An output stream, such as System.out
   */
  public void listProperties(PrintStream out) {
    out.println("### watpiyn configuration at startup is: ###");
    properties.list(out);
  }


  public String getFilePath() {
    return filePath;
  }


  public boolean isImageEnabled() {
    return StringUtils.isNotBlank(getImageFilename());
  }

  public String getImageFilename() {
    return properties.getProperty(IMAGE_FILENAME);
  }

  public String getImageUrl() {
    String filename = getImageFilename();
    if (filename == null) {
      return null;
    } else {
      return "/images/" + new File(filename).getName();
    }
  }

  public boolean isHeaderEnabled() {
    return Boolean.parseBoolean(properties.getProperty(HEADER_ENABLED));
  }

  public String getHeaderText() {
    return properties.getProperty(HEADER_TEXT);
  }

  public boolean isStartAgainEnabled() {
    return Boolean.parseBoolean(properties.getProperty(STARTAGAIN_ENABLED));
  }

  public String getStartAgainText() {
    return properties.getProperty(STARTAGAIN_TEXT);
  }

  public boolean isIntroEnabled() {
    return StringUtils.isNotBlank(getIntroHeading())
      || StringUtils.isNotBlank(getIntroText());
  }

  public String getIntroHeading() {
    return properties.getProperty(INTRO_HEADING);
  }

  public String getIntroText() {
    String text = properties.getProperty(INTRO_TEXT);
    return text == null ? null : text.replace("\n", "</p><p>"); // Convert line breaks into new paragraphs
  }

  public boolean isPrivacyEnabled() {
    return StringUtils.isNotBlank(getPrivacyHeading())
      || StringUtils.isNotBlank(getPrivacyText());
  }

  public String getPrivacyHeading() {
    return properties.getProperty(PRIVACY_HEADING);
  }

  public String getPrivacyText() {
    String privacyFile = properties.getProperty(PRIVACY_FILENAME);
    String text = null;
    if (privacyFile != null) {
      text = loadTextFile(new File(privacyFile));
    }
    if (text == null) {
      text = properties.getProperty(PRIVACY_TEXT);
    }
    return text;
  }







  public String loadTextFile(File file) {
    try (Reader fileReader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
      return IOUtils.toString(fileReader);
    } catch (FileNotFoundException e) {
      System.out.println("Text file " + file + " does not exist.");
    } catch (UnsupportedEncodingException e) {
      System.out.println("Cannot load text because UTF-8 encoding is unsupported, this should not be possible!");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Cannot load text due to exception reading file " + file);
      e.printStackTrace();
    }
    return null;
  }

  public void loadProperties(File file) {
    filePath = file.getAbsolutePath();
    try (Reader propertiesReader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
      properties.load(propertiesReader);
      ConfigChangeHandler.handleChanges(this);
    } catch (FileNotFoundException e) {
      System.out.println("Properties file " + file + " does not exist. No configuration is being loaded.");
    } catch (UnsupportedEncodingException e) {
      System.out.println("Cannot load configuration because UTF-8 encoding is unsupported, this should not be possible!");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Cannot load configuration due to exception reading properties file "  + file);
      e.printStackTrace();
    }
  }


  /**
   * For future use, would save configuration to a properties file. Not currently used.
   * @param file
   */
  public void saveProperties(File file) {
    System.out.println("Saving configuration to file " + file);
    filePath = file.getAbsolutePath();
    try (Writer propertiesWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
      properties.store(propertiesWriter, "## watpiyn configuration ###");
    } catch (UnsupportedEncodingException e) {
      System.out.println("Cannot save configuration because UTF-8 encoding is unsupported, this should not be possible!");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Cannot save configuration due to exception writing properties file");
      e.printStackTrace();
    }
  }




}
