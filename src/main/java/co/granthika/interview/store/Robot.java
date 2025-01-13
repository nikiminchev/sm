package co.granthika.interview.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Hello world!
 *
 */
public class Robot 
{
    private Store store;
    
	private Stack<AbstractCommand> commands = new Stack<AbstractCommand>();
    
    public Robot(int numContainers, int poolSize) {
    	initStore(numContainers, poolSize);
    	addContainers(numContainers);
    }
    
    private void initStore(int numContainers, int poolSize) {
    	int places = numContainers/poolSize;
    	while(places*poolSize<=(numContainers+poolSize)) {
    		places++;
    	}
    	
    	store = new Store(places, poolSize);
    }
    
    private void addContainers(int numContainers) {
    	List<Container> containers = new ArrayList<Container>();
    	for(int i=0;i<numContainers; i++) {
    		String name = "b"+i;
    		Container container = new Container(name);
    		containers.add(container);
    	}
    	
    	for(Container container: containers) {
    		store.addContainer(container);
    	}
    }
    
    
    public Robot(ContainersPool[] containerRoom, int poolSize) {
    	init(containerRoom, poolSize);
    }
    private void init(ContainersPool[] containerRoom, int poolSize) {
    	store = new TestStore(containerRoom, poolSize);
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
}
