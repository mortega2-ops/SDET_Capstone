package io.catalyte.SDET_Capstone_Project.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
  private final String email;

  private final String password;

  @JsonCreator
  public Account(@JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }

  @Override
  public String toString() {
    return "{\n" +
        "    \"email\": " + String.format("\"%s\"", email) + ",\n" +
        "    \"password\": " + String.format("\"%s\"", password) + "\n" +
        '}';
  }

}
