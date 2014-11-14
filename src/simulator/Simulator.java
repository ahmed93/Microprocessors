package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import factories.InstructionFactory;
import Abstracts.Cache;
import Abstracts.Instruction;

public class Simulator {

	Cache[] caches;
	HashMap<String, Register> registers;
	String inputFile = "input.txt";
	static final int REGISTERS_NUMBER = 8;
	private Memory memory;
	public static int pc;

	int instruction_starting_address;
	Vector<String> data;
	Vector<String> instructions;

	public Simulator(Vector<String> data, Vector<String> instructions,
			int instruction_starting_address) {
		this.instruction_starting_address = instruction_starting_address;
		this.instructions = instructions;
		this.data = data;
	}

	public void Initialize() throws IOException {
		// Initialize Cache
		// Initialize registers
		this.initializeRegisters();
		// Prompt user to enter instructions, and starting address
		// Use Instruction factory to create instructions
		this.pc = instruction_starting_address;
		this.storeUserInstructions();
		this.storeUserData();
		this.runInstructions();
		// Use Instruction Cache to create Cache
		// Start instruction execution
	}

	public void storeUserInstructions() {
		for (String instruction : this.instructions) {
			Instruction new_instruction = InstructionFactory
					.create_instruction(instruction, this);
			memory.setInstructionIndex(instruction_starting_address);
			memory.storeInstruction(new_instruction);
		}
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
}
