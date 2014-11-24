package cache;

import factories.InstructionFactory;
import interfaces.Word;
import simulator.Block;
import simulator.Data;
import Abstracts.Cache;
import Abstracts.Instruction;
import instructions.*;

public class FullyAssociative extends Cache {
	int instruction_index = 0; // last inserted index in instruction cache
	int data_index = 0; // last inserted index in data cache

	public FullyAssociative(int blockSize, int cacheSize) {
		this.blockSize = blockSize;
		this.cacheSize = cacheSize;
		this.associativity = cacheSize;
		instructions = new Block[(int) cacheSize / blockSize];
		data = new Block[(int) cacheSize / blockSize];
		for (int i = 0; i < data.length; i++) {
			data[i].initialize(blockSize, Cache.DATA);
			instructions[i] = new Block(blockSize, Cache.INSTRUCTION);
		}
	}

	public Data insertData(Data data) {
		this.data_index = (this.data_index + blockSize) % blockSize;
		return super.insertData(data);
	}

	@Override
	public Data searchData(int address) {
		Block block;
		for (int i = 0; i < data_index; i++) {
			block = data[i];
			for (int j = 0; j < blockSize; j++) {
				if (block.words[j].getAddress() == address) {
					hits++;
					return (Data) block.words[j];
				}
			}
		}
		misses++;
		System.out.println("MISS");
		return null;
	}

	public int startingAddress(int address) {
		return address;
	}

	public int endingAddress(int address) {
		return address + blockSize - 1;
	}

	@Override
	public Instruction searchInstruction(int address) {
		Block block;
		for (int i = 0; i < instruction_index; i++) {
			block = instructions[i];
			for (int j = 0; j < blockSize; j++) {
				if (block.words[j].getAddress() == address) {
					hits++;
					return (Instruction) block.words[j];
				}
			}
		}
		misses++;
		return null;
	}

	@Override
	protected Word getWordAtAddress(int address, String type) {
		Word word;
		if (type == INSTRUCTION)
			word = searchInstruction(address);
		else
			word = searchData(address);
		return word;
	}

	@Override
	public void setWordAtAddress(Instruction instruction, String type) {
		if (type == Cache.INSTRUCTION) {
			for (int i = 0; i <= this.instructions[data_index].words.length; i++) {
				Instruction k = (Instruction) this.instructions[data_index].words[i];
				if (k.getClass() == NOP.class) {
					this.instructions[data_index].words[i] = InstructionFactory
							.create_instruction(instruction.getInstruction(),
									instruction.getSimulator());
					((Instruction) instructions[data_index].words[i])
							.setAddress(instruction.getAddress());
					return;
				}
			}
			for (int i = 1; i < blockSize; i++) {
				this.instructions[data_index].words[i] = InstructionFactory
						.create_instruction("NOP", null);
				((Instruction) instructions[data_index].words[i]).setAddress(0);
			}
			this.instructions[data_index].words[0] = InstructionFactory
					.create_instruction(instruction.getInstruction(),
							instruction.getSimulator());
			((Instruction) instructions[data_index].words[0])
					.setAddress(instruction.getAddress());

		}
		// TODO Auto-generated method stub

	}

}
