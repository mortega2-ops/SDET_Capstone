package io.catalyte.SDET_Capstone_Project.pages;

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

  final WebDriver driver;
  final Actions actions;

  public Signup(WebDriver driver) {
    this.driver = driver;
    actions = new Actions(driver);
    PageFactory.initElements(this.driver, this);
  }

  public void submitUser(String email, String password) {
    emailInput.sendKeys(email);
    passwordInput.sendKeys(password);
    actions.moveToElement(signupButton).perform();
    actions.click(signupButton).perform();
  }
}
