package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.command.Command;

import java.security.Principal;

public interface CommandHandler {

  Class<? extends Command> getCommandClass();

  void execute(Command command, Principal principal);

}
