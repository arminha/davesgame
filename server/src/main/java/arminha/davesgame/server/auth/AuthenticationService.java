package arminha.davesgame.server.auth;

public interface AuthenticationService {

  User authenticate(String user, String password);

}
