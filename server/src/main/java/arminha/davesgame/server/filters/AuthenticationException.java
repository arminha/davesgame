package arminha.davesgame.server.filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AuthenticationException extends WebApplicationException {

  private static final long serialVersionUID = 4981666494879917198L;

  private final String message;
  private final String realm;

  public AuthenticationException(String message, String realm) {
    super(unauthorized(message, realm));
    this.message = message;
    this.realm = realm;
  }

  private static Response unauthorized(String message, String realm) {
    return Response.status(Status.UNAUTHORIZED)
        .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"" + realm + "\"").entity(message)
        .type(MediaType.TEXT_PLAIN).build();
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getRealm() {
    return realm;
  }

}
