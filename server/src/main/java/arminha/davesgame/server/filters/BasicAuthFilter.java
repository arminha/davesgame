package arminha.davesgame.server.filters;

import arminha.davesgame.server.auth.AuthenticationService;
import arminha.davesgame.server.auth.User;

import com.google.common.base.Splitter;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

  private static final String BASIC_PREFIX = "Basic ";

  private static Splitter userPasswordSplitter = Splitter.on(':').limit(2);

  private AuthenticationService authentication;

  private String realm = "Test";

  @Inject
  public BasicAuthFilter(AuthenticationService authentication) {
    this.authentication = Objects.requireNonNull(authentication);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    if (authorization == null) {
      throw new AuthenticationException("Authentication credentials are required", realm);
    }
    if (!authorization.startsWith(BASIC_PREFIX)) {
      throw new AuthenticationException("Only Basic authentication is supported", realm);
    }
    String userPassword = new String(Base64.decodeBase64(authorization.substring(BASIC_PREFIX
        .length())), StandardCharsets.ISO_8859_1);
    List<String> list = userPasswordSplitter.splitToList(userPassword);
    if (list.size() != 2) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
    String userName = list.get(0);
    String password = list.get(1);
    User user = authentication.authenticate(userName, password);
    if (user == null) {
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }
    requestContext.setSecurityContext(new UserSecurityContext(user));
  }

  private final static class UserSecurityContext implements SecurityContext {

    private final User user;

    public UserSecurityContext(User user) {
      this.user = Objects.requireNonNull(user);
    }

    @Override
    public Principal getUserPrincipal() {
      return user;
    }

    @Override
    public boolean isUserInRole(String role) {
      return user.isGroupMember(role);
    }

    @Override
    public boolean isSecure() {
      return false;
    }

    @Override
    public String getAuthenticationScheme() {
      return SecurityContext.BASIC_AUTH;
    }

  }

}
