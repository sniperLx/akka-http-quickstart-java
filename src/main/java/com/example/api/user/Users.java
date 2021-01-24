package com.example.api.user;

import java.util.LinkedList;
import java.util.List;

public final class Users {
  public final List<User> users;

  public Users(List<User> users) {
    this.users = users;
  }

  public Users() {
    this.users = new LinkedList<>();
  }

  public List<User> getUsers() {
    return users;
  }
}
