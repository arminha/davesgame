package arminha.davesgame.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

  private Board board;

  @Before
  public void setup() {
    board = new Board();
  }

  @Test
  public void emptyBoardHasNoWinner() throws Exception {
    assertFalse(board.hasWinner());
  }

  @Test
  public void simpleHorizontalWinningBoard() throws Exception {
    board.put(new Stone(0), 0, 0);
    board.put(new Stone(1), 0, 1);
    board.put(new Stone(2), 0, 2);
    board.put(new Stone(3), 0, 3);

    assertTrue(board.hasWinner());
  }

  @Test
  public void horizontalFlatWinningBoard() throws Exception {
    board.put(new Stone(8), 1, 0);
    board.put(new Stone(9), 1, 1);
    board.put(new Stone(10), 1, 2);
    board.put(new Stone(12), 1, 3);

    assertTrue(board.hasWinner());
  }

  @Test
  public void simpleVerticalWinningBoard() throws Exception {
    board.put(new Stone(0), 0, 0);
    board.put(new Stone(1), 1, 0);
    board.put(new Stone(2), 2, 0);
    board.put(new Stone(3), 3, 0);

    assertTrue(board.hasWinner());
  }

  @Test
  public void simpleLeftDiagonalWinningBoard() throws Exception {
    board.put(new Stone(0), 0, 0);
    board.put(new Stone(1), 1, 1);
    board.put(new Stone(2), 2, 2);
    board.put(new Stone(3), 3, 3);

    assertTrue(board.hasWinner());
  }

  @Test
  public void simpleRightDiagonalWinningBoard() throws Exception {
    board.put(new Stone(0), 0, 3);
    board.put(new Stone(1), 1, 2);
    board.put(new Stone(2), 2, 1);
    board.put(new Stone(3), 3, 0);

    assertTrue(board.hasWinner());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotPutSameSpotTwice() throws Exception {
    board.put(new Stone(0), 0, 0);
    board.put(new Stone(1), 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotPutUsedStone() throws Exception {
    board.put(new Stone(0), 0, 0);
    board.put(new Stone(0), 0, 1);
  }

  @Test
  public void isEmpty() throws Exception {
    assertTrue(board.isEmpty(0, 0));
    board.put(new Stone(0), 0, 0);
    assertFalse(board.isEmpty(0, 0));
  }

  @Test
  public void isUsed() throws Exception {
    assertFalse(board.isUsed(new Stone(0)));
    board.put(new Stone(0), 0, 0);
    assertTrue(board.isUsed(new Stone(0)));
  }
}
