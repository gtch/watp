package com.charlesgutjahr.watpiyn.config;

import com.charlesgutjahr.watpiyn.model.QuestionType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Handles the loading (and, in future, saving) of configuration files.
 */
public class ConfigLoader {

  private static String CSV_FILENAME = "csv.filename";
  private static String XLS_FILENAME = "xls.filename";
  private static String XLS_SHEET = "xls.sheet";

  private static String IMAGE_FILENAME = "image.filename";

  private static String HEADER_ENABLED = "header.enabled";
  private static String HEADER_TEXT = "header.text";
  private static String STARTAGAIN_ENABLED = "startagain.enabled";
  private static String STARTAGAIN_TEXT = "startagain.text";

  private static String INTRO_HEADING = "intro.heading";
  private static String INTRO_TEXT = "intro.text";

  private static String PRIVACY_HEADING = "privacy.heading";
  private static String PRIVACY_FILENAME = "privacy.filename";
  private static String PRIVACY_TEXT = "privacy.text";

  private static String THANKYOU_HEADING = "thankyou.heading";
  private static String THANKYOU_FILENAME = "thankyou.filename";
  private static String THANKYOU_TEXT = "thankyou.text";

  private static String QUESTION_PREFIX = "question.";
  private static String QUESTION_SUFFIX_LABEL = ".label";
  private static String QUESTION_SUFFIX_TYPE = ".type";
  private static String QUESTION_SUFFIX_TEXT = ".text";
  private static String QUESTION_SUFFIX_HELP = ".help";
  private static String QUESTION_SUFFIX_REQUIRED = ".required";


  public static Config loadConfig() {
    return loadProperties(getDefaultPropertiesFile());
  }


  public static File getDefaultPropertiesFile() {
    URL jarUrl = Config.class.getProtectionDomain().getCodeSource().getLocation();
    File jarFile = new File(jarUrl.getPath(), "watpiyn.properties");
    return jarFile;
  }

  public static String loadTextFile(File file) {
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


  public static Config loadProperties(File file) {
    try (Reader propertiesReader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
      Properties properties = new Properties();
      properties.load(propertiesReader);
      List<Question> questions = getQuestions(properties);
      Config config = new Config(
        file.getAbsolutePath(),
        getCsvFilename(properties),
        getXlsFilename(properties),
        getXlsSheet(properties),
        getImageFilename(properties),
        getImageUrl(properties),
        getHeaderEnabled(properties),
        getHeaderText(properties),
        getStartAgainEnabled(properties),
        getStartAgainText(properties),
        getIntroHeading(properties),
        getIntroText(properties),
        getPrivacyHeading(properties),
        getPrivacyText(properties),
        getThankyouHeading(properties),
        getThankyouText(properties),
        questions
      );
      ConfigChangeHandler.handleChanges(config);
      return config;
    } catch (FileNotFoundException e) {
      System.out.println("Properties file " + file + " does not exist. No configuration is being loaded.");
    } catch (UnsupportedEncodingException e) {
      System.out.println("Cannot load configuration because UTF-8 encoding is unsupported, this should not be possible!");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Cannot load configuration due to exception reading properties file "  + file);
      e.printStackTrace();
    }
    return null;
  }


  private static List<Question> getQuestions(Properties properties) {
    List<Question> questions = new ArrayList<>();

    int questionNumber = 0;
    boolean foundQuestion = false;

    do {
      questionNumber++;
      String questionTypeString = properties.getProperty(QUESTION_PREFIX + questionNumber + QUESTION_SUFFIX_TYPE);
      if (questionTypeString == null) {
        foundQuestion = false;
      } else {
        foundQuestion = true;
        try {
          QuestionType type = QuestionType.valueOf(questionTypeString.toUpperCase());
          String text = properties.getProperty(QUESTION_PREFIX + questionNumber + QUESTION_SUFFIX_TEXT);
          String label = properties.getProperty(QUESTION_PREFIX + questionNumber + QUESTION_SUFFIX_LABEL);
          String help = properties.getProperty(QUESTION_PREFIX + questionNumber + QUESTION_SUFFIX_HELP);
          boolean required = Boolean.parseBoolean(properties.getProperty(QUESTION_PREFIX + questionNumber + QUESTION_SUFFIX_REQUIRED));
          Question question = new Question(questionNumber, type, required, text, label, help);
          questions.add(question);
        } catch (IllegalArgumentException e) {
          System.err.println("Warning: unable to add question " + questionNumber + " due to exception:");
          e.printStackTrace();
        }
      }

    } while (foundQuestion);

    return questions;
  }





  public static String getCsvFilename(Properties properties) {
    return properties.getProperty(CSV_FILENAME);
  }


  public static String getXlsFilename(Properties properties) {
    return properties.getProperty(XLS_FILENAME);
  }


  public static String getXlsSheet(Properties properties) {
    return properties.getProperty(XLS_SHEET);
  }

  public static String getImageFilename(Properties properties) {
    return properties.getProperty(IMAGE_FILENAME);
  }

  public static String getImageUrl(Properties properties) {
    String filename = getImageFilename(properties);
    if (filename == null) {
      return null;
    } else {
      return "/images/" + new File(filename).getName();
    }
  }

  private static boolean getHeaderEnabled(Properties properties) {
    return Boolean.parseBoolean(properties.getProperty(HEADER_ENABLED));
  }

  public static String getHeaderText(Properties properties) {
    return properties.getProperty(HEADER_TEXT);
  }

  private static boolean getStartAgainEnabled(Properties properties) {
    return Boolean.parseBoolean(properties.getProperty(STARTAGAIN_ENABLED));
  }

  public static String getStartAgainText(Properties properties) {
    return properties.getProperty(STARTAGAIN_TEXT);
  }

  public static String getIntroHeading(Properties properties) {
    return properties.getProperty(INTRO_HEADING);
  }

  public static String getIntroText(Properties properties) {
    String text = properties.getProperty(INTRO_TEXT);
    return text == null ? null : text.replace("\n", "</p><p>"); // Convert line breaks into new paragraphs
  }

  public static String getPrivacyHeading(Properties properties) {
    return properties.getProperty(PRIVACY_HEADING);
  }

  public static String getPrivacyText(Properties properties) {
    String privacyFile = properties.getProperty(PRIVACY_FILENAME);
    String text = null;
    if (privacyFile != null) {
      text = ConfigLoader.loadTextFile(new File(privacyFile));
    }
    if (text == null) {
      text = properties.getProperty(PRIVACY_TEXT);
    }
    return text;
  }

  public static String getThankyouHeading(Properties properties) {
    return properties.getProperty(THANKYOU_HEADING);
  }

  public static String getThankyouText(Properties properties) {
    String thankyouFile = properties.getProperty(THANKYOU_FILENAME);
    String text = null;
    if (thankyouFile != null) {
      text = ConfigLoader.loadTextFile(new File(thankyouFile));
    }
    if (text == null) {
      text = properties.getProperty(THANKYOU_TEXT);
    }
    return text == null ? null : text.replace("\n", "</p><p>"); // Convert line breaks into new paragraphs
  }


}
