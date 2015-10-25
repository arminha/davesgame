package arminha.davesgame.server.auth;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User implements Principal {

  private final String name;

  private final Set<String> groups;

  public User(String name, Collection<String> groups) {
    this.name = Objects.requireNonNull(name);
    this.groups = new HashSet<>(groups);
  }

  @Override
  public String getName() {
    return name;
  }

  public boolean isGroupMember(String group) {
    return groups.contains(group);
  }
}
