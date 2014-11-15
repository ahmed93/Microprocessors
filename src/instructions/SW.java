package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class SW extends Instruction {
	public SW(Simulator simulator, String instruction, Register regA,
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

		if (imm >= -64 || imm <= 63) {
			int address = regB.get_value() + imm;
			simulator.getMemory().storeDataAtAddress(regA.get_value(), address);
		} else {
			System.err.println("Wrong immediate" + imm);
		}
	}

}