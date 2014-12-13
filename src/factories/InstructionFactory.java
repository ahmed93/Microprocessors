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
			i.executionCycles = simulator.instructionsLatencies.get("LW");
			break;
		case "SW":
			i = new SW(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("SW");
			break;
		case "JMP":
			i = new JMP(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					Integer.parseInt(instructionArray[2]));
			i.executionCycles = simulator.instructionsLatencies.get("JMP");
			break;
		case "BEQ":
			i = new BEQ(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("BEQ");
			break;
		case "JALR":
			i = new JALR(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]));
			i.executionCycles = simulator.instructionsLatencies.get("JALR");
			break;
		case "RET":
			i = new RET(simulator, instruction,
					simulator.getRegister(instructionArray[1]));
			i.executionCycles = simulator.instructionsLatencies.get("RET");
			break;
		case "ADD":
			i = new ADD(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("ADD");
			break;
		case "SUB":
			i = new SUB(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("SUB");
			break;
		case "ADDI":
			i = new ADDI(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					Integer.parseInt(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("ADDI");
			break;
		case "NAND":
			i = new NAND(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("NAND");
			break;
		case "MUL":
			i = new MUL(simulator, instruction,
					simulator.getRegister(instructionArray[1]),
					simulator.getRegister(instructionArray[2]),
					simulator.getRegister(instructionArray[3]));
			i.executionCycles = simulator.instructionsLatencies.get("MUL");
			break;
		case "NOP":
			i = new NOP("");
			break;
		default:
			System.out.println("Error Creating Instruction");
			return null;
		}
		switch (instructionArray.length) {
		case 0:
			i.setOp(instructionArray[0]);
		case 1:
			i.setRi(instructionArray[1]);
		case 2:
			i.setRj(instructionArray[2]);
		case 3:
			i.setRk(instructionArray[3]);
		}

		// Returns an object that corresponds to the instruction specified in
		// the parameter.
		
		return i;
	}

}