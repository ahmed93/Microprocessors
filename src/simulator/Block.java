package simulator;

import factories.InstructionFactory;
import Abstracts.Cache;
import Abstracts.Instruction;
import interfaces.Word;

public class Block {
	public Word[] words;
	
	public Block(int blockSize, String type){
		initialize(blockSize, type);
	}

	public void initialize(int blockSize, String type) {
		words = new Word[blockSize];
		for (int i = 0; i < words.length; i++) {
			if (type.equals(Cache.DATA))
				((Data) words[i]).set_value(0);
		}

	}
}
