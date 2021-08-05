package io.catalyte.SDET_Capstone_Project.test.selenium;

import static io.catalyte.SDET_Capstone_Project.constants.Paths.TODOS_PATH;
import static io.catalyte.SDET_Capstone_Project.pages.Todos.todos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import io.catalyte.SDET_Capstone_Project.pages.Todos;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TodosTest {
  private static WebDriver driver;
  private static WebDriverWait wait;
  public static Todos todosPageInst;
  public static final List<String> expectedLabelsList = ImmutableList
      .of("Pick up the kids from school", "Feed the chickens");

  /**
   * Adds three to-dos to the to-do page in order.
   */
  public void addTodos() {
    todosPageInst.addNewTodo("Buy bread");
    todosPageInst.addNewTodo("Pick up the kids from school");
    todosPageInst.addNewTodo("Feed the chickens");
  }

  @Before
  public void beforeMethod() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, 10);

    driver.get(TODOS_PATH);

    todosPageInst = PageFactory.initElements(driver, Todos.class);
    todosPageInst.removeAllTodos();
    addTodos();
    wait.until(ExpectedConditions.visibilityOfAllElements(todos));
  }

  @Test
  public void testAddTodos() {
    assertTrue(todosPageInst.stringIsDisplayed("Buy bread"));
    assertTrue(todosPageInst.stringIsDisplayed("Pick up the kids from school"));
    assertTrue(todosPageInst.stringIsDisplayed("Feed the chickens"));
  }

  @Test
  public void testCompleteTodo() {
    todosPageInst.completeTodo();
    assertTrue(todosPageInst.checkBoxIsChecked());
    assertTrue(todosPageInst.todoHasLineThroughIt());
  }

  @Test
  public void testRemoveTodo() {
    int totalNumberOfTodos = todosPageInst.getTodoListSize();

    todosPageInst.removeFirstTodo();
    driver.navigate().refresh();
    assertFalse(todosPageInst.stringIsDisplayed("Buy bread"));
    assertEquals(totalNumberOfTodos - 1, todosPageInst.getTodoListSize());
  }

  @Test
  public void testRemoveItemAndEnsureOrderRemains() {
    todosPageInst.removeFirstTodo();
    driver.navigate().refresh();
    assertEquals(expectedLabelsList, todosPageInst.todosToString());
  }

  @Test
  public void testDoubleClickTodoAndEditIt() {
    todosPageInst.editTodo("Renamed todo");
    assertTrue(todosPageInst.stringIsDisplayed("Renamed todo"));
  }

  @After
  public void afterMethod() {
    driver.quit();
  }
}
