package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

import java.util.ArrayDeque;


public class SelfishMiner extends CompliantMiner implements Miner {

	protected Block prev;
	protected ArrayDeque<Block> all_my_blocks = new ArrayDeque<>();

	protected NetworkStatistics netstats;

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
			// System.out.println(currentHead.getHeight() - block.getHeight());
			double relative_conn = this.getConnectivity();
			double relative_hash = (double)((double)this.getHashRate() / (double)netstats.getTotalHashRate());
			relative_conn = relative_conn / ((double)netstats.getTotalConnectivity());
//			System.out.println("conn ==========: " + relative_conn);
//			System.out.println("hashpower    ===========:    " + relative_hash);
//			System.out.println("conn : " + relative_conn);
//			System.out.println("hash : " + relative_hash);
			if (block.getHeight() < currentHead.getHeight() - 1) {
				if (relative_conn < 0.5 && relative_hash < 0.25) {
					System.out.println("Actually gets here!!!");
					this.prev = this.currentHead;
				} else {
					while(!all_my_blocks.isEmpty()) {
						Block b = all_my_blocks.removeFirst();
						if (b.getHeight() > block.getHeight()) {
							all_my_blocks.addFirst(b);
//							if (relative_conn <= 0.7) {
//								prev = b;
//							}
							break;
						}
						prev = b;
					}
				}
			}
			else if (block.getHeight() == currentHead.getHeight() - 1) {
				if (relative_conn <= 0.7 || relative_hash < 0.25) {
					this.prev = this.currentHead;
				} else {
					while(!all_my_blocks.isEmpty()) {
						Block b = all_my_blocks.removeFirst();
						if (b.getHeight() > block.getHeight()) {
							all_my_blocks.addFirst(b);
//							if (relative_conn <= 0.7) {
//								prev = b;
//							}
							break;
						}
						prev = b;
					}
				}
			}
			else if (block.getHeight() == currentHead.getHeight()) {
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
		all_my_blocks.clear();
		all_my_blocks.addLast(genesis);
		this.netstats = networkStatistics;
	}

	public void networkUpdate(NetworkStatistics statistics) {
		netstats = statistics;
	}
	// TODO Override methods to implement Selfish Mining
}