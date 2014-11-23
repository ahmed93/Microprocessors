package cache;

import interfaces.Word;
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
		for (int i =0 ; i< data.length; i++){
			data[i].initialize(blockSize, Cache.INSTRUCTION);
		}
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
	
	public int startingAddress(int address){
		return address;
	}
	
	public int endingAddress(int address){
		return address + blockSize -1;
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
		if(type == INSTRUCTION)
			word = searchInstruction(address);
		else
			word = searchData(address);
		return word;
	}

}
