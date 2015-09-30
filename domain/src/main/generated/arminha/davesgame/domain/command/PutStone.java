
package arminha.davesgame.domain.command;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class PutStone
    implements Command
{

    private final UUID id;
    private final UUID playerId;
    private final int x;
    private final int y;

    @JsonCreator
    public PutStone(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("playerId")
        UUID playerId,
        @JsonProperty("x")
        int x,
        @JsonProperty("y")
        int y) {
        this.id = Objects.requireNonNull(id);
        this.playerId = Objects.requireNonNull(playerId);
        this.x = Objects.requireNonNull(x);
        this.y = Objects.requireNonNull(y);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("playerId", playerId).add("x", x).add("y", y).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, x, y);
    }

    @Override
    public boolean equals(
        @Nullable
        Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PutStone)) {
            return false;
        }
        PutStone other = ((PutStone) obj);
        if (!id.equals(other.id)) {
            return false;
        }
        if (!playerId.equals(other.playerId)) {
            return false;
        }
        if (x!= other.x) {
            return false;
        }
        if (y!= other.y) {
            return false;
        }
        return true;
    }

}
