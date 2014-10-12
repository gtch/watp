package com.charlesgutjahr.watp.config;

import com.charlesgutjahr.watp.Application;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * Detects changes in the configuration which require special handling. For example, images needs to be copied to
 * a temporary directory if the image changes in the configuration.
 */
public class ConfigChangeHandler {

  private static String imageFilename;


  public static void handleChanges(Config config) {
    if (config != null) {
      if (config.getImageFilename() != null && !config.getImageFilename().equals(imageFilename)) {
        // The image has changed, copy it to the temp directory
        imageFilename = config.getImageFilename();
        if (Application.getTempDirectory() != null) {
          File fromFile = new File(imageFilename);
          String name = fromFile.getName();
          File toFile = new File(Application.getTempDirectory().toFile(), name);
          try {
            System.out.println("Copying image " + fromFile.getAbsolutePath() + " to temporary directory "
                + toFile.getAbsolutePath());
            FileUtils.copyFile(fromFile, new File(Application.getTempDirectory().toFile(), name));
          } catch (IOException e) {
            System.err.println("Warning: unable to copy image file " + fromFile.getAbsolutePath() + " to temporary directory "
              + toFile.getAbsolutePath() + ". Logo images will not be displayed.");
            e.printStackTrace();
          }
        }
      }
    }
  }

}
