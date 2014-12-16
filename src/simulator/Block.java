package simulator;

import factories.InstructionFactory;
import interfaces.Word;
import Abstracts.Cache;
import Abstracts.Instruction;

public class Block {
	public Word[] words;

	public Block(int blockSize, String type) {
		initialize(blockSize, type);
	}

	public void initialize(int blockSize, String type) {
		words = new Word[blockSize];
		for (int i = 0; i < words.length; i++) {
			if (type.equals(Cache.DATA))
			{ 	
				words[i] = new Data(0);
				//((Data) words[i]).set_value(0);
			}
			else
			{
				words[i] = InstructionFactory.create_instruction("NOP", null);
				words[i] = (Instruction) words[i];
			}
		}

	}
}
