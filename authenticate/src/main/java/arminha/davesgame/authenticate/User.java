package arminha.davesgame.authenticate;

import java.security.Principal;

public interface User extends Principal {

  boolean isGroupMember(String group);

  String getId();

}
