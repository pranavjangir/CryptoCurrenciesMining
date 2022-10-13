package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class MajorityMiner extends CompliantMiner implements Miner {

    protected NetworkStatistics netstats;
    
    public MajorityMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);
    }
    public void blockMined(Block block, boolean isMinerMe) {
        if(isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.currentHead = block;
            }
        }
        else {
            double relative_hash = (double)((double)this.getHashRate() / (double)netstats.getTotalHashRate());
            if (block.getHeight() > currentHead.getHeight() && relative_hash < 0.5) {
                this.currentHead = block;
            }
        }
    }

    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
        this.netstats = networkStatistics;
    }

    public void networkUpdate(NetworkStatistics statistics) {
        netstats = statistics;
    }

	// TODO Override methods to implement Majority Mining
    
}
