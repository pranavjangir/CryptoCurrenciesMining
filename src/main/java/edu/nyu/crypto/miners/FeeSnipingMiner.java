package edu.nyu.crypto.miners;


import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class FeeSnipingMiner extends CompliantMiner implements Miner {


    protected Block local_head;
    protected NetworkStatistics netstats;
    
    public FeeSnipingMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }

    public Block currentlyMiningAt() {
        return local_head;
    }

    public Block currentHead() {
        return local_head;
    }

    public void blockMined(Block block, boolean isMinerMe) {
        if(isMinerMe) {
            if (block.getHeight() > local_head.getHeight()) {
                this.local_head = block;
            }
        }
        else {
            double relative_hash = (double)((double)this.getHashRate() / (double)netstats.getTotalHashRate());
            if (block.getHeight() > local_head.getHeight() + 1) {
                this.local_head = block;
                return;
            }
            if (block.getHeight() == local_head.getHeight() + 1) {
                double block_reward_threshold = 2.0 / (relative_hash * relative_hash) - 3.0;
                block_reward_threshold = Math.max(block_reward_threshold, 0.0);
                // TODO(pranav): If possible try to incorporate the block reward distribution parameters.
                if (block.getBlockValue() > block_reward_threshold) {
                    // Do nothing, let us try to fork!
                } else {
                    this.local_head = block;
                }
           }
        }
    }


    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
        this.local_head = genesis;
        this.netstats = networkStatistics;
    }

    public void networkUpdate(NetworkStatistics statistics) {
        netstats = statistics;
    }


	// TODO Override methods to implement Fee Sniping
}
