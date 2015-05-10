package arminha.davesgame.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.UUID;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import arminha.davesgame.domain.event.Event;
import arminha.davesgame.domain.event.GameCreated;
import arminha.davesgame.domain.event.NextStoneChoosen;
import arminha.davesgame.domain.event.StonePut;
import arminha.davesgame.domain.event.Winner;

public class GameTest {

    private static final String NAME = "test game";

    private final UUID id = UUID.randomUUID();
    private final UUID firstPlayerId = UUID.randomUUID();
    private final UUID secondPlayerId = UUID.randomUUID();

    private Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void canCreate() throws Exception {
        game = new Game(id, NAME, firstPlayerId, secondPlayerId);

        assertHasChanges(game, is(new GameCreated(id, NAME, firstPlayerId, secondPlayerId)));
    }

    @Test(expected = InvalidPlayerException.class)
    public void checkFirstPlayer() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.chooseNextStone(secondPlayerId, new Stone(0));
    }

    @Test
    public void canChooseFirstStone() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.chooseNextStone(firstPlayerId, new Stone(0));

        assertHasChanges(game, is(new NextStoneChoosen(id, firstPlayerId, 0)));
    }

    @Test(expected = InvalidPlayerException.class)
    public void checkPlayerOnPutStone() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId), new NextStoneChoosen(id,
                firstPlayerId, 0));
        game.putStone(firstPlayerId, 0, 0);
    }

    @Test
    public void canPutFirstStone() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId), new NextStoneChoosen(id,
                firstPlayerId, 0));
        game.putStone(secondPlayerId, 1, 1);

        assertHasChanges(game, is(new StonePut(id, secondPlayerId, 1, 1)));
    }

    @Test(expected = NoStoneSelectedException.class)
    public void cannotPutStoneBeforeChoosing() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.putStone(firstPlayerId, 0, 0);
    }

    @Test
    public void simpleWinningGame() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.chooseNextStone(firstPlayerId, new Stone(0));
        game.putStone(secondPlayerId, 0, 0);
        game.chooseNextStone(secondPlayerId, new Stone(1));
        game.putStone(firstPlayerId, 0, 1);
        game.chooseNextStone(firstPlayerId, new Stone(2));
        game.putStone(secondPlayerId, 0, 2);
        game.chooseNextStone(secondPlayerId, new Stone(3));
        game.markChangesAsCommited();

        game.putStone(firstPlayerId, 0, 3);

        assertHasChanges(game, is(new StonePut(id, firstPlayerId, 0, 3)), is(new Winner(id, firstPlayerId)));
    }

    @Test(expected = InvalidLocationException.class)
    public void cannotPutSameLocation() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.chooseNextStone(firstPlayerId, new Stone(0));
        game.putStone(secondPlayerId, 0, 0);
        game.chooseNextStone(secondPlayerId, new Stone(1));
        game.putStone(firstPlayerId, 0, 0);
    }

    @Test(expected = DuplicateStoneException.class)
    public void cannotPutSameStone() throws Exception {
        loadHistory(game, new GameCreated(id, NAME, firstPlayerId, secondPlayerId));
        game.chooseNextStone(firstPlayerId, new Stone(0));
        game.putStone(secondPlayerId, 0, 0);
        game.chooseNextStone(secondPlayerId, new Stone(0));
    }

    @SafeVarargs
    protected final void assertHasChanges(AggregateRoot aggregateRoot, Matcher<? extends Event>... matchers) {
        @SuppressWarnings("unchecked")
        Matcher<Event>[] cast = (Matcher<Event>[]) matchers;
        assertThat(aggregateRoot.getUncommitedChanges(), IsIterableContainingInOrder.contains(cast));
    }

    protected void loadHistory(AggregateRoot aggregateRoot, Event... events) {
        aggregateRoot.loadsFromHistory(Arrays.asList(events));
    }
}
