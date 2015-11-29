package arminha.davesgame.domain;

import arminha.davesgame.domain.event.GameCreated;
import arminha.davesgame.domain.event.NextStoneChoosen;
import arminha.davesgame.domain.event.StonePut;
import arminha.davesgame.domain.event.Winner;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game extends AggregateRoot {

  private UUID id = NULL_ID;
  private final List<UUID> players = new ArrayList<>();
  private UUID currentPlayerId = NULL_ID;
  private Optional<Stone> nextStone = Optional.absent();
  private final Board board = new Board();

  public Game() {
  }

  public Game(UUID id, String name, UUID firstPlayerId, UUID secondPlayerId) {
    applyChange(new GameCreated(id, name, firstPlayerId, secondPlayerId));
  }

  @Override
  public UUID getId() {
    return id;
  }

  public void chooseNextStone(UUID playerId, Stone stone) throws InvalidPlayerException,
      DuplicateStoneException {
    if (!currentPlayerId.equals(playerId)) {
      throw new InvalidPlayerException();
    }
    if (board.isUsed(stone)) {
      throw new DuplicateStoneException();
    }
    applyChange(new NextStoneChoosen(id, playerId, stone.getFlags()));
  }

  public void putStone(UUID playerId, int x, int y) throws InvalidPlayerException,
      NoStoneSelectedException, InvalidLocationException {
    if (!currentPlayerId.equals(playerId)) {
      throw new InvalidPlayerException();
    }
    if (!nextStone.isPresent()) {
      throw new NoStoneSelectedException();
    }
    if (!board.isEmpty(x, y)) {
      throw new InvalidLocationException();
    }
    applyChange(new StonePut(id, playerId, x, y));
    checkWinner();
  }

  private void checkWinner() {
    if (board.hasWinner()) {
      applyChange(new Winner(id, currentPlayerId));
    }
  }

  @SuppressWarnings("unused")
  private void apply(GameCreated event) {
    id = event.getId();
    currentPlayerId = event.getFirstPlayerId();
    players.add(event.getFirstPlayerId());
    players.add(event.getSecondPlayerId());
  }

  @SuppressWarnings("unused")
  private void apply(NextStoneChoosen event) {
    int ind = players.indexOf(currentPlayerId);
    currentPlayerId = players.get((ind + 1) % 2);
    nextStone = Optional.of(new Stone(event.getStone()));
  }

  @SuppressWarnings("unused")
  private void apply(StonePut event) {
    board.put(nextStone.get(), event.getX(), event.getY());
    nextStone = Optional.absent();
  }

  @SuppressWarnings("unused")
  private void apply(Winner event) {
    // TODO Auto-generated method stub

  }
}
