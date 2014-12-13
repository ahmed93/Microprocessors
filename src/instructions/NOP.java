package instructions;

import Abstracts.Instruction;

public class NOP extends Instruction {
	public NOP(String instruction) {
		super();
		this.instruction = instruction;
	}

	@Override
	public int execute() {
		return -1;
	}
	
	@Override
	public String getName() {
		return null;
	}

}