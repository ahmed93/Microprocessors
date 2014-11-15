package simulator;

import interfaces.Word;
import Abstracts.Instruction;

public class Memory {

	final int SIZE = 32768;
	Word[] cells;
	private static Memory memory_instance;

	int percentage;
	int d; // Data Index
	int i; // Instruction Index
	final int DATA_STARTING_ADDRESS;

	private Memory() {
		this.cells = new Word[SIZE];
		this.d = percentage / 100 * SIZE;
		this.DATA_STARTING_ADDRESS = d;
		this.i = 0;
	}

	public Memory get_instance() {
		if (memory_instance == null)
			memory_instance = new Memory();
		return memory_instance;
	}

	public void storeDataAtAddress(int data, int address) {
		if (address >= DATA_STARTING_ADDRESS && address <= SIZE) {
			this.cells[address] = new Data(data);
		}
	}

	public void storeInstructionAtAddress(Instruction instruction, int address) {
		this.cells[address] = instruction;
	}

	public void storeData(int data) {
		if (d == SIZE)
			d = DATA_STARTING_ADDRESS;
		this.cells[d++] = new Data(data);
	}

	public void storeInstruction(Instruction instruction) {
		this.cells[i++] = instruction;
	}

	public Instruction getInstructionAt(int address) {
		if (address >= 0 && address < DATA_STARTING_ADDRESS) {
			return (Instruction) this.cells[address];
		}
		return null;
	}

	public int getDataAt(int address) {
		if (address >= DATA_STARTING_ADDRESS && address <= SIZE) {
			return ((Data)this.cells[address]).get_value();
		}
		return -1;
	}

	public void setInstructionIndex(int i) {
		this.i = i;
	}

	public void setDataIndex(int d) {
		this.d = d;
	}

	public int getInstructionIndex() {
		return this.i;
	}

}
