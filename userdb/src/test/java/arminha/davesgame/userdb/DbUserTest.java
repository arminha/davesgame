package arminha.davesgame.userdb;

import static org.junit.Assert.assertEquals;

import arminha.davesgame.userdb.DbUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbUserTest {

  private EntityManager em;

  @Before
  public void setup() {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("userDb");
    em = factory.createEntityManager();
    em.getTransaction().begin();
  }

  @After
  public void tearDown() {
    em.getTransaction().rollback();
    em.close();
  }

  @Test
  public void createUser() throws Exception {
    createDbUser();
  }

  @Test
  public void findUser() throws Exception {
    DbUser user = createDbUser();
    DbUser user2 = em.find(DbUser.class, user.getId());
    assertEquals(user.getEmail(), user2.getEmail());
  }

  private DbUser createDbUser() {
    DbUser user = new DbUser();
    user.setId(UUID.randomUUID().toString());
    user.setEmail("test@example.com");
    user.setName("Test user");
    user.setPasswordHash("xxxxxx");
    em.persist(user);
    return user;
  }
}
