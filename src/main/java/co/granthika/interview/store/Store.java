package co.granthika.interview.store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Store {
	public static boolean DEBUG_MODE; 
	private ContainersPool[] containerRoom;
	private int poolSize;
	private Set<Integer> visitedSource = new HashSet<Integer>();
	private Set<Integer> visitedTarget = new HashSet<Integer>();
	
	protected Store(ContainersPool[] containerRoom) {
		setContainerRoom(containerRoom);
		this.poolSize = getContainerRoom()[0].getPoolSize();
	}

	public Store(int numContainers, int poolSize) {
		int places = initStore(numContainers, poolSize);
		if(places<=0) {
			throw new ArrayIndexOutOfBoundsException(places);
		}
		this.poolSize = poolSize;
		initContainerRoom(places);
		addContainers(numContainers);
	}
	
    private int initStore(int numContainers, int poolSize) {
    	int places = numContainers/poolSize;
    	while(places*poolSize<=(numContainers+poolSize)) {
    		places++;
    	}
    	//requires one more container which is empty at the start
    	return ++places;
    }
    
	synchronized private void initContainerRoom(int places) {
		containerRoom = new ContainersPool[places];
		for(int i=0;i<containerRoom.length;i++) {
			containerRoom[i] = new ContainersPool(poolSize);
		}
	}
	
    private void addContainers(int numContainers) {
    	List<Container> containers = new ArrayList<Container>();
    	for(int i=0;i<numContainers; i++) {
    		String name = "b"+i;
    		Container container = new Container(name);
    		containers.add(container);
    	}
    	
    	for(Container container: containers) {
    		addContainer(container);
    	}
    }


	public synchronized ContainersPool[] getContainerRoom() {
		synchronized (this) {
			return containerRoom;
		}
	}
	
	synchronized public void addContainer(Container container) {
		synchronized (this) {
			int index = findFreePool(0);
			if(DEBUG_MODE) {
				System.out.println("index="+index);
			}
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				if(!pool.isFull()) {
					pool.add(container);
				} else {
					throw new IllegalArgumentException("Pool is full!");
				}
			}
		}
	}

	public int findContainerInRoom(String name){
		synchronized (this) {
			int indexInRoom = -1;
			for(ContainersPool pool: containerRoom) {
				synchronized (pool) {
					if(Store.DEBUG_MODE) {
						System.err.println(pool.toString());
					}
					indexInRoom++;
					if(pool.hasContainer(name)) {
						return indexInRoom;
					}
				}
			}
			return -1;
		}
	}
	
	public boolean isOnTop(String name, int index) {
		synchronized (this) {
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				return pool.isOnTop(name) && pool.size()<=poolSize;
			}
		}
	}
	
	public boolean isOnTopOn(String source, String target) {
		synchronized (this) {
			int sourcePoolIndex = findContainerInRoom(source);
			int targetPoolIndex = findContainerInRoom(target);
			if(sourcePoolIndex!=targetPoolIndex) {
				return false;
			}else {
				boolean found = false; 
				ContainersPool pool = getContainerRoom()[sourcePoolIndex];
				synchronized (pool) {
					ContainersPool tempPool = new ContainersPool(poolSize);
					if(Store.DEBUG_MODE) {
						System.err.println("pool.pop.size()="+pool.size());
						System.err.println("tempPool.size()="+tempPool.size());
					}
					if(pool.size()>getPoolSize()) {
						System.err.println(pool.toString());
					}
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
//						System.err.println("tempPool.add.size()="+tempPool.size());
					}

					
					if(Store.DEBUG_MODE) {
						System.err.println("pool.size()="+pool.size());
					}
					while(!tempPool.isEmpty()) {
						pool.push(tempPool.pop());
					}
					return found;
				}
			}
		}
	}

	boolean isEmpty(int index) {
		synchronized (this) {
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				return pool.isEmpty();
			}
		}
	}
	
	public Container top(int index) {
		synchronized (this) {
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				if(!pool.isEmpty()) {
					return pool.peek();
				}
			}
			throw new IllegalArgumentException(pool.toString());
		}
	}
	
	public boolean hasFreePlace(int poolIndex) {
		synchronized (this) {
			ContainersPool pool = getContainerRoom()[poolIndex];
			synchronized (pool) {
				return !pool.isFull();
			}
		}
	}
	
	public Container pop(int index) {
		synchronized (this) {
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				return pool.pop();
			}
		}
	}
	
	public void push(int index, Container container) {
		synchronized (this) {
			if(index<0 || index>getContainerRoom().length-1) {
				throw new ArrayIndexOutOfBoundsException(index);
			}
			ContainersPool pool = getContainerRoom()[index];
			synchronized (pool) {
				pool.push(container);
			}
		}
	}
	
	public int findFreePool(int start) {
		synchronized (this) {
			start = start%getContainerRoom().length;
			for(int index=start; index<getContainerRoom().length; index++) {
				ContainersPool pool = getContainerRoom()[index];
				if(Store.DEBUG_MODE) {
					System.err.println("index="+index);
					System.err.println("isVisited(index)="+isVisited(index));
					System.err.println("pool.isFull()="+pool.isFull());
				}
				synchronized (pool) {
					if(!isVisited(index) && !pool.isFull()) {
						return index;
					}
				}
			}
			return -1;
		}
	}
	private Map<Integer, Integer> freePlacesMap = new TreeMap<Integer, Integer>();
	
	public Map<Integer, Integer> getFreePlaces() {
		synchronized (this) {
			freePlacesMap.clear();
			int len = getContainerRoom().length;
			for(int index = 0; index<len; index++) {
				if(isVisited(index)) {
					continue;
				}
				ContainersPool pool = getContainerRoom()[index];
				synchronized (pool) {
					int freePlaces = poolSize-pool.size();
					if(freePlaces>0) {
						freePlacesMap.put(index, freePlaces);
					}
				}
			}
			return freePlacesMap;
		}
	}

	public String toString() {
		synchronized (this) {
			StringBuilder buffer = new StringBuilder();
			String delim = "";
			String delimPool = "";
			StringBuilder poolBuilder = new StringBuilder();
			for(int i=0; i<getContainerRoom().length; i++) {
				ContainersPool pool = getContainerRoom()[i];
				synchronized (pool) {
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
			}
			return buffer.toString();
		}
	}

	private void setContainerRoom(ContainersPool[] containerRoom) {
		this.containerRoom = containerRoom;
	}
	
	boolean isVisited(int i){
		synchronized (this) {
			return visitedSource.contains(i)||visitedTarget.contains(i);
		}
	}
	
	public void setVisitedSource(int i) {
		synchronized (this) {
			visitedSource.add(i);
		}
	}
	
	public void setVisitedTarget(int i) {
		synchronized (this) {
			visitedTarget.add(i);
		}
	}
	
	public void removeVisitedTarget(int i) {
		synchronized (this) {
			visitedTarget.remove(i);
		}
	}

	public void clearVisited() {
		synchronized (this) {
			visitedSource.clear();
			visitedTarget.clear();
		}
	}

	public int getPoolSize() {
		return poolSize;
	}
	
	public void printVisited() {
		synchronized (this) {
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
}
