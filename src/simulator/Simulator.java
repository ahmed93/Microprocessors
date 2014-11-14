package simulator;
import Abstracts.Cache;

public class Simulator {
	
	Simulator simulator_instance;
	Cache[] caches;
	private Memory memory;
	public static int pc;
	Register[] registers;
	
	private Simulator(){}
	
	public Simulator get_instance(){
		if (simulator_instance == null)
			simulator_instance = new Simulator();
		return simulator_instance;
	}
	
	public void Initialize(){
		// Initialize Cache
		// Prompt user to enter instructions, and starting address
		// Use Instruction factory to create instructions
		// Use Instruction Cache to create Cache
		// Start instruction execution
	}

	public Memory getMemory() {
		return memory;
	}	
}
