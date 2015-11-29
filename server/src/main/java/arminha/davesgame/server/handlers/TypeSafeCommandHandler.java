package arminha.davesgame.server.handlers;

import arminha.davesgame.domain.command.Command;

import java.security.Principal;

public abstract class TypeSafeCommandHandler<T extends Command> implements CommandHandler {

  private final Class<T> commandClass;
  private final GameRepository repository;

  public TypeSafeCommandHandler(Class<T> commandClass, GameRepository repository) {
    this.commandClass = commandClass;
    this.repository = repository;
  }

  @Override
  public Class<? extends Command> getCommandClass() {
    return commandClass;
  }

  @Override
  public void execute(Command command, Principal principal) {
    executeTypeSafe(commandClass.cast(command), principal);
  }

  protected abstract void executeTypeSafe(T command, Principal principal);

  protected GameRepository getRepository() {
    return repository;
  }

}
