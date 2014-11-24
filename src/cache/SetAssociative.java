package cache;

import factories.InstructionFactory;
import instructions.NOP;
import interfaces.Word;
import simulator.Block;
import simulator.Data;
import Abstracts.Cache;
import Abstracts.Instruction;

public class SetAssociative extends Cache {

	public SetAssociative(int blockSize, int cacheSize, int associativity) {
		this.blockSize = blockSize;
		this.cacheSize = cacheSize;
		this.associativity = associativity;
		instructions = new Block[(int) cacheSize / blockSize];
		data = new Block[(int) cacheSize / blockSize];
		for (int i = 0; i < data.length; i++) {
			data[i] = new Block(blockSize, Cache.DATA);
			data[i].initialize(blockSize, Cache.DATA);
			instructions[i] = new Block(blockSize, Cache.INSTRUCTION);
			// data[i].initialize(blockSize, Cache.INSTRUCTION);
		}
	}

	public Word getWordAtAddress(int address, String type) {
		int num_of_words_in_set = blockSize * associativity;
		int number_of_sets = (cacheSize / blockSize) / associativity;
		Word word = null;
		Block block = null;
		int set_offset;
		int block_starting_address;
		set_offset = (address / blockSize) % number_of_sets;
		block_starting_address = set_offset * associativity;
		for (int j = block_starting_address; j < block_starting_address
				+ associativity; j++) {
			if (type == Cache.INSTRUCTION) {
				block = instructions[j];
			} else if (type == Cache.DATA) {
				block = data[j];
			}
			for (int i = 0; i < block.words.length; i++) {
				if (block.words[i].getAddress() == address) {
					word = block.words[i];
					return word;
				}
			}
		}
		return word;
	}

	public int startingAddress(int address) {
		// int word_offset_in_set = address % num_of_words_in_set;
		int word_offset_in_block = address % blockSize;
		return address - word_offset_in_block;

	}

	public int endingAddress(int address) {
		return startingAddress(address) + blockSize - 1;
	}

	@Override
	public Instruction searchInstruction(int address) {
		Instruction word = (Instruction) getWordAtAddress(address, INSTRUCTION);
		if (word != null && word.getAddress() == address) {
			hits++;
			return word;
		} else {
			// System.out.println("MISS");
			misses++;
			return null;
		}
	}

	@Override
	public Data searchData(int address) {
		Data word = (Data) getWordAtAddress(address, DATA);
		if (word != null && word.getAddress() == address) {
			hits++;
			return word;
		} else {
			misses++;
			return null;
		}
	}

	public void setWordAtAddress(Word input_word, String type) {
		int address = input_word.getAddress();
		int num_of_words_in_set = blockSize * associativity;
		int number_of_sets = (cacheSize / blockSize) / associativity;
		Word word = null;
		Block block = null;
		int set_offset;
		int block_starting_address;
		set_offset = (address / blockSize) % number_of_sets;
		block_starting_address = set_offset * associativity;
		for (int j = block_starting_address; j < block_starting_address
				+ associativity; j++) {
			if (type == Cache.INSTRUCTION) {
				block = instructions[j];
			} else if (type == Cache.DATA) {
				block = data[j];
			}
			for (int i = 0; i < block.words.length; i++) {
				if (type == Cache.INSTRUCTION) {
					Instruction instruction = (Instruction) (input_word);
					Instruction k = (Instruction) block.words[i];
					if (k.getClass() == NOP.class) {
						instructions[j].words[i] = InstructionFactory
								.create_instruction(
										instruction.getInstruction(),
										instruction.getSimulator());
						((Instruction) instructions[j].words[i])
								.setAddress(instruction.getAddress());
						return;

					}
				} else if (type == Cache.DATA) {
					Data input_data = (Data) (input_word);
					Data k = (Data) block.words[i];
					if (k.get_value() == 0 && k.getAddress() == 0) {
						Data target_data = (Data) data[j].words[i];
						target_data.set_value(input_data.get_value());
						target_data.setAddress(input_data.getAddress());
						target_data.setDirtyBit(input_data.isDirtyBit());
						return;
					}
					insertData(input_data);
				}
			}
		}

		// return word;
		//
		// int address = instruction.getAddress();
		// int num_of_words_in_set = blockSize * associativity;
		// int set_index = address / num_of_words_in_set;
		// int block_offset_in_set = address % associativity;
		// int word_offset_in_block = address % blockSize;
		// instructions[set_index * associativity +
		// block_offset_in_set].words[word_offset_in_block] = InstructionFactory
		// .create_instruction(instruction.getInstruction(),
		// instruction.getSimulator());

	}

}