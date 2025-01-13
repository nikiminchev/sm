package co.granthika.interview.store;

public abstract class AbstractCommand implements StoreAware{
	
	private Store store;
	public AbstractCommand(Store store) {
		this.store = store;
	}

	public Store getStore() {
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
