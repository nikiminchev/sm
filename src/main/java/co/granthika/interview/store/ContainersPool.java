package co.granthika.interview.store;

import java.util.Stack;

@SuppressWarnings("serial")
public class ContainersPool extends Stack<Container> {

	private int poolSize=-1;
	
	public ContainersPool(int poolSize) {
		this.poolSize = poolSize;
	}
	
	synchronized boolean hasContainer(String name) {
		boolean hasContainer = false; 
		synchronized (this) {
			for(Container cont: this) {
				if(Store.DEBUG_MODE) {
					System.err.println("cont.getName()="+cont.getName());
				}
				if(cont.getName().contentEquals(name)) {
					hasContainer = true;
					break;
				}
			}
			return hasContainer;
		}
	}
	synchronized boolean isOnTop(String name) {
		synchronized (this) {
			if(!isEmpty()) {
				Container top = peek();
				if(top.getName().contentEquals(name)) {
					return true;
				}else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	@Override
	public Container push(Container item) {
		synchronized (this) {
			if(size()>=poolSize) {
				throw new IllegalArgumentException(this.toString());
			}
			return super.push(item);
		}
	}

	@Override
	public boolean add(Container item) {
		synchronized (this) {
			if(size()>=poolSize) {
				throw new IllegalArgumentException(this.toString());
			}
			return super.add(item);
		}
	}

	public boolean isFull() {
		synchronized (this) {
			return size()>=poolSize;
		}
	}
	public int getPoolSize() {
		return poolSize;
	}
	
	@Override
	public boolean isEmpty() {
		synchronized (this) {
			return super.isEmpty();
		}
	}
}
