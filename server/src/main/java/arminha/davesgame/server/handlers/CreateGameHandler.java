package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.Game;
import arminha.davesgame.domain.command.CreateGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Principal;

import javax.inject.Inject;

@Component
public class CreateGameHandler extends TypeSafeCommandHandler<CreateGame> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameHandler.class);

  @Inject
  public CreateGameHandler(GameRepository repository) {
    super(CreateGame.class, repository);
  }

  @Override
  protected void executeTypeSafe(CreateGame command, Principal principal) {
    LOGGER.debug(command.toString());
    Game game = new Game(command.getId(), command.getName(), command.getFirstPlayerId(),
        command.getSecondPlayerId());
    getRepository().save(game);
  }
}
