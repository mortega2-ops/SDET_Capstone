package io.catalyte.SDET_Capstone_Project.test.restAssured;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.ACCOUNTS_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.LOGIN_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.SIGNUP_PATH;
import static io.catalyte.SDET_Capstone_Project.constants.Paths.TODOS_JSON_PATH;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.catalyte.SDET_Capstone_Project.domains.Account;
import io.catalyte.SDET_Capstone_Project.domains.Todo;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TodosApiTest {

  final String ID = "1234567";

  JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
      .newBuilder().setValidationConfiguration(
          ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();

  @Before
  public void beforeMethod() {
    Todo todo = new Todo("Walk the dog.", false, ID);
    given()
        .contentType("application/json")
        .body(todo.toString())
        .when()
        .post(TODOS_JSON_PATH)
        .then()
        .statusCode(201);
  }

  @Test
  public void getTodos() throws IOException {
    given()
        .when()
        .get(TODOS_JSON_PATH)
        .then()
        .statusCode(200)
        .assertThat()
        .body(matchesJsonSchemaInClasspath("todos-schema.json").using(jsonSchemaFactory));
  }

  /**
   * The post occurs in the @Before method. We simply validate the fact that it has been posted
   * here. The posted to-do item is used as seed data for other tests.
   */
  @Test
  public void validatePostedTodo() {
    given()
        .when()
        .get(TODOS_JSON_PATH + ID)
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("todo-schema.json").using(jsonSchemaFactory));
  }

  @Test
  public void patchTodo() {
    String completedPatch = "{\n" +
        "    \"completed\": " + false + "\n" +
        '}';
    Todo result =
        given()
            .when()
            .contentType("application/json")
            .body(completedPatch)
            .when()
            .patch(TODOS_JSON_PATH + ID)
            .then()
            .statusCode(200)
            .extract()
            .as(Todo.class);

    assertFalse(result.isCompleted());

  }


  @Test
  public void deleteSpecificTodoByID() {
    given()
        .when()
        .delete(TODOS_JSON_PATH + ID)
        .then()
        .statusCode(200);

    given()
        .when()
        .get(TODOS_JSON_PATH + ID)
        .then()
        .statusCode(404);
  }

  @Test
  public void deleteAllTodos() throws InterruptedException {
    Thread.sleep(1000); //Prevents failure on first run
    given()
        .when()
        .delete("http://localhost:3000/todos")
        .then()
        .statusCode(204);

    given()
        .when()
        .get(TODOS_JSON_PATH)
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(equalTo("[]"));
  }

  @Test
  public void postArrayOfTodosToSeed() {
    String first = new Todo("Buy bread", true, "0987654321").toString();
    String second = new Todo("Pick up kids from school", false, "0987654322").toString();
    String third = new Todo("Feed the chickens", false, "0987654333").toString();

    given()
        .when()
        .contentType("application/json")
        .body("[" + first + "," + second + "," + third + "]")
        .when()
        .post("http://localhost:3000/todos/seed")
        .then()
        .statusCode(201);

    given()
        .when()
        .get(TODOS_JSON_PATH + "0987654321")
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("todo-schema.json").using(jsonSchemaFactory));

    given()
        .when()
        .get(TODOS_JSON_PATH + "0987654322")
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("todo-schema.json").using(jsonSchemaFactory));

    given()
        .when()
        .get(TODOS_JSON_PATH + "0987654333")
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("todo-schema.json").using(jsonSchemaFactory));
  }

  @Test
  public void postNewAccountToSignUpPage() {
    Account account = new Account("@something.com", "pwd");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(201);
  }

  @Test
  public void postNewAccountToSignUpPageWithTakenEmail409() {
    Account account = new Account("takenemail@taken.com", "pwd");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(201);

    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(409);
  }

  @Test
  public void postNewAccountToSignUpPageWithoutEmail401() {
    Account account = new Account("", "pwd");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(401);
  }

  @Test
  public void postNewAccountToSignUpPageWithoutPassword401() {
    Account account = new Account("@something.com", "");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(401);
  }

  @Test
  public void postNewAccountToSignUpPageThenLoginToAccount() {
    Account account = new Account("email@example.com", "abc123");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(201);

    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(200);
  }

  @Test
  public void postNewAccountToSignUpPageThenLoginToAccountWithBadEmail409() {
    Account account = new Account("email@example.com", "abc123");
    given()
        .contentType("application/json")
        .body(account.toString())
        .when()
        .post(SIGNUP_PATH)
        .then()
        .statusCode(201);

    Account typoEmailAccount = new Account("email@example.com", "abc1234");
    given()
        .contentType("application/json")
        .body(typoEmailAccount.toString())
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(401);
  }

  @Test
  public void postReset() {
    given()
        .contentType("application/json")
        .when()
        .post("http://localhost:3000/reset")
        .then()
        .statusCode(204);
  }

  @Test
  public void deleteAllAccounts() throws InterruptedException {
    Thread.sleep(1000); //Prevents failure on first run
    given()
        .when()
        .delete("http://localhost:3000/accounts")
        .then()
        .statusCode(204);

    given()
        .when()
        .get(ACCOUNTS_PATH)
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body(equalTo("[]"));
  }

  @Test
  public void postArrayOfAccountsToSeed() {
    String first = new Account("something@email1.com", "hamburgler123").toString();
    String second = new Account("something@email2.com", "nuggets123").toString();
    String third = new Account("something@email3.com", "fries123").toString();

    given()
        .when()
        .contentType("application/json")
        .body("[" + first + "," + second + "," + third + "]")
        .when()
        .post("http://localhost:3000/accounts/seed")
        .then()
        .statusCode(201);
  }
  
  @After
  public void afterMethod() {
    delete("http://localhost:3000/todos");
    delete("http://localhost:3000/accounts");
  }
}
