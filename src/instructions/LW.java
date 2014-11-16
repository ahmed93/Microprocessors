package instructions;

import simulator.Data;
import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class LW extends Instruction {
	public LW(Simulator simulator, String instruction, Register regA,
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
			Data data = this.simulator.getCachedOrMemoryData(address);
			regA.set_value(data.get_value());
		} else {
			System.err.println("Wrong immediate" + imm);
		}
	}

}