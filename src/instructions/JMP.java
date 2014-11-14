package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class JMP extends Instruction {
	public JMP(Simulator simulator, String instruction, String command,
			Register regA, int imm) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.command = command;
		this.regA = regA;
		this.imm = imm;
	}

	@Override
	public void execute() {
		simulator.pc = simulator.pc + regA.get_value() + imm;
	}
}