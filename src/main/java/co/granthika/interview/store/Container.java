package co.granthika.interview.store;

public class Container {
	
	private String name;
	private int liquid;
	
	public Container(String name) {
		this.name = name;
	}

	public Container(String name, int fluid) {
		this.name = name;
		this.liquid = fluid;
	}

	public int getLiquid() {
		return liquid;
	}

	public void putLiquid(int liquid) {
		this.liquid = liquid;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Container(\""+name+"\", "+liquid+")";
	}
}
