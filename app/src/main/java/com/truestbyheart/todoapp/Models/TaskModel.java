package com.truestbyheart.todoapp.Models;

import java.io.Serializable;

public class TaskModel implements Serializable {
  public Integer id;
  public String task, date, time;
  public Boolean isDone;
}
