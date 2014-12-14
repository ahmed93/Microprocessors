package factories;

import Abstracts.Cache;
import cache.DirectMapped;
import cache.FullyAssociative;
import cache.SetAssociative;

public class CacheFactory {

	public static Cache createCache(int associativity, int blockSize,
			int cacheSize, boolean[] writePolicies, int hitTime, int missTime) {
		Cache cache;
		if (associativity == cacheSize / blockSize)
			cache = new FullyAssociative(blockSize, cacheSize);
		else if (associativity == 1)
			cache = new DirectMapped(blockSize, cacheSize);
		else
			cache = new SetAssociative(blockSize, cacheSize, associativity);
		cache.setWriteBack(writePolicies[0]);
		cache.setWriteAround(writePolicies[1]);
		cache.setWriteThrough(writePolicies[2]);
		cache.setWriteAllocate(writePolicies[3]);
		cache.setHitTime(hitTime);
		cache.setMissTime(missTime);
		return cache;
	}

}
