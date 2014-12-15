package Abstracts;

import interfaces.Word;
import simulator.Register;
import simulator.Simulator;

public abstract class Instruction implements Word {
	protected Simulator simulator;
	protected String status = "";
	public int executionCycles = 0;
	public static final String ISSUED = "issued";
	public static final String EXECUTED = "executed";
	public static final String WRITTEN = "written";
	public static final String COMMITED = "commited";
	protected int resIndex = -1;
	protected String instruction;
	protected Register regA;
	protected Register regB;
	protected Register regC;
	protected int imm;
	protected int address;
	protected boolean dirtyBit;
	protected String Ri;
	protected String Rj;
	protected String Rk;
	protected String Op;
	protected int executionValue;
	protected int ROBIndex;
	protected int storeAddress;

	public int getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(int storeAddress) {
		this.storeAddress = storeAddress;
	}

	public int getROBIndex() {
		return ROBIndex;
	}

	public void setROBIndex(int rOBIndex) {
		ROBIndex = rOBIndex;
	}

	public int getExecutionCycles() {
		return executionCycles;
	}

	public void setExecutionCycles(int executionCycles) {
		this.executionCycles = executionCycles;
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

	public abstract int execute();

	public abstract String getName();

	public Register getRegA() {
		return regA;
	}

	public void setRegA(Register regA) {
		this.regA = regA;
	}

	public Register getRegB() {
		return regB;
	}

	public void setRegB(Register regB) {
		this.regB = regB;
	}

	public Register getRegC() {
		return regC;
	}

	public void setRegC(Register regC) {
		this.regC = regC;
	}

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

	public int getImm() {
		return imm;
	}

	public String getRi() {
		return Ri;
	}

	public void setRi(String ri) {
		Ri = ri;
	}

	public String getRj() {
		return Rj;
	}

	public void setRj(String rj) {
		Rj = rj;
	}

	public String getRk() {
		return Rk;
	}

	public void setRk(String rk) {
		Rk = rk;
	}

	public String getOp() {
		return Op;
	}

	public void setOp(String op) {
		Op = op;
	}
	
	public int getExecutionValue(){
		return this.executionValue;
	}
	
	public void setExecutionValue(int executionValue){
		this.executionValue = executionValue;
	}

}
