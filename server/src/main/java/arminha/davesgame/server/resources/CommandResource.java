package arminha.davesgame.server.resources;

import arminha.davesgame.domain.command.Command;
import arminha.davesgame.server.handlers.CommandHandler;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Component
@Path("commands")
public class CommandResource {

  private final Map<Class<? extends Command>, CommandHandler> handlers = new HashMap<>();

  @Context
  private SecurityContext securityContext;

  @Inject
  public CommandResource(Collection<CommandHandler> handlers) {
    for (CommandHandler handler : handlers) {
      this.handlers.put(handler.getCommandClass(), handler);
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void execute(Command command) {
    CommandHandler handler = handlers.get(command.getClass());
    handler.execute(command, securityContext.getUserPrincipal());
  }
}
