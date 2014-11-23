import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import simulator.Simulator;
import cache.FullyAssociative;

public class Main {

	public static void main (String [] args) throws IOException{
		Vector<String> data = new Vector<>();
		Vector<String> instructions = new Vector<>();
		ArrayList<HashMap<String, Integer>> cache_information = new ArrayList<>(); 
		//HashMap<String, Integer> cache_information = new HashMap<>()[4];
		for(int i=0;i<cache_information.size();i++)
		{ 
		    cache_information.set(i,new HashMap<String,Integer>());
		}
		
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
