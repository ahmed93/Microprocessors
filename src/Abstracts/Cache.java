package Abstracts;
import simulator.Block;
import simulator.Data;

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
	
	
	public abstract Data searchData(int address);
	public abstract Instruction searchInstruction(int address);
	
}