package simulator;

import interfaces.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Abstracts.Instruction;

public class Memory {

	final int SIZE = 32768;
	Word[] cells;
	private static Memory memory_instance;
	private static ArrayList<Integer> memoryIndex;

	int percentage;
	int d; // Data Index
	int i; // Instruction Index
	public final int DATA_STARTING_ADDRESS;

	private Memory() {
		this.cells = new Word[SIZE];
		this.d = 100;
		this.DATA_STARTING_ADDRESS = d;
		this.i = 0;
		memoryIndex = new ArrayList<Integer>();
	}

	public static Memory getInstance() {
		if (memory_instance == null)
			memory_instance = new Memory();
		return memory_instance;
	}

	public void storeDataAtAddress(int data, int address) {
		address+= DATA_STARTING_ADDRESS;
		if (address >= DATA_STARTING_ADDRESS && address <= SIZE) {
//			System.out.println("Storing " + data + " At address " + address);
			if(!memoryIndex.contains(address)) memoryIndex.add(address);
			this.cells[address] = new Data(data);
		}
	}

	public void storeInstructionAtAddress(Instruction instruction, int address) {
		this.cells[address] = instruction;
	}

	public void storeData(int data) {
		if (d == SIZE)
			d = DATA_STARTING_ADDRESS;
		if(!memoryIndex.contains(d)) memoryIndex.add(d);
		this.cells[d++] = new Data(data);
	}

	public void storeInstruction(Instruction instruction) {
//		System.out.println("Adding instruction at address " + i);
		instruction.setAddress(i);
		this.cells[i++] = instruction;
	}

	public Instruction getInstructionAt(int address) {
//		System.out.println("Data Starting address" + DATA_STARTING_ADDRESS);
		if (address >= 0 && address < DATA_STARTING_ADDRESS) {
			return (Instruction) this.cells[address];
		}
		return null;
	}

	public Data getDataAt(int address) {
		address += DATA_STARTING_ADDRESS;
//		System.out.println("Getting data at " + address);
		if (address >= DATA_STARTING_ADDRESS && address <= SIZE) {
			if(this.cells[address]==null){
				cells[address] = new Data(0);
			}
			Data data = (Data)this.cells[address];
			data.setAddress(address - DATA_STARTING_ADDRESS);
				return data;
		}
		return null;
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
	
	public HashMap<Integer, Integer> getAllData() {
		HashMap<Integer, Integer> dataMem = new HashMap<Integer, Integer>();
		System.out.println(Arrays.toString(memoryIndex.toArray()));
		for (int data : memoryIndex)
			dataMem.put(data, getDataAt(data).get_value());
		return dataMem;
	}

}
