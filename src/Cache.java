public abstract class Cache {
	Block[] instructions;
	Block[] data;
	
	
	
	int blockSize;
	int cacheSize;
	int associativity;
	
	public static final boolean DATA = true;
	public static final boolean INSTRUCTION = false;
	
	public boolean writeBack;
	public boolean writeAround;
	public boolean writeThrough;
	public boolean writeAllocate;
	
	abstract int search(String item, boolean type);
	
}