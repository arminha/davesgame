package arminha.davesgame.server.filters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import arminha.davesgame.server.auth.AuthenticationService;
import arminha.davesgame.server.auth.User;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

public class BasicAuthFilterTest {

  @Mock
  private AuthenticationService service;
  @Mock
  private ContainerRequestContext request;

  @Captor
  private ArgumentCaptor<SecurityContext> securityContext;

  private BasicAuthFilter filter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    filter = new BasicAuthFilter(service);
  }

  @Test(expected = AuthenticationException.class)
  public void noAuthentication() throws Exception {
    filter.filter(request);
  }

  @Test(expected = AuthenticationException.class)
  public void notBasicAuthentication() throws Exception {
    whenAuthorization("Digest: foo");
    filter.filter(request);
  }

  @Test(expected = WebApplicationException.class)
  public void missingPassword() throws Exception {
    whenAuthorization("Basic " + encode("test"));
    filter.filter(request);
  }

  @Test(expected = WebApplicationException.class)
  public void badEncoding() throws Exception {
    whenAuthorization("Basic "
        + Base64.encodeBase64String("test:äüö".getBytes(StandardCharsets.UTF_8)));
    when(service.authenticate("test", "äüö")).thenReturn(mock(User.class));
    filter.filter(request);
  }

  @Test
  public void validUser() throws Exception {
    whenAuthorization("Basic " + encode("test:äüö"));
    User user = mock(User.class);
    when(service.authenticate("test", "äüö")).thenReturn(user);
    filter.filter(request);
    verify(request).setSecurityContext(securityContext.capture());
    SecurityContext sc = securityContext.getValue();
    assertEquals(user, sc.getUserPrincipal());
    assertEquals("BASIC", sc.getAuthenticationScheme());
  }

  @Test(expected = WebApplicationException.class)
  public void invalidUser() throws Exception {
    whenAuthorization("Basic " + encode("test:äüö"));
    when(service.authenticate("test", "äüö")).thenReturn(null);
    filter.filter(request);
  }

  private String encode(String string) {
    return Base64.encodeBase64String(string.getBytes(StandardCharsets.ISO_8859_1));
  }

  private void whenAuthorization(String authorization) {
    when(request.getHeaderString(eq(HttpHeaders.AUTHORIZATION))).thenReturn(authorization);
  }

}
