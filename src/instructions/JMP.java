package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class JMP extends Instruction {
	public JMP(Simulator simulator, String instruction, Register regA, int imm) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.imm = imm;
	}

	@Override
	public int execute() {
		simulator.pc = simulator.pc + regA.get_value() + imm;
		return 1;
	}
	
	@Override
	public String getName() {
		return null;
	}
}