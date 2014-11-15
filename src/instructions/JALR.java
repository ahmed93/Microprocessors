package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class JALR extends Instruction {
	public JALR(Simulator simulator, String instruction, String command,
			Register regA, Register regB) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.regB = regB;
	}

	@Override
	public void execute() {
		regA.set_value(simulator.pc);
		simulator.pc = regB.get_value();

	}
}