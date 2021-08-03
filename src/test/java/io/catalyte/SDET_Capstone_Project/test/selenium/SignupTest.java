package io.catalyte.SDET_Capstone_Project.test.selenium;

import io.catalyte.SDET_Capstone_Project.pages.Signup;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class SignupTest {

  private static WebDriver driver;
  public static Signup signupPageInst;

  @Before
  public void beforeMethod() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get("http://localhost:3000/signup");

    signupPageInst = PageFactory.initElements(driver, Signup.class);
  }

  @Test
  public void testAddNewUser() {
    String email = "email@email.com";
    String password = "password";
    signupPageInst.submitUser(email, password);
  }

  @After
  public void afterMethod() {
    driver.quit();
  }
}
