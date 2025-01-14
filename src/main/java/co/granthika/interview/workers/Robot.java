package co.granthika.interview.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import co.granthika.interview.commands.AbstractCommand;
import co.granthika.interview.commands.FillCommand;
import co.granthika.interview.commands.MoveCommand;
import co.granthika.interview.store.Container;
import co.granthika.interview.store.ContainersPool;
import co.granthika.interview.store.Store;
import co.granthika.interview.store.TestStore;

/**
 * Hello world!
 *
 */
public class Robot 
{
    private Store store;
    
	private Stack<AbstractCommand> commands = new Stack<AbstractCommand>();
    
    public Robot(Store store) {
    	this.store = store;
    }
    
    public Robot(ContainersPool[] containerRoom) {
    	init(containerRoom);
    }
    private void init(ContainersPool[] containerRoom) {
    	store = new TestStore(containerRoom);
    }

	public AbstractCommand fill(String name, int fluid) {
		FillCommand command = new FillCommand(store);
//		System.out.println("fill: "+name+" with "+fluid+"l.");
		command.getStore().clearVisited();
		command.executeCommand(name, fluid);
		commands.push(command);
		return command;
	}
	
	public AbstractCommand move(String source, String target) {
		MoveCommand command = new MoveCommand(store);
//		System.out.println("move: "+source+" to "+target);
		command.getStore().clearVisited();
		command.executeCommand(source, target);
		commands.push(command);
		return command;
	}
	
	public AbstractCommand undo() {
		if(commands.isEmpty()) {
			throw new IllegalArgumentException("No commands to undo");
		}
		AbstractCommand command = commands.pop();
		store = command.getStore();
		return command;
	}

	public AbstractCommand redo() {
		if(commands.isEmpty()) {
			throw new IllegalArgumentException("No commands to redo");
		}
		AbstractCommand command = commands.pop();
//		System.out.println(command.toString());
		command.redo();
		return command;
	}
	
	@Override
	public String toString() {
		return store.toString();
	}

	public Store getStore() {
		return store;
	}
}
