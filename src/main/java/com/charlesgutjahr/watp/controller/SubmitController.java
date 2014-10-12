package com.charlesgutjahr.watp.controller;

import com.charlesgutjahr.watp.config.Config;
import com.charlesgutjahr.watp.config.ConfigLoader;
import com.charlesgutjahr.watp.config.Question;
import com.charlesgutjahr.watp.model.Form;
import com.charlesgutjahr.watp.model.QuestionType;
import com.charlesgutjahr.watp.model.XlsSpreadsheet;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.Map;


@Controller
public class SubmitController {

  @RequestMapping(value = "/submit", method = RequestMethod.POST)
  public ModelAndView submit(@ModelAttribute Form form) {
    Config config = ConfigLoader.loadConfig();

    saveCsv(config, form.getQuestion());
    saveXls(config, form.getQuestion());

    ModelAndView mav = new ModelAndView("thankyou");
    mav.addObject("config", config);

    return mav;
  }


  private void saveCsv(Config config, Map<Integer, String> responses) {

    if (config.isCsvEnabled()) {
      StringBuilder sb = new StringBuilder();
      for (Question question : config.getQuestions()) {
        if (question.getNumber() > 1) {
          sb.append(",");
        }

        String response = responses.get(question.getNumber());

        if (question.getType() == QuestionType.CHECKBOX) {
          // checkboxes are converted into Y and N
          sb.append("true".equals(response) ? "Y" : "N");
        } else {
          sb.append(StringEscapeUtils.escapeCsv(response));
        }
      }

      File csvFile = new File(config.getCsvFilename());
      try (Writer csvWriter = new OutputStreamWriter(new FileOutputStream(csvFile, true), "UTF-8")) {
        csvWriter.write(sb.toString() + "\n");
        System.out.println("Saved to CSV: " + sb.toString());
      } catch (FileNotFoundException e) {
        System.out.println("Error creating CSV file " + csvFile.getAbsolutePath() + ". Cannot save CSV: " + sb.toString());
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        System.out.println("Cannot save CSV file " + csvFile.getAbsolutePath() + " because UTF-8 encoding is unsupported, this should not be possible!");
        e.printStackTrace();
      } catch (IOException e) {
        System.out.println("Exception writing to CSV file " + csvFile.getAbsolutePath() + ". Cannot save CSV: " + sb.toString());
        e.printStackTrace();
      }
    }

  }


  private void saveXls(Config config, Map<Integer, String> responses) {
    if (config.isXlsEnabled()) {
      File xlsFile = new File(config.getXlsFilename());
      try {
        XlsSpreadsheet xls = new XlsSpreadsheet(xlsFile);
        xls.setup(config);
        xls.writeResponses(config, responses);
        xls.save();

      } catch (FileNotFoundException e) {
        System.out.println("Error creating XLS file " + xlsFile.getAbsolutePath());
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        System.out.println("Cannot save XLS file " + xlsFile.getAbsolutePath() + " because UTF-8 encoding is unsupported, this should not be possible!");
        e.printStackTrace();
      } catch (IOException e) {
        System.out.println("Exception writing to XLS file " + xlsFile.getAbsolutePath() );
        e.printStackTrace();
      }

    }

  }

}