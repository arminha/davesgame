package arminha.davesgame.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StoneTest {

    @Test
    public void isRound() throws Exception {
        assertFalse(new Stone(0).isRound());
        assertTrue(new Stone(1).isRound());
    }

    @Test
    public void isBig() throws Exception {
        assertFalse(new Stone(0).isBig());
        assertTrue(new Stone(2).isBig());
    }

    @Test
    public void isBlack() throws Exception {
        assertFalse(new Stone(0).isBlack());
        assertTrue(new Stone(4).isBlack());
    }

    @Test
    public void isFlat() throws Exception {
        assertFalse(new Stone(0).isFlat());
        assertTrue(new Stone(8).isFlat());
    }

}
