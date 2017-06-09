package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.OldGenerationRepo.OldGenerationData;

public class TestOldGenerationRepo {

	@Test
	public void test() {
		OldGenerationRepo repo = new OldGenerationRepo(3, true);
		
		/**
		 * Both of these will get pruned because of their immediate (currentTimeMillis) expiration.
		 * 
		 */
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+0l, 100);

		assertEquals(repo.getSize(),0);
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(0,repo.getSize());
	}
	@Test
	public void testPruneSome() {
		OldGenerationRepo repo = new OldGenerationRepo(3,true);
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);
		
		/**
		 * With a 10 second expiration, this one will remain after the prune finally happens.
		 */
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+10000l, 100);

		assertEquals(0,repo.getSize());
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(1,repo.getSize());
	}
	@Test
	public void testPruneSomeAfterTimeElapses() {
		OldGenerationRepo repo = new OldGenerationRepo(3,true);
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);

		/**
		 * With a 2 second expiration, this one will get pruned but only after we wait (3 seconds) until after the expiration.
		 */
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+2000l, 100);

		assertEquals(repo.getSize(),0);
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		repo.maybePrune();
		assertEquals(0,repo.getSize());
	}
	/**
	 * @stolen from: https://stackoverflow.com/questions/9090500/how-to-compare-that-sequence-of-doubles-are-all-approximately-equal-in-java
	 * @param a
	 * @param b
	 * @param eps
	 * @return
	 */
	public static boolean almostEqual(int a, int b, int eps){
	    return Math.abs(a-b)<eps;
	}
	@Test
	public void testDisabledAdd() {
		OldGenerationRepo repo = new OldGenerationRepo(3,
				false); //disabled -- nothing should get added
		
		/**
		 * Both of these will get pruned because of their immediate (currentTimeMillis) expiration.
		 * 
		 */
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+0l, 100);

		assertEquals(repo.getSize(),0);
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		
		/**
		 * Because we repo is disabled (see ctor),
		 * the calls to add() above won't add anything,
		 * thus the size will remain at zero.
		 */
		assertEquals(0,repo.getSize());
		
	}
	@Test
	public void testClear() {
		OldGenerationRepo repo = new OldGenerationRepo(3,true);
		
		/**
		 * Both of these will get pruned because of their immediate (currentTimeMillis) expiration.
		 * 
		 */
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+0l, 100);

		assertEquals(repo.getSize(),0);
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		
		repo.clear();
		assertEquals(0,repo.getSize());
		
	}
	@Test
	public void testSizeOfAllocation() {
		Runtime runtime = Runtime.getRuntime();
		long memoryUsedB4 = runtime.totalMemory() - runtime.freeMemory();
		int allocationSize1gb = 1024*1024*1024;
		OldGenerationRepo repo = new OldGenerationRepo(3,true);
		/**
		 * With a 10 second expiration, this one will remain after the prune finally happens.
		 */
		OldGenerationRepo.OldGenerationData data_1 = 
				new OldGenerationData(System.currentTimeMillis()+1000l, allocationSize1gb);
		
		repo.maybeAdd(data_1);
		long memoryUsedAfter = runtime.totalMemory() - runtime.freeMemory();
		System.out.printf("B4: %d after: %d allocated: %d\n", memoryUsedB4,memoryUsedAfter, allocationSize1gb);
		//If we've allocated roughly 1gb, who cares whether we've off by 10k.
		assertTrue( almostEqual(allocationSize1gb, (int)(memoryUsedAfter-memoryUsedB4), 10*1024) );
		

	}
	@Test
	public void testDisablePrune() {
		OldGenerationRepo repo = new OldGenerationRepo(3, true);
		
		/**
		 * Both of these will get pruned because of their immediate (currentTimeMillis) expiration.
		 * 
		 */
		OldGenerationRepo.OldGenerationData data_1 = new OldGenerationData(System.currentTimeMillis()+0l, 100);
		OldGenerationRepo.OldGenerationData data_2 = new OldGenerationData(System.currentTimeMillis()+0l, 100);

		assertEquals(repo.getSize(),0);
		
		repo.maybeAdd(data_1);
		repo.maybeAdd(data_2);
		assertEquals(2,repo.getSize());
		
		/**
		 * When you disable, no pruning will happen
		 * even with calls to prune().
		 */
		repo.setEnabled(false);
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());
		repo.maybePrune();
		assertEquals(2,repo.getSize());  //would have been 0 if enabled.
	}
}
