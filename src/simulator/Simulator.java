package simulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import Abstracts.Cache;
import Abstracts.Instruction;
import factories.InstructionFactory;

public class Simulator {

	public Cache[] caches;
	HashMap<String, Register> registers;
	String inputFile = "input.txt";
	static final int REGISTERS_NUMBER = 8;
	private Memory memory;
	public static int pc;

	int instruction_starting_address;
	int instructions_ending_address;
	Vector<String> data;
	Vector<String> instructions;

	Vector<Integer> instructions_addresses;

	public Simulator(Vector<String> data, Vector<String> instructions,
			Cache[] caches, int instruction_starting_address) {
		this.memory = Memory.getInstance();
		this.instruction_starting_address = instruction_starting_address;
		this.instructions = instructions;
		this.data = data;
		this.instructions_addresses = new Vector<Integer>();
		this.caches = caches;
	}

	public void Initialize() throws IOException {
		// Initialize Cache
		// Initialize registers
		this.initializeRegisters();
		// Prompt user to enter instructions, and starting address
		// Use Instruction factory to create instructions
		memory.setInstructionIndex(instruction_starting_address);
		this.storeUserInstructions();
		this.storeUserData();
		// Use Instruction Cache to create Cache
		// Start instruction execution
	}

	public void storeUserInstructions() {
		for (String instruction : this.instructions) {
			Instruction new_instruction = InstructionFactory
					.create_instruction(instruction, this);
			memory.storeInstruction(new_instruction);
			this.instructions_addresses.add(new_instruction.getAddress());
		}
		this.instructions_ending_address = memory.getInstructionIndex() - 1;
	}

	public void storeUserData() {
		for (String data_address : this.data) {
			String[] data_address_array = data_address.split(" ");
			int data = Integer.parseInt(data_address_array[0]);
			int address = Integer.parseInt(data_address_array[1]);
			memory.storeDataAtAddress(data, address);
		}
	}

	public void runInstructions() {
		for (int i = 0; i < this.instructions_addresses.size(); i++) {
			Instruction instruction = null;
			for (int j = 0; j < this.caches.length; j++) {
				instruction = caches[j]
						.searchInstruction(this.instructions_addresses.get(i));
				if (instruction != null) {
					// place instruction in higher cache levels(j)
					break;
				}
			}
			if (instruction == null) {
				// miss
				instruction = this.memory
						.getInstructionAt(this.instructions_addresses.get(i));
				// place instruction in higher levels of cache.(number of
				// caches)
			}
			instruction.execute();
		}
		// for (int i = instruction_starting_address; i<=
		// instructions_ending_address; i++){
		// System.out.println("Getting instruction at " + i + " : " +
		// memory.getInstructionAt(i));
		// memory.getInstructionAt(i).execute();
		// }
	}

	public void initializeRegisters() {
		registers = new HashMap<>();
		for (int i = 0; i <= REGISTERS_NUMBER; i++) {
			Register Ri = (i == 0) ? new Register(0, true) : new Register(0,
					false);
			registers.put("R" + i, Ri);
		}
	}

	public Memory getMemory() {
		return memory;
	}

	public Register getRegister(String key) {
		if (this.registers.containsKey(key))
			return this.registers.get(key);
		else {
			System.err.println("Error: Register " + key + " not found");
			return null;
		}
	}

	public void printRegisters() {
		for (Entry<String, Register> entry : registers.entrySet()) {
			System.out.println(entry.getKey() + ": "
					+ entry.getValue().get_value());
		}
	}

	public void printMemroy() {
		for (int i = 0; i < 10; i++) {
			System.out.println(i + " : " + this.memory.getInstructionAt(i));
		}
		System.out.println("#################");
		for (int i = 0; i <= 10; i++) {
			System.out.println(this.memory.DATA_STARTING_ADDRESS + i + " : "
					+ this.memory.getDataAt(i));
		}
	}

	public Data getCachedOrMemoryData(int address) {
		Data data = null;
		// Search Caches for data
		for (int i = 0; i < caches.length; i++) {
			data = caches[i].searchData(address);
			if (data != null) {
				// Place data in higher levels of cache(i) places data with
				// dirty bit in memory
				return data;
			}
		}
		data = this.memory.getDataAt(address);
		// Place data in all higher levels of cache(number of caches) places
		// data with dirty bit in memory
		return data;
	}

	public void writeDataWithPolicies(int address, int data_value) {
		for (int i = 0; i < caches.length; i++) {
			Data dataWord = caches[i].searchData(address);
			if (data != null) {
				// write hit
				caches[i].setDataValue(dataWord, data_value);
				if (caches[i].isWriteThrough()) {
					// Update lower levels with the same value.
					updateLowerLevels(i, dataWord);
					// Write data in higher levels of Cache
					insertInHigherLevels(i, dataWord, false); // Without marking
																// bit as dirty
				} else if (caches[i].isWriteBack()) {
					// Write data in higher levels of Cache.
					insertInHigherLevels(i, dataWord, true); // With marking bit
																// as dirty
				}
				return;
			}
		}
		// Write data in higher levels of cache.
	}

	public void updateLowerLevels(int from_index, Data dataWord) {
		for (int i = from_index; i <= caches.length; i++) {
			caches[i].updateLower(dataWord);
		}
		this.memory.storeDataAtAddress(dataWord.get_value(),
				dataWord.getAddress());
	}

	public void insertInHigherLevels(int from_index, Data dataWord,
			boolean makeDirty) {
		for (int i = from_index; i >= 0; i--) {
			if (caches[i].isWriteAllocate()) {
				// Insert data in higher level from lower level
				if (makeDirty) {
					dataWord.isDirtyBit(true);
				} else {
					dataWord.isDirtyBit(false);
				}
				Data old_data = caches[i].insertData(dataWord);
				if (old_data != null){
					this.memory.storeDataAtAddress(old_data.get_value(), old_data.getAddress());
				}
			}
		}
	}

}
