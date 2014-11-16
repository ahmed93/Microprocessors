package Abstracts;
import simulator.Block;

public abstract class Cache {
	protected Block[] instructions;
	protected Block[] data;
	
	protected int hits = 0;
	protected  int misses = 0;
	
	
	protected int blockSize;
	protected int cacheSize;
	protected int associativity;
	
	public boolean writeBack;
	public boolean writeAround;
	public boolean writeThrough;
	public boolean writeAllocate;
	
	public abstract int searchData(int address);
	public abstract int searchInstruction(int address);
	
	public abstract boolean existsData(int address);
	public abstract boolean existsInstruction(int address);
	
}