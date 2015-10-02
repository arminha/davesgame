package arminha.davesgame.domain.command;

import arminha.davesgame.domain.Message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Marker interface for commands.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "command")
@JsonSubTypes(value = { @JsonSubTypes.Type(CreateGame.class),
    @JsonSubTypes.Type(ChooseNextStone.class), @JsonSubTypes.Type(PutStone.class) })
public interface Command extends Message {
}
