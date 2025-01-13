package co.granthika.interview.store;

public class FillCommand  extends AbstractCommand{
	
	private String name;
	private int fluid;

	FillCommand(Store store) {
		super(store);
	}

	public void executeCommand(String name, int fluid) {
		this.name = name;
		this.fluid = fluid;
		int poolIndex = getStore().findContainerInRoom(name);
		if(poolIndex>=0) {
			if(getStore().isOnTop(name, poolIndex)) {
				getStore().top(poolIndex).putLiquid(fluid);
			}else {
				MoveCommand command = new MoveCommand(getStore());
				int freeIndex = getStore().findFreePool(0);
				command.executeCommand(poolIndex, freeIndex);
				getStore().top(poolIndex).putLiquid(fluid);
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
