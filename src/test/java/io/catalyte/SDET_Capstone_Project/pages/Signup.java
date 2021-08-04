package io.catalyte.SDET_Capstone_Project.pages;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.SIGNUP_PATH;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Signup {
  @FindBy(css = "input[type='email']")
  public static WebElement emailInput;

  @FindBy(css = "input[type='password']")
  public static WebElement passwordInput;

  @FindBy(className = "signup-button")
  public static WebElement signupButton;

  @FindBy(tagName = "body")
  public static WebElement body;

  final WebDriver driver;
  final Actions actions;

  public Signup(WebDriver driver) {
    this.driver = driver;
    actions = new Actions(driver);
    PageFactory.initElements(this.driver, this);
  }

  /**
   * Submits new user login credentials on the sign up page
   * @param email - String representing the users email
   * @param password - String representing the users chosen password
   */
  public void submitUser(String email, String password) {
    driver.get(SIGNUP_PATH);
    emailInput.sendKeys(email);
    passwordInput.sendKeys(password);
    actions.moveToElement(signupButton).perform();
    actions.click(signupButton).perform();
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
