import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {                                                 //(htmlfilelocation,destinationimageslocation)
    WebDriver driver = null;
    WebElement elementCategory = null;
    String reportPath = args[0];
    String destinationPath = args[1];
    try {
      if (!new File(reportPath).exists()) {
        throw new Exception("Report not found at " + reportPath);
      }
      if (!new File(destinationPath).exists()) {
        throw new Exception("Destination folder not found at " + destinationPath);
      }

      System.out.println("Report Path - " + reportPath);
      System.out.println("Destination Folder - " + destinationPath);
      WebDriverManager.chromedriver().setup();
      ChromeOptions chromeOptions = new ChromeOptions();
      chromeOptions.addArguments("--disable-gpu", "--headless", "--no-sandbox", "--disable-dev-shm-usage", "-allow-running-insecure-content");
      driver = new ChromeDriver(chromeOptions);
      System.out.println("Chrome Launched !!!");
      driver.get("file:///" + reportPath);                                                                       //append htmlfilelocation
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      driver.manage().window().maximize();
      try {
        elementCategory = driver.findElement(By.cssSelector("div.card-panel"));
      } catch (ElementNotInteractableException e) {
        System.out.println(e.getMessage());
      }

      if(elementCategory != null)
        takeSnapShot(driver, destinationPath + "\\categoryImage.png", elementCategory);

      System.out.println("Creating HTML file...");
      File resultFile = new File(destinationPath + "\\emailReport.html");
      PrintWriter writer = new PrintWriter(resultFile);
      writer.write("<html>\n");
      writer.append("<body>\n");
      writer.append("<p><img src=\"categoryImage.png\"/></p>\n");
      writer.append("</html>\n");
      writer.close();
      System.out.println("Email Report Generation Complete!");
    } catch (Exception e) {
      System.out.println("Error Message = " + e.getMessage());
      e.printStackTrace();
    } finally {
      assert driver != null;
      driver.close();
    }
  }

  private static void takeSnapShot(WebDriver webdriver, String destinationPath, WebElement webElement) {
    try {
      TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
      File srcFile = scrShot.getScreenshotAs(OutputType.FILE);                                      //storing screenshot in srcfile
      BufferedImage fullImg = ImageIO.read(srcFile);

      Point point = webElement.getLocation();                                                      // getting upper left corner of particular section
      int width = webElement.getSize().getWidth();                                                 //find width from that corner
      int height = webElement.getSize().getHeight();                                              // find height from that corner
      BufferedImage ElementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), width, height);//getting subimage from complete webpage
      ImageIO.write(ElementScreenshot, "png", srcFile);
      File DestFile = new File(destinationPath);                                                       //create the png file in particular folder
      FileUtils.copyFile(srcFile, DestFile);
    } catch (WebDriverException e) {
      System.out.println("webdriver Exception");
    } catch (IOException e) {
      System.out.println("InputOutput Exception");
    }
  }
}

