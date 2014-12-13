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
	public int execute() {
		simulator.pc = regA.get_value();
		return 1;
	}
	
	@Override
	public String getName() {
		return getSimulator().INTEGER;
	}
}