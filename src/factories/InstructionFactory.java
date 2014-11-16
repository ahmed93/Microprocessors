package factories;

import instructions.*;
import simulator.Simulator;
import Abstracts.Instruction;

public class InstructionFactory {

	public static Instruction create_instruction(String instruction,
			Simulator simulator) {
		String[] instructionArray = instruction.split("(?: |, )");
		switch (instructionArray[0]) {
		case "LW":
			return new LW(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
		case "SW":
			return new SW(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
		case "JMP":
			return new JMP(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					Integer.parseInt(instructionArray[2]));
		case "BEQ":
			return new BEQ(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
		case "JALR":
			return new JALR(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]));
		case "RET":
			return new RET(simulator, instruction,
					simulator.getRegister(instructionArray[1]));
		case "ADD":
			return new ADD(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
		case "SUB":
			return new SUB(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
		case "ADDI":
			return new ADDI(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
		case "NAND":
			return new NAND(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
		case "MUL":
			return new MUL(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));

		}

		// Returns an object that corresponds to the instruction specified in
		// the parameter.

		System.out.println("Error Creating Instruction");
		return null;
	}

}