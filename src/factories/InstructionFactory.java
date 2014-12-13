package factories;

import instructions.*;
import simulator.Simulator;
import Abstracts.Instruction;

public class InstructionFactory {

	public static Instruction create_instruction(String instruction,
			Simulator simulator) {
		String[] instructionArray = instruction.split("(?: |, )");
		Instruction i = null;
		switch (instructionArray[0]) {
		case "LW":
			i = new LW(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.setRj(instructionArray[2]);
			break;
		case "SW":
			i = new SW(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.setRj(instructionArray[2]);
			break;
		case "JMP":
			i = new JMP(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					Integer.parseInt(instructionArray[2]));
			break;
		case "BEQ":
			i = new BEQ(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.setRj(instructionArray[2]);
			break;
		case "JALR":
			i = new JALR(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]));
			i.setRj(instructionArray[2]);
			break;
		case "RET":
			i = new RET(simulator, instruction,
					simulator.getRegister(instructionArray[1]));
			break;
		case "ADD":
			i = new ADD(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.setRj(instructionArray[2]);
			i.setRk(instructionArray[3]);
			break;
		case "SUB":
			i = new SUB(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.setRj(instructionArray[2]);
			i.setRk(instructionArray[3]);
			break;
		case "ADDI":
			i = new ADDI(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.setRj(instructionArray[2]);
			break;
		case "NAND":
			i = new NAND(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.setRj(instructionArray[2]);
			i.setRk(instructionArray[3]);
			break;
		case "MUL":
			i = new MUL(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.setRj(instructionArray[2]);
			i.setRk(instructionArray[3]);
			break;
		case "NOP":
			i = new NOP("");
			break;
		default:
			System.out.println("Error Creating Instruction");
			return null;
		}
		for (int j = 0; j < instructionArray.length; j++) {
			System.out.println(instructionArray[j]);
		}
		i.setOp(instructionArray[0]);
		if (!instructionArray[0].equals("NOP"))
			i.setRi(instructionArray[1]);
		i.executionCycles = simulator.instructionsLatencies
				.get(instructionArray[0]);

		// Returns an object that corresponds to the instruction specified in
		// the parameter.

		return i;
	}

}