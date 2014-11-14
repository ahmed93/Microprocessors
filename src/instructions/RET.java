package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class RET extends Instruction {
	public RET(Simulator simulator, String instruction, String command,
			Register regA, Register regB, int imm) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.command = command;
		this.regA = regA;
	}

	@Override
	public void execute() {
		simulator.pc = regA.get_value();
	}
}