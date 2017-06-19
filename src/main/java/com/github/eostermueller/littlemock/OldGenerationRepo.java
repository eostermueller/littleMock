package com.github.eostermueller.littlemock;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class OldGenerationRepo implements IntegerChangeListener {
	AtomicBoolean ynEnabled = new AtomicBoolean(false);
	
	AtomicLong currentCount = new AtomicLong(0);
	Queue<OldGenerationData> allOldGenData = new LinkedBlockingQueue<OldGenerationData>();

	private long oldGenRequestCountThresholdForPruning;
	public OldGenerationRepo(long l, boolean enabled) {
		this();
		this.setOldGenRequestCountThresholdForPruning( l );
		this.setEnabled(enabled);
	}
	public void clear() {
		this.allOldGenData.clear();
	}
	public OldGenerationRepo() {
		setOldGenRequestCountThresholdForPruning(0);
	}
	public void setOldGenRequestCountThresholdForPruning(long val) {
		
		if (val < 0) {
			throw new RuntimeException("Attempt to set Config.RequestCountThresholdForPruningOldGen to [" + val + "].  neg numbers not allowed.");
		}
		
		if (val==0)
			this.setEnabled(false);
		else
			this.setEnabled(true);
		
		this.oldGenRequestCountThresholdForPruning = val;
	}
	private long getOldGenRequestCountThresholdForPruning() {
		return this.oldGenRequestCountThresholdForPruning;
	}

	public boolean isEnabled() {
		//System.out.println("ogr.isEnabled [" + this.ynEnabled.get() + "]");
		return this.ynEnabled.get();
	}
	public void setEnabled(boolean val) {
		this.ynEnabled.set(val);
	}
	public static class OldGenerationData {

		List<MyMemoryLeak> wrappedData = new ArrayList<MyMemoryLeak>();
		long expirationTimestamp = 0;
		public OldGenerationData(long val, int byteCount) {
			this.expirationTimestamp = val;
			
			for(int i = 0; i < byteCount; i++)
				wrappedData.add(new MyMemoryLeak() );
		}
		public long getExpirationTimestamp() {
			return this.expirationTimestamp;
		}

	}
	public long getSize() {
		return this.allOldGenData.size();
	}

	/**
	 * Instead of a background thread to periodically expire objects into garbage,
	 * this method decides when one lucky request will bear the load of
	 * removing data (expiring it) based on a timestamp.
	 * @return
	 */
	public boolean isTimeToPrune() {
		boolean ynRC = false;
		
		if ( this.isEnabled() ) {
			
			if ( this.currentCount.incrementAndGet() 
					% getOldGenRequestCountThresholdForPruning() 
					== 0) {
				ynRC = true;
			}
		}
		return ynRC;
	}

	public void maybePrune() {
		if (this.isTimeToPrune()) {
			for( OldGenerationData data : allOldGenData ) {
				if (System.currentTimeMillis() >= data.getExpirationTimestamp())
					allOldGenData.remove(data);
			}
		}
	}

	public void maybeAdd(OldGenerationData data) {
		if (isEnabled())
			this.allOldGenData.add(data);
	}
	@Override
	public void newValue(int val) {
//		System.out.println("oldGenRepo rec'd count threshold [" + val + "]");
		setOldGenRequestCountThresholdForPruning(val);
	}

}
