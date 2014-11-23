package cache;

import simulator.Block;
import simulator.Data;
import Abstracts.Cache;
import Abstracts.Instruction;

public class FullyAssociative extends Cache {
	int instruction_index = 0; // last inserted index in instruction cache
	int data_index = 0; // last inserted index in data cache

	public FullyAssociative(int blockSize, int cacheSize) {
		this.blockSize = blockSize;
		this.cacheSize = cacheSize;
		this.associativity = cacheSize;
		instructions = new Block[(int) cacheSize / blockSize];
		data = new Block[(int) cacheSize / blockSize];
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
		return null;
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


}
