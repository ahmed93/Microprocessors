package simulator;

import instructions.BEQ;
import instructions.NOP;
import instructions.SW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import speculation.ReorderBuffer;
import speculation.ReservationStation;
import Abstracts.Cache;
import Abstracts.Instruction;
import cache.DirectMapped;
import factories.CacheFactory;
import factories.InstructionFactory;

public class Simulator {

	public Cache[] caches;
	HashMap<String, Register> registers;
	String inputFile = "input.txt";
	public static final int REGISTERS_NUMBER = 8;
	public static final String INTEGER = "integer";
	public static final String LOGIC = "logic";
	public static final String LOAD = "load";
	public static final String STORE = "store";
	public static final String MULT = "mult";
	private Memory memory;
	private int memoryAccessTime;
	private HashMap<String, Integer> registers_status = new HashMap<String, Integer>();
	public int pc;
	public int predictedPC;
	int instruction_starting_address;
	int instructions_ending_address;
	Vector<String> data;
	Vector<String> instructions;
	public int instructions_executed;
	public int calculatedNumberOfCycles;
	Vector<Integer> instructions_addresses;
	ArrayList<ReservationStation> reservationStations;
	HashMap<Integer, Instruction> instructionsToRun;
	public HashMap<String, Integer> instructionsLatencies;
	ReorderBuffer rob;
	boolean cdbAvailable;
	int nWay;

	public int getMemoryAccessTime() {
		return memoryAccessTime;
	}

	public void setMemoryAccessTime(int memoryAccessTime) {
		this.memoryAccessTime = memoryAccessTime;
	}

	// public Simulator(Vector<String> data, Vector<String> instructions,
	// ArrayList<HashMap<String, Integer>> input_caches,
	// int instruction_starting_address, int memoryAccessTime, HashMap<String,
	// Integer> inputReservationStations) {
	// this.memory = Memory.getInstance();
	// this.memoryAccessTime = memoryAccessTime;
	// this.instruction_starting_address = instruction_starting_address;
	// this.instructions = instructions;
	// this.data = data;
	// this.instructions_addresses = new Vector<Integer>();
	// this.initializeCaches(input_caches);
	// this.initializeReservationStations(inputReservationStations);
	// this.InitailizeRegistersStatus();
	// }

	public Simulator(Vector<String> data, Vector<String> instructions,
			ArrayList<HashMap<String, Integer>> input_caches,
			int instruction_starting_address, int memoryAccessTime,
			HashMap<String, Integer> inputReservationStations, int ROB_Size,
			HashMap<String, Integer> inputinstructionsLatencies) {
		this.memory = Memory.getInstance();
		this.memoryAccessTime = memoryAccessTime;
		this.instruction_starting_address = instruction_starting_address;
		this.instructions = instructions;
		this.data = data;
		this.instructionsLatencies = inputinstructionsLatencies;
		this.initializeCaches(input_caches);
		this.instructions_addresses = new Vector<Integer>();
		this.instructionsToRun = new HashMap<Integer, Instruction>();
		this.initializeReservationStations(inputReservationStations);
		this.InitailizeRegistersStatus();
		rob = new ReorderBuffer(ROB_Size);
	}

	public void InitailizeRegistersStatus() {
		for (int i = 0; i < 8; i++) {
			registers_status.put("R" + i, 0);
		}
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

	public void initializeReservationStations(
			HashMap<String, Integer> inputReservationStations) {
		Iterator it = inputReservationStations.entrySet().iterator();
		reservationStations = new ArrayList<ReservationStation>();
		for (Map.Entry<String, Integer> entry : inputReservationStations
				.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				ReservationStation rs = new ReservationStation(entry.getKey());
				reservationStations.add(rs);
			}
		}
	}

