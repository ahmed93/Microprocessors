import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import simulator.Simulator;
import cache.FullyAssociative;
import factories.CacheFactory;

public class Main {

	public static void main(String[] args) throws IOException {
		Vector<String> data = new Vector<>();
		Vector<String> instructions = new Vector<>();
		ArrayList<HashMap<String, Integer>> cache_information = new ArrayList<>();
		int memoryAccessTime = 0;
		// HashMap<String, Integer> cache_information = new HashMap<>()[4];
		for (int i = 0; i < 2; i++) {
			HashMap<String, Integer> cache = new HashMap<String, Integer>();
			cache.put("associativity", i+1);
			cache.put("cacheSize", 8);
			cache.put("blockSize", i+1);
			cache.put("writeBack", 0);
			cache.put("writeThrough", 1);
			cache.put("writeAllocate", 0);
			cache.put("writeAround", 1);
			cache.put("hitTime", 1);
			cache.put("missTime", 1);
			cache_information.add(cache);
		}
		HashMap<String, Integer> cache = new HashMap<String, Integer>();
		cache.put("associativity", 4);
		cache.put("cacheSize", 24);
		cache.put("blockSize", 3);
		cache.put("writeBack", 0);
		cache.put("writeThrough", 1);
		cache.put("writeAllocate", 0);
		cache.put("writeAround", 1);
		cache.put("hitTime", 1);
		cache.put("missTime", 1);
		cache_information.add(cache);
		instructions.add("ADDI R1, R0, 1");
		instructions.add("ADDI R2, R0, 10");
		instructions.add("ADDI R3, R0, 0");
		instructions.add("ADD R1, R1, R1");
		instructions.add("ADDI R3, R3, 1");
		instructions.add("BEQ R2, R3, 1");
		instructions.add("JMP R0, -4");
		instructions.add("SW R1, R0, 4");
		instructions.add("LW R4, R0, 4");
		int instruction_starting_address = 0;
		Simulator simulator = new Simulator(data, instructions,
				cache_information, instruction_starting_address,
				memoryAccessTime);
		simulator.Initialize();
		simulator.runInstructions();
		for (int i = 0; i < simulator.caches.length; i++) {
			simulator.caches[i].printCache();
		}
		simulator.printMemory();
		simulator.printRegisters();
		System.out.println(simulator.instructions_executed);
		System.out.println(simulator.output().toString());

	}

}
