package co.granthika.interview.store;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Store {
	public static boolean DEBUG_MODE; 
	private ContainersPool[] containerRoom;
	private int poolSize;
	private Set<Integer> visitedSource = new HashSet<Integer>();
	private Set<Integer> visitedTarget = new HashSet<Integer>();
	
	protected Store(ContainersPool[] containerRoom, int poolSize) {
		setContainerRoom(containerRoom);
		this.poolSize = poolSize;
	}

	public Store(int places, int poolSize) {
		if(places<=0) {
			throw new ArrayIndexOutOfBoundsException(places);
		}
		this.poolSize = poolSize;
		initContainerRoom(places);
	}
	
	private void initContainerRoom(int places) {
		containerRoom = new ContainersPool[places];
		for(int i=0;i<containerRoom.length;i++) {
			containerRoom[i] = new ContainersPool(poolSize);
		}
	}

	ContainersPool[] getContainerRoom() {
		return containerRoom;
	}
	
	public void addContainer(Container container) {
		int index = findFreePool(0);
		getContainerRoom()[index].add(container);
	}

	int findContainerInRoom(String name){
		int indexInRoom = -1;
		for(ContainersPool pool: containerRoom) {
			indexInRoom++;
			if(pool.hasContainer(name)) {
				return indexInRoom;
			}
		}
		return -1;
	}
	
	boolean isOnTop(String name, int index) {
		return getContainerRoom()[index].isOnTop(name) && getContainerRoom()[index].size()<=poolSize;
	}
	
	boolean isOnTopOn(String source, String target) {
		int sourcePoolIndex = findContainerInRoom(source);
		int targetPoolIndex = findContainerInRoom(target);
		if(sourcePoolIndex!=targetPoolIndex) {
			return false;
		}else {
			boolean found = false; 
			ContainersPool pool = getContainerRoom()[sourcePoolIndex];
			ContainersPool tempPool = new ContainersPool(poolSize);
			while(!pool.isEmpty()) {
				Container c = pool.pop();
				if(c.getName().contentEquals(source)) {
					if(!pool.isEmpty()) {
						Container c1 = pool.peek();
						if(c1.getName().contentEquals(target)) {
							found = true;
						}
					}
				}
				tempPool.add(c);
			}
			
			while(!tempPool.isEmpty()) {
				pool.push(tempPool.pop());
			}
			return found;
		}
	}
	
	boolean isEmpty(int index) {
		return getContainerRoom()[index].isEmpty();
	}
	
	Container top(int index) {
		return getContainerRoom()[index].peek();
	}
	
	boolean hasFreePlace(int poolIndex) {
		return getContainerRoom()[poolIndex].size()<=(poolSize-1);
	}
	
	Container pop(int index) {
		return getContainerRoom()[index].pop();
	}
	
	void push(int index, Container container) {
		if(index<0 || index>getContainerRoom().length-1) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		getContainerRoom()[index].push(container);
	}
	
	int findFreePool(int start) {
		start = start%getContainerRoom().length;
		for(int size=0; size<poolSize; size++) {
			for(int index=start; index<getContainerRoom().length*2;index++) {
				int i = index%getContainerRoom().length;
				ContainersPool pool = getContainerRoom()[i];
				if(!isVisited(i) && pool.size()==size) {
					return i;
				}
			}
		}
		return -1;
	}
	private Map<Integer, Integer> freePlacesMap = new TreeMap<Integer, Integer>();
	
	Map<Integer, Integer> getFreePlaces() {
		freePlacesMap.clear();
		int len = getContainerRoom().length;
		for(int index = 0; index<len; index++) {
			if(isVisited(index)) {
				continue;
			}
			ContainersPool pool = getContainerRoom()[index];
			int freePlaces = poolSize-pool.size();
			if(freePlaces>0) {
				freePlacesMap.put(index, freePlaces);
			}
		}
		return freePlacesMap;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		String delim = "";
		String delimPool = "";
		StringBuilder poolBuilder = new StringBuilder();
		for(int i=0; i<getContainerRoom().length; i++) {
			ContainersPool pool = getContainerRoom()[i];
			buffer.append(delimPool);
			
			poolBuilder.setLength(0);
			poolBuilder.append("pool[");
			poolBuilder.append(i);
			poolBuilder.append("]");
			String poolString = poolBuilder.toString();
			buffer.append(poolString);
			delim="";
			for(Container container: pool) {
				buffer.append(delim);
				buffer.append(container.toString());
				delim = ";"+poolString;
			}
			delimPool = ";\n";
		}
		return buffer.toString();
	}

	private void setContainerRoom(ContainersPool[] containerRoom) {
		this.containerRoom = containerRoom;
	}
	
	boolean isVisited(int i){
		return visitedSource.contains(i)||visitedTarget.contains(i);
	}
	
	void setVisitedSource(int i) {
		visitedSource.add(i);
	}
	
	void setVisitedTarget(int i) {
		visitedTarget.add(i);
	}
	
	void removeVisitedTarget(int i) {
		visitedTarget.remove(i);
	}

	void clearVisited() {
		visitedSource.clear();
		visitedTarget.clear();
	}

	int getPoolSize() {
		return poolSize;
	}
	
	void printVisited() {
		System.err.println("visitedSource:");
		for(Integer i: visitedSource) {
			System.err.println(i);
		}
		System.err.println("visitedTarget:");
		for(Integer i: visitedTarget) {
			System.err.println(i);
		}
	}
}
