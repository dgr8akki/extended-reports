import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.concurrent.TimeUnit;

public class TestBase {
  static WebDriver driver;
  static String baseUrl;

  public static void setUp() {
    System.out.println("Test suite setup");
    baseUrl = "https://www.google.com";
    WebDriverManager.chromedriver().setup();
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
    chromeOptions.addArguments("--headless");
    chromeOptions.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
    driver = new ChromeDriver(chromeOptions);
    System.out.println("Chrome Launched !!!");

    driver
      .manage()
      .window()
      .setSize(new Dimension(1360, 768));
    driver.get(baseUrl);

    driver.manage()
      .timeouts()
      .implicitlyWait(10,
        TimeUnit.SECONDS);
  }

  public static void tearDown() {
    if (driver != null) {
      driver.quit();
    }
    System.out.println("Test execution completed !!!");
  }

  protected static void onElementClick(String locator) {
    driver.findElement(By.cssSelector(locator))
      .click();
    System.out.println("Clicked on element with CSS selector as : " + locator);
  }

  protected static void explicitWait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected static boolean isElementVisible(String locator) {
    WebElement element;
    try {
      element = driver.findElement(By.cssSelector(locator));
    } catch (NoSuchElementException e) {
      return false;
    }
    return element.isDisplayed();
  }
}
