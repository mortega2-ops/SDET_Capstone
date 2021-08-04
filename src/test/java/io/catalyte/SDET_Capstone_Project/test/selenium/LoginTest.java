package io.catalyte.SDET_Capstone_Project.test.selenium;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.LOGIN_PATH;

import io.catalyte.SDET_Capstone_Project.pages.Login;
import io.catalyte.SDET_Capstone_Project.pages.Signup;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class LoginTest {

  private static WebDriver driver;
  public static Login loginPageInst;
  public static Signup signupPageInst;
  public static RandomStringGenerator generator = new RandomStringGenerator.Builder()
      .withinRange('a', 'z').build();

  @Before
  public void beforeMethod() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get(LOGIN_PATH);

    loginPageInst = PageFactory.initElements(driver, Login.class);
    signupPageInst = PageFactory.initElements(driver, Signup.class);
  }

  @Test
  public void testExistingUserCanLogin() {
    String email = "email@email.com";
    String password = "password";
    signupPageInst.submitUser(email, password);
    loginPageInst.loginUser(email, password);
  }
}
