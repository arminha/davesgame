package arminha.davesgame.userdb;

import arminha.davesgame.authenticate.AuthenticationService;
import arminha.davesgame.authenticate.User;

import com.google.common.base.Splitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.bind.DatatypeConverter;

public class DbAuthenticationService implements AuthenticationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DbAuthenticationService.class);

  private static final Splitter pwHashSplitter = Splitter.on('$').limit(3);

  EntityManager em;

  public DbAuthenticationService(EntityManager em) {
    this.em = em;
  }

  @Override
  public User authenticate(String userName, String password) {
    DbUser dbUser = findDbUserByName(userName);
    if (dbUser != null) {
      List<String> list = pwHashSplitter.splitToList(dbUser.getPasswordHash());
      if (list.size() == 3) {
        int iterationCount = Integer.parseInt(list.get(0));
        byte[] salt = DatatypeConverter.parseHexBinary(list.get(1));
        byte[] storedHash = DatatypeConverter.parseHexBinary(list.get(2));
        byte[] hash = computePasswordHash(iterationCount, salt, password.toCharArray());
        if (Arrays.equals(storedHash, hash)) {
          return new DbAuthUser(dbUser.getId(), dbUser.getName(), new ArrayList<String>());
        }
      } else {
        LOGGER.error("Stored password hash with invalid format");
      }
    } else {
      computePasswordHash(1000, new byte[10], password.toCharArray());
    }
    return null;
  }

  private DbUser findDbUserByName(String userName) {
    TypedQuery<DbUser> query = em.createNamedQuery(DbUser.QUERY_BY_NAME, DbUser.class);
    query.setParameter("name", userName);
    List<DbUser> results = query.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  private byte[] computePasswordHash(int iterationCount, byte[] salt, char[] password) {
    try {
      PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, 64 * 8);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      return keyFactory.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  private static final class DbAuthUser implements User {
    private final String id;
    private final String name;
    private final HashSet<String> groups;

    public DbAuthUser(String id, String name, Collection<String> groups) {
      this.id = Objects.requireNonNull(id);
      this.name = Objects.requireNonNull(name);
      this.groups = new HashSet<>(groups);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isGroupMember(String group) {
      return groups.contains(group);
    }

    @Override
    public String getId() {
      return id;
    }
  }
}
