package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class SUB extends Instruction {
	public SUB(Simulator simulator, String instruction, Register regA,
			Register regB, Register regC) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.regA = regA;
		this.regB = regB;
		this.regC = regC;
	}

	@Override
	public void execute() {
		regA.set_value(regB.get_value() - regC.get_value());
	}
	
	@Override
	public String getName() {
		return "Add";
	}
}
