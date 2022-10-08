package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class MajorityMiner extends CompliantMiner implements Miner {

    protected Block garbage_block;
    
    public MajorityMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);
    }
    public void blockMined(Block block, boolean isMinerMe) {
        if(isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.garbage_block = this.currentHead;
                this.currentHead = block;
            }
        }
        else {
            if (block.getHeight() > currentHead.getHeight() + 10) {
                this.currentHead = block;
            }
        }
    }

	// TODO Override methods to implement Majority Mining
    
}
