
package arminha.davesgame.domain.command;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class CreateGame
    implements Command
{

    private final UUID id;
    private final String name;
    private final UUID firstPlayerId;
    private final UUID secondPlayerId;

    @JsonCreator
    public CreateGame(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("firstPlayerId")
        UUID firstPlayerId,
        @JsonProperty("secondPlayerId")
        UUID secondPlayerId) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.firstPlayerId = Objects.requireNonNull(firstPlayerId);
        this.secondPlayerId = Objects.requireNonNull(secondPlayerId);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getFirstPlayerId() {
        return firstPlayerId;
    }

    public UUID getSecondPlayerId() {
        return secondPlayerId;
    }

    @Override
    public java.lang.String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("firstPlayerId", firstPlayerId).add("secondPlayerId", secondPlayerId).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstPlayerId, secondPlayerId);
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
        if (!(obj instanceof CreateGame)) {
            return false;
        }
        CreateGame other = ((CreateGame) obj);
        if (!id.equals(other.id)) {
            return false;
        }
        if (!name.equals(other.name)) {
            return false;
        }
        if (!firstPlayerId.equals(other.firstPlayerId)) {
            return false;
        }
        if (!secondPlayerId.equals(other.secondPlayerId)) {
            return false;
        }
        return true;
    }

}
