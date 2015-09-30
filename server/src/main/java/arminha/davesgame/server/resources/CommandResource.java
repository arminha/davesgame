package arminha.davesgame.server.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import arminha.davesgame.domain.command.Command;
import arminha.davesgame.server.handlers.CommandHandler;

@Component
@Path("commands")
public class CommandResource {

    private final Map<Class<? extends Command>, CommandHandler> handlers = new HashMap<>();

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
        handler.execute(command);
    }
}
