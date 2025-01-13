package co.granthika.interview.store;

import java.util.Stack;

@SuppressWarnings("serial")
public class ContainersPool extends Stack<Container> {

	private int poolSize=2;
	
	public ContainersPool(int poolSize) {
		this.poolSize = poolSize;
	}
	
	boolean hasContainer(String name) {
		boolean hasContainer = false; 
		for(Container cont: this) {
			if(cont.getName().contentEquals(name)) {
				hasContainer = true;
				break;
			}
		}
		return hasContainer;
	}
	boolean isOnTop(String name) {
		Container top = peek();
		if(top.getName().contentEquals(name)) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public Container push(Container item) {
		if(size()>=poolSize) {
			throw new IllegalArgumentException();
		}
		return super.push(item);
	}
}
