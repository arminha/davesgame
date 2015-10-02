package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.command.Command;
import arminha.davesgame.domain.command.CreateGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateGameHandler implements CommandHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameHandler.class);

  @Override
  public Class<? extends Command> getCommandClass() {
    return CreateGame.class;
  }

  @Override
  public void execute(Command command) {
    CreateGame create = (CreateGame) command;
    LOGGER.info(create.toString());
  }

}
