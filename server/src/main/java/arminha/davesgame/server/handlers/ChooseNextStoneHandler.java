package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.Game;
import arminha.davesgame.domain.GameException;
import arminha.davesgame.domain.Stone;
import arminha.davesgame.domain.command.ChooseNextStone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

public class ChooseNextStoneHandler extends TypeSafeCommandHandler<ChooseNextStone> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChooseNextStoneHandler.class);

  @Inject
  public ChooseNextStoneHandler(GameRepository repository) {
    super(ChooseNextStone.class, repository);
  }

  @Override
  protected void executeTypeSafe(ChooseNextStone command, Principal principal) {
    Game game = getRepository().load(command.getId());
    try {
      game.chooseNextStone(command.getPlayerId(), new Stone(command.getStone()));
    } catch (GameException e) {
      LOGGER.error("PutStone command failed", e);
      // TODO better error message
      throw new BadRequestException();
    }
    getRepository().save(game);
  }
}
