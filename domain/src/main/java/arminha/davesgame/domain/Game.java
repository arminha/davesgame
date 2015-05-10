package arminha.davesgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Optional;

import arminha.davesgame.domain.event.GameCreated;
import arminha.davesgame.domain.event.NextStoneChoosen;
import arminha.davesgame.domain.event.StonePut;
import arminha.davesgame.domain.event.Winner;

public class Game extends AggregateRoot {

    private UUID id = NULL_ID;
    private final List<UUID> players = new ArrayList<>();
    private UUID currentPlayerID = NULL_ID;
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

    public void chooseNextStone(UUID playerId, Stone stone) throws InvalidPlayerException, DuplicateStoneException {
        if (!currentPlayerID.equals(playerId)) {
            throw new InvalidPlayerException();
        }
        if (board.isUsed(stone)) {
            throw new DuplicateStoneException();
        }
        applyChange(new NextStoneChoosen(id, playerId, stone.getFlags()));
    }

    public void putStone(UUID playerId, int x, int y) throws InvalidPlayerException, NoStoneSelectedException,
            InvalidLocationException {
        if (!currentPlayerID.equals(playerId)) {
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
            applyChange(new Winner(id, currentPlayerID));
        }
    }

    @SuppressWarnings("unused")
    private void apply(GameCreated e) {
        id = e.getId();
        currentPlayerID = e.getFirstPlayerId();
        players.add(e.getFirstPlayerId());
        players.add(e.getSecondPlayerId());
    }

    @SuppressWarnings("unused")
    private void apply(NextStoneChoosen e) {
        int ind = players.indexOf(currentPlayerID);
        currentPlayerID = players.get((ind + 1) % 2);
        nextStone = Optional.of(new Stone(e.getStone()));
    }

    @SuppressWarnings("unused")
    private void apply(StonePut e) {
        board.put(nextStone.get(), e.getX(), e.getY());
        nextStone = Optional.absent();
    }

    @SuppressWarnings("unused")
    private void apply(Winner e) {
        // TODO Auto-generated method stub

    }
}
