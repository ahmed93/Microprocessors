package Abstracts;
import simulator.Block;

public abstract class Cache {
	Block[] instructions;
	Block[] data;
	
	
	
	int blockSize;
	int cacheSize;
	int associativity;
	
	public boolean writeBack;
	public boolean writeAround;
	public boolean writeThrough;
	public boolean writeAllocate;
	
	public abstract int searchData(int address);
	public abstract int searchInstruction(int address);
	
}