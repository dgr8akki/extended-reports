import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {                                                 //(htmlfilelocation,destinationimageslocation)
    WebDriver driver = null;
    WebElement elementCategory = null;
    String reportPath = args[0];
    String destinationPath = args[1];
    String image = "";
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
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      Dimension dimension = new Dimension(1440, 768);
      driver.manage()
        .window()
        .setSize(dimension);
      driver.get("file:///" + reportPath);
      try {
        elementCategory = driver.findElement(By.tagName("html"));
      } catch (ElementNotInteractableException e) {
        System.out.println(e.getMessage());
      }

      if(elementCategory != null)
        takeSnapShot(driver, destinationPath + "categoryImage.png", elementCategory);

      System.out.println("Creating HTML file...");
      File resultFile = new File(destinationPath + "emailReport.html");
      PrintWriter writer = new PrintWriter(resultFile);
      writer.write("<html>\n");
      writer.append("<body>\n");
      writer.append("<p><img src=\"cid:categoryImage.png\"/></p>\n");
//      writer.append("<p><img alt=\"image\" data-inline=\"true\" src=\"data:image/png;base64, ").append(image).append("\"/></p>\n");
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
    String base64String = "";
    try {
      TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
      File srcFile = scrShot.getScreenshotAs(OutputType.FILE);                                      //storing screenshot in srcfile
      BufferedImage fullImg = ImageIO.read(srcFile);

      Point point = webElement.getLocation();                                                      // getting upper left corner of particular section
      int width = webElement.getSize().getWidth();                                                 //find width from that corner
      int height = webElement.getSize().getHeight();                                              // find height from that corner
      BufferedImage ElementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), width, height);//getting subimage from complete webpage
      base64String = imgToBase64String(ElementScreenshot);
      ImageIO.write(ElementScreenshot, "png", srcFile);
      File DestFile = new File(destinationPath);
//      byte[] encodedBytes = Base64.getEncoder().encode("Test".getBytes());
//      base64String = new String(encodedBytes);
//      System.out.println("encodedBytes : " + base64String);
//      System.out.println("Arrays.toString encodedBytes : " + Arrays.toString(encodedBytes));
//      byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
//      System.out.println("decodedBytes " + new String(decodedBytes));
      FileUtils.copyFile(srcFile, DestFile);
    } catch (WebDriverException e) {
      System.out.println("webdriver Exception");
    } catch (IOException e) {
      System.out.println("InputOutput Exception");
    }
//    return base64String;
  }

  private static String imgToBase64String(final RenderedImage img)
  {
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    try
    {
      ImageIO.write(img, "png", os);
      return Base64.getEncoder().encodeToString(os.toByteArray());
    }
    catch (final IOException ioe)
    {
      throw new UncheckedIOException(ioe);
    }
  }
}

