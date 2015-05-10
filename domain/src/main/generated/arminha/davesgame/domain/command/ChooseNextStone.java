
package arminha.davesgame.domain.command;

import java.util.UUID;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class ChooseNextStone
    implements Command
{

    private final UUID id;
    private final UUID playerId;
    private final int stone;

    @JsonCreator
    public ChooseNextStone(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("playerId")
        UUID playerId,
        @JsonProperty("stone")
        int stone) {
        this.id = Preconditions.checkNotNull(id);
        this.playerId = Preconditions.checkNotNull(playerId);
        this.stone = Preconditions.checkNotNull(stone);
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
        return Objects.toStringHelper(this).add("id", id).add("playerId", playerId).add("stone", stone).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, playerId, stone);
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
        if (!(obj instanceof ChooseNextStone)) {
            return false;
        }
        ChooseNextStone other = ((ChooseNextStone) obj);
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