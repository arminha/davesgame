package arminha.davesgame.server.auth;

import arminha.davesgame.authenticate.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DummyUser implements User {

  private final String name;
  private final String id;
  private final Set<String> groups;

  public DummyUser(String name, String id, Collection<String> groups) {
    this.name = Objects.requireNonNull(name);
    this.id = Objects.requireNonNull(id);
    this.groups = new HashSet<>(groups);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean isGroupMember(String group) {
    return groups.contains(group);
  }
}
