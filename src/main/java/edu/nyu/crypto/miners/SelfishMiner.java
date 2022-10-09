package edu.nyu.crypto.miners;
import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class SelfishMiner extends CompliantMiner implements Miner {

	protected Block to_broadcast;
	protected Block current_best;

	public SelfishMiner(String id, int hashRate, int connectivity) {
		super(id, hashRate, connectivity);
	}


	public Block currentlyMiningAt() {
		return currentHead;
	}

	public Block currentHead() {
		if (current_best.getHeight() < to_broadcast.getHeight()) {
			Block tmp = to_broadcast;
			to_broadcast = currentHead;
			return tmp;
		} else {
			to_broadcast = currentHead;
			return currentHead;
		}
	}

	public void initialize(Block genesis, NetworkStatistics networkStatistics) {
		this.currentHead = genesis;
		this.to_broadcast = genesis;
		this.current_best = genesis;
	}

	public void blockMined(Block block, boolean isMinerMe) {
		if(isMinerMe) {
			if (block.getHeight() > currentHead.getHeight()) {
				this.currentHead = block;
			}
		}
		else {
			if (block.getHeight() > current_best.getHeight()) {
				current_best = block;
			}
			if (block.getHeight() > currentHead.getHeight()) {
				this.currentHead = block;
			} else {
				if (to_broadcast == currentHead) {
					// nothing.
				} else {
					if (block.getHeight() == currentHead.getHeight()) {
						to_broadcast = currentHead;
					}
				}
			}
		}
	}

   
	// TODO Override methods to implement Selfish Mining
}
