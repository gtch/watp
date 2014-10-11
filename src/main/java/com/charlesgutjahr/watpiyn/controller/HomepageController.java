package com.charlesgutjahr.watpiyn.controller;

import com.charlesgutjahr.watpiyn.config.Config;
import com.charlesgutjahr.watpiyn.config.ConfigLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomepageController {

  @RequestMapping("/")
  public ModelAndView index() {
    ModelAndView mav = new ModelAndView();
    Config config = ConfigLoader.loadConfig();
    if (config.isValid()) {
      mav.setViewName("form");
    } else {
      mav.setViewName("setup");
    }
    mav.addObject("config", config);
    return mav;
  }

}