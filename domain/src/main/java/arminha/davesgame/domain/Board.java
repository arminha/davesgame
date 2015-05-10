package arminha.davesgame.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Game board with size 4x4.
 */
public class Board {

    private static final int ALL_FLAGS = 0xf;

    private static final int BOARD_SIZE = 4;

    private final Stone[][] stones;

    /**
     * Create an empty board.
     */
    public Board() {
        stones = new Stone[BOARD_SIZE][BOARD_SIZE];
    }

    /**
     * Put a {@link Stone} on the board at position (x, y).
     * 
     * @param stone
     *            the {@link Stone}
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     */
    public void put(Stone stone, int x, int y) {
        Preconditions.checkNotNull(stone);
        Preconditions.checkElementIndex(x, BOARD_SIZE);
        Preconditions.checkElementIndex(y, BOARD_SIZE);
        Preconditions.checkArgument(isEmpty(x, y));
        Preconditions.checkArgument(!isUsed(stone));
        stones[x][y] = stone;
    }

    /**
     * Check if position (x, y) on the board is empty. The coordinates x and y are 0-based.
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @return true if the position is empty
     */
    public boolean isEmpty(int x, int y) {
        return stones[x][y] == null;
    }

    public boolean isUsed(Stone stone) {
        Preconditions.checkNotNull(stone);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (stone.equals(stones[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasWinner() {
        return Iterables.any(getLines(), new Predicate<Iterable<Stone>>() {
            @Override
            public boolean apply(Iterable<Stone> line) {
                int positive = ALL_FLAGS;
                int negative = 0x0;
                for (Stone stone : line) {
                    positive = positive & (stone != null ? stone.getFlags() : 0);
                    negative = negative | (stone != null ? stone.getFlags() : ALL_FLAGS);
                }
                for (int f = 1; f < ALL_FLAGS; f = f << 1) {
                    if ((positive & f) != 0 || (negative & f) == 0) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private Iterable<Iterable<Stone>> getLines() {
        List<Iterable<Stone>> lines = new ArrayList<>();

        // horizontal lines
        for (int i = 0; i < BOARD_SIZE; i++) {
            lines.add(Arrays.asList(stones[i]));
        }

        // vertical lines
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<Stone> line = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                line.add(stones[j][i]);
            }
            lines.add(line);
        }

        // diagonals
        List<Stone> leftDiagonal = new ArrayList<Stone>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            leftDiagonal.add(stones[i][i]);
        }
        lines.add(leftDiagonal);
        List<Stone> rightDiagonal = new ArrayList<Stone>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            rightDiagonal.add(stones[i][BOARD_SIZE - i - 1]);
        }
        lines.add(rightDiagonal);
        return lines;
    }

}
