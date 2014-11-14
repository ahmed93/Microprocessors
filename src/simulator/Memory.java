package simulator;
import interfaces.Word;
import Abstracts.Instruction;

public class Memory {

	final int SIZE = 32768;
	Word[] cells = new Word[SIZE];
	private static Memory memory_instance;

	int percentage;
	int d = percentage / 100 * SIZE; // Data Index
	int i; // Instruction Index
	
	private Memory(){}
	
	public Memory get_instance(){
		if (memory_instance == null)
			memory_instance = new Memory();
		return memory_instance;
	}
	public void store_data(String data, int address){}

	public void store_instruction(Instruction instruction, int address){}	
}
