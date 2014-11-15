package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class RET extends Instruction {
	public RET(Simulator simulator, String instruction, Register regA) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
	}

	@Override
	public void execute() {
		simulator.pc = regA.get_value();
	}
}