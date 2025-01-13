package co.granthika.interview.store;

import java.util.Iterator;
import java.util.Map;

public class MoveCommand extends AbstractCommand{
	
	private String source;
	private String target;
	
	MoveCommand(Store store) {
		super(store);
	}

	public void executeCommand(String source, String target) {
		this.source = source;
		this.target = target;
		if(source.contentEquals(target)) {
			throw new IllegalArgumentException("source==target");
		}
		if(Store.DEBUG_MODE) {
			System.out.println(getStore());
		}
		int sourcePoolIndex = getStore().findContainerInRoom(source);
		getStore().setVisitedSource(sourcePoolIndex);
		int targetPoolIndex = getStore().findContainerInRoom(target);
		getStore().setVisitedTarget(targetPoolIndex);

		int freeIndex = getStore().findFreePool(0);
		if(sourcePoolIndex!=targetPoolIndex) {
			while(!getStore().isOnTop(source, sourcePoolIndex)) {
				executeCommand(sourcePoolIndex, freeIndex);
				freeIndex = getStore().findFreePool(freeIndex);
			}
			int freeIndex2 = getStore().findFreePool(0);
			while(!getStore().isOnTop(target, targetPoolIndex)) {
				executeCommand(targetPoolIndex, freeIndex2);
				freeIndex2 = getStore().findFreePool(freeIndex2);
			}
			executeCommand(sourcePoolIndex, targetPoolIndex);
		}else {
			if(sourcePoolIndex==targetPoolIndex) {
				if(getStore().isOnTopOn(source, target)) {
					return;
				}else {
					executeCommand(targetPoolIndex, freeIndex);
					getStore().removeVisitedTarget(targetPoolIndex);
					MoveCommand newCommand = new MoveCommand(getStore());
					newCommand.executeCommand(source, target);
				}
			}
		}

		if(Store.DEBUG_MODE) {
			targetPoolIndex = getStore().findContainerInRoom(target);
			System.out.println(getStore().getContainerRoom()[targetPoolIndex]);
		}

	}
	
	void executeCommand(int sourcePoolIndex, int targetPoolIndex) {
		while(!getStore().hasFreePlace(targetPoolIndex)) {
			int freeIndex = createFreePlace();
			executeCommand(targetPoolIndex, freeIndex);
			targetPoolIndex = freeIndex;
		}
		getStore().push( targetPoolIndex,getStore().pop(sourcePoolIndex));
	}
	
	int createFreePlace() {
		Map<Integer, Integer> freePlaces = getStore().getFreePlaces();
		int numFreeSpaces = 0;
		for(Integer places: freePlaces.values()) {
			numFreeSpaces += places;
		}
		
		if(numFreeSpaces<2) {
			System.err.println(getStore().toString());
			getStore().printVisited();
			printFreePlaces(freePlaces);
			throw new NoStoreSolutionException(Integer.toString(numFreeSpaces));
		}else {
			int i=-1;
			Iterator<Integer> iterPlaces = freePlaces.keySet().iterator();
			while (iterPlaces.hasNext()) {
				Integer sourceIndex = (Integer) iterPlaces.next();
				int places1 = freePlaces.get(sourceIndex);
				int poolElementsSize = getStore().getPoolSize()-places1;
				while (iterPlaces.hasNext()) {
					Integer targetIndex = (Integer) iterPlaces.next();
					int places2 = freePlaces.get(targetIndex);
					
					for(int j=0;j<poolElementsSize&&j<places2;j++) {
						i++;
						places1+=i;
						executeCommand(sourceIndex, targetIndex);
						if(places1>1) {
							return sourceIndex;
						}
					}
				}
				if(places1>1) {
					return sourceIndex;
				}
			}
			System.err.println(getStore().toString());
			getStore().printVisited();
			printFreePlaces(freePlaces);
			throw new NoStoreSolutionException(Integer.toString(i));
		}		
	}
	
	void printFreePlaces(Map<Integer, Integer> freePlaces) {
		for(int places: freePlaces.keySet()) {
			System.err.println("index: "+freePlaces.get(places)+", places: "+places);
		}
	}
	
	public void redo() {
		if(source==null || target==null) {
			throw new IllegalStateException("source: "+source+", target: "+target);
		}else {
			executeCommand(source, target);
		}
	}

	public String toString() {
		return "move: "+source+" to "+target;
	}

	String getSource() {
		return source;
	}

	String getTarget() {
		return target;
	}
}
