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

	Cache[] caches;
	HashMap<String, Register> registers;
	String inputFile = "input.txt";
	static final int REGISTERS_NUMBER = 8;
	private Memory memory;
	public static int pc;

	int instruction_starting_address;
	int instructions_ending_address;
	Vector<String> data;
	Vector<String> instructions;

	public Simulator(Vector<String> data, Vector<String> instructions,
			int instruction_starting_address) {
		this.memory = Memory.getInstance();
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
		}
		this.instructions_ending_address = memory.getInstructionIndex()-1;
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
		for (int i = instruction_starting_address; i<= instructions_ending_address; i++){
//			System.out.println("Getting instruction at " + i + " : " + memory.getInstructionAt(i));
			memory.getInstructionAt(i).execute();
		}
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
	
	public void printRegisters(){
		for (Entry<String, Register> entry : registers.entrySet()){
			System.out.println(entry.getKey() + ": " + entry.getValue().get_value());
		}
	}
	
	public void printMemroy(){
		for (int i = 0; i < 10; i++){
			System.out.println(i + " : " + this.memory.getInstructionAt(i));
		}
		System.out.println("#################");
		for (int i = memory.DATA_STARTING_ADDRESS; i <= memory.DATA_STARTING_ADDRESS+10; i++){
			System.out.println(i + " : " + this.memory.getDataAt(i));
		}
	}
}
