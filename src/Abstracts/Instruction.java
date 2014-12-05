package Abstracts;

import interfaces.Word;
import simulator.Register;
import simulator.Simulator;

public abstract class Instruction implements Word {
	protected Simulator simulator;
	protected String status ="";
	protected int cycles = 0;
	public final String ISSUED = "issued";
	public final String EXECUTED = "executed";
	public final String WRITTEN = "written";
	public final String COMMITED = "commited";
	protected int resIndex = -1;

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	protected String instruction;
	protected Register regA;
	protected Register regB;
	protected Register regC;
	protected int imm;
	protected int address;
	protected boolean dirtyBit;

	public abstract void execute();
	
	public abstract String getName();

	public int getAddress() {
		return address;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
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

	public int getResIndex() {
		return resIndex;
	}

	public void setResIndex(int resIndex) {
		this.resIndex = resIndex;
	}

}
