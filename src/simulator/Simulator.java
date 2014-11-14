package simulator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Abstracts.Cache;

public class Simulator {
	
	static Simulator simulator_instance;
	Cache[] caches;
	HashMap<String, Register> registers;
	String inputFile = "input.txt";
	static final int REGISTERS_NUMBER = 8;
	private Memory memory;
	public static int pc;
	
	private Simulator(){}
	
	public static Simulator getInstance(){
		if (simulator_instance == null)
			simulator_instance = new Simulator();
		return simulator_instance;
	}
	
	public void Initialize() throws IOException{
		// Initialize Cache
		// Initialize registers
		initializeRegisters();
		// Prompt user to enter instructions, and starting address
		File file = new File(inputFile);
		BufferedReader reader = null;
		try {
			reader  = new BufferedReader(new FileReader(file));
			while(reader.ready()){
				String read = reader.readLine();
				System.out.println(read);
				if (read == "END"){
					reader.close();
					return;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			reader.close();
		}
		System.out.println("Done Reading !!!");
		// Use Instruction factory to create instructions
		// Use Instruction Cache to create Cache
		// Start instruction execution
	}
	public void runInstructions(){}
	
	public void initializeRegisters(){
		for (int i = 0; i <= REGISTERS_NUMBER; i++){
			Register Ri = (i ==0)? new Register(0, true): new Register(0,false);
			registers.put("R"+i, Ri);
		}
		
	}
	
	public Memory getMemory() {
		return memory;
	}	
}
