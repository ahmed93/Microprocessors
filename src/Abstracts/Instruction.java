package Abstracts;

import interfaces.Word;
import simulator.Register;
import simulator.Simulator;

public abstract class Instruction implements Word {
	protected Simulator simulator;
	protected String instruction;
	protected Register regA;
	protected Register regB;
	protected Register regC;
	protected int imm;
	protected int address;
	protected boolean dirtyBit;

	public abstract void execute();

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public boolean isDirtyBit() {
		return dirtyBit;
	}

	public void setDirtyBit(boolean dirtyBit) {
		this.dirtyBit = dirtyBit;
	}


	
}
