package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.Game;

import java.util.UUID;

public interface GameRepository {

  void save(Game game);

  Game load(UUID id);

}
