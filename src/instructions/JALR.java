package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class JALR extends Instruction {
	public JALR(Simulator simulator, String instruction, Register regA,
			Register regB) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.regB = regB;
	}

	@Override
	public int execute() {
		int PC =  this.simulator.pc;
		this.simulator.pc = this.regB.get_value();
		return PC;

	}
	
	@Override
	public String getName() {
		return null;
	}
}