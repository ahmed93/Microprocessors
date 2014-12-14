package Abstracts;

import interfaces.Word;
import simulator.Block;
import simulator.Data;

public abstract class Cache {
	public Block[] instructions;
	public Block[] data;

	public int hits = 0;
	public int misses = 0;

	protected int blockSize;
	protected int cacheSize;
	protected int associativity;

	private boolean writeBack;
	private boolean writeAround;
	private boolean writeThrough;
	private boolean writeAllocate;

	int hitTime;
	int missTime;

	public int getHitTime() {
		return hitTime;
	}

	public void setHitTime(int hitTime) {
		this.hitTime = hitTime;
	}

	public int getMissTime() {
		return missTime;
	}

	public void setMissTime(int missTime) {
		this.missTime = missTime;
	}

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
		setWordAtAddress(instruction, Cache.INSTRUCTION);
		// int address = instruction.getAddress();
		// Instruction i = (Instruction) getWordAtAddress(address,
		// Cache.INSTRUCTION);
		// // Instruction i = searchInstruction(address);
		// i = InstructionFactory.create_instruction(instruction.instruction,
		// instruction.simulator);
		// i.address = instruction.address;
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
		if (destination == null)
			return null;
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

	public void printCache() {
		System.out.println("----Instructions------");
		for (int i = 0; i < instructions.length; i++) {
			for (int j = 0; j < instructions[i].words.length; j++) {
				Instruction instruction = ((Instruction) (instructions[i].words[j]));
				System.out.print("|" + instruction.instruction + "|");
			}
			System.out.println("");
		}
		System.out.println("----Data------");
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].words.length; j++) {
				Data datum = ((Data) (data[i].words[j]));
				System.out.print("|" + datum.get_value() + "|");
			}
			System.out.println("");
		}
		System.out.println("##################3");
	}

	public abstract void setWordAtAddress(Word word, String type);
	
	public double getMissRate(){
		return ((double) misses)/((double) misses+hits); 
	}
	public double getHitRate(){
		return 1 - this.getMissRate();
	}
}