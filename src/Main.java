import java.io.IOException;
import java.util.Vector;

import simulator.Simulator;
import Abstracts.Cache;
import cache.FullyAssociative;

public class Main {

	public static void main (String [] args) throws IOException{
		Vector<String> data = new Vector<>();
		Vector<String> instructions = new Vector<>();
		Cache[] caches = new Cache[1];
		caches[0] = new FullyAssociative(2, 8);
		instructions.add("ADDI R1, R1, 2");
		instructions.add("ADDI R2, R2, 3");
		instructions.add("NAND R3, R1, R2");
		instructions.add("SW R2, R0, 2");
		instructions.add("LW R4, R0, 2");
		int instruction_starting_address = 0;
		Simulator simulator = new Simulator(data, instructions, caches,  instruction_starting_address);
		simulator.Initialize();
		simulator.runInstructions();
		simulator.printMemroy();
		simulator.printRegisters();
	}

}
