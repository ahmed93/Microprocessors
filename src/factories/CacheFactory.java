package factories;

import cache.DirectMapped;
import cache.FullyAssociative;
import cache.SetAssociative;
import Abstracts.Cache;

public class CacheFactory {
	
	public static Cache createCache(int associativity, int blockSize,
			int cacheSize) {
		if (associativity == cacheSize / blockSize)
			return new FullyAssociative(blockSize, cacheSize);
		else if (associativity == 1)
			return new DirectMapped(blockSize, cacheSize);
		else
			return new SetAssociative(blockSize, cacheSize, associativity);
	}
	
}
