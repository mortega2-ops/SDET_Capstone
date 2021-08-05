package io.catalyte.SDET_Capstone_Project.test.selenium;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.LOGIN_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.TODOS_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.ToastNotificationMessages.SUCCESSFUL_LOGIN;
import static io.catalyte.SDET_Capstone_Project.constants.ToastNotificationMessages.UNSUCCESSFUL_LOGIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.catalyte.SDET_Capstone_Project.pages.Login;
import io.catalyte.SDET_Capstone_Project.pages.Signup;
import io.catalyte.SDET_Capstone_Project.pages.Todos;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {

  private static WebDriver driver;
  public static WebDriverWait wait;
  public static Login loginPageInst;
  public static Signup signupPageInst;
  public static Todos todosPageInst;
  public static RandomStringGenerator generator = new RandomStringGenerator.Builder()
      .withinRange('a', 'z').build();

  public static final String staticEmail = "email@email.com";
  public static final String staticPassword = "password";
  public static final String randomNonExistentEmail = generator.generate(30);
  public static final String randomPassword = generator.generate(30);

  /**
   * This function ensures that a generic user will be always be signed up to test against.
   * Called in the @before method for each test case. Once the user has been submitted or confirmed
   * to already exist, the path is set to the login page.
   * @param email
   * @param password
   */
  public void signupGenericUser(String email, String password) {
    signupPageInst.submitUser(email, password);
    driver.get(LOGIN_PATH);
  }

  @Before
  public void beforeMethod() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, 10);

    loginPageInst = PageFactory.initElements(driver, Login.class);
    signupPageInst = PageFactory.initElements(driver, Signup.class);
    todosPageInst = PageFactory.initElements(driver, Todos.class);

    signupGenericUser(staticEmail, staticPassword);
  }

  @Test
  public void testExistingUserCanLoginAndIsAtTodoPage() {
    loginPageInst.loginUser(staticEmail, staticPassword);
    assertEquals(TODOS_PATH, driver.getCurrentUrl());
  }

  @Test
  public void testExistingUserCanLoginAndGetsASuccessfulToastNotification() {
    loginPageInst.loginUser(staticEmail, staticPassword);
    assertTrue(todosPageInst.stringIsDisplayed(SUCCESSFUL_LOGIN));
  }

  @Test
  public void testExistingUserEntersWrongPassword() {
    loginPageInst.loginUser(staticEmail, randomPassword);
    assertTrue(loginPageInst.stringIsDisplayed(UNSUCCESSFUL_LOGIN));
    assertEquals(LOGIN_PATH, driver.getCurrentUrl());
  }

  @Test
  public void testNonExistingUserAttemptsLoginAndIsStillAtLoginPage() {
    loginPageInst.loginUser(randomNonExistentEmail, randomPassword);
    assertEquals(LOGIN_PATH, driver.getCurrentUrl());
  }

  @Test
  public void testNonExistingUserAttemptsLoginAndGetsAnErrorToastNotification() {
    loginPageInst.loginUser(randomNonExistentEmail, randomPassword);
    assertTrue(loginPageInst.stringIsDisplayed(UNSUCCESSFUL_LOGIN));
  }

  @After
  public void afterMethod() {
    driver.quit();
  }
}
