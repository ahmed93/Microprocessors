package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class NAND extends Instruction {
	public NAND(Simulator simulator, String instruction, Register regA,
			Register regB, Register regC) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.regB = regB;
		this.regC = regC;
		this.status = "";
	}

	@Override
	public int execute() {
		return ~((regB.get_value() & regC.get_value()));
	}
	
	@Override
	public String getName() {
		return getSimulator().LOGIC;
	}

}
