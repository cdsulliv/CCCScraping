import java.util.regex.Pattern;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class SeleniumCCC {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
	public SeleniumCCC() {
		System.out.println("HelloFirstClarkConstr"); 
	}

//  @Before
  public void setUp() throws Exception {
	System.out.println("HelloSetUp");
	System.setProperty("webdriver.gecko.driver", "/Users/shantanupuri26/Downloads/geckodriver");
	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	capabilities.setCapability("marionette", true);
    driver = new FirefoxDriver(capabilities);
    baseUrl = "https://www.clarkcountycourts.us/Anonymous/Search.aspx?ID=400";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

//  @Test
  public String testFirstClark(String caseNumber, int skip) throws Exception {
    System.out.println("HelloTestFirstClark");
    if (skip == 0) {
        driver.get(baseUrl + "/Anonymous/Search.aspx?ID=400");
        driver.findElement(By.linkText("District Civil/Criminal Records")).click();
    }
 
    driver.findElement(By.id("CrossRefNumberOption")).click();
    driver.findElement(By.id("CaseSearchValue")).clear();
    driver.findElement(By.id("CaseSearchValue")).sendKeys(caseNumber);
    driver.findElement(By.id("SearchSubmit")).click();
    System.out.println(caseNumber);
    Thread.sleep(2000);
    String sourceIntermediate = driver.getPageSource();
    int caseNotFound = parseIntermediatePage(sourceIntermediate);
    if (caseNotFound == 1) {
        driver.findElement(By.cssSelector("a[href*='Search.aspx?ID=400']")).click();
    	return("Sorry, case not found!");
    }
    else {
    	driver.findElement(By.cssSelector("a[href*='CaseDetail']")).click();
    	String source = driver.getPageSource();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("a[href*='Search.aspx?ID=400']")).click();
        return source;
    }
//    String currentUrl = driver.getCurrentUrl();
//    System.out.println(source);
//    driver.navigate().back();
//    driver.navigate().back();
  }

  
  public int parseIntermediatePage(String source) {
	  Document document = Jsoup.parse(source);
	  Elements tables = document.select("body > table");
      System.out.println("Number of tables: " + tables.size());
      if (tables.size() == 0) {
    	  System.out.println(source);
    	  return 1;
      } else{
      Element requiredTable = tables.last();
      Elements rows = requiredTable.select("td");
	  for(Element td : rows) {
		  if (td.text().compareTo("No cases matched your search criteria.") == 0) {
			  return 1;
		  }
	  }
	  return 0;
  }
  }  

  // WRITING LINE BY LINE TO A TEXT FILE
//  String filename = caseNumber + ".txt";   
//  try {
//      PrintWriter outStream = new PrintWriter(filename);
//      outStream.println(source);
//      outStream.close();	
//  }
//  catch (FileNotFoundException e) {
//  	e.printStackTrace();
//  }
 
  
//  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
//      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}