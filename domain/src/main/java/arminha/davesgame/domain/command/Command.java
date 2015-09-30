package arminha.davesgame.domain.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import arminha.davesgame.domain.Message;

/**
 * Marker interface for commands.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "command")
@JsonSubTypes(value = { @JsonSubTypes.Type(CreateGame.class), @JsonSubTypes.Type(ChooseNextStone.class),
        @JsonSubTypes.Type(PutStone.class) })
public interface Command extends Message {
}