	public void initializeCaches(
			ArrayList<HashMap<String, Integer>> input_caches) {
		this.caches = new Cache[input_caches.size()];
		for (int i = 0; i < input_caches.size(); i++) {
			HashMap<String, Integer> input_cache = input_caches.get(i);
			boolean[] policy = new boolean[4];
			policy[0] = (input_cache.get("writeBack") != 0);
			policy[1] = (input_cache.get("writeAround") != 0);
			policy[2] = (input_cache.get("writeThrough") != 0);
			policy[3] = (input_cache.get("writeAllocate") != 0);
			caches[i] = CacheFactory.createCache(
					input_cache.get("associativity"),
					input_cache.get("blockSize"), input_cache.get("cacheSize"),
					policy, input_cache.get("hitTime"),
					input_cache.get("missTime"));
		}
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

	public void getInstructionsToRun() {
		int counter = instructions_addresses.firstElement();
		while (counter != instructions_addresses.lastElement() + 1) {
			Instruction instruction = null;
			for (int j = 0; j < this.caches.length; j++) {
				instruction = caches[j].searchInstruction(counter);
				if (instruction != null && instruction.getClass() != NOP.class) {
					updateInstructionInHigherCaches(j, counter);
					// place instruction in higher cache levels(j)
					caches[j].hits++;
					break;
				}
				caches[j].misses++;
			}
			if (instruction == null || instruction.getClass() == NOP.class) {
				// place instruction in higher levels of cache.(number of
				// caches)
				// miss
				int instruction_address = counter;
				instruction = this.memory.getInstructionAt(instruction_address);
				updateInstructionInHigherCaches(caches.length,
						instruction_address);

			}
			instructionsToRun.put(counter, instruction);
			counter++;
			// instruction.execute();
			// instructions_executed++;
		}
		// for (int i = instruction_starting_address; i<=
		// instructions_ending_address; i++){
		// System.out.println("Getting instruction at " + i + " : " +
		// memory.getInstructionAt(i));
		// memory.getInstructionAt(i).execute();
		// }
	}

	public void runInstructions() {
		int instructionsCommited = 0;
		int instructionsToCommit = instructionsToRun.size();
		pc = instruction_starting_address;
		while (instructionsCommited <= instructionsToCommit) {
			System.out.println("i -> " + instruction_starting_address + "pc -> " + pc);
			for (int i = instruction_starting_address; i <= pc; i++) {
				System.out.println("Inside inner loop");
				Instruction instruction = instructionsToRun.get(i);
				int value = 0;
				if (issuable(instruction)) {
					System.out.println("Issuing");
					for (int j = 0; i < nWay; j++) {
						System.out.println("Still Issuing");
						instruction = instructionsToRun.get(i + j);
						if (issuable(instruction)) {
							issue(instruction);
							pc++;
						} else {
							break;
						}
					}
				} else if (executable(instruction)) {
					System.out.println("Executing");
					if (instruction.executionCycles == 1) {
						if (instruction.getClass() != SW.class) {
							value = instruction.execute();
							instruction.setStatus(Instruction.EXECUTED);
						} else {
							int address = instruction.getRegB().get_value()
									+ instruction.getImm();
							reservationStations.get(instruction.getResIndex())
									.setA(address);
						}
						instruction.executionCycles--;
					} else {
						instruction.executionCycles--;
					}
				} else if (writable(instruction)
						&& instruction.executionCycles == 0) {
					System.out.println("Writing");
					write(instruction, value);
				} else if (committable(instruction)) {
					System.out.println("Commiting");
					commit(instruction);
					instructionsCommited++;
				}
			}
		}
		// loop until all instructions are written
		// int instructionsCommited = 0;
		// int instructionsToCommit = instructionsToRun.size();
		// pc = instructions_addresses.firstElement();
		// int instructionPointer = pc;
		// while(instructionsCommited <= instructionsToCommit){
		// Instruction instruction = instructionsToRun.get(instructionPointer);
		// if (issuable(instruction)){
		// for (int i = 0; i < nWay; i++){
		// instruction = instructionsToRun.get(instructionPointer);
		// if (issuable(instruction)){
		// issue(instruction);
		// pc++;
		// }else {
		// break;
		// }
		// }ad
		// }else if (executable(inhetruction)){
		// if (instruction.getExecutionCycles() == 1){
		// execute(instruction);
		// }else {
		// instruction.setExecutionCycles(instruction.getExecutionCycles() - 1);
		// }
		// }else if (writable(instruction) && instruction.getExecutionCycles()
		// == 0){
		// write(instruction);
		// }else if (commitable(instruction)){
		// commit(instruction);
		// }
		//
		// }
	}
	public void updateInstructionInHigherCaches(int cacheIndex,
			int instruction_address) {
		Instruction instruction = null;
		for (int j = cacheIndex - 1; j >= 0; j--) {
			for (int k = caches[j].startingAddress(instruction_address); k <= caches[j]
					.endingAddress(instruction_address); k++) {
				instruction = this.memory.getInstructionAt(k);
				if (instruction != null) {
					caches[j].cacheInstruction(instruction);
				}
			}
		}

	}

	public void initializeRegisters() {
		registers = new HashMap<>();
		for (int i = 0; i < REGISTERS_NUMBER; i++) {
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

	public void printMemory() {
		for (int i = 0; i < 10; i++) {
			System.out.println(i + " : " + this.memory.getInstructionAt(i));
		}
		System.out.println("#################");
		for (int i = 0; i <= 10; i++) {
			System.out.println(this.memory.DATA_STARTING_ADDRESS + i + " : "
					+ this.memory.getDataAt(i).get_value());
		}
	}

	public Data getCachedOrMemoryData(int address) {
		Data dataWord = null;
		// Search Caches for data
		for (int i = 0; i < caches.length; i++) {
			dataWord = caches[i].searchData(address);
			if (dataWord != null) {
				// Place data in higher levels of cache(i) places data with
				insertInHigherLevelsRead(i, dataWord);
				// dirty bit in memory
				caches[i].hits++;
				return dataWord;
			}
			caches[i].misses++;
		}
		dataWord = this.memory.getDataAt(address);
		dataWord.setAddress(address);
		insertInHigherLevelsRead(this.caches.length, dataWord);
		// Place data in all higher levels of cache(number of caches) places
		// data with dirty bit in memory
		return dataWord;
	}

	public void insertInHigherLevelsRead(int from_index, Data dataWord) {
		for (int i = from_index - 1; i >= 0; i--) {
			int starting_address = caches[i].startingAddress(dataWord
					.getAddress());
			int ending_address = caches[i].endingAddress(dataWord.getAddress());
			for (int j = starting_address; j <= ending_address; j++) {
				Data data = this.memory.getDataAt(j);
				if (caches[i].getClass() == DirectMapped.class) {
					Data replaced = caches[i].insertData(data);
					if (replaced != null) {
						updateLowerLevels(j, replaced);
					}

				} else {
					caches[i].setWordAtAddress(data, Cache.DATA);
				}

			}
		}

	}

	public void writeDataWithPolicies(int address, int data_value) {
		if (caches.length == 0) {
			this.memory.storeDataAtAddress(data_value, address);
			return;
		}
		String WritePolicy = "";
		for (int i = 0; i < caches.length; i++) {
			Data dataWord = caches[i].searchData(address);
			if (dataWord != null) {
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
				caches[i].hits++;
				return;
			}
			caches[i].misses++;
			if (i == caches.length - 1) {
				WritePolicy = (caches[i].isWriteAround()) ? "WriteAround"
						: "WriteAllocate";
			}
		}
		if (WritePolicy.equals("WriteAllocate")) {
			Data dataWord = this.memory.getDataAt(address);
			insertInHigherLevels(caches.length, dataWord, false); // With
																	// marking
																	// bit
		} else {
			Data dataWord = this.memory.getDataAt(address);
			this.memory.storeDataAtAddress(data_value, address);
		}
	}

	public void updateLowerLevels(int from_index, Data dataWord) {
		for (int i = from_index + 1; i < caches.length; i++) {
			caches[i].updateLower(dataWord);
		}
		this.memory.storeDataAtAddress(dataWord.get_value(),
				dataWord.getAddress());
	}

	public void insertInHigherLevels(int from_index, Data dataWord,
			boolean makeDirty) {
		for (int i = from_index - 1; i >= 0; i--) {
			if (caches[i].isWriteAllocate()) {
				// Insert data in higher level from lower level
				if (makeDirty) {
					dataWord.setDirtyBit(true);
				} else {
					dataWord.setDirtyBit(false);
				}
				int starting_address = caches[i].startingAddress(dataWord
						.getAddress());
				int ending_address = caches[i].endingAddress(dataWord
						.getAddress());
				for (int j = starting_address; j <= ending_address; j++) {
					if (caches[i].getClass() == DirectMapped.class) {
						Data replaced_data = caches[i].insertData(dataWord);
						if (replaced_data != null) {
							this.memory.storeDataAtAddress(
									replaced_data.get_value(),
									replaced_data.getAddress());
						}
					} else {
						caches[i].setWordAtAddress(dataWord, Cache.DATA);
					}

				}
			}
		}
	}

	public HashMap<Integer, Integer> getRegistersValues() {
		HashMap<Integer, Integer> reg = new HashMap<Integer, Integer>();
		for (Entry<String, Register> enty : registers.entrySet()) {
			int key = Integer.parseInt(enty.getKey().replaceAll("\\D+", ""));
			int value = enty.getValue().get_value();
			reg.put(key, value);
		}
		return reg;
	}

	public HashMap<Integer, Integer> getMemoryValues() {
		HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();
		for (int i = 0; i <= 1000; i++) {
			int x = this.memory.getDataAt(i).get_value();
			tmp.put(this.memory.DATA_STARTING_ADDRESS + i, x);
		}
		return tmp;
	}

	public void setPC(int i) {
		this.pc = i;
	}

	public int getPc() {
		return this.pc;
	}

	public double calculateAMAT() {
		double amat = memoryAccessTime;
		for (int i = caches.length - 1; i >= 0; i--) {
			Cache c = caches[i];
			amat = c.getHitTime() + c.getMissRate() * amat;
		}
		return amat;
	}

	public ArrayList<String> output() {
		ArrayList<String> output = new ArrayList<String>();
		output.add("Number of instructions executed : " + instructions_executed);
		for (int i = 0; i < caches.length; i++) {
			output.add("Cache Level " + (Integer) (i + 1));
			output.add("\tHits: " + caches[i].hits);
			output.add("\tMisses: " + caches[i].misses);
			output.add("\tTotat number of accessess: " + caches[i].hits
					+ caches[i].misses);
			output.add("\tHit Rate: " + caches[i].getHitRate());
		}
		output.add("Global AMAT:" + calculateAMAT() + " cycles");
		return output;
	}

	public boolean issuable(Instruction i) {
		if(i.getStatus() == ""){
		for (int j = 0; j < reservationStations.size(); j++) {
			if (this.reservationStations.get(j).getName().equals(i.getName())
					&& !this.reservationStations.get(j).isBusy()
					&& !this.rob.isFull()) {
				i.setResIndex(j);
				return true;
			}
		}
		}
		return false;
	}

	public boolean executable(Instruction i) {
		if(i.getStatus() == i.ISSUED){
		boolean qj = this.reservationStations.get(i.getResIndex()).getQj() == 0;
		if (i.getName().equals("Store"))
			return qj;
		else
			return qj
					&& this.reservationStations.get(i.getResIndex()).getQk() == 0;
		}
		return false;

	}

	public boolean writable(Instruction i) {
		boolean tmp = i.getStatus().equals(i.EXECUTED) && this.cdbAvailable;
		if (i.getName().equals("Store"))
			return tmp
					&& this.reservationStations.get(i.getResIndex()).getQk() == 0;
		else
			return tmp;
	}

	public boolean committable(Instruction i) {
		if(i.getStatus() == i.WRITTEN){
		if (this.rob.getHead() == this.reservationStations.get(i.getResIndex())
				.getDest()
				&& this.rob.getEntryAtHead().get("Ready").equals("true"))
			return true;
		else
			return false;
		}
		return false;
		
	}

	public void issue(Instruction i) {
		String name = i.getName();
		Boolean busy = true;
		String operation = i.getOp();
		int vj = 0;
		int vk = 0;
		int qj = 0;
		int qk = 0;
		int dest = rob.getTail();
		int address = 0;
		i.setStatus(Instruction.ISSUED);
		if (registers_status.get(i.getRj()) == 0)
			vj = i.getRegB().get_value();
		else
			qj = registers_status.get(i.getRj());

		if (registers_status.get(i.getRk()) == 0)
			vk = i.getRegC().get_value();
		else
			qk = registers_status.get(i.getRk());

		HashMap<String, String> entry = new HashMap<String, String>();
		entry.put("Type", operation);
		entry.put("Destination", i.getRi());
		entry.put("Value", "");
		entry.put("Ready", "false");
		rob.addEntry(entry);

		ReservationStation r = new ReservationStation(name, busy, operation,
				vj, vk, qj, qk, dest, address);
		reservationStations.add(r);
		if (i.getClass() == BEQ.class)
			predictBranch((BEQ) i);
	}

	public void predictBranch(BEQ i) {
		if (i.getImm() < 0) // taken
		{
			pc += i.getImm();
			predictedPC = pc;
		}
	}

	public void commit(Instruction i) {
		if (i.getClass() == SW.class) {
			i.execute();
		} else {
			if (i.getClass() == BEQ.class && checkBranchPrediction(predictedPC)) {
				int rob_index = reservationStations.get(i.getResIndex())
						.getDest();
				int value = Integer.parseInt(rob.getEntryAt(rob_index).get(
						"Value"));
				registers_status.put(i.getRi(), 0);
				i.getRegA().set_value(value);
				rob.moveHead();
				i.setStatus(Instruction.COMMITED);
			} else {
				rob.reset();
				reservationStations.clear(); 
			}
		}

	}

	public boolean checkBranchPrediction(int predictedPC) {
		if (pc == predictedPC) {
			return true;
		} else {
			return false;
		}
	}

	public void write(Instruction i, int value) {
		int dest = this.registers_status.get(i.getRi());
		this.rob.getEntryAt(dest).put("Value", "" + value);
		this.rob.getEntryAt(dest).put("Ready", "true");
		for (int j = 0; j < this.reservationStations.size(); j++) {
			if (this.reservationStations.get(j).getQj() == dest) {
				this.reservationStations.get(j).setVj(value);
				this.reservationStations.get(j).setQj(0);
			}
			if (this.reservationStations.get(j).getQk() == dest) {
				this.reservationStations.get(j).setVk(value);
				this.reservationStations.get(j).setQk(0);
			}
		}
		this.reservationStations.set(i.getResIndex(), new ReservationStation(
				this.reservationStations.get(i.getResIndex()).getName()));
		i.setStatus(Instruction.WRITTEN);
	}

}
