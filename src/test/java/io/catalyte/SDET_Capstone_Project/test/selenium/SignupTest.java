package io.catalyte.SDET_Capstone_Project.test.selenium;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.SIGNUP_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.TODOS_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.ToastNotificationMessages.SUCCESSFUL_LOGIN;
import static io.catalyte.SDET_Capstone_Project.constants.ToastNotificationMessages.UNSUCCESSFUL_SIGNUP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.catalyte.SDET_Capstone_Project.pages.Accounts;
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

public class SignupTest {

  public static Accounts accountPageInst;
  public static Signup signupPageInst;
  public static Todos todosPageInst;
  public static RandomStringGenerator generator = new RandomStringGenerator.Builder()
      .withinRange('a', 'z').build();

  private static WebDriver driver;

  /**
   * Calls POM method to submit new user information into corresponding WebElements.
   * @return - String user email. Used to perform assertion check.
   */
  public String submitNewRandomUser() {
    String email = generator.generate(7) + "@email.com";
    String password = generator.generate(15);
    signupPageInst.submitUser(email, password);
    return email;
  }

  /**
   * Calls POM method to get the body of text contained in the /accounts page. Searches and counts
   * the text for all occurrences of the email string and returns the count.
   * @param email
   * @return
   */
  public int getEmailCount(String email) {
    int count = 0;
    String bodyText = accountPageInst.getBodyText();
    while(bodyText.contains(email)){
      count++;
      bodyText = bodyText.substring(bodyText.indexOf(email) + email.length());
    }
    return count;
  }

  @Before
  public void beforeMethod() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get(SIGNUP_PATH);

    signupPageInst = PageFactory.initElements(driver, Signup.class);
    accountPageInst = PageFactory.initElements(driver, Accounts.class);
    todosPageInst = PageFactory.initElements(driver, Todos.class);
  }

  @Test
  public void testNewAccountCreated() {
    String email = submitNewRandomUser();
    assertTrue(accountPageInst.accountExists(email));
  }

  @Test
  public void testSuccessfulSignUpAndIsAtTheTodosPage() {
    submitNewRandomUser();
    assertEquals(TODOS_PATH, driver.getCurrentUrl());
  }

  @Test
  public void testSuccessfulToastNotification() {
    submitNewRandomUser();
    assertTrue(todosPageInst.stringIsDisplayed(SUCCESSFUL_LOGIN));
  }

  @Test
  public void testUserEntersExistingEmailAndNewAccountIsNotCreated() {
    String email = submitNewRandomUser(); // First submission (sign up) of a new user
    signupPageInst.submitUser(email, "password"); // Re-submission (sign up) of existing user
    assertEquals(1, getEmailCount(email)); // Should only be one such email in accounts
  }

  @Test
  public void testUserEntersExistingEmailAndIsAtTheSignUpPage() {
    String email = submitNewRandomUser(); // First submission (sign up) of a new user
    signupPageInst.submitUser(email, "password"); // Re-submission (sign up) of existing user
    assertEquals(SIGNUP_PATH, driver.getCurrentUrl());
  }

  @Test
  public void testUserEntersExistingEmailAndGetsAnErrorToastNotification() {
    String email = submitNewRandomUser();
    signupPageInst.submitUser(email, "password");
    assertTrue(signupPageInst.stringIsDisplayed(UNSUCCESSFUL_SIGNUP));
  }

  @After
  public void afterMethod() {
    driver.quit();
  }
}
