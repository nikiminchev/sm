package co.granthika.interview.commands;

import co.granthika.interview.store.Store;

public class FillCommand  extends AbstractCommand{
	
	private String name;
	private int fluid;

	public FillCommand(Store store) {
		super(store);
	}

	public void executeCommand(String name, int fluid) {
		this.name = name;
		this.fluid = fluid;
		Store store = getStore();
		synchronized (store) {
			int poolIndex = store.findContainerInRoom(name);
			if(poolIndex>=0) {
				if(store.isOnTop(name, poolIndex)) {
					store.top(poolIndex).putLiquid(fluid);
				}else {
					MoveCommand command = new MoveCommand(store);
					int freeIndex = store.findFreePool(0);
					command.executeCommand(poolIndex, freeIndex);
					store.top(poolIndex).putLiquid(fluid);
				}
			}
		}
	}
	
	public void redo() {
		if(name==null) {
			throw new IllegalStateException(name);
		}else {
			executeCommand(name, fluid);
		}
	}
	
	public String toString() {
		return "fill: "+name+" with "+fluid+"l.";
	}

	String getName() {
		return name;
	}

	int getFluid() {
		return fluid;
	}
}
