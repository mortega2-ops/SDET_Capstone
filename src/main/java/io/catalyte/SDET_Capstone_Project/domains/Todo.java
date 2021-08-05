package io.catalyte.SDET_Capstone_Project.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Todo {
  private final String title;

  private final boolean completed;

  private final String id;


  @JsonCreator
  public Todo(@JsonProperty("title") String title,
      @JsonProperty("completed") Boolean completed,
      @JsonProperty("id") String id) {
    this.title = title;
    this.completed = completed;
    this.id = id;
  }


  public boolean isCompleted() {
    return completed;
  }

  @Override
  public String toString() {
    return "{\n" +
        "    \"title\": " + String.format("\"%s\"", title) + ",\n" +
        "    \"completed\": " + completed + ",\n" +
        "    \"id\": " + String.format("\"%s\"", id) + "\n" +
        '}';
  }
}

