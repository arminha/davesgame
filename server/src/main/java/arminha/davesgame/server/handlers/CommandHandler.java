package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.command.Command;

public interface CommandHandler {

  Class<? extends Command> getCommandClass();

  void execute(Command command);

}
