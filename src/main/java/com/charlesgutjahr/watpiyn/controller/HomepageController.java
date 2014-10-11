package com.charlesgutjahr.watpiyn.controller;

import com.charlesgutjahr.watpiyn.config.Config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomepageController {

  @RequestMapping("/")
  public ModelAndView index() {
    ModelAndView mav = new ModelAndView();
    Config config = getConfig();
    if (config.isValid()) {
      mav.setViewName("form");
    } else {
      mav.setViewName("setup");
    }
    mav.addObject("config", config);
    return mav;
  }

  private Config getConfig() {
    return new Config(Config.getDefaultPropertiesFile());
  }

}