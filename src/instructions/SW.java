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
		this.status = "";
	}

	@Override
	public int execute() {
		if (imm >= -64 || imm <= 63) {
			int address = regB.get_value() + imm;
			// Write Policy
			 this.simulator.writeDataWithPolicies(address, regA.get_value());
			 return 1;
//			this.simulator.getMemory().storeDataAtAddress(regA.get_value(), address);
		} else {
			System.err.println("Wrong immediate" + imm);
			return -1;
		}
	}
	
	@Override
	public String getName() {
		return getSimulator().STORE;
	}

}