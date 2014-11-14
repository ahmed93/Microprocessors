package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class LW extends Instruction {
	public LW(Simulator simulator, String instruction, String command,
			Register regA, Register regB, int imm) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.command = command;
		this.regA = regA;
		this.regB = regB;
		this.imm = imm;
	}

	@Override
	public void execute() {
		if (imm >= -64 || imm <= 63) {
			int address = regB.get_value() + imm;
			regA.set_value(simulator.getMemory().getData(address));
		} else {
			System.out.println("Wrong immediate" + imm);
		}
	}

}