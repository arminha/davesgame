package arminha.davesgame.authenticate;

public interface AuthenticationService {

  User authenticate(String userName, String password);

}
