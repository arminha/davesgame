package arminha.davesgame.server.auth;

import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DummyAuthenticationService implements AuthenticationService {

  @Override
  public User authenticate(String user, String password) {
    if (user.equals("user") && password.equals("changeit")) {
      return new User(user, Collections.singleton("user"));
    }
    return null;
  }

}
