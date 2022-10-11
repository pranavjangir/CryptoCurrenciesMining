package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

import java.util.ArrayDeque;


public class SelfishMiner extends CompliantMiner implements Miner {

	protected Block prev;
	protected ArrayDeque<Block> all_my_blocks = new ArrayDeque<>();

	public Block currentlyMiningAt(){
		return currentHead;
	}

	public Block currentHead() {
		return this.prev;
	}
	public SelfishMiner(String id, int hashRate, int connectivity) {
		super(id, hashRate, connectivity);
	}

	public void blockMined(Block block, boolean isMinerMe) {
		if(isMinerMe) {
			if (block.getHeight() > currentHead.getHeight()) {
				//this.prev = currentHead;
				this.currentHead = block;
				all_my_blocks.addLast(currentHead);
			}
		}
		else {
			if (block.getHeight() < currentHead.getHeight() - 1) {
				while(!all_my_blocks.isEmpty()) {
					Block b = all_my_blocks.removeFirst();
					if (b.getHeight() > block.getHeight()) {
						all_my_blocks.addFirst(b);
						break;
					}
					prev = b;
				}
			}
			else if (block.getHeight() == currentHead.getHeight() - 1) {
				this.prev = this.currentHead;
			} else if (block.getHeight() == currentHead.getHeight()) {
				this.prev = this.currentHead;
			}
			else if(block.getHeight() > currentHead.getHeight()) {
				this.currentHead = block;
				this.prev = block;
			}
		}
	}

	public void initialize(Block genesis, NetworkStatistics networkStatistics) {
		this.currentHead = genesis;
		this.prev = genesis;
		all_my_blocks.addLast(genesis);
	}
	// TODO Override methods to implement Selfish Mining
}
