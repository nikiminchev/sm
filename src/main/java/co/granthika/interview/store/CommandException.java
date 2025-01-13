package co.granthika.interview.store;

@SuppressWarnings("serial")
public class CommandException extends RuntimeException{
	StringBuilder buffer = new StringBuilder();

	public CommandException(AbstractCommand command, String message) {
		if(command instanceof MoveCommand) {
			buffer.append("Can't execute move command."+command.toString()+" Reason: ");
		}else if(command instanceof FillCommand) {
			buffer.append("Can't execute fill command."+command.toString()+" Reason: ");
		}
		buffer.append(message);
	}
	
	public String getMessage() {
		return buffer.toString();
	}
}
