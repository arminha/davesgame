package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.Game;
import arminha.davesgame.domain.GameException;
import arminha.davesgame.domain.command.PutStone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Component
public class PutStoneHandler extends TypeSafeCommandHandler<PutStone> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PutStoneHandler.class);

  @Inject
  public PutStoneHandler(GameRepository repository) {
    super(PutStone.class, repository);
  }

  @Override
  protected void executeTypeSafe(PutStone command, Principal principal) {
    Game game = getRepository().load(command.getId());
    try {
      game.putStone(command.getPlayerId(), command.getX(), command.getY());
    } catch (GameException e) {
      LOGGER.error("PutStone command failed", e);
      // TODO better error message
      throw new BadRequestException();
    }
    getRepository().save(game);
  }
}
