package arminha.davesgame.server;

import arminha.davesgame.server.resources.CommandResource;
import arminha.davesgame.server.resources.EventResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    register(CommandResource.class);
    register(EventResource.class);
  }

}
