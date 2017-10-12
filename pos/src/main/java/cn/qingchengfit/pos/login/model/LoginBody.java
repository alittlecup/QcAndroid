package cn.qingchengfit.pos.login.model;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginBody {

  public String username;
  public String passward;

  public LoginBody(String username, String passward) {
    this.username = username;
    this.passward = passward;
  }
}
