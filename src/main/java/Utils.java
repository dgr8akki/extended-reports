import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;

class Utils extends TestBase {
  private static Platform platform;
  private static String macPath = generatePath("tests", "target", "surefire-reports");
  private static String windowsPath = generatePath("tests", "target", "surefire-reports");
  private static String screenshotPath;

  //Get path
  static String generatePath(String... path) {
    Platform platform = getCurrentPlatform();
    StringBuilder pathBuilder = new StringBuilder();
    pathBuilder.append(System.getProperty("user.dir"));
    switch (platform) {
      case UNIX:
      case LINUX:
      case MAC:
        pathBuilder.append("/");
        for(String pathComponents : path)
          pathBuilder.append(pathComponents).append("/");
        System.out.println("File Path for MAC: " + pathBuilder.toString() + "\n");
        break;
      case WINDOWS:
        pathBuilder.append("\\");
        for(String pathComponents : path)
          pathBuilder.append(pathComponents).append("\\");
        System.out.println("File Path for WINDOWS: " + pathBuilder.toString() + "\n");
        break;
      default:
        System.out.println("File Path has not been set! There is a problem!\n");
        break;
    }
    return pathBuilder.toString();
  }

  //Get current platform
  private static Platform getCurrentPlatform() {
    if (platform == null) {
      String operSys = System.getProperty("os.name").toLowerCase();
      if (operSys.contains("win")) {
        platform = Platform.WINDOWS;
      } else if (operSys.contains("nix") || operSys.contains("nux")
        || operSys.contains("aix")) {
        platform = Platform.LINUX;
      } else if (operSys.contains("mac")) {
        platform = Platform.MAC;
      }
    }
    return platform;
  }

  static void captureScreenshot() throws IOException {
    Platform platform = getCurrentPlatform();
    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    String screenshotName = "test.jpg";
    switch (platform) {
      case UNIX:
      case LINUX:
      case MAC:
        screenshotPath = macPath + screenshotName;
        System.out.println("Screenshot captured with File Path for MAC: " + screenshotPath + "\n");
        break;
      case WINDOWS:
        screenshotPath = windowsPath + screenshotName;
        System.out.println("Screenshot captured with File Path for WINDOWS: " + screenshotPath + "\n");
        break;
      default:
        System.out.println("Screenshot capturing File Path has not been set! There is a problem!\n");
        break;
    }
    FileUtils.copyFile(scrFile, new File(screenshotPath));
  }
}
