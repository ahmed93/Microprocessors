package instructions;

import simulator.Register;
import simulator.Simulator;
import Abstracts.Instruction;

public class NOP extends Instruction {
	public NOP(Simulator simulator, String instruction) {
		super();
		this.simulator = simulator;
		this.instruction = instruction;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}