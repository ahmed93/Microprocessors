package cache;

import factories.InstructionFactory;
import instructions.NOP;
import interfaces.Word;
import simulator.Block;
import simulator.Data;
import Abstracts.Cache;
import Abstracts.Instruction;

public class DirectMapped extends Cache {

	public DirectMapped(int blockSize, int cacheSize) {
		this.blockSize = blockSize;
		this.cacheSize = cacheSize;
		this.associativity = 1;
		instructions = new Block[(int) cacheSize / blockSize];
		data = new Block[(int) cacheSize / blockSize];
		for (int i = 0; i < data.length; i++) {
			data[i] = new Block(blockSize, Cache.DATA);
			data[i].initialize(blockSize, Cache.DATA);
			instructions[i] = new Block(blockSize, Cache.INSTRUCTION);

		}
	}

	public Word getWordAtAddress(int address, String type) {
		int num_of_words_in_set = blockSize * associativity;
		// int word_offset_in_set = address % num_of_words_in_set;
		int index = (address / blockSize) % (cacheSize / blockSize);
		int word_offset_in_block = address % blockSize;
		Word word;
		if (type == INSTRUCTION)
			word = instructions[index * associativity].words[word_offset_in_block];
		else
			word = data[index * associativity].words[word_offset_in_block];
		return word;
	}

	public void setWordAtAddress(Instruction instruction) {
		int address = instruction.getAddress();
		int index = (address / blockSize) % (cacheSize / blockSize);
		int word_offset_in_block = address % blockSize;
		instructions[index * associativity].words[word_offset_in_block] = InstructionFactory
				.create_instruction(instruction.getInstruction(),
						instruction.getSimulator());
	}

	public int startingAddress(int address) {
		int num_of_words_in_set = blockSize * associativity;
		int index = (address / blockSize) % (cacheSize / blockSize);
		int word_offset_in_block = address % blockSize;
		return address - word_offset_in_block;

	}

	public int endingAddress(int address) {
		return startingAddress(address) + blockSize - 1;
	}

	@Override
	public Instruction searchInstruction(int address) {
		Instruction word = (Instruction) getWordAtAddress(address, INSTRUCTION);
		if (word.getAddress() == address && word.getClass() != NOP.class) {
			hits++;
			return word;
		} else {
//			System.out.println("MISS");
			misses++;
			return null;
		}
	}

	@Override
	public Data searchData(int address) {
		Data word = (Data) getWordAtAddress(address, DATA);
		if (word.getAddress() == address) {
			hits++;
			return (Data) word;
		} else {
			misses++;
			return null;
		}
	}

}
