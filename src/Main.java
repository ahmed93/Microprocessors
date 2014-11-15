import java.io.IOException;
import java.util.Vector;

import simulator.Simulator;
import Abstracts.Instruction;

public class Main {

	public static void main (String [] args) throws IOException{
		Vector<String> data = new Vector<>();
		Vector<String> instructions = new Vector<>();
		int instruction_starting_address = 0;
		Simulator simulator = new Simulator(data, instructions, instruction_starting_address);
		simulator.Initialize();
		simulator.runInstructions();
	}

}
