package arminha.davesgame.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * A game stone.
 */
public class Stone {

  private static final int NUMBER_OF_STONES = 16;
  private static final int ROUND = 0x1;
  private static final int BIG = 0x2;
  private static final int BLACK = 0x4;
  private static final int FLAT = 0x8;

  private final int flags;

  public Stone(int flags) {
    Preconditions.checkArgument(flags >= 0);
    Preconditions.checkArgument(flags < NUMBER_OF_STONES);
    this.flags = flags;
  }

  public boolean isRound() {
    return (flags & ROUND) != 0;
  }

  public boolean isBig() {
    return (flags & BIG) != 0;
  }

  public boolean isBlack() {
    return (flags & BLACK) != 0;
  }

  public boolean isFlat() {
    return (flags & FLAT) != 0;
  }

  public int getFlags() {
    return flags;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("flags", flags).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(flags);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Stone other = (Stone) obj;
    if (flags != other.flags) {
      return false;
    }
    return true;
  }

}
