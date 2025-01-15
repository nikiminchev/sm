package co.granthika.interview.commands;

import java.util.Iterator;
import java.util.Map;

import co.granthika.interview.store.NoStoreSolutionException;
import co.granthika.interview.store.Store;

public class MoveCommand extends AbstractCommand{
	
	private String source;
	private String target;
	
	public MoveCommand(Store store) {
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
		Store store = getStore();
		synchronized (store) {
			
			int sourcePoolIndex = store.findContainerInRoom(source);
			if(sourcePoolIndex<0) {
				throw new IllegalArgumentException(source+" not found");
			}
			store.setVisitedSource(sourcePoolIndex);
			int targetPoolIndex = store.findContainerInRoom(target);
			if(targetPoolIndex<0) {
				throw new IllegalArgumentException(target+" not found");
			}
			store.setVisitedTarget(targetPoolIndex);

			if(sourcePoolIndex!=targetPoolIndex && !store.getContainerRoom()[sourcePoolIndex].isEmpty()) {
				int freeIndex = store.findFreePool(0);
				while(!store.isOnTop(source, sourcePoolIndex)) {
					executeCommand(sourcePoolIndex, freeIndex);
					freeIndex = store.findFreePool(freeIndex);
				}
				int freeIndex2 = store.findFreePool(0);
				while(!store.isOnTop(target, targetPoolIndex)) {
					executeCommand(targetPoolIndex, freeIndex2);
					freeIndex2 = store.findFreePool(freeIndex2);
				}
				executeCommand(sourcePoolIndex, targetPoolIndex);
			}else {
				if(sourcePoolIndex==targetPoolIndex) {
					int freeIndex = store.findFreePool(0);
					if(store.isOnTopOn(source, target)) {
						return;
					}else {
						executeCommand(targetPoolIndex, freeIndex);
						store.removeVisitedTarget(targetPoolIndex);
						MoveCommand newCommand = new MoveCommand(store);
						newCommand.executeCommand(source, target);
					}
				} else {
					executeCommand(sourcePoolIndex, targetPoolIndex);
				}
			}
			if(Store.DEBUG_MODE) {
				targetPoolIndex = store.findContainerInRoom(target);
				System.out.println(store.getContainerRoom()[targetPoolIndex]);
			}
		}
	}
	
	void executeCommand(int sourcePoolIndex, int targetPoolIndex) {
		Store store = getStore();
		synchronized (store) {
			while(!store.hasFreePlace(targetPoolIndex)) {
				int freeIndex = createFreePlace(store);
				executeCommand(targetPoolIndex, freeIndex);
				targetPoolIndex = freeIndex;
			}
			store.push( targetPoolIndex,getStore().pop(sourcePoolIndex));
		}
	}
	
	int createFreePlace(Store store) {
		Map<Integer, Integer> freePlaces = store.getFreePlaces();
		int numFreeSpaces = 0;
		for(Integer places: freePlaces.values()) {
			numFreeSpaces += places;
		}
		
		if(numFreeSpaces<2) {
			if(Store.DEBUG_MODE) {
				System.err.println(getStore().toString());
			}
			store.printVisited();
			printFreePlaces(freePlaces);
			throw new NoStoreSolutionException(Integer.toString(numFreeSpaces));
		}else {
			int i=-1;
			Iterator<Integer> iterPlaces = freePlaces.keySet().iterator();
			while (iterPlaces.hasNext()) {
				Integer sourceIndex = (Integer) iterPlaces.next();
				int places1 = freePlaces.get(sourceIndex);
				int poolElementsSize = store.getPoolSize()-places1;
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
			if(Store.DEBUG_MODE) {
				System.err.println(getStore().toString());
				store.printVisited();
				printFreePlaces(freePlaces);
			}
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
