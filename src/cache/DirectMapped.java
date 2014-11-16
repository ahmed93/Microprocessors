package cache;

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
	}

	@Override
	public Instruction searchInstruction(int address) {
		int num_of_words_in_set = blockSize * associativity;
		//int word_offset_in_set = address % num_of_words_in_set;
		int index = (address / blockSize) % (cacheSize/blockSize);
		int word_offset_in_block = address % blockSize;
		Instruction word = (Instruction) instructions[index * associativity].words[word_offset_in_block];
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
		int index = (address / blockSize) % (cacheSize/blockSize);
		int word_offset_in_block = address % blockSize;
		Data word = (Data) data[index * associativity].words[word_offset_in_block];
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
