
package arminha.davesgame.domain.event;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class NextStoneChoosen
    implements Event
{

    private final UUID id;
    private final UUID playerId;
    private final int stone;

    @JsonCreator
    public NextStoneChoosen(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("playerId")
        UUID playerId,
        @JsonProperty("stone")
        int stone) {
        this.id = Objects.requireNonNull(id);
        this.playerId = Objects.requireNonNull(playerId);
        this.stone = Objects.requireNonNull(stone);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getStone() {
        return stone;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("playerId", playerId).add("stone", stone).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, stone);
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
        if (!(obj instanceof NextStoneChoosen)) {
            return false;
        }
        NextStoneChoosen other = ((NextStoneChoosen) obj);
        if (!id.equals(other.id)) {
            return false;
        }
        if (!playerId.equals(other.playerId)) {
            return false;
        }
        if (stone!= other.stone) {
            return false;
        }
        return true;
    }

}
