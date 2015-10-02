package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.command.Command;
import arminha.davesgame.domain.command.CreateGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Component
public class CreateGameHandler implements CommandHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameHandler.class);

  @Context
  private SecurityContext securityContext;

  @Override
  public Class<? extends Command> getCommandClass() {
    return CreateGame.class;
  }

  @Override
  public void execute(Command command, Principal principal) {
    CreateGame create = (CreateGame) command;
    LOGGER.info(create.toString());
    LOGGER.info(principal != null ? principal.toString() : "");
  }

}
