package com.charlesgutjahr.watp.config;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class Config {

  final String filePath;

  final String csvFilename;
  final String xlsFilename;
  final String xlsSheet;

  final String imageFilename;
  final String imageUrl;
  final boolean headerEnabled;
  final String headerText;
  final boolean startAgainEnabled;
  final String startAgainText;
  final String introHeading;
  final String introText;
  final String privacyHeading;
  final String privacyText;
  final String thankyouHeading;
  final String thankyouText;


  final List<Question> questions;


  Config(String filePath, String csvFilename, String xlsFilename, String xlsSheet, String imageFilename, String imageUrl, boolean headerEnabled, String headerText, boolean startAgainEnabled, String startAgainText,
         String introHeading, String introText, String privacyHeading, String privacyText, String thankyouHeading, String thankyouText, List<Question> questions) {
    this.filePath = filePath;
    this.csvFilename = csvFilename;
    this.xlsFilename = xlsFilename;
    this.xlsSheet = xlsSheet;
    this.imageFilename = imageFilename;
    this.imageUrl = imageUrl;
    this.headerEnabled = headerEnabled;
    this.headerText = headerText;
    this.startAgainEnabled = startAgainEnabled;
    this.startAgainText = startAgainText;
    this.introHeading = introHeading;
    this.introText = introText;
    this.privacyHeading = privacyHeading;
    this.privacyText = privacyText;
    this.thankyouHeading = thankyouHeading;
    this.thankyouText = thankyouText;
    this.questions = questions;
  }



  public boolean isValid() {
    return questions != null && questions.size() > 0
      && (isCsvEnabled() || isXlsEnabled());
  }


  public String getFilePath() {
    return filePath;
  }


  public boolean isCsvEnabled() {
    return StringUtils.isNotBlank(csvFilename);
  }


  public String getCsvFilename() {
    return csvFilename;
  }


  public boolean isXlsEnabled() {
    return StringUtils.isNotBlank(xlsFilename);
  }


  public String getXlsFilename() {
    return xlsFilename;
  }


  public String getXlsSheet() {
    return xlsSheet;
  }


  public boolean isHeaderEnabled() {
    return headerEnabled;
  }

  public boolean isImageEnabled() {
    return StringUtils.isNotBlank(imageFilename);
  }

  public boolean isIntroEnabled() {
    return StringUtils.isNotBlank(introHeading)
      || StringUtils.isNotBlank(introText);
  }

  public boolean isPrivacyEnabled() {
    return StringUtils.isNotBlank(privacyHeading)
      || StringUtils.isNotBlank(privacyText);
  }

  public String getImageFilename() {
    return imageFilename;
  }


  public String getImageUrl() {
    return imageUrl;
  }


  public String getHeaderText() {
    return headerText;
  }

  public boolean isStartAgainEnabled() {
    return startAgainEnabled;
  }


  public String getStartAgainText() {
    return startAgainText;
  }


  public String getIntroHeading() {
    return introHeading;
  }


  public String getIntroText() {
    return introText;
  }


  public String getPrivacyHeading() {
    return privacyHeading;
  }


  public String getPrivacyText() {
    return privacyText;
  }


  public String getThankyouHeading() {
    return thankyouHeading;
  }


  public String getThankyouText() {
    return thankyouText;
  }


  public List<Question> getQuestions() {
    return questions;
  }
}
