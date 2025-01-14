package co.granthika.interview.commands;

import co.granthika.interview.store.Store;
import co.granthika.interview.store.StoreAware;

public abstract class AbstractCommand implements StoreAware{
	
	private Store store;
	public AbstractCommand(Store store) {
		this.store = store;
	}

	public synchronized Store getStore() {
		return store;
	}
	
	public abstract void redo();
	
	public String print() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.toString());
		buffer.append("; Store: [");
		buffer.append(getStore().toString());
		buffer.append("]");
		return buffer.toString();
	}
}
