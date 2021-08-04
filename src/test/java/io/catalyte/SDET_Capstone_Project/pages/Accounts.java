package io.catalyte.SDET_Capstone_Project.pages;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.ACCOUNTS_PATH;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Accounts {

  @FindBy(tagName = "body")
  public static WebElement body;

  final WebDriver driver;

  public Accounts(WebDriver driver) {
    this.driver = driver;
  }

  /**\
   * A function that ensures an account email is shown on the account page.(Emails must be unique)
   * @param email - String email that was submitted
   * @return
   */
  public boolean accountExists(String email) {
    driver.get(ACCOUNTS_PATH);
    return body.getText().contains(email);
  }

  public String getBodyText() {
    driver.get(ACCOUNTS_PATH);
    return body.getText();
  }
}
