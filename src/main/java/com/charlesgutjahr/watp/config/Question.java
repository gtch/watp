package com.charlesgutjahr.watp.config;

import com.charlesgutjahr.watp.model.QuestionType;


public class Question {

  final int number;
  final QuestionType type;
  final boolean required;
  final String text;
  final String label;
  final String help;

  Question(int number, QuestionType type, boolean required, String text, String label, String help) {
    this.number = number;
    this.type = type;
    this.required = required;
    this.text = text;
    this.label = label;
    this.help = help;
  }


  public int getNumber() {
    return number;
  }


  public QuestionType getType() {
    return type;
  }


  public boolean isRequired() {
    return required;
  }


  public String getText() {
    return text;
  }


  public String getLabel() {
    return label;
  }


  public String getHelp() {
    return help;
  }


  @Override
  public String toString() {
    return "Question{" +
      "number=" + number +
      ", type=" + type +
      ", required=" + required +
      ", text='" + text + '\'' +
      ", label='" + label + '\'' +
      ", help='" + help + '\'' +
      '}';
  }
}
