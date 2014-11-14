package Abstracts;
import interfaces.Word;
import simulator.Register;
import simulator.Simulator;

public abstract class Instruction implements Word {
	protected Simulator simulator;
	String instruction;
	String command;
	Register regA;
	Register regB;
	Register regC;
	int imm;
	public abstract void execute();

}
