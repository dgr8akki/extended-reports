import java.io.*;

public class Main extends TestBase {
  public static void main(String[] args) {
    setUp();
    String destinationPath = Utils.generatePath("tests", "target", "surefire-reports");
    baseUrl = destinationPath + "extent.html";
    try {
      if (!new File(baseUrl).exists()) {
        throw new Exception("Report not found at " + baseUrl);
      }
      if (!new File(destinationPath).exists()) {
        throw new Exception("Destination folder not found at " + destinationPath);
      }

      System.out.println("Report Path - " + baseUrl);
      System.out.println("Destination Folder - " + destinationPath);
      System.out.println("BASE URL: " + baseUrl);
      driver.get("file:///" + baseUrl);

      try {
        driver.switchTo().alert().accept();
      } catch(Exception e){
        System.out.println("unexpected alert not present");
      }

      String locator = "#slide-out > li:nth-child(3) > a > i";

      if(isElementVisible(locator)) {
        onElementClick(locator);
        explicitWait(1000);
        System.out.println("Dashboard Page Opened");
      }

      Utils.captureScreenshot();
      System.out.println("Screenshot Captured...");
    } catch (Exception e) {
      System.out.println("Error Message = " + e.getMessage());
      e.printStackTrace();
    } finally {
      tearDown();
    }
  }
}

