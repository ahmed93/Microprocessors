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
	
	private boolean writeBack;
	private boolean writeAround;
	private boolean writeThrough;
	private boolean writeAllocate;
	
	
	public abstract Data searchData(int address);
	public abstract Instruction searchInstruction(int address);
	
	public abstract Data insertData(int address);
	public abstract Instruction insertInstruction(int address);
	public boolean isWriteBack() {
		return writeBack;
	}
	public void setWriteBack(boolean writeBack) {
		this.writeBack = writeBack;
	}
	public boolean isWriteAround() {
		return writeAround;
	}
	public void setWriteAround(boolean writeAround) {
		this.writeAround = writeAround;
	}
	public boolean isWriteThrough() {
		return writeThrough;
	}
	public void setWriteThrough(boolean writeThrough) {
		this.writeThrough = writeThrough;
	}
	public boolean isWriteAllocate() {
		return writeAllocate;
	}
	public void setWriteAllocate(boolean writeAllocate) {
		this.writeAllocate = writeAllocate;
	}
	
}