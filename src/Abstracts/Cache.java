package Abstracts;
import interfaces.Word;
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
	
	protected static final String INSTRUCTION = "instruction";
	protected static final String DATA = "data";
	
	
	
	public abstract Data searchData(int address);
	public abstract Instruction searchInstruction(int address);
	protected abstract Word getWordAtAddress(int address, String type);

	public void setDataValue(Data dataWord, int value) {
		dataWord.set_value(value);
		if(writeBack && writeAllocate)
		{
			dataWord.setDirtyBit(true);
		}
	}

	public void insertInstruction(Instruction instruction, int address) {
		Instruction  i = searchInstruction(address);
		i = instruction;
	}
	

	public void updatelower(Data dataWord) {
		int address = dataWord.getAddress();
		Data d = searchData(address);
		d = dataWord;
		dataWord.setDirtyBit(false);
	}

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
	
	public Data insertData(Data data) {
		Data destination = (Data) getWordAtAddress(data.getAddress(), DATA);
		if (destination.isDirtyBit())
			return destination;
		else {
			destination.set_value(data.get_value());
			destination.setDirtyBit(data.isDirtyBit());
			return null;
		}
	}
	
}