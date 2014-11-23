import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import simulator.Simulator;
import cache.FullyAssociative;
import factories.CacheFactory;

public class Main {

	public static void main (String [] args) throws IOException{
		Vector<String> data = new Vector<>();
		Vector<String> instructions = new Vector<>();
		ArrayList<HashMap<String, Integer>> cache_information = new ArrayList<>(); 
		//HashMap<String, Integer> cache_information = new HashMap<>()[4];
		for(int i=0;i<3;i++)
		{
			HashMap<String,Integer>cache = new HashMap<String,Integer>();
			cache.put("associativity", 1);
			cache.put("cacheSize", 32);
			cache.put("blockSize",4);
			cache.put("writeBack", 1);
			cache.put("writeThrough", 0);
			cache.put("writeAllocate", 1);
			cache.put("writeAround", 0);
			cache_information.add(cache);
		}
		
		instructions.add("ADDI R1, R1, 2");
		instructions.add("ADDI R2, R2, 3");
		instructions.add("NAND R3, R1, R2");
		instructions.add("SW R2, R0, 2");
		instructions.add("LW R4, R0, 2");
		int instruction_starting_address = 0;
		Simulator simulator = new Simulator(data, instructions, cache_information,  instruction_starting_address);
		simulator.Initialize();
		simulator.runInstructions();
		simulator.printMemroy();
		simulator.printRegisters();
	}

}
