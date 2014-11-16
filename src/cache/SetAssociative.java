package cache;

import simulator.Block;
import Abstracts.Cache;

public class SetAssociative extends Cache {

	public SetAssociative(int blockSize, int cacheSize, int associativity) {
		this.blockSize = blockSize;
		this.cacheSize = cacheSize;
		this.associativity = associativity;
		instructions = new Block[(int) cacheSize / blockSize];
		data = new Block[(int) cacheSize / blockSize];
	}

	@Override
	public int searchData(int address) {
		//int index = (address/blockSize)%
		return 0;
	}

	@Override
	public int searchInstruction(int address) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int existsData(int address) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int existsInstruction(int address) {
		// TODO Auto-generated method stub
		return 0;
	}

}