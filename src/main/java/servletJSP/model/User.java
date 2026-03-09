package servletJSP.model;

import java.io.Serializable;

public class User implements Serializable {
  private String id;
  private String name;
  private String pass;
  private String profile;

  public User() { }
  public User(String id, String name, String pass, String profile) {
    this.id = id;
    this.name = name;
    this.pass = pass;
    this.profile = profile;
  }
  public String getId() { return id; }
  public String getPass() { return pass; }
  public String getName() { return name; }
  public String getProfile() { return profile; }
}