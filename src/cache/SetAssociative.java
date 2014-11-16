package cache;

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
	}

	@Override
	public Instruction searchInstruction(int address) {
		int num_of_words_in_set = blockSize * associativity;
		//int word_offset_in_set = address % num_of_words_in_set;
		int set_index = address / num_of_words_in_set;
		int block_offset_in_set = address % associativity;
		int word_offset_in_block = address % blockSize;
		Instruction word = (Instruction) instructions[set_index * associativity + block_offset_in_set].words[word_offset_in_block];
		if (word.getAddress() == address) {
			hits++;
			return word;
		}
		else {
			misses++;
			return null;
		}
	}

	@Override
	public Data searchData(int address) {
		int num_of_words_in_set = blockSize * associativity;
		//int word_offset_in_set = address % num_of_words_in_set;
		int set_index = address / num_of_words_in_set;
		int block_offset_in_set = address % associativity;
		int word_offset_in_block = address % blockSize;
		Data word = (Data) data[set_index * associativity + block_offset_in_set].words[word_offset_in_block];
		if (word.getAddress() == address) {
			hits++;
			return word;
		}
		else {
			misses++;
			return null;
		}
	}

	@Override
	public Data insertData(int address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instruction insertInstruction(int address) {
		// TODO Auto-generated method stub
		return null;
	}

}