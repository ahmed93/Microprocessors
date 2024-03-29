package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class ADDI extends Instruction {
	public ADDI(Simulator simulator, String instruction, Register regA,
			Register regB, int imm) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.regB = regB;
		this.imm = imm;
		this.status = "";
	}

	@Override
	public int execute() {
		return regB.get_value() + imm;
	}
	
	@Override
	public String getName() {
		return getSimulator().INTEGER;
	}

}
