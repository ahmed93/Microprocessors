package Abstracts;
import interfaces.Word;
import simulator.Register;
import simulator.Simulator;

public abstract class Instruction implements Word {
	protected Simulator simulator;
	protected String instruction;
	protected String command;
	protected Register regA;
	protected Register regB;
	protected Register regC;
	protected int imm;
	
	public abstract void execute();

}
