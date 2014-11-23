package cache;

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
	}
	
	
	public Word getWordAtAddress(int address, String type)
	{
		int num_of_words_in_set = blockSize * associativity;
		//int word_offset_in_set = address % num_of_words_in_set;
		int index = (address / blockSize) % (cacheSize/blockSize);
		int word_offset_in_block = address % blockSize;
		Word word;
		if(type == INSTRUCTION)
			 word = instructions[index * associativity].words[word_offset_in_block];
		else 
			word =  data[index * associativity].words[word_offset_in_block];
		return word;
		
	}

	@Override
	public Instruction searchInstruction(int address) {
		Instruction word  = (Instruction) getWordAtAddress(address, INSTRUCTION);
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
		Data word  = (Data) getWordAtAddress(address, DATA);
		if (word.getAddress() == address) {
			hits++;
			return (Data) word;
		}
		else {
			misses++;
			return null;
		}
	}
}
