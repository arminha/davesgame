package arminha.davesgame.userdb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import arminha.davesgame.authenticate.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbAuthenticationServiceTest {

  private EntityManager em;
  private DbAuthenticationService service;

  @Before
  public void setup() {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("userDb");
    em = factory.createEntityManager();
    em.getTransaction().begin();
    insertUser("test_id", "Bob", "bob@example.com",
        "1000$"
            + "5A17B0B5A17B0BA11335$"
            + "E7349A4D0C9B78FFFB1612E3507D5BE8907DA451D1E312D5CF8DED992A9BA645"
            + "D8777995D056E36BC6869153D257B95A4CB81D7EECF5B0C6B41255ED9F358407");
    service = new DbAuthenticationService(em);
  }

  private void insertUser(String id, String name, String email, String passwordHash) {
    DbUser user = new DbUser();
    user.setId(id);
    user.setName(name);
    user.setEmail(email);
    user.setPasswordHash(passwordHash);
    em.persist(user);
  }

  @Test
  public void allowValidUser() throws Exception {
    User user = service.authenticate("Bob", "somePassword");
    assertThat(user, not(nullValue()));
    assertThat(user.getId(), is("test_id"));
    assertThat(user.getName(), is("Bob"));
  }

  @Test
  public void denyValidUserWrongPassword() throws Exception {
    User user = service.authenticate("Bob", "anotherPassword");
    assertThat(user, nullValue());
  }

  @Test
  public void denyUnknownUser() throws Exception {
    User user = service.authenticate("Alice", "somePassword");
    assertThat(user, nullValue());
  }

  @After
  public void tearDown() {
    em.getTransaction().rollback();
    em.close();
  }

}
