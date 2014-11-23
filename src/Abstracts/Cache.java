package Abstracts;

import interfaces.Word;
import simulator.Block;
import simulator.Data;

public abstract class Cache {
	public Block[] instructions;
	public Block[] data;

	protected int hits = 0;
	protected int misses = 0;

	protected int blockSize;
	protected int cacheSize;
	protected int associativity;

	private boolean writeBack;
	private boolean writeAround;
	private boolean writeThrough;
	private boolean writeAllocate;

	public static final String INSTRUCTION = "instruction";
	public static final String DATA = "data";

	public abstract Data searchData(int address);

	public abstract Instruction searchInstruction(int address);

	protected abstract Word getWordAtAddress(int address, String type);

	public void setDataValue(Data dataWord, int value) {
		dataWord.set_value(value);
		if (writeBack && writeAllocate) {
			dataWord.setDirtyBit(true);
		}
	}

	public void cacheInstruction(Instruction instruction) {
		int address = instruction.getAddress();
		Instruction i = searchInstruction(address);
		i = instruction;
	}

	public void updateLower(Data dataWord) {
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
		Data replaced = null;
		Data destination = (Data) getWordAtAddress(data.getAddress(), DATA);
		if (destination.isDirtyBit()) {
			replaced = new Data(destination.get_value());
			replaced.setAddress(destination.getAddress());
		}
		destination.set_value(data.get_value());
		destination.setAddress(data.getAddress());
		destination.setDirtyBit(data.isDirtyBit());
		return replaced;
	}

	public abstract int startingAddress(int address);

	public abstract int endingAddress(int address);

}