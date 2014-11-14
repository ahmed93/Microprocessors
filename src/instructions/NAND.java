package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class NAND extends Instruction {
	public NAND(Simulator simulator, String instruction, String command,
			Register regA, Register regB, Register regC) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
		this.command = command;
		this.regA = regA;
		this.regB = regB;
		this.regC = regC;
	}

	@Override
	public void execute() {
		regA.set_value((~(regB.get_value() & regC.get_value())));
	}

}
