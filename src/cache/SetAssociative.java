package cache;

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
		for (int i =0 ; i< data.length; i++){
			data[i] =  new Block(blockSize, Cache.DATA);
			data[i].initialize(blockSize, Cache.DATA);
			instructions[i] =  new Block(blockSize, Cache.INSTRUCTION);
			//data[i].initialize(blockSize, Cache.INSTRUCTION);
		}
	}

	
	public Word getWordAtAddress(int address, String type)
	{
		int num_of_words_in_set = blockSize * associativity;
		//int word_offset_in_set = address % num_of_words_in_set;
		int set_index = address / num_of_words_in_set;
		int block_offset_in_set = address % associativity;
		int word_offset_in_block = address % blockSize;
		Word word;
		if(type == INSTRUCTION)
			word = instructions[set_index * associativity + block_offset_in_set].words[word_offset_in_block];
		else 
			word = data[set_index * associativity + block_offset_in_set].words[word_offset_in_block];
		return word;
		
	}
	
	public int startingAddress(int address) {
		//int word_offset_in_set = address % num_of_words_in_set;
		int word_offset_in_block = address % blockSize;
		return address - word_offset_in_block;

	}
	
	public int endingAddress(int address){
		return startingAddress(address) + blockSize -1;
	}	
	@Override
	public Instruction searchInstruction(int address) {
		Instruction word  = (Instruction) getWordAtAddress(address, INSTRUCTION);
		if (word.getAddress() == address) {
			hits++;
			return word;
		}
		else {
			System.out.println("MISS");
			misses++;
			return null;
		}
	}

	@Override
	public Data searchData(int address) {
		Data word  = (Data) getWordAtAddress(address, DATA);
		if (word.getAddress() == address) {
			hits++;
			return word;
		}
		else {
			misses++;
			return null;
		}
	}

}