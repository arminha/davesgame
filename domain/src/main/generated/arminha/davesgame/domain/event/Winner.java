
package arminha.davesgame.domain.event;

import java.util.UUID;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Winner
    implements Event
{

    private final UUID id;
    private final UUID winnerId;

    @JsonCreator
    public Winner(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("winnerId")
        UUID winnerId) {
        this.id = Preconditions.checkNotNull(id);
        this.winnerId = Preconditions.checkNotNull(winnerId);
    }

    public UUID getId() {
        return id;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id).add("winnerId", winnerId).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, winnerId);
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
        if (!(obj instanceof Winner)) {
            return false;
        }
        Winner other = ((Winner) obj);
        if (!id.equals(other.id)) {
            return false;
        }
        if (!winnerId.equals(other.winnerId)) {
            return false;
        }
        return true;
    }

}
