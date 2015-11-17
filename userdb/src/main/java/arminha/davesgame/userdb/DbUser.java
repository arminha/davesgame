package arminha.davesgame.userdb;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@NamedQuery(name = DbUser.QUERY_BY_NAME, query = "select u from User u where u.name = :name")
@Access(AccessType.FIELD)
@Entity(name = "User")
public class DbUser {

  public static final String QUERY_BY_NAME = "findUserByName";

  @Id
  @Column(length = 36)
  private String id;

  @NotNull
  @Column(length = 255, nullable = false)
  private String name;

  @NotNull
  @Column(length = 255, nullable = false)
  private String email;

  @NotNull
  @Column(length = 255, nullable = false)
  private String passwordHash;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

}
