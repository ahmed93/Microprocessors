package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class BEQ extends Instruction {
	public BEQ(Simulator simulator, String instruction, Register regA,
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
		if (regA.get_value() == regB.get_value()) {
			simulator.setPC(imm + simulator.getPc());
			return 1;
		}
		return 0;
	}
	
	@Override
	public String getName() {
		return getSimulator().LOGIC;
	}
}
