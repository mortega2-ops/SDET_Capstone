package io.catalyte.SDET_Capstone_Project.pages;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.LOGIN_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.SIGNUP_PATH;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login {
  @FindBy(css = "input[type='email']")
  public static WebElement emailInput;

  @FindBy(css = "input[type='password']")
  public static WebElement passwordInput;

  @FindBy(className = "login-button")
  public static WebElement loginButton;

  @FindBy(tagName = "body")
  public static WebElement body;

  final WebDriver driver;
  final Actions actions;

  public Login(WebDriver driver) {
    this.driver = driver;
    actions = new Actions(driver);
    PageFactory.initElements(driver, this);
  }

  /**
   * Submits user login credentials on the login page
   * @param email - String representing the users email
   * @param password - String representing the users chosen password
   */
  public void loginUser(String email, String password) {
    driver.get(LOGIN_PATH);
    emailInput.sendKeys(email);
    passwordInput.sendKeys(password);
    actions.moveToElement(loginButton).perform();
    actions.click(loginButton).perform();
  }

  /**
   * Searches the body for displayed text String patter and returns true or false if found or not.
   * @param str - String representing the string to find
   * @return - boolean
   */
  public boolean stringIsDisplayed(String str) {
    return body.getText().contains(str);
  }
}
