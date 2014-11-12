package simulator;
import interfaces.Word;
import Abstracts.Instruction;

public class Memory {

	final int SIZE = 32768;
	Word[] cells = new Word[SIZE];

	int percentage;
	int d = percentage / 100 * SIZE; // Data Index
	int i; // Instruction Index

	public void store_date(String data, int address){}

	public void store_instruction(Instruction instruction, int address){}	
}
