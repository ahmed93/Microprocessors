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
	}

	@Override
	public void execute() {
		regA.set_value(regB.get_value() + imm);
	}
	
	@Override
	public String getName() {
		return "Add";
	}

}
