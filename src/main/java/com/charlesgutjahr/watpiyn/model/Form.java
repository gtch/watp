package com.charlesgutjahr.watpiyn.model;

import java.util.HashMap;
import java.util.Map;


public class Form {

  private Map<Integer, String> question;


  public Form() {
    this.question = new HashMap<>();
  }


  public Map<Integer, String> getQuestion() {
    return question;
  }


  public void setQuestion(Map<Integer, String> question) {
    this.question = question;
  }
}
